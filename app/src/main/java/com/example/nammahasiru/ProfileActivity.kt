package com.example.nammahasiru

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_profile)

        val db = AppDatabase.get(this)

        db.dao().getAll().observe(this) { list ->
            val total = list.size
            val alive = list.count { it.status == "Alive" }

            val rate = if (total > 0) (alive * 100 / total) else 0

            findViewById<TextView>(R.id.totalPlants).text = "Total Plants: $total"
            findViewById<TextView>(R.id.survivalRate).text = "Survival Rate: $rate%"
        }
    }
}
