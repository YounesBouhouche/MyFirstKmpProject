package org.example.project.dependencies

interface MyRepo {
    fun helloWorld(): String
}

class MyRepoImpl(
    private val dbClient: DbClient
): MyRepo {
    override fun helloWorld(): String {
        return "Hello world!"
    }
}