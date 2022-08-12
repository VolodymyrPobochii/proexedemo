package com.pobochii.someapp.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pobochii.someapp.R
import com.pobochii.someapp.Result
import com.pobochii.someapp.databinding.UserDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {
    private lateinit var viewBinding: UserDetailsFragmentBinding
    private val viewModel by viewModels<UserDetailsViewModel>()
    private val args by navArgs<UserDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.selectUser(args.userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.user_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding = UserDetailsFragmentBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    when (it) {
                        Result.Busy -> viewBinding.detailsGroup.visibility = View.INVISIBLE
                        is Result.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            val details = it.data
                            Glide.with(viewBinding.image)
                                .load(details.profileImage)
                                .apply(RequestOptions().fitCenter())
                                .into(viewBinding.image)
                            viewBinding.apply {
                                userName.text =
                                    getString(R.string.label_user_name, details.userName)
                                detailsGroup.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }
}