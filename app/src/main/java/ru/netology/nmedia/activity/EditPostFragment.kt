package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.Arguments
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )

        val textForEdit = arguments?.getString(Arguments.CONTENT).toString()
        val videoLinkForEdit = arguments?.getString(Arguments.VIDEO_LINK).toString()
        val postId = arguments?.getLong(Arguments.POST_ID)

        binding.etInputArea.setText(textForEdit)
        binding.etEditedPostVideoLink.setText(videoLinkForEdit)

        binding.etInputArea.requestFocus()

        binding.fabConfirmation.setOnClickListener {
            if (binding.etInputArea.text.isNullOrBlank() && binding.etEditedPostVideoLink.text.isNullOrBlank()) {
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
                return@setOnClickListener
            }

            val content = binding.etInputArea.text.toString()
            val videoLink = binding.etEditedPostVideoLink.text.toString()
            if (videoLinkForEdit != "" && !AndroidUtils.urlValidChecker(videoLinkForEdit)) {
                Toast.makeText(
                    activity,
                    getString(R.string.error_url_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            AndroidUtils.hideKeyboard(requireView())

            viewModel.changeContent(content, videoLink)
            if (postId != null) {
                viewModel.searchAndChangePost(postId.toLong())
            }
            findNavController().navigate(R.id.mainFragment)
        }

        binding.mbCancelEditing.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}