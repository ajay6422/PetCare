package com.example.petcare

class PetBot {

    fun getResponse(userMessage: String): String {
        val message = userMessage.lowercase().trim()

        return when {
            // Greetings
            containsAny(message, arrayOf("hello", "hi", "hey")) ->
                "Hello! I'm your Pet Care Assistant. How can I help you with your pet today?"

            // Dog-related questions
            containsAll(message, arrayOf("dog", "food")) ->
                "Dogs should eat high-quality dog food appropriate for their age, size, and activity level. " +
                        "Puppies need puppy food, adults need adult food, and seniors need senior formula. " +
                        "Avoid chocolate, grapes, onions, garlic, and xylitol as they're toxic to dogs."

            containsAll(message, arrayOf("dog", "exercise")) ->
                "Most dogs need 30 minutes to 2 hours of exercise daily. " +
                        "High-energy breeds like Border Collies need more, while low-energy breeds like Bulldogs need less. " +
                        "Include walks, playtime, and mental stimulation."

            containsAll(message, arrayOf("dog", "groom")) ->
                "Dogs typically need bathing every 4-6 weeks, but this varies by breed. " +
                        "Brush regularly to prevent matting. Trim nails every 3-4 weeks. " +
                        "Clean ears weekly and brush teeth daily if possible."

            // Cat-related questions
            containsAll(message, arrayOf("cat", "food")) ->
                "Cats are obligate carnivores and need high-protein food. " +
                        "Wet food helps with hydration, while dry food helps with dental health. " +
                        "Avoid feeding dog food, chocolate, onions, garlic, and alcohol."

            containsAll(message, arrayOf("cat", "litter")) ->
                "Provide one litter box per cat plus one extra. Scoop daily and change litter weekly. " +
                        "Place in quiet, accessible locations. Most cats prefer unscented, clumping litter."

            containsAll(message, arrayOf("cat", "groom")) ->
                "Cats groom themselves but benefit from brushing 2-3 times weekly. " +
                        "Long-haired cats need daily brushing to prevent mats. " +
                        "Trim nails every 2-3 weeks and clean ears as needed."

            // Rabbit-related questions
            containsAll(message, arrayOf("rabbit", "food")) ->
                "Rabbits need unlimited timothy hay (70% of diet), fresh vegetables (20%), " +
                        "and limited pellets (10%). Good veggies include romaine lettuce, carrot tops, and herbs. " +
                        "Avoid iceberg lettuce and sudden diet changes."

            containsAll(message, arrayOf("rabbit", "care")) ->
                "Rabbits need a spacious enclosure, daily exercise time, and mental stimulation. " +
                        "They're social animals and often do better in pairs. " +
                        "Provide hiding spots and chew toys to prevent boredom."

            // Health questions
            containsAny(message, arrayOf("vaccine", "vaccination")) ->
                "Core vaccines for dogs: rabies and DHPP. For cats: rabies and FVRCP. " +
                        "Puppies and kittens need a series of shots. Adult pets need boosters. " +
                        "Always consult your veterinarian for specific recommendations."

            containsAny(message, arrayOf("emergency", "urgent")) ->
                "If your pet shows these signs, seek immediate vet care: " +
                        "Difficulty breathing, seizures, collapse, bleeding, bloated abdomen, " +
                        "inability to urinate, or ingestion of toxic substances."

            // Training questions
            containsAny(message, arrayOf("train", "training", "behavior")) ->
                "Use positive reinforcement with treats and praise. Keep sessions short (5-15 minutes). " +
                        "Be consistent with commands. Socialize puppies and kittens early. " +
                        "Address behavior issues promptly with professional help if needed."

            // General pet care
            containsAny(message, arrayOf("new pet", "adopt")) ->
                "Before getting a pet: Research the breed/species, pet-proof your home, " +
                        "gather supplies, find a veterinarian, and prepare for a lifetime commitment. " +
                        "Consider adoption from shelters or rescue groups."

            // Fallback responses
            else -> getFallbackResponse(message)
        }
    }

    private fun containsAny(message: String, keywords: Array<String>): Boolean {
        return keywords.any { message.contains(it, ignoreCase = true) }
    }

    private fun containsAll(message: String, keywords: Array<String>): Boolean {
        return keywords.all { message.contains(it, ignoreCase = true) }
    }

    private fun getFallbackResponse(message: String): String {
        return when {
            containsAny(message, arrayOf("dog", "puppy")) ->
                "I'd be happy to help with dog care! You can ask me about dog food, exercise, grooming, training, or health topics."

            containsAny(message, arrayOf("cat", "kitten")) ->
                "I can help with cat care questions! Ask me about cat food, litter training, grooming, behavior, or health concerns."

            containsAny(message, arrayOf("rabbit", "bunny")) ->
                "I have information about rabbit care! Feel free to ask about rabbit diet, housing, behavior, or health needs."

            containsAny(message, arrayOf("bird", "fish", "hamster", "guinea", "reptile")) ->
                "I have basic information about various pets! Could you be more specific about what you'd like to know?"

            else ->
                "I'm here to help with pet care questions! You can ask me about: " +
                        "• Food and nutrition\n• Grooming needs\n• Exercise requirements\n• Health concerns\n• Training tips\n• Behavior issues\n" +
                        "Just let me know what pet you have and what you'd like to know!"
        }
    }
}