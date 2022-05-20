package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.NumberEditor.numberEditing
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) { post ->

            with(binding) {
                authorView.text = post.author
                contentView.text = post.content
                publishedView.text = post.published
                numberOfSharedView.text = numberEditing(post.numberOfSharedToInt)
                numberOfLikesView.text = numberEditing(post.numberOfLikesToInt)
                numberOfOverlookedView.text = numberEditing(post.numberOfOverlookedToInt)

                likes.setImageResource(
                    if (post.likedByMe) {
                        R.drawable.ic_baseline_favorite_like_24dp
                    } else {
                        R.drawable.ic_baseline_favorite_dislike_24dp
                    }
                )
            }
        }

        binding.likes.setOnClickListener {
            viewModel.like()
        }

        binding.shared.setOnClickListener {
            viewModel.share()
        }

        binding.overlooked.setOnClickListener {
            viewModel.overlook()
        }
    }
}