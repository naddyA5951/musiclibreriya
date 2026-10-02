package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val coverGradientStart: Long,
    val coverGradientEnd: Long,
    val audioUrl: String,
    val genre: String,
    val lyrics: String,
    val isLiked: Boolean = false,
    val playCount: Int = 0,
    val isFeatured: Boolean = false
) {
    fun formatDuration(): String {
        val minutes = durationSeconds / 60
        val seconds = durationSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
