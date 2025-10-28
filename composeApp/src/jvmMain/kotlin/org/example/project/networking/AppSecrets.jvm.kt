package org.example.project.networking

actual object AppSecrets {
    actual val apiKey: String
        get() {
            System.getProperty("apiKey")?.takeIf { it.isNotBlank() }?.let { return it }
            System.getenv("API_KEY")?.takeIf { it.isNotBlank() }?.let { return it }
            val fromGenerated = runCatching {
                val cls = Class.forName("org.example.project.ApiKeys")
                val field = cls.getDeclaredField("API_KEY")
                field.isAccessible = true
                field.get(null) as? String
            }.getOrNull()
            if (!fromGenerated.isNullOrBlank()) return fromGenerated
            return ""
        }
}