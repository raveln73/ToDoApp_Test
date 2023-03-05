package com.example.todoapp_test

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp_test.DTO.Todo
import com.example.todoapp_test.databinding.ActivityEditTodoBinding
import java.text.SimpleDateFormat

class EditTodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditTodoBinding
    private var todo: Todo?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("type")

        if (type.equals("ADD")) {
            binding.SaveBtn.text = "추가하기"
        } else {
            todo = intent.getSerializableExtra("item")as Todo?
            binding.TodoTitle.setText(todo!!.title)
            binding.TodoContent.setText(todo!!.content)
            binding.SaveBtn.text = "수정하기"
        }

        binding.SaveBtn.setOnClickListener {
            val title = binding.TodoTitle.text.toString()
            val content = binding.TodoContent.text.toString()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

            if (type.equals("ADD")) {
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    val todo = Todo(0, title, content, currentDate, false)
                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag", 0)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else {
                if (title.isNotEmpty() && content.isNotEmpty()){
                    val todo = Todo(todo!!.id, title, content, currentDate, todo!!.isChecked)


                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag", 0)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

        }

    }
}