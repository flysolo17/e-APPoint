package com.flysolo.e_appoint.presentation.navigation

import androidx.annotation.DrawableRes
import com.flysolo.e_appoint.R
import com.flysolo.e_appoint.config.AppRouter

data class NavDrawerItems(
    val label : String,
    @DrawableRes val selectedIcon : Int,
    @DrawableRes val unselectedIcon : Int,
    val route : String
)


data class BottomNavigationItems(
    val label : String,
    @DrawableRes val selectedIcon : Int,
    @DrawableRes val unselectedIcon : Int,
    val hasNews : Boolean,
    val badgeCount : Int? = null,
    val route : String
) {
    companion object {
        val BOTTOM_NAV = listOf(
            BottomNavigationItems(
                label = "Home",
                selectedIcon = R.drawable.ic_home_filled,
                unselectedIcon = R.drawable.ic_home_outlined,
                hasNews = false,
                route = AppRouter.HOME.route
            ),
            BottomNavigationItems(
                label = "Appointments",
                selectedIcon = R.drawable.ic_activities_filled,
                unselectedIcon = R.drawable.ic_activities_outline,
                hasNews = false,
                route = AppRouter.APPOINTMENTS.route
            ),
            BottomNavigationItems(
                label = "Profile",
                selectedIcon = R.drawable.ic_profile_filled,
                unselectedIcon = R.drawable.ic_person_outline,
                hasNews = false,
                route = AppRouter.PROFILE.route
            ),
        )
    }
}