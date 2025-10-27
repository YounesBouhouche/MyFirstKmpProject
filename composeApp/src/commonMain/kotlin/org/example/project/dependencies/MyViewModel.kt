package org.example.project.dependencies

import androidx.lifecycle.ViewModel

class MyViewModel(private val repo: MyRepo): ViewModel() {
    fun getHelloWorld(): String {
        return repo.helloWorld()
    }
}