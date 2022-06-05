package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.AndroidUtils.Companion.FILE_STORE

class PostRepositoryFileImpl(
    private val context: Context
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val file = context.filesDir.resolve(FILE_STORE)
    private var posts = run {
        if (!file.exists()) return@run emptyList<Post>()
        file.readText()
            .ifBlank {
                return@run emptyList<Post>()
            }
            .let {
                gson.fromJson(it, type)
            }
    }

    var nextId = if (posts.isEmpty()) 1 else (posts.first().id + 1)

    private val data = MutableLiveData(posts)
    override fun getAll(): LiveData<List<Post>> = data

    override fun addPost(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Я",
                    published = AndroidUtils.addLocalDataTime(),
                    likedByMe = false,
                    video = "",
                    numberOfLikesToInt = 0L,
                    numberOfSharedToInt = 0L,
                    numberOfOverlookedToInt = 1L
                )
            ) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content, video = post.video)
        }
        data.value = posts
        sync()
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

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    numberOfLikesToInt = if (it.likedByMe) it.numberOfLikesToInt - 1 else it.numberOfLikesToInt + 1
                )
            }
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id == id) it.copy(numberOfSharedToInt = it.numberOfSharedToInt + 1) else it
        }
        data.value = posts
        sync()
    }

    override fun overlookById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(numberOfOverlookedToInt = it.numberOfOverlookedToInt + 1)
            }
        }
        data.value = posts
        sync()
    }

    private fun sync() {
        context.openFileOutput(FILE_STORE, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun findPostById(id: Long): Post {
        return posts.first { it.id == id }
    }
}