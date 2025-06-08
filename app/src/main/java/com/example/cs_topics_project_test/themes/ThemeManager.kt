package com.example.cs_topics_project_test.themes

import android.content.Context
import android.graphics.Color
import org.json.JSONObject
import androidx.core.graphics.toColorInt

object ThemeManager {
    var currentThemeColors: ThemeColors? = null
    var currentThemeName: String = "mango"

    fun loadTheme(context: Context, themeName: String) {
        val json = context.assets.open("colors/mango.json")
            .bufferedReader().use { it.readText() }
        val obj = JSONObject(json)

        currentThemeColors = ThemeColors(
            toolbar = obj.getString("toolbar").toColorInt(),
            header = obj.getString("header").toColorInt(),
            startColor = obj.getString("start_color").toColorInt(),
            centerColor = obj.getString("center_color").toColorInt(),
            endColor = obj.getString("end_color").toColorInt(),
            gradientDark = obj.getString("gradient_dark").toColorInt(),
            gradientMedium = obj.getString("gradient_medium").toColorInt(),
            gradientLight = obj.getString("gradient_light").toColorInt(),
            background = obj.getString("background").toColorInt(),
            homeText = obj.getString("home_text").toColorInt(),
            dueLater = obj.getString("due_later").toColorInt(),
            duePast = obj.getString("due_past").toColorInt(),
            dueToday = obj.getString("due_today").toColorInt(),
            icon = obj.getString("icon").toColorInt(),
            chatAccent = obj.getString("chat_accent").toColorInt(),
            monthCurrent = obj.getString("month_current").toColorInt(),
            monthNext = obj.getString("month_next").toColorInt(),
            today = obj.getString("today").toColorInt(),
            completedText = obj.getString("completed_text").toColorInt(),
            settingsLight = obj.getString("settings_light").toColorInt(),
            settingsDark = obj.getString("settings_dark").toColorInt(),
            thumb = obj.getString("thumb").toColorInt(),
            track = obj.getString("track").toColorInt(),
            editTask = obj.getString("edit_task").toColorInt(),
            editDate = obj.getString("edit_date").toColorInt()
        )
    }
}
