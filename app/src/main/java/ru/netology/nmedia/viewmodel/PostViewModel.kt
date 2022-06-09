package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
    video = "",
    likes = 0,
    shares = 0,
    views = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositorySQLiteImpl(AppDb.getInstance(application).postDao)

    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun addPost() {
        edited.value?.let {
            repository.addPost(it)
        }
        edited.value = empty
    }

    fun changeContent(content: String, videoLink: String) {
        val text = content.trim()
        val link = videoLink.trim()
        if (edited.value?.content == text && edited.value?.video == link) {
            return
        }
        edited.value = edited.value?.copy(content = text, video = link)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun overlookById(id: Long) = repository.overlookById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun searchPost(id: Long): Post {
        return repository.findPostById(id)
    }

    fun searchAndChangePost(id: Long) {
        val thisPost = repository.findPostById(id)
        val editedPost =
            thisPost.copy(content = edited.value?.content.toString(), video = edited.value?.video)
        repository.addPost(editedPost)
    }
}