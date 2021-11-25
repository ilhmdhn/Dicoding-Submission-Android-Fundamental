package com.idnsoft.githubusers.getdata

fun url(type: String, username: String): String {
    return when (type) {
        "search" -> return "https://api.github.com/search/users?q=${username}"
        "detail" -> return "https://api.github.com/users/${username}"
        "following" -> return "https://api.github.com/users/${username}/following"
        "followers" -> return "https://api.github.com/users/${username}/followers"
        else -> "incorrect type"
    }
}
