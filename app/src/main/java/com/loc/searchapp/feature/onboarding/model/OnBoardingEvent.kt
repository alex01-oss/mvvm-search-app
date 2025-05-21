package com.loc.searchapp.feature.onboarding.model

sealed class OnBoardingEvent {
    data object SaveAppEntry : OnBoardingEvent()
}