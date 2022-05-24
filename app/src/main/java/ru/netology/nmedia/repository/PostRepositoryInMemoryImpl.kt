package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.NumberEditor.numberEditing
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = listOf(
        Post(
            id = 2,
            author = "Нетология. Университет интернет - профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов.Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            published = "30 мая в 21:05",
            likedByMe = false,
            numberOfSharedToInt = 1999999,
            numberOfLikesToInt = 1999999,
            numberOfOverlookedToInt = 1999999
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет - профессий будущего",
            content = "С нами вы можете получить новую профессию, освоить навыки для развития карьеры или перенастроить свой бизнес. Выбирайте подходящую из более 80 программ.",
            published = "1 июня в 20:17",
            likedByMe = false,
            numberOfSharedToInt = 19999,
            numberOfLikesToInt = 199,
            numberOfOverlookedToInt = 23212
        )
    )

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
        numberEditing(posts[id.toInt()-1].numberOfLikesToInt)
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            it.copy(numberOfSharedToInt = it.numberOfSharedToInt + 1)
        }
        numberEditing(posts[id.toInt()-1].numberOfSharedToInt)
        data.value = posts
    }

    override fun overlookById(id: Long) {
        posts = posts.map {
            it.copy(numberOfOverlookedToInt = it.numberOfOverlookedToInt + 1)
        }
        numberEditing(posts[id.toInt()-1].numberOfOverlookedToInt)
        data.value = posts
    }
}