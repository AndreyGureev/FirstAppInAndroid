package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean,
    var numberOfSharedToInt: Long,
    var numberOfLikesToInt: Long,
    var numberOfOverlookedToInt: Long
)
