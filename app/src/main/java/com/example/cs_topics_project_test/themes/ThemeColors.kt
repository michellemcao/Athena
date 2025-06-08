package com.example.cs_topics_project_test.themes

data class ThemeColors(
    // app header
    val toolbar: Int,
    val header: Int,

    // nav bar gradient
    val startColor: Int,
    val centerColor: Int,
    val endColor: Int,

    // home fragment
    val gradientDark: Int,
    val gradientMedium: Int,
    val gradientLight: Int,
    val background: Int,
    val homeText: Int,
    // the home fragment graphs
    val dueLater: Int,
    val duePast: Int,
    val dueToday: Int,

    // icon tints
    val icon: Int,
    val chatAccent: Int,

    // calendar colors
    val monthCurrent: Int,
    val monthNext: Int,
    val today: Int,
    val completedText: Int,

    val settingsDark: Int,
    val settingsLight: Int,
    val thumb: Int,
    val track: Int,

    // edit date background color
    val editTask: Int,
    val editDate: Int,
)
