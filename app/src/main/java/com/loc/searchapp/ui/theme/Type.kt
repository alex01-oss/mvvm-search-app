package com.loc.searchapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.loc.searchapp.R

val Monsterat = FontFamily(
    fonts = listOf(
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_semibold, FontWeight.SemiBold),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_extrabold, FontWeight.ExtraBold),
        Font(R.font.montserrat_black, FontWeight.Black),
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_extralight, FontWeight.ExtraLight),
        Font(R.font.montserrat_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.montserrat_bolditalic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.montserrat_blackitalic, FontWeight.Black, FontStyle.Italic),
        Font(R.font.montserrat_mediumitalic, FontWeight.Medium, FontStyle.Italic),
        Font(R.font.montserrat_lightitalic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.montserrat_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
        Font(R.font.montserrat_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(R.font.montserrat_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    )
)

val Typography = Typography(
    displaySmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Monsterat,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 32.sp,
        fontFamily = Monsterat,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = Monsterat,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = Monsterat,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 13.sp,
        fontFamily = Monsterat,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp,
    )
)