package com.example.nammahasiru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class PlantListActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_list)

        val db = AppDatabase.get(this)

        db.dao().getAll().observe(this) {
            findViewById<RecyclerView>(R.id.rv).adapter =
                Adapter(it, this)
        }
    }
}
