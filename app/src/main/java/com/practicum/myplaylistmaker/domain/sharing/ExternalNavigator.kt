package com.practicum.myplaylistmaker.domain.sharing

import com.practicum.myplaylistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(offer: String)
    fun openEmail(email: EmailData)
    fun getShapeLink(): String
    fun getTermsLink(): String
    fun getSupportEmailData(): EmailData
}