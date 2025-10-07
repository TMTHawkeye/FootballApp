package com.example.footballapp.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.footballapp.BuildConfig
import com.example.footballapp.R


fun Fragment.feedBackMyFootbalEmail() {
    val feedbackEmail = "o3.apps@gmail.com"
    val addresses = arrayOf(feedbackEmail)
    try {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.type = "plain/text"
        emailIntent.setClassName(
            "com.google.android.gm",
            "com.google.android.gm.ComposeActivityGmail"
        )
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.please_share_your_feedback)
        )
        startActivity(emailIntent)
    } catch (e: Exception) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:" + addresses[0])
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.please_share_your_feedback)
        )
        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_using)))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(requireContext(), R.string.no_email_clients_installed, Toast.LENGTH_SHORT).show()
        }
    }
}


fun Activity.feedBackMyFootballEmail() {
    val feedbackEmail = "o3.apps@gmail.com"
    val addresses = arrayOf(feedbackEmail)
    try {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.type = "plain/text"
        emailIntent.setClassName(
            "com.google.android.gm",
            "com.google.android.gm.ComposeActivityGmail"
        )
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.please_share_your_feedback)
        )
        startActivity(emailIntent)


    } catch (e: Exception) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:" + addresses[0])
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.please_share_your_feedback)
        )
        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_using)))

        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_email_clients_installed, Toast.LENGTH_SHORT).show()
        }
    }
}



fun Activity.ratingMyFootballApp() {

    kotlin.runCatching {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
        )
        startActivity(intent)

    }

}



