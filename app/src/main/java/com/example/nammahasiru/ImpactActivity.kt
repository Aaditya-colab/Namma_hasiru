package com.example.nammahasiru

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ImpactActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_impact)

        val db = AppDatabase.get(this)

        db.dao().getAll().observe(this) { list ->
            val total = list.size
            // simple estimation: 21kg CO2 per tree per year
            val co2 = total * 21

            findViewById<TextView>(R.id.trees).text = "Trees Planted: $total"
            findViewById<TextView>(R.id.co2).text = "Estimated CO₂ Absorption:\n$co2 kg/year"
        }
    }
}
