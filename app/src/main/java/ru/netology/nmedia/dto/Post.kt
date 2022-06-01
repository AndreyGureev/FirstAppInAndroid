package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val video: String,
    val numberOfSharedToInt: Long,
    val numberOfLikesToInt: Long,
    val numberOfOverlookedToInt: Long
) : Parcelable