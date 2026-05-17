package com.example.nammahasiru

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.add).setOnClickListener {
            startActivity(Intent(this, AddPlantActivity::class.java))
        }

        findViewById<Button>(R.id.list).setOnClickListener {
            startActivity(Intent(this, PlantListActivity::class.java))
        }

        findViewById<Button>(R.id.profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<Button>(R.id.analytics).setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }

        findViewById<Button>(R.id.impact).setOnClickListener {
            startActivity(Intent(this, ImpactActivity::class.java))
        }

        findViewById<Button>(R.id.about).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}
