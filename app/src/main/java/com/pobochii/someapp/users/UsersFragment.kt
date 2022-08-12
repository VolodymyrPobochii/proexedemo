package com.pobochii.someapp.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.AbstractListDetailFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.pobochii.someapp.R
import com.pobochii.someapp.Result
import com.pobochii.someapp.databinding.UsersFragmentBinding
import com.pobochii.someapp.userdetails.UserDetailsFragmentDirections
import com.pobochii.someapp.utils.isTabletLandscape
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : AbstractListDetailFragment() {
    private lateinit var destChangeListener: UsersListDestChangeListener
    private lateinit var usersListAdapter: UsersListAdapter
    private lateinit var viewBinding: UsersFragmentBinding
    private val viewModel by activityViewModels<UsersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        usersListAdapter = UsersListAdapter(UsersDiffCallback()) { userId ->
            viewModel.selectUser(userId)
        }
    }

    override fun onDestroy() {
        detailPaneNavHostFragment.navController
            .removeOnDestinationChangedListener(destChangeListener)
        super.onDestroy()
    }

    override fun onCreateDetailPaneNavHostFragment(): NavHostFragment {
        return NavHostFragment.create(R.navigation.nav_graph_details)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val navController = detailPaneNavHostFragment.navController
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            navController,
            AppBarConfiguration(setOf(if (isTabletLandscape) R.id.user_details_dest else R.id.empty_dest))
        )
        destChangeListener = UsersListDestChangeListener(slidingPaneLayout)
        navController.addOnDestinationChangedListener(destChangeListener)
    }

    override fun onCreateListPaneView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.users_fragment, container, false)
    }

    override fun onListPaneViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding = UsersFragmentBinding.bind(view).apply {
            usersList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = usersListAdapter
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            UsersListOnBackPressedCallback(slidingPaneLayout) { closed ->
                activity?.invalidateOptionsMenu()
                if (closed) {
                    viewModel.resetEvent()
                    detailPaneNavHostFragment.navController.navigateUp()
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.users.collect {
                        when (it) {
                            Result.Busy -> viewBinding.progressBar.visibility = View.VISIBLE
                            is Result.Error -> {
                                viewBinding.apply {
                                    progressBar.visibility = View.GONE
                                    noData.visibility = View.VISIBLE
                                }
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            }
                            is Result.Success -> {
                                viewBinding.progressBar.visibility = View.GONE
                                if (it.data.isEmpty()) {
                                    viewBinding.noData.visibility = View.VISIBLE
                                    return@collect
                                }
                                usersListAdapter.submitList(it.data)
                            }
                        }
                    }
                }
                launch {
                    viewModel.event.collect {
                        when (it) {
                            is Event.UserSelected -> {
                                openDetails(it.id)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
        if (isTabletLandscape) {
            viewModel.selectFirstUserIfNeeded()
        }
    }

    override fun onStart() {
        super.onStart()
        if (isTabletLandscape) {
            val halfWidth = resources.displayMetrics.widthPixels / 2
            viewBinding.root.updateLayoutParams<SlidingPaneLayout.LayoutParams> {
                width = halfWidth
            }
            detailPaneNavHostFragment.view?.updateLayoutParams<FrameLayout.LayoutParams> {
                width = halfWidth
            }
        }
    }

    private fun openDetails(itemId: Int) {
        val toUserDetails = UserDetailsFragmentDirections.globalToUserDetails(itemId)
        detailPaneNavHostFragment.navController.apply {
            navigate(
                toUserDetails,
                NavOptions.Builder()
                    .setPopUpTo(R.id.user_details_dest, true)
                    .apply {
                        if (slidingPaneLayout.isOpen) {
                            setEnterAnim(androidx.navigation.ui.R.animator.nav_default_enter_anim)
                            setExitAnim(androidx.navigation.ui.R.animator.nav_default_exit_anim)
                        }
                    }
                    .build()

            )
        }
        slidingPaneLayout.open()
    }
}