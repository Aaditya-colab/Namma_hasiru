package com.example.nammahasiru

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_update)

        val db = AppDatabase.get(this)
        val id = intent.getIntExtra("id", -1)

        val spinner = findViewById<Spinner>(R.id.statusSpinner)
        val notesInput = findViewById<EditText>(R.id.notes)
        val updateBtn = findViewById<Button>(R.id.updateBtn)

        val options = listOf("Alive", "Dried", "Infected", "Dead", "Pending")

        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        // Pre-fill data
        CoroutineScope(Dispatchers.IO).launch {
            val plant = db.dao().getById(id)
            withContext(Dispatchers.Main) {
                plant?.let {
                    notesInput.setText(it.notes)
                    val index = options.indexOf(it.status)
                    if (index >= 0) spinner.setSelection(index)
                }
            }
        }

        updateBtn.setOnClickListener {
            val status = spinner.selectedItem.toString()
            val notes = notesInput.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val plant = db.dao().getById(id)
                plant?.let {
                    db.dao().update(it.copy(status = status, notes = notes))
                }
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }
}
