package com.birdushenin.newssphere.data

import com.birdushenin.newssphere.R

data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
) {
    fun getSourceDrawable(): Int {
        return when (source.id?.lowercase()) {
            "bbc-news" -> R.drawable.bbcnews
            "usa-today" -> R.drawable.usatoday
            "reuters" -> R.drawable.reuters
            "the-washington-post" -> R.drawable.thewashingtonpost
            "bloomberg" -> R.drawable.bloomberg
            "abc-news" -> R.drawable.abcnews
            "axios" -> R.drawable.axios
            "ansa" -> R.drawable.ansa
            "bbc-sport" -> R.drawable.bbcsport
            "bild" -> R.drawable.bild
            "business-insider" -> R.drawable.businessinsider
            "buzzfeed" -> R.drawable.buzzfeed
            "cbc-news" -> R.drawable.cbcnews
            "cbs-news" -> R.drawable.cbsnews
            "cnn" -> R.drawable.cnn
            "fox-news" -> R.drawable.foxnews
            "fox-sports" -> R.drawable.foxnews
            "google-news" -> R.drawable.googlenews
            "the-wall-street-journal" -> R.drawable.thewallstreetjournal
            "the-washington-times" -> R.drawable.thewashingtonpost
            "the-new-york-times" -> R.drawable.thenewyorktimes
            else -> R.drawable.baseline_public
        }
    }
}

data class Source(
    val id: String?,
    val name: String
)

