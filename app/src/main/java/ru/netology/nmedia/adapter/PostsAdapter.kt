package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.NumberEditor
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onOverlook(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
}

class PostsAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener

) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            authorView.text = post.author
            contentView.text = post.content
            publishedView.text = post.published
            numberOfLikesView.text = NumberEditor.numberEditing(post.numberOfLikesToInt)
            numberOfSharedView.text = NumberEditor.numberEditing(post.numberOfSharedToInt)
            numberOfOverlookedView.text = NumberEditor.numberEditing(post.numberOfOverlookedToInt)

            likes.setImageResource(
                if (post.likedByMe) {
                    R.drawable.ic_baseline_favorite_like_24dp
                } else {
                    R.drawable.ic_baseline_favorite_dislike_24dp
                }
            )

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shared.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            overlooked.setOnClickListener {
                onInteractionListener.onOverlook(post)
            }

            menuPostView.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.removePost -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.editPost -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}