package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.NumberEditor.numberEditing
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет - профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов.Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
        published = "30 мая в 21:05",
        likedByMe = false,
        numberOfSharedToInt = 1999999,
        numberOfLikesToInt = 1999999,
        numberOfOverlookedToInt = 1999999
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe, numberOfLikesToInt = if (post.likedByMe) {
                post.numberOfLikesToInt - 1
            } else {
                post.numberOfLikesToInt + 1
            }
        )
        numberEditing(post.numberOfLikesToInt)
        data.value = post
    }

    override fun share() {
        post = post.copy(numberOfSharedToInt = post.numberOfSharedToInt + 1)
        numberEditing(post.numberOfSharedToInt)
        data.value = post
    }

    override fun overlook() {
        post = post.copy(numberOfOverlookedToInt = post.numberOfOverlookedToInt + 1)
        numberEditing(post.numberOfOverlookedToInt)
        data.value = post
    }
}