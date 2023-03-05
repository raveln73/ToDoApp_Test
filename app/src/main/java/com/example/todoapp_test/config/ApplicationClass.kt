package com.example.todoapp_test.config

import android.app.Application
import com.example.todoapp_test.repository.TodoRepository

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        TodoRepository.initialize(this)
    }
}