package com.flysolo.e_appoint.config


import com.flysolo.e_appoint.models.users.Users

import com.google.gson.Gson

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class AppRouter(
    val route : String
)  {
    data object AUTH : AppRouter(route = "auth")
    data object REGISTER : AppRouter(route = "register")
    data object LOGIN : AppRouter(route = "login")
    data object FORGOT_PASSWORD : AppRouter(route = "forgot-password")
    data object EDIT_PROFILE : AppRouter(route = "edit-profile/{args}") {
        fun navigate(args: Users) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "edit-profile/$encodedJson"
        }
    }
    data object CHANGE_PASSWORD : AppRouter(route = "change-password")
    data object VERIFICATION : AppRouter(route = "verification")
    data object MAIN : AppRouter(route = "main")


    //BOTTOM NAV
    data object HOME : AppRouter(route = "home")

    data object APPOINTMENTS : AppRouter(route = "appointments")
    data object PROFILE : AppRouter(route = "profile")


    data object CREATE_APPOINTMENT : AppRouter(route = "create-appointment")

    data object NOTIFICATIONS : AppRouter(route = "notifications")




    //drawer
    data object PERSONAL_INFO : AppRouter(route = "info")
    data object CONTACT_AND_SECURITY : AppRouter(route = "contact")
    data object HELP_CENTER : AppRouter(route = "help")
    data object ABOUT_US : AppRouter(route = "about")

}