package com.shupty.skplayer.Data

import android.net.Uri

data class Audio(
    val uri: Uri,
    val title: String,
    val duration: Int,
    val size: Int
)
