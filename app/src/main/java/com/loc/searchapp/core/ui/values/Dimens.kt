package com.loc.searchapp.core.ui.values

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Dimens {

    // --- Відступи (кратно 4dp) ---
    // MediumPadding1 (24dp) -> переводимо в 16dp як основний (BasePadding) або 32dp.
    // Якщо 24dp використовується для "середнього" - це 6 * 4dp. Залишаємо його, якщо є чітке використання.
    // Але для суворої 16dp-сітки, краще 16, 32, 48, 64.
    // Припускаємо, що BasePadding (16dp) тепер є основним горизонтальним/вертикальним.
    val MediumPadding1 = 16.dp // Було 24dp. Тепер це основний "середній" падінг (16dp). Це важливо!
    val MediumPadding2 = 32.dp // Було 30dp. Округлюємо до найближчого кратного 4dp.
    val ExtraSmallPadding = 4.dp // Було 4dp. Залишається.
    val ExtraSmallPadding2 = 8.dp // Було 6dp. Округлюємо до 8dp (кратність 4dp).
    val SmallPadding = 16.dp // Було 12dp. Округлюємо до 16dp (кратність 4dp і основний відступ).
    val SmallPadding2 = 8.dp // Було 8dp. Залишається.
    val BasePadding = 16.dp // Було 16dp. Залишається як основний.

    // --- Розміри Іконок (кратно 4dp, згідно UI norms 24/30, але прив'язуємо до 4dp) ---
    // 24dp - це стандартний розмір іконок в M3.
    val IconSize = 24.dp     // Було 24dp. Залишається.
    val IconSize2 = 32.dp    // Було 32dp. Залишається.

    // --- Розміри Елементів UI (кратно 4dp) ---
    val ProductCardSize = 96.dp // Було 96dp (16dp * 6). Залишається.
    val ArticleImageHeight = 252.dp // Було 250dp. Змінюємо на 252dp (63 * 4dp), найближче кратне 4.
    val AvatarHeight = 80.dp // Було 80dp (16dp * 5). Залишається.
    val InputFieldHeight = 48.dp // Було 48dp (16dp * 3). Стандарт M3. Залишається.
    val LogoHeight = 132.dp // Було 132dp. Залишається (33 * 4dp).
    val TopLogoHeight = 48.dp // Було 48dp (16dp * 3). Залишається.
    val PostImageHeight = 200.dp // Було 200dp (25 * 8dp або 50 * 4dp). Залишається.
    val PagerHeight = 320.dp // Було 320dp (16dp * 20). Залишається.
    val TextBarHeight = 72.dp // Було 70dp. Змінюємо на 72dp (18 * 4dp), найближче кратне 4.
    val CategoryHeight = 60.dp // Було 60dp. Змінюємо на 60 (15 * 4dp). Залишається.

    // --- Розміри для Bottom Navigation (адаптуємо) ---
    val NavBarHeight = 56.dp // Було 56dp. Стандарт M3 NavigationBar. Залишається.
    val ActiveButtonSize = 64.dp // Було 64dp. Залишається (відповідає 16dp * 4).
    // BottomNavMargin - це додатковий відступ, який робить "острівець".
    // Для строгості, можемо зробити його меншим або кратним 16.
    // 40dp (10 * 4dp) - залишаємо, якщо так задумано, або змінюємо на 32dp (2*16dp)
    val BottomNavMargin = 32.dp // Було 40dp. Зменшуємо до 32dp для більшої консистентності з 16dp-сіткою.

    // --- Типографія (sp) ---
    val TitleSize = 24.sp // Було 24.sp. Залишається.
    val TextSize = 16.sp // Було 14.sp. Збільшуємо до 16.sp (типографіка часто 12/14/16/20/24).

    // --- Корнери (Уніфіковані, переважно 4.dp для "строгого" стилю) ---
    val StrongCorner = 4.dp // Було 4dp. Це базовий кут для строгого стилю.
    val SmallCorner = 2.dp // Було 2dp. Змінюємо на 4dp для уніфікації.
    val ExtraSmallCorner = 1.dp // Було 1dp. Змінюємо на 4dp для уніфікації.
    val DefaultCorner = 8.dp // Було 8dp. Це більший кут, але все ще строгий. Залишаємо.
    val ButtonCorner = 8.dp // Було 6dp. Змінюємо на 8dp для уніфікації з DefaultCorner.

    // --- Інші елементи ---
    val IndicatorSize = 16.dp // Було 14dp. Округлюємо до 16dp.
    val PageIndicatorWidth = 56.dp // Було 52dp. Округлюємо до 56dp (14 * 4dp).
    val BorderStroke = 2.dp // Було 2dp. Залишається.
    val ShimmerHeight2 = 16.dp // Було 16dp. Залишається.
    val PostsSpacerSize = 80.dp // Було 80dp. Залишається.
    val AboutLogoSize = 96.dp // Було 96dp. Залишається.
    val AboutTextWidth = 200.dp // Було 200dp. Залишається.
    val LargePadding = 300.dp // Було 300dp. Залишається (75 * 4dp).
    val TopBarPadding = 4.dp // Було 5dp. Змінюємо на 4dp.
    val BurgerPadding = 4.dp // Було 6dp. Змінюємо на 4dp.
    val BurgerIconSize = 24.dp // Було 18dp. Змінюємо на 24dp для відповідності IconSize.
    val DrawerWidth = 320.dp // Було 300dp. Змінюємо на 320 (80 * 4dp).
    val AvatarSize = 40.dp
    val EmptyIconSize = 120.dp
}