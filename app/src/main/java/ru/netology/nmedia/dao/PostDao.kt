package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun overlookById(id: Long)
    fun removeById(id: Long)
    fun addPost(post: Post) : Post
    fun findPostById(id: Long): Post
}