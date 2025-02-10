package com.shupty.skplayer.ui.theme

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shupty.skplayer.Data.Audio
import com.shupty.skplayer.getAllAudio


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Gui(context: Context) {
    val audio = getAllAudio(context) + getAllAudio(context)
    var isPlaying by remember { mutableStateOf(true) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var currentPlayingIndex by remember { mutableIntStateOf(0) }
    var fileName by remember { mutableStateOf("Nothing Is Playing") }


    fun playFile(audioP: Audio) {
        val pp = isPlaying
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, audioP.uri)
            prepare()
            if(pp) {
                start()
                isPlaying = true
            }
            setOnCompletionListener {
                currentPlayingIndex = (currentPlayingIndex + 1) % audio.lastIndex
                playFile(audio[currentPlayingIndex])
            }
        }
        fileName = audioP.title
    }

    fun playPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }
            isPlaying = !isPlaying
        } else {
            playFile(audio[currentPlayingIndex])
        }
    }

    fun playNext() {
        currentPlayingIndex = (currentPlayingIndex + 1) % audio.lastIndex
        playFile(audio[currentPlayingIndex])
    }

    fun playPrev() {
        currentPlayingIndex = (currentPlayingIndex - 1).takeIf { it >= 0 } ?: audio.lastIndex
        playFile(audio[currentPlayingIndex])
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(bottom = 20.dp)) {
            Text(text = "${currentPlayingIndex+1}_" + fileName )
        }
        Row(modifier = Modifier.padding(top = 20.dp)) {
            Button(                                                                     // import
                onClick = {
                    // loadUri()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(
                    topStart = 15.dp,
                    bottomStart = 15.dp,
                    topEnd = 15.dp,
                    bottomEnd = 15.dp
                ),
            ) {

                    Text(audio.size.toString() + MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL).toString())
            }
        }

        Row(modifier = Modifier.height(70.dp)) {
            Button(                                                                  // Prev button
                onClick = {
                    playPrev()
                },
                modifier = Modifier.height(60.dp),
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 4.dp, bottomStart = 15.dp, bottomEnd = 5.dp)
            ) {
                Text(text = "Prev")
            }

            Button(                                                               // Play Pause Button
                onClick = {
                    if (mediaPlayer != null) {
                        playPause()
                    } else {
                        //loadUri()
                        currentPlayingIndex = 0
                        playFile(audio[currentPlayingIndex])
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .padding(start = 1.dp),
                shape = RoundedCornerShape(
                    topStart = 5.dp,
                    bottomStart = 5.dp,
                    topEnd = 5.dp,
                    bottomEnd = 5.dp
                )
            ) {
                Text(if (isPlaying) "Pause" else "Play")
            }

            Button(                                                              // Next Button
                onClick = {
                    playNext()
                },
                modifier = Modifier
                    .height(60.dp)
                    .padding(horizontal = 1.dp),
                shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp, topEnd = 15.dp, bottomEnd = 15.dp)
            ) {
                Text(text = "Next")
            }
        }
    }
}