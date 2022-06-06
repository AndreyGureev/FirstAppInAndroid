package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun addPost(post: Post) {
        val id = post.id
        val saved = dao.addPost(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun addVideoById(id: Long) {
        posts = posts.map {
            if (id == it.id) {
                it.copy(video = it.video)
            } else
                it
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                numberOfLikesToInt = if (it.likedByMe) it.numberOfLikesToInt - 1 else it.numberOfLikesToInt + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                numberOfSharedToInt = it.numberOfSharedToInt + 1
            )
        }
        data.value = posts
    }

    override fun overlookById(id: Long) {
        dao.overlookById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                numberOfOverlookedToInt = it.numberOfOverlookedToInt + 1
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun findPostById(id: Long): Post {
        dao.findPostById(id)
        return posts.first { it.id == id }
    }
}