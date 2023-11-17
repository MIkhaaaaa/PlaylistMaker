package com.practicum.myplaylistmaker.domain.sharing.impl

import com.practicum.myplaylistmaker.domain.sharing.ExternalNavigator
import com.practicum.myplaylistmaker.domain.sharing.SharingInteractor
import com.practicum.myplaylistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return externalNavigator.getShapeLink()
    }

    private fun getTermsLink(): String {
        return externalNavigator.getTermsLink()
    }

    private fun getSupportEmailData(): EmailData {
        return externalNavigator.getSupportEmailData()
    }
}