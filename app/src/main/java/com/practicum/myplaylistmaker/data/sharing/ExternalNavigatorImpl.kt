package com.practicum.myplaylistmaker.data.sharing

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.practicum.myplaylistmaker.R

class ExternalNavigatorImpl(private val application: Application)  {
     fun shareLink(shareAppLink: String) {

        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.PractAdr))
        intentSend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSend)
    }

     fun openLink() {
        val intentAgreement3 =
            Intent(Intent.ACTION_VIEW, Uri.parse(application.getString(R.string.AgreementUrl)))
        intentAgreement3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentAgreement3)
    }

     fun openEmail() {
        val intentSendTo2 = Intent(Intent.ACTION_SENDTO)
        intentSendTo2.data = Uri.parse("mailto:")
        val email = application.getString(R.string.defaultEmail)
        intentSendTo2.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intentSendTo2.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.DefaultTextEmail))
        intentSendTo2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSendTo2)
    }
     fun getShareLink ():String {
        return "https://practicum.yandex.ru/profile/android-developer"
    }
}