package com.loc.searchapp.presentation.onboarding.model

sealed class OnBoardingEvent {
    data object SaveAppEntry : OnBoardingEvent()
}