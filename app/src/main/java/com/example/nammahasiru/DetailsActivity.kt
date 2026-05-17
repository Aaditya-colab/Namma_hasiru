package com.example.nammahasiru

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_details)

        val id = intent.getIntExtra("id", -1)
        val db = AppDatabase.get(this)

        CoroutineScope(Dispatchers.IO).launch {
            val plant = db.dao().getById(id)
            withContext(Dispatchers.Main) {
                plant?.let {
                    Glide.with(this@DetailsActivity)
                        .load(Uri.parse(it.imageUri))
                        .into(findViewById<ImageView>(R.id.img))

                    findViewById<TextView>(R.id.species).text = it.species
                    findViewById<TextView>(R.id.status).text = "Status: ${it.status}"
                    findViewById<TextView>(R.id.notes).text = it.notes

                    findViewById<Button>(R.id.map).setOnClickListener {
                        val url = "https://www.google.com/maps?q=${plant.latitude},${plant.longitude}"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                }
            }
        }
    }
}
