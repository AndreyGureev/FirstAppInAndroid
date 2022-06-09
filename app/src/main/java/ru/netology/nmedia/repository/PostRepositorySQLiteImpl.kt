package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toPost

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> =
        Transformations.map(dao.getAll()) { it.map(PostEntity::toPost) }

    override fun addPost(post: Post) {
        dao.addPost(PostEntity.fromPost(post))
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun overlookById(id: Long) {
        dao.overlookById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun findPostById(id: Long): Post {
        return dao.findPostById(id)
    }
}