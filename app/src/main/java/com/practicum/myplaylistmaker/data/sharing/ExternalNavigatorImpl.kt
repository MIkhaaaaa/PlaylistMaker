package com.practicum.myplaylistmaker.data.sharing

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.domain.sharing.ExternalNavigator
import com.practicum.myplaylistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val application: Application): ExternalNavigator {
    override fun shareLink(shareAppLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openLink(offer: String) {
        val url = Uri.parse(offer)
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openEmail(email: EmailData) {
        val message = application.getString(R.string.DefaultTextEmail)
        val subject = application.getString(R.string.subjectEmail)
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(shareIntent)
    }

    override fun getShapeLink(): String {
        return application.getString(R.string.UrlPracticum)
    }

    override fun getTermsLink(): String {
        return application.getString(R.string.AgreementUrl)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(application.getString(R.string.defaultEmail))
    }
}