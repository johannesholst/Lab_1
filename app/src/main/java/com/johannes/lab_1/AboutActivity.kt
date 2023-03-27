package com.johannes.lab_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.johannes.lab_1.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)   // Setup View
        setContentView(binding.root)



        val btnBack = binding.buttonBack


        btnBack.setOnClickListener {
            val intentNavigate = Intent(this, MainActivity::class.java)
            startActivity(intentNavigate)

        }
    }
}