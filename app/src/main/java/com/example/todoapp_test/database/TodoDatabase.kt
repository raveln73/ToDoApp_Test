package com.example.todoapp_test.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp_test.DAO.TodoDao
import com.example.todoapp_test.DTO.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase(){
    abstract fun todoDao(): TodoDao

}