package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPreferenciesImpl(context: Context) : PostRepository {

    private companion object {
        const val POST_FILE = "posts.json"
        const val POST_KEY = "posts"
    }

    private val preferences = context.getSharedPreferences(POST_FILE, Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val gson = Gson()
    private var nextId = 1L
    private var posts: List<Post> = preferences.getString(POST_KEY, null)?.let {
        gson.fromJson(it, type)
    } ?: emptyList()
        set(value) {
            field = value
            sync()
        }

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe, numberOfLikesToInt = if (it.likedByMe) {
                    it.numberOfLikesToInt - 1
                } else {
                    it.numberOfLikesToInt + 1
                }
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (id == it.id) {
                it.copy(numberOfSharedToInt = it.numberOfSharedToInt + 1)
            } else {
                it
            }
        }
        data.value = posts
    }

    override fun overlookById(id: Long) {
        posts = posts.map {
            if (id == it.id) {
                it.copy(numberOfOverlookedToInt = it.numberOfOverlookedToInt + 1)
            } else {
                it
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
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

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Я",
                    published = "Сейчас",
                    likedByMe = false,
                    video = "",
                    numberOfLikesToInt = 0L,
                    numberOfSharedToInt = 0L,
                    numberOfOverlookedToInt = 1L
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    private fun sync() {
        preferences.edit {
            putString(POST_KEY, gson.toJson(posts))
        }
    }
}