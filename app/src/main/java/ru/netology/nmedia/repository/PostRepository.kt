package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun overlookById(id: Long)
    fun removeById(id: Long)
    fun addPost(post: Post)
    fun addVideoById (id: Long)
    fun findPostById(id: Long): Post
}