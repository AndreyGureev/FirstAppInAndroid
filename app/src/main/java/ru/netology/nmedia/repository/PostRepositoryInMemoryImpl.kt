package ru.netology.nmedia.repository

import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет - профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов.Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            published = "30 мая в 21:05",
            likedByMe = false,
            video = "",
            numberOfSharedToInt = 1999999,
            numberOfLikesToInt = 1999999,
            numberOfOverlookedToInt = 1999999
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет - профессий будущего",
            content = "С нами вы можете получить новую профессию, освоить навыки для развития карьеры или перенастроить свой бизнес. Выбирайте подходящую из более 80 программ.",
            published = "1 июня в 20:17",
            likedByMe = false,
            video = "https://www.youtube.com/watch?v=tQ4cl4mWRh8",
            numberOfSharedToInt = 19999,
            numberOfLikesToInt = 199,
            numberOfOverlookedToInt = 23212
        ),
        Post(
            id = nextId++,
            author = "Нетология.",
            content = "С нами вы можете получить новую профессию",
            published = "30 июня в 10:17",
            likedByMe = false,
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
            numberOfSharedToInt = 199919,
            numberOfLikesToInt = 3199,
            numberOfOverlookedToInt = 133212
        )
    ).reversed()

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
}