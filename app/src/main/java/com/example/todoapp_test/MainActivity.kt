package com.example.todoapp_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp_test.DTO.Todo
import com.example.todoapp_test.adapter.TodoAdapter
import com.example.todoapp_test.databinding.ActivityMainBinding
import com.example.todoapp_test.viewmodel.TodoviewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var todoViewModel: TodoviewModel
    lateinit var todoAdapter: TodoAdapter

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == RESULT_OK){
            val todo = it.data?.getSerializableExtra("todo")as Todo

            when(it.data?.getIntExtra("flag", -1)){
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        todoViewModel.insert(todo)
                    }
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }

                1 -> {
                    CoroutineScope(Dispatchers.IO).launch{
                        todoViewModel.update(todo)
                    }
                    Toast.makeText(this, "수정되었습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        todoViewModel = ViewModelProvider(this)[TodoviewModel::class.java]

        binding.fabAdd.setOnClickListener{
            val intent = Intent(this, EditTodoActivity::class.java).apply{
                putExtra("type","ADD")
            }
            requestActivity.launch(intent)
        }

        todoViewModel.todoList.observe(this){
            todoAdapter.update(it)
        }

        todoAdapter = TodoAdapter(this)
        binding.TodoList.layoutManager = LinearLayoutManager(this)
        binding.TodoList.adapter = todoAdapter

        todoAdapter.setItemCheckBoxClickListener(object : TodoAdapter.ItemCheckBoxClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch{
                    val todo = todoViewModel.getOne(itemId)
                    todo.isChecked = !todo.isChecked
                    todoViewModel.update(todo)
                }
            }
        })

        todoAdapter.setItemClickListener(object: TodoAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                Toast.makeText(this@MainActivity, "$itemId", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getOne(itemId)

                    val intent = Intent(this@MainActivity, EditTodoActivity::class.java).apply {
                        putExtra("type", "EDIT")
                        putExtra("item", todo)
                    }
                    requestActivity.launch(intent)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show()
                todoViewModel.todoList.value!!.forEach {
                    if (it.isChecked) {
                        todoViewModel.delete(it)
                    }
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}