package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String?,
    val content: String,
    val video: String?,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Int,
    val shares: Int,
    val views: Int
) {
    companion object {
        fun fromPost(post: Post): PostEntity {
            return with(post) {
                PostEntity(
                    id,
                    author,
                    content,
                    video,
                    published,
                    likedByMe,
                    likes,
                    shares,
                    views
                )
            }
        }
    }
}

fun PostEntity.toPost(): Post {
    return Post(
        id,
        author,
        content,
        video,
        published,
        likedByMe,
        likes,
        shares,
        views
    )
}
