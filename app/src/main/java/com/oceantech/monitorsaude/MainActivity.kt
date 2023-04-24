package com.oceantech.monitorsaude

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oceantech.monitorsaude.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMenuRemedios.setOnClickListener {
            val intent = Intent(this, MedicamentoActivity::class.java)
            startActivity(intent)
        }
    }
}