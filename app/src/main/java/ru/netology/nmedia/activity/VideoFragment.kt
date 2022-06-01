package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.nmedia.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVideoBinding.inflate(inflater, container, false)

        binding.playVideoNetology.setOnClickListener {
            val intent = Intent()
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT)))
            )
        }
        return binding.root
    }
}