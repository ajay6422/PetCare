package com.example.petcare

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import java.io.FileNotFoundException
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class KnowYourPetActivity : AppCompatActivity() {

    private lateinit var viewFinder: PreviewView
    private lateinit var btnCapture: Button
    private lateinit var btnRetake: Button
    private lateinit var resultCardView: CardView
    private lateinit var tvBreed: TextView
    private lateinit var tvName: TextView
    private lateinit var tvConfidence: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService

    private var classifier: ImageClassifier? = null
    private var labels: List<String> = emptyList()

    // Pet breed name mappings (expanded list)
    private val petNames = mapOf(
        "labrador_retriever" to "Buddy",
        "german_shepherd" to "Max",
        "golden_retriever" to "Charlie",
        "bulldog" to "Rocky",
        "beagle" to "Cooper",
        "poodle" to "Jack",
        "siamese" to "Luna",
        "persian_cat" to "Bella",
        "maine_coon" to "Lucy",
        "ragdoll" to "Daisy",
        "bengal" to "Molly",
        "sphynx" to "Oliver",
        "british_shorthair" to "Leo",
        "russian_blue" to "Milo",
        "siberian_husky" to "Duke",
        "boxer" to "Bear",
        "pug" to "Tucker"
    )

    // Pet type categorization
    private val dogBreeds = setOf(
        "labrador_retriever", "german_shepherd", "golden_retriever", "bulldog",
        "beagle", "poodle", "siberian_husky", "boxer", "pug"
    )

    private val catBreeds = setOf(
        "siamese", "persian_cat", "maine_coon", "ragdoll", "bengal",
        "sphynx", "british_shorthair", "russian_blue"
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission is required to use this feature", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_know_your_pet)

        initializeViews()
        setupBottomNavigation()
        setupClickListeners()

        // Initialize camera executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // Load the TFLite model and labels asynchronously
        loadModelAndLabels()
    }

    private fun initializeViews() {
        viewFinder = findViewById(R.id.viewFinder)
        btnCapture = findViewById(R.id.btnCapture)
        btnRetake = findViewById(R.id.btnRetake)
        resultCardView = findViewById(R.id.resultCardView)
        tvBreed = findViewById(R.id.tvBreed)
        tvName = findViewById(R.id.tvName)
        tvConfidence = findViewById(R.id.tvConfidence)
        progressBar = findViewById(R.id.progressBar)

        // Hide result card initially and disable capture until model is loaded
        resultCardView.visibility = View.GONE
        btnCapture.isEnabled = false
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation4)
        bottomNav.selectedItemId = R.id.nav_socialise
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_socialise -> true
                R.id.nav_shop -> {
                    startActivity(Intent(this, ShopActivity::class.java))
                    true
                }
                R.id.nav_services -> {
                    startActivity(Intent(this, ServicesActivity::class.java))
                    true
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeScreen::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupClickListeners() {
        btnCapture.setOnClickListener {
            takePhotoAndClassify()
        }

        btnRetake.setOnClickListener {
            hideResultsAndRestartCamera()
        }
    }

    private fun loadModelAndLabels() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val modelFilename = "pet_breed_model.tflite"
                val labelsFilename = "labels.txt"

                // Check if model file exists
                try {
                    val fileList = assets.list("")
                    if (fileList?.contains(modelFilename) != true) {
                        throw FileNotFoundException("Model file '$modelFilename' not found in assets")
                    }
                    Log.d("ModelLoad", "Model file found in assets")
                } catch (e: Exception) {
                    Log.e("ModelLoad", "Error checking assets: ${e.message}")
                    throw e
                }

                // Create classifier with error handling
                val options = ImageClassifier.ImageClassifierOptions.builder()
                    .setMaxResults(3)
                    .setScoreThreshold(0.3f)
                    .build()

                classifier = ImageClassifier.createFromFileAndOptions(
                    this@KnowYourPetActivity,
                    modelFilename,
                    options
                )

                // Load labels
                labels = loadLabelsFromAssets(labelsFilename)
                Log.d("ModelLoad", "Labels loaded: ${labels.size}")

                if (labels.isEmpty()) {
                    Log.w("ModelLoad", "Labels file is empty or not found")
                }

                Log.d("ModelLoad", "Model and labels loaded successfully")

                runOnUiThread {
                    btnCapture.isEnabled = true
                    Toast.makeText(
                        this@KnowYourPetActivity,
                        "Ready to detect pets!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Log.e("ModelLoad", "Model loading failed: ${e.message}", e)
                runOnUiThread {
                    showModelErrorDialog(e.message ?: "Unknown error")
                }
            }
        }
    }

    private fun showModelErrorDialog(message: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Model Loading Failed")
            .setMessage("Failed to load pet recognition model: $message\n\nPlease make sure:\n1. pet_breed_model.tflite is in assets folder\n2. labels.txt is in assets folder")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                // Enable mock mode
                btnCapture.isEnabled = true
            }
            .show()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("Camera", "Camera start failed: ${exc.message}", exc)
                runOnUiThread {
                    Toast.makeText(this, "Camera start failed: ${exc.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhotoAndClassify() {
        if (!::imageCapture.isInitialized) {
            Toast.makeText(this, "Camera not ready", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading progress
        progressBar.visibility = View.VISIBLE
        btnCapture.isEnabled = false

        imageCapture.takePicture(ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    try {
                        val bitmap = imageProxy.toBitmap()
                        classifyBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.e("Classification", "Error converting image: ${e.message}", e)
                        runOnUiThread {
                            progressBar.visibility = View.GONE
                            btnCapture.isEnabled = true
                            Toast.makeText(
                                this@KnowYourPetActivity,
                                "Error processing image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } finally {
                        imageProxy.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Camera", "Capture error: ${exception.message}", exception)
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        btnCapture.isEnabled = true
                        Toast.makeText(
                            this@KnowYourPetActivity,
                            "Capture failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun classifyBitmap(bitmap: Bitmap) {
        val localClassifier = classifier
        if (localClassifier == null) {
            Log.w("Classification", "Classifier is null, using mock classification")
            runOnUiThread {
                progressBar.visibility = View.GONE
                btnCapture.isEnabled = true
                mockClassification()
            }
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Convert bitmap to TensorImage
                val tensorImage = TensorImage.fromBitmap(bitmap)

                // Perform classification
                val results = localClassifier.classify(tensorImage)

                // Process results
                if (results.isNotEmpty() && results[0].categories.isNotEmpty()) {
                    val topCategory = results[0].categories[0]
                    val label = topCategory.label
                    val confidence = (topCategory.score * 100).toInt()

                    Log.d("Classification", "Top result: $label (${confidence}%)")

                    // Map the label to a breed name and type
                    val mappedBreed = mapLabel(label)
                    val petType = getPetType(mappedBreed)
                    val petName = petNames[mappedBreed] ?: generatePetName(petType)

                    runOnUiThread {
                        displayResults(mappedBreed, petName, confidence, petType)
                    }
                } else {
                    Log.w("Classification", "No classification results")
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        btnCapture.isEnabled = true
                        Toast.makeText(
                            this@KnowYourPetActivity,
                            "No pet detected. Try again with a clearer image.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("Classification", "Classification error: ${e.message}", e)
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    btnCapture.isEnabled = true
                    mockClassification()
                }
            }
        }
    }

    private fun mockClassification() {
        // Mock classification for testing without model
        val mockBreeds = listOf("Labrador Retriever", "German Shepherd", "Siamese Cat", "Persian Cat")
        val randomBreed = mockBreeds.random()
        val confidence = (70..95).random()
        val petType = if (randomBreed.contains("Cat")) "Cat" else "Dog"
        val petName = generatePetName(petType)

        displayResults(randomBreed, petName, confidence, petType)

        Toast.makeText(this, "Using demo mode - model not loaded", Toast.LENGTH_LONG).show()
    }

    private fun displayResults(breed: String, name: String, confidence: Int, petType: String) {
        runOnUiThread {
            progressBar.visibility = View.GONE
            btnCapture.isEnabled = true

            tvBreed.text = breed.replace("_", " ").replaceFirstChar { it.uppercase() }
            tvName.text = name
            tvConfidence.text = "$confidence%"

            // Add emoji based on pet type
            val emoji = when (petType.lowercase()) {
                "dog" -> "🐕"
                "cat" -> "🐈"
                else -> "🐾"
            }

            tvBreed.text = "$emoji ${tvBreed.text}"

            resultCardView.visibility = View.VISIBLE
        }
    }

    private fun hideResultsAndRestartCamera() {
        resultCardView.visibility = View.GONE
        startCamera()
    }

    private fun mapLabel(rawLabel: String): String {
        return try {
            // If label is an index
            val idx = rawLabel.toIntOrNull()
            if (idx != null && labels.isNotEmpty() && idx in labels.indices) {
                labels[idx]
            } else {
                // Try to find matching label in our known breeds
                val cleaned = rawLabel.trim().lowercase().replace(" ", "_")
                // Check if it matches any known breed
                val knownBreeds = dogBreeds + catBreeds
                knownBreeds.find { cleaned.contains(it, ignoreCase = true) } ?: rawLabel
            }
        } catch (e: Exception) {
            rawLabel
        }
    }

    private fun getPetType(breed: String): String {
        val cleanBreed = breed.lowercase().replace(" ", "_")
        return when {
            dogBreeds.any { cleanBreed.contains(it, ignoreCase = true) } -> "Dog"
            catBreeds.any { cleanBreed.contains(it, ignoreCase = true) } -> "Cat"
            else -> "Pet"
        }
    }

    private fun generatePetName(petType: String): String {
        val defaultNames = when (petType.lowercase()) {
            "dog" -> listOf("Buddy", "Max", "Charlie", "Cooper", "Rocky")
            "cat" -> listOf("Luna", "Bella", "Lucy", "Daisy", "Molly")
            else -> listOf("Sparky", "Coco", "Bailey", "Sophie", "Zoe")
        }
        return defaultNames.random()
    }

    private fun loadLabelsFromAssets(filename: String): List<String> {
        return try {
            assets.open(filename).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readLines().map { it.trim() }.filter { it.isNotEmpty() }
                }
            }
        } catch (e: Exception) {
            Log.e("LabelsLoad", "Failed to load labels: ${e.message}")
            emptyList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        classifier?.close()
        classifier = null
    }
}