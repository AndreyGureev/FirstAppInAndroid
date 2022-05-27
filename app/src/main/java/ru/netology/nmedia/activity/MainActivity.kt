package ru.netology.nmedia.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import ru.netology.nmedia.adapter.OnInteractionListener
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onOverlook(post: Post) {
                viewModel.overlookById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })

        binding.listPostFeed.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }

            with(binding.postContent) {
                requestFocus()
                setText(post.content)
            }
        }

        binding.postContent.doAfterTextChanged {
            binding.undoEditing.isVisible = it?.isNotEmpty() == true
        }

        binding.undoEditing.setOnClickListener {
            viewModel.clearEditing()
            AndroidUtils.hideKeyboard(it)
            binding.postContent.text = null
            binding.postContent.clearFocus()
        }

        binding.savePost.setOnClickListener {
            with(binding.postContent) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Поле контента не может быть пустым",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
    }
}