package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onOverlook(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onAddVideo(post: Post)
    fun onPost(post: Post) {}
}

class PostAdapter(private val onInteractionListener: OnInteractionListener) :
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
            author.text = post.author
            content.text = post.content
            published.text = post.published
            likes.text = AndroidUtils.counter(post.likes)
            shared.text = AndroidUtils.counter(post.shares)
            overlooked.text = AndroidUtils.counter(post.views)

            likes.isChecked = post.likedByMe

            if (post.video == "") {
                postVideo.visibility = View.GONE
            } else {
                postVideo.visibility = View.VISIBLE
            }

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shared.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            overlooked.setOnClickListener {
                onInteractionListener.onOverlook(post)
            }

            postVideo.setOnClickListener {
                onInteractionListener.onAddVideo(post)
            }
            content.setOnClickListener {
                onInteractionListener.onPost(post)
            }

            //when click on Menu item, we do either edit or delete
            menuPost.setOnClickListener {
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