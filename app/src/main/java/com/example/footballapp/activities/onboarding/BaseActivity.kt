package com.example.footballapp.activities.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.footballapp.Helper.noInternetDialog
import com.example.footballapp.Helper.setupNoInternetDialog
import com.example.footballapp.R
import com.example.footballapp.utils.NetworkConnectionLiveData

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNoInternetDialog(this@BaseActivity)
        observeNetworkChanges()
    }

    private fun observeNetworkChanges() {
        val connectionLiveData = NetworkConnectionLiveData(applicationContext)
        connectionLiveData.observe(this, Observer { isConnected ->
            try {
                if (!isConnected) {
                    if (noInternetDialog?.isShowing == false) noInternetDialog?.show()
                } else {
                    if (noInternetDialog?.isShowing == true) noInternetDialog?.dismiss()
                }
            }catch(e: Exception){

            }
        })
    }
}