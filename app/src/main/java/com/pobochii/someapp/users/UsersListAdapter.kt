package com.pobochii.someapp.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pobochii.someapp.databinding.UserListItemBinding
import com.pobochii.someapp.utils.isTabletLandscape

/**
 * Items adapter for users RecyclerView
 */
class UsersListAdapter(
    diffCallback: DiffUtil.ItemCallback<UserListItem>,
    private val onItemClicked: (userId: Int) -> Unit
) :
    ListAdapter<UserListItem, UserListItemViewHolder>(diffCallback) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = UserListItemBinding.inflate(inflater, parent, false)
        return UserListItemViewHolder(viewBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/**
 * RecyclerView item's view holder
 */
class UserListItemViewHolder(
    private val viewBinding: UserListItemBinding,
    private val onItemClicked: (userId: Int) -> Unit
) :
    ViewHolder(viewBinding.root) {
    fun bind(item: UserListItem) {
        viewBinding.apply {
            Glide.with(image)
                .load(item.imageUrl)
                .apply(
                    RequestOptions()
                        .fitCenter()
                        .circleCrop()
                )
                .into(image)
            name.text = item.text
            root.apply {
                if (isTabletLandscape) {
                    isSelected = item.selected
                }
                setOnClickListener {
                    if (!isSelected) {
                        onItemClicked(item.id)
                    }
                }
            }
        }
    }
}

data class UserListItem(
    val id: Int,
    val text: String,
    val imageUrl: String,
    var selected: Boolean = false
)

class UsersDiffCallback : DiffUtil.ItemCallback<UserListItem>() {
    override fun areItemsTheSame(oldItem: UserListItem, newItem: UserListItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserListItem, newItem: UserListItem) =
        oldItem == newItem

}