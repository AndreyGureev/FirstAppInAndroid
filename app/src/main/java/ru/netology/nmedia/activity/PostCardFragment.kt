package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class PostCardFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val post = arguments?.getParcelable<Post>("post") as Post

        val binding = FragmentPostCardBinding.inflate(
            inflater,
            container,
            false
        )

        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.text = AndroidUtils.counter(post.likes)
            shared.text = AndroidUtils.counter(post.shares)
            numberOfViews.text = AndroidUtils.counter(post.views)
            likes.isChecked = post.likedByMe

            if (post.video == "") {
                postVideo.visibility = View.GONE
            } else {
                postVideo.visibility = View.VISIBLE
            }
        }

        binding.likes.setOnClickListener {
            viewModel.likeById(post.id)
            val updatedPost = viewModel.searchPost(post.id)
            binding.likes.text = updatedPost.likes.toString()
        }

        binding.shared.setOnClickListener {
            viewModel.shareById(post.id)
            val updatedPost = viewModel.searchPost(post.id)
            binding.shared.text = updatedPost.shares.toString()
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        binding.postVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
            val playVideoValidation = context?.let { AndroidUtils.startIntent(it, intent) }
            if (playVideoValidation == false) {
                Toast.makeText(
                    activity,
                    getString(R.string.error_play_video_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            startActivity(intent)
        }

        binding.menuPost.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.removePost -> {
                            viewModel.removeById(post.id)
                            findNavController().navigateUp()
                            true
                        }
                        R.id.editPost -> {
                            val bundle = Bundle().apply {
                                putString(Arguments.CONTENT, post.content)
                                putString(Arguments.VIDEO_LINK, post.video)
                                putLong(Arguments.POST_ID, post.id)
                            }
                            findNavController().navigate(
                                R.id.action_postCardFragment_to_editPostFragment,
                                bundle
                            )
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        return binding.root
    }
}