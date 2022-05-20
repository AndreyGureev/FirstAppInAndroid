package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val numberOfSharedToInt: Long,
    val numberOfLikesToInt: Long,
    val numberOfOverlookedToInt: Long
)