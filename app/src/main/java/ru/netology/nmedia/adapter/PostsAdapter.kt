package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
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
    fun onAddVideo(post: Post)
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
            likes.text = NumberEditor.numberEditing(post.numberOfLikesToInt)
            shared.text = NumberEditor.numberEditing(post.numberOfSharedToInt)
            overlooked.text = NumberEditor.numberEditing(post.numberOfOverlookedToInt)

            likes.isChecked = post.likedByMe

            addVideo.isVisible = post.video?.isNotBlank() == true

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            addVideo.setOnClickListener {
                onInteractionListener.onAddVideo(post)
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