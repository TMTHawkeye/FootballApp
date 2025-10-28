package com.example.footballapp.activities.onboarding

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.footballapp.Helper.noInternetDialog
import com.example.footballapp.Helper.setupNoInternetDialog
import com.example.footballapp.R
import com.example.footballapp.utils.LocaleHelper
import com.example.footballapp.utils.NetworkConnectionLiveData
import com.example.footballapp.utils.SharedPrefrence

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

//    override fun attachBaseContext(base: Context) {
//        var sharedPreference = SharedPrefrence(base)
//        val language = sharedPreference.getingString("languageCode", "en")
//        when (language) {
//            "en" -> LocaleHelper.setLocale(base, "en")
//            "fr" -> LocaleHelper.setLocale(base, "fr")
//            "es" -> LocaleHelper.setLocale(base, "es")
//            "zh" -> LocaleHelper.setLocale(base, "zh")
//            "hi" -> LocaleHelper.setLocale(base, "hi")
//            "ar" -> LocaleHelper.setLocale(base, "ar")
//             "pt" -> LocaleHelper.setLocale(base, "pt")
//
//        }
//
//        super.attachBaseContext(base)
//    }

    override fun attachBaseContext(base: Context) {
        val sharedPreference = SharedPrefrence(base)
        val language = sharedPreference.getingString("languageCode", "en")
        val context = LocaleHelper.setLocale(base, language)
        super.attachBaseContext(context)
    }

}