package com.loc.searchapp.presentation.mainActivity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.loc.searchapp.presentation.mainActivity.components.AppBackground
import com.loc.searchapp.presentation.nvgraph.NavGraph
import com.loc.searchapp.ui.theme.SearchAppTheme
import com.loc.searchapp.utils.LanguagePreference
import com.loc.searchapp.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun attachBaseContext(newBase: Context) {
        val language = LanguagePreference.getLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.splashCondition
            }
        }

        setContent {
            SearchAppTheme {
                AppBackground {
                    NavGraph(startDestination = viewModel.startDestination)
                }
            }
        }
    }
}