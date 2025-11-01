package com.example.footballapp.activities.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.FootballViewModel
import com.example.footballapp.Helper.LANGUAGE_POSITION_KEY
import com.example.footballapp.Helper.PREF_NAME_LANGUAGE
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.adapters.LanguageAdapter
import com.example.footballapp.databinding.ActivityExitScreenBinding
import com.example.footballapp.databinding.ActivityLanguageBinding
import com.example.footballapp.interfaces.SelectedLanguageCallback
import com.example.footballapp.models.LanguageModel
import com.example.footballapp.utils.LocaleHelper
import com.example.footballapp.utils.SharedPrefrence
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class LanguageActivity : BaseActivity() , SelectedLanguageCallback {
    private lateinit var binding: ActivityLanguageBinding

    private val viewModel: FootballViewModel by viewModel()

    var selectedPosition = -1
    var intentFrom: String? = null
    var sharedPreference = SharedPrefrence(this)

    private lateinit var adapter: LanguageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intentFrom = intent?.getStringExtra("intentFrom")
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true // For dark icons (use with light backgrounds)
            // OR
            isAppearanceLightStatusBars = false // For light icons (use with dark backgrounds)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        intentFrom?.let{
//            binding.btnBack.visible()
//        }?:run{
//            binding.btnBack.invisible()
//        }

        val sharedPreferences =
            getSharedPreferences(PREF_NAME_LANGUAGE, Context.MODE_PRIVATE)
        val savedPosition =
            sharedPreferences.getInt(LANGUAGE_POSITION_KEY, selectedPosition)
        selectedPosition = savedPosition


        binding.btnDone.isEnabled = selectedPosition != -1
        binding.btnDone.alpha = if (selectedPosition != -1) 1f else 0.5f


//        if (savedInstanceState == null) {
//        viewModel.loadMatchesWithStages()
//        }

//        binding.btnDone.setOnClickListener {
//
//            startActivity(Intent(this@LanguageActivity, MainActivity::class.java))
//
//        }


        val languages = getLanguagesList()

        adapter = LanguageAdapter(this, languages, savedPosition,this)

        binding.rvLanguage.adapter = adapter
        binding.rvLanguage.layoutManager = LinearLayoutManager(this@LanguageActivity)


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (intentFrom==null) {
//                     finishAffinity()
//                    System.exit(0);
                    startActivity(Intent(this@LanguageActivity, MainActivity::class.java))
                } else {
                     finish()
                }
            }
        })

        binding.btnBack.setOnClickListener {
            if (intentFrom==null) {
//                     finishAffinity()
//                    System.exit(0);
                startActivity(Intent(this@LanguageActivity, MainActivity::class.java))
            } else {
                finish()
            }
        }



        binding.btnDone.setOnClickListener {

              Log.d("TAG_savedPosition", "savedPosition: $selectedPosition")
            if (selectedPosition != -1) {
                sharedPreferences.edit().putInt(LANGUAGE_POSITION_KEY, selectedPosition).apply()

                sharedPreference.putingString("languageCode", adapter.languageCode)
                sharedPreference.putingPosition("langPosition", selectedPosition)
                LocaleHelper.setLocale(this@LanguageActivity, adapter.languageCode)

                val intent = /*if (intentFrom != null) {*/
                    Intent(this, MainActivity::class.java)
//                } else {
//                    Intent(this, GuideActivity::class.java)
//                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                sharedPreferences.edit().putBoolean("LangPref", false).apply()

                startActivity(intent)
                finish()


            }
        }

    }

    fun getLanguagesList(): ArrayList<LanguageModel> {
        val languageList = ArrayList<LanguageModel>()
        languageList.add(
            LanguageModel(
                getString(R.string.english),
                getDrawable(R.drawable.english_img),
                "en",
                getString(R.string.selectEnglish)
            )
        )

        languageList.add(
            LanguageModel(
                getString(R.string.spanish),
                getDrawable(R.drawable.spanish_img),
                "es",
                getString(R.string.selectSpanish)

            )
        )
        languageList.add(
            LanguageModel(
                getString(R.string.russian),
                getDrawable(R.drawable.russian_img),
                "ru",
                getString(R.string.selectRussian)

            )
        )
        languageList.add(
            LanguageModel(
                getString(R.string.hindi),
                getDrawable(R.drawable.hindi_img),
                "hi",
                getString(R.string.selectHindi)

            )
        )

        languageList.add(
            LanguageModel(
                getString(R.string.portuguese),
                getDrawable(R.drawable.portugal_img),
                "pt",
                getString(R.string.selectPortuguese)

            )
        )

        languageList.add(
            LanguageModel(
                getString(R.string.arabic),
                getDrawable(R.drawable.arabic_img),
                "ar",
                getString(R.string.selectArabic)

            )
        )

        languageList.add(
            LanguageModel(
                getString(R.string.chinese),
                getDrawable(R.drawable.chinese_img),
                "zh",
                getString(R.string.selectChinese)

            )
        )
        return languageList
    }

    override fun languageSelected(position: Int) {
        this.selectedPosition = position

        binding.btnDone.apply {
            isEnabled = true
            isClickable = true
            alpha = 1f
        }

    }
}