package com.example.footballapp



import android.app.Application
import android.content.Context
import com.example.footballapp.utils.LocaleHelper
import com.example.footballapp.utils.SharedPrefrence
import com.example.footballapp.utils.appMi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(footballApiModule, appMi)
        }
    }

//    override fun attachBaseContext(base: Context?) {
//        val sharedPreference: SharedPrefrence = SharedPrefrence(base!!)
//        val languageCode: String = sharedPreference.getingString("languageCode", "en")
//        when (languageCode) {
//            "en" -> LocaleHelper.setLocale(base, "en")
//            "fr" -> LocaleHelper.setLocale(base, "fr")
//            "es" -> LocaleHelper.setLocale(base, "es")
//            "zh" -> LocaleHelper.setLocale(base, "zh")
//            "hi" -> LocaleHelper.setLocale(base, "hi")
//            "ar" -> LocaleHelper.setLocale(base, "ar")
//             "pt" -> LocaleHelper.setLocale(base, "pt")
//        }
//        super.attachBaseContext(base)
//    }

    override fun attachBaseContext(base: Context) {
        val sharedPreference = SharedPrefrence(base)
        val languageCode = sharedPreference.getingString("languageCode", "en")
        val context = LocaleHelper.setLocale(base, languageCode)
        super.attachBaseContext(context)
    }

}
