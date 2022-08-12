package com.pobochii.someapp.users

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.slidingpanelayout.widget.SlidingPaneLayout

class UsersListOnBackPressedCallback(
    private val slidingPaneLayout: SlidingPaneLayout,
    private val onPanelToggled: (closed: Boolean) -> Unit

) : OnBackPressedCallback(
    slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen
), SlidingPaneLayout.PanelSlideListener {

    init {
        slidingPaneLayout.apply {
            lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
            addPanelSlideListener(this@UsersListOnBackPressedCallback)
        }
    }

    override fun handleOnBackPressed() {
        slidingPaneLayout.closePane()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {}

    override fun onPanelOpened(panel: View) {
        isEnabled = true
        onPanelToggled(false)
    }

    override fun onPanelClosed(panel: View) {
        isEnabled = false
        onPanelToggled(true)
    }
}
