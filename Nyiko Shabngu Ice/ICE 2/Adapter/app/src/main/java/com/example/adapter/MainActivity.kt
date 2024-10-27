package com.example.adapter

import ProductAdapter
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var productArray: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageID = intArrayOf(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d)
        val names = arrayOf("Lot 1", "Lot 2", "Lot 3", "Lot 4")
        val category = arrayOf("Car", "Bakkie", "Compact", "Sedan")

        productArray = ArrayList()

        for (i in names.indices) {
            productArray.add(Product(names[i], category[i], imageID[i]))
        }

        val listView: ListView = findViewById(R.id.ListView1)
        val myAdapter = ProductAdapter(this, productArray)
        listView.adapter = myAdapter

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}