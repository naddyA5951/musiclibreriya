package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val gradientStart: Long,
    val gradientEnd: Long,
    val isCustom: Boolean = false,
    val trackIdsJson: String = "" // comma-separated track IDs
) {
    fun getTrackIdList(): List<String> {
        if (trackIdsJson.isBlank()) return emptyList()
        return trackIdsJson.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
}
