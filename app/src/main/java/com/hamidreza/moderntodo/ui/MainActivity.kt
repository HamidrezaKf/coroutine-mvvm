package com.hamidreza.moderntodo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hamidreza.moderntodo.R
import com.hamidreza.moderntodo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}