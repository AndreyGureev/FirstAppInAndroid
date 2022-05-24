package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(onLikeListener = { viewModel.likeById(it.id) },
            onShareListener = { viewModel.shareById(it.id) },
            onOverlookListener = { viewModel.overlookById(it.id) }) /*{
            *//*viewModel.likeById(it.id)
            viewModel.shareById(it.id)
            viewModel.overlookById(it.id)*//*
        }*/

        binding.listPostFeed.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}