package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.NumberEditor
import ru.netology.nmedia.dto.Post

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit
typealias OnOverlookListener = (post: Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener, private val onShareListener: OnShareListener,
    private val onOverlookListener: OnOverlookListener

) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener, onOverlookListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    var listOfPost = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener,
    private val onOverlookListener: OnOverlookListener

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
                onLikeListener(post)
            }

            shared.setOnClickListener {
                onShareListener(post)
            }

            overlooked.setOnClickListener {
                onOverlookListener(post)
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