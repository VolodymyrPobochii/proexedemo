package com.pobochii.someapp.users

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.pobochii.someapp.R

class UsersListDestChangeListener(private val slidingPaneLayout: SlidingPaneLayout) :
    NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id == R.id.empty_dest) {
            slidingPaneLayout.closePane()
        }
    }
}