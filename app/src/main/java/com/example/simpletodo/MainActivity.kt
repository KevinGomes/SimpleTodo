package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //remove item
                listOfTasks.removeAt(position)
                //notify adapter of change
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        //detect when user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            Log.i("Test","User has clicked the button")
//        }

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        //connect recyclerView with adapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // allow user to enter tasks using the button and text field
        findViewById<Button>(R.id.button).setOnClickListener {

            //get the text that the user enters
            val userInputTask = inputTextField.text.toString()

            //add string to listOfItems
            listOfTasks.add(userInputTask)

            //notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //clear Text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //save inputted data
    //by writing and read from a file

    //method to get data file
    fun getDataFile() : File {
        return File(filesDir, "toDoData.txt")
    }

    //load the items from file into app
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    //save items from the app into the file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}
