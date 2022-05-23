package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет - профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов.Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            published = "30 мая в 21:05",
            likedByMe = false,
            numberOfSharedToInt = 1999999,
            numberOfLikesToInt = 1999999,
            numberOfOverlookedToInt = 1999999
        )

        with(binding) {
            authorView.text = post.author
            contentView.text = post.content
            publishedView.text = post.published
            numberOfSharedView.text = numberEditing(post.numberOfSharedToInt)
            numberOfLikesView.text = numberEditing(post.numberOfLikesToInt)
            numberOfOverlookedView.text = numberEditing(post.numberOfOverlookedToInt)

            binding.root.setOnClickListener {}

            if (post.likedByMe) {
                likes.setImageResource(R.drawable.ic_baseline_favorite_dislike_24dp)
            }

            likes.setOnClickListener {
                post.likedByMe = !post.likedByMe
                if (post.likedByMe) {
                    post.numberOfLikesToInt++
                    numberOfLikesView.text = numberEditing(post.numberOfLikesToInt)
                } else {
                    post.numberOfLikesToInt--
                    numberOfLikesView.text = numberEditing(post.numberOfLikesToInt)
                }
                likes.setImageResource(
                    if (post.likedByMe) {
                        R.drawable.ic_baseline_favorite_like_24dp
                    } else {
                        R.drawable.ic_baseline_favorite_dislike_24dp
                    }
                )
            }

            shared.setOnClickListener {
                post.numberOfSharedToInt++
                numberOfSharedView.text = numberEditing(post.numberOfSharedToInt)
            }

            overlooked.setOnClickListener {
                post.numberOfOverlookedToInt++
                numberOfOverlookedView.text = numberEditing(post.numberOfOverlookedToInt)
            }
        }
    }
}

private val formatThousands = DecimalFormat("#.#").apply {
    roundingMode = RoundingMode.DOWN
}

fun numberEditing(number: Long): String =
    when (number) {
        in 1000..1099 -> "${number / 1000}K"
        in 1100..9999 -> "${formatThousands.format(number.toDouble() / 1000)}K"
        in 10_000..999_999 -> "${(number / 1000)}K"
        in 1_000_000..1_099_999 -> "${number / 1_000_000}M"
        in 1_100_000..999_999_999 -> "${formatThousands.format(number.toDouble() / 1_000_000)}M"
        else -> number.toString()
    }