package com.shupty.skplayer

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.shupty.skplayer.Data.Audio
import com.shupty.skplayer.ui.theme.Gui

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Gui(applicationContext)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
fun getAllAudio(context: Context): MutableList<Audio> {
    var audioList = mutableListOf<Audio>()

    val collection = if (Build.VERSION.SDK_INT >= 30) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else MediaStore.Files.getContentUri("external")


    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.DURATION,
        MediaStore.Files.FileColumns.SIZE,
    )

        val query = context.contentResolver.query(
           collection,
            projection,
            null,
            null,
            null
        )

        query?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val title = cursor.getString(nameCol) ?: "Unknown"
                val duration = cursor.getInt(durationCol).takeIf { it > 0 } ?: 0
                val size = cursor.getInt(sizeCol).takeIf { it > 0 } ?: 0

                // Create URI specific to this volume
                val uri = ContentUris.withAppendedId(collection, id)

                audioList.add(Audio(uri, title, duration, size))
            }
        }
        query?.close()

    return audioList
}
