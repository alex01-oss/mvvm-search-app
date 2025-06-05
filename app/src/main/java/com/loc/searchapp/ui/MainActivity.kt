package com.loc.searchapp.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.loc.searchapp.core.ui.theme.SearchAppTheme
import com.loc.searchapp.core.utils.LanguagePreference
import com.loc.searchapp.core.utils.LocaleHelper
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.ui.components.AppBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    override fun attachBaseContext(newBase: Context) {
        val language = LanguagePreference.getLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.splashCondition
            }
        }

        setContent {
            SearchAppTheme {
                AppBackground {
                    MainContent(
                        mainViewModel = mainViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}