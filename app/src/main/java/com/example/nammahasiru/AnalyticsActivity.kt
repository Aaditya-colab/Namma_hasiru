package com.example.nammahasiru

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_analytics)

        val db = AppDatabase.get(this)

        db.dao().getAll().observe(this) { list ->
            val alive = list.count { it.status == "Alive" }
            val dead = list.count { it.status == "Dead" }
            val infected = list.count { it.status == "Infected" }

            findViewById<TextView>(R.id.alive).text = "Alive: $alive"
            findViewById<TextView>(R.id.dead).text = "Dead: $dead"
            findViewById<TextView>(R.id.infected).text = "Infected: $infected"

            val best = list.groupBy { it.species }
                .maxByOrNull { it.value.count { p -> p.status == "Alive" } }

            findViewById<TextView>(R.id.bestSpecies).text =
                "Best Species: ${best?.key ?: "-"}"
        }
    }
}
