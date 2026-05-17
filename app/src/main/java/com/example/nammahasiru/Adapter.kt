package com.example.nammahasiru

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Adapter(val list: List<Plant>, val ctx: Context) :
    RecyclerView.Adapter<Adapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(LayoutInflater.from(ctx).inflate(R.layout.item, p, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(h: VH, i: Int) {

        val p = list[i]

        Glide.with(ctx).load(Uri.parse(p.imageUri))
            .into(h.itemView.findViewById<ImageView>(R.id.img))

        h.itemView.findViewById<TextView>(R.id.species).text = p.species
        
        // OPEN DETAILS
        h.itemView.setOnClickListener {
            val intent = Intent(ctx, DetailsActivity::class.java)
            intent.putExtra("id", p.id)
            ctx.startActivity(intent)
        }

        val statusText = h.itemView.findViewById<TextView>(R.id.status)
        statusText.text = "Status: ${p.status}"

        // Color coding status
        when (p.status) {
            "Alive" -> statusText.setTextColor(Color.parseColor("#1B5E20")) // Dark Green
            "Dried" -> statusText.setTextColor(Color.parseColor("#FBC02D")) // Amber/Yellow
            "Infected" -> statusText.setTextColor(Color.parseColor("#7B1FA2")) // Purple
            "Dead" -> statusText.setTextColor(Color.RED)
            else -> statusText.setTextColor(Color.GRAY)
        }

        // MAP (browser)
        h.itemView.findViewById<Button>(R.id.map).setOnClickListener {
            val url = "https://www.google.com/maps?q=${p.latitude},${p.longitude}"
            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        // UPDATE
        h.itemView.findViewById<Button>(R.id.update).setOnClickListener {
            val intent = Intent(ctx, UpdateActivity::class.java)
            intent.putExtra("id", p.id)
            ctx.startActivity(intent)
        }

        // DELETE
        h.itemView.findViewById<Button>(R.id.delete).setOnClickListener {
            AlertDialog.Builder(ctx)
                .setTitle("Delete Plant")
                .setMessage("Are you sure you want to delete this plantation record?")
                .setPositiveButton("Delete") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val db = AppDatabase.get(ctx)
                        db.dao().delete(p)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
