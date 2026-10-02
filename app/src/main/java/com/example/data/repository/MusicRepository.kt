package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.DefaultMusicData
import com.example.data.db.PlaylistDao
import com.example.data.db.TrackDao
import com.example.data.model.Playlist
import com.example.data.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID

class MusicRepository(
    private val trackDao: TrackDao,
    private val playlistDao: PlaylistDao
) {
    val allTracks: Flow<List<Track>> = trackDao.getAllTracks()
    val likedTracks: Flow<List<Track>> = trackDao.getLikedTracks()
    val allPlaylists: Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        val currentTracks = trackDao.getAllTracks().first()
        if (currentTracks.isEmpty()) {
            trackDao.insertTracks(DefaultMusicData.sampleTracks)
        }
        val currentPlaylists = playlistDao.getAllPlaylists().first()
        if (currentPlaylists.isEmpty()) {
            playlistDao.insertPlaylists(DefaultMusicData.samplePlaylists)
        }
    }

    fun getPlaylistById(id: String): Flow<Playlist?> = playlistDao.getPlaylistById(id)

    suspend fun getTrackById(id: String): Track? = withContext(Dispatchers.IO) {
        trackDao.getTrackByIdOnce(id)
    }

    fun searchTracks(query: String): Flow<List<Track>> = trackDao.searchTracks(query)

    fun getTracksByGenre(genre: String): Flow<List<Track>> = trackDao.getTracksByGenre(genre)

    suspend fun toggleLike(trackId: String, currentLike: Boolean) = withContext(Dispatchers.IO) {
        trackDao.updateLikeStatus(trackId, !currentLike)
    }

    suspend fun recordTrackPlayed(trackId: String) = withContext(Dispatchers.IO) {
        trackDao.incrementPlayCount(trackId)
    }

    suspend fun createPlaylist(title: String, description: String, gradientStart: Long, gradientEnd: Long): String =
        withContext(Dispatchers.IO) {
            val id = "custom_" + UUID.randomUUID().toString().take(8)
            val playlist = Playlist(
                id = id,
                title = title.ifBlank { "Naveed's Mix" },
                description = description.ifBlank { "Personalized playlist created on Naveedify" },
                gradientStart = gradientStart,
                gradientEnd = gradientEnd,
                isCustom = true,
                trackIdsJson = ""
            )
            playlistDao.insertPlaylist(playlist)
            id
        }

    suspend fun addTrackToPlaylist(playlistId: String, trackId: String) = withContext(Dispatchers.IO) {
        val playlist = playlistDao.getPlaylistByIdOnce(playlistId) ?: return@withContext
        val currentList = playlist.getTrackIdList().toMutableList()
        if (!currentList.contains(trackId)) {
            currentList.add(trackId)
            val updated = playlist.copy(trackIdsJson = currentList.joinToString(","))
            playlistDao.updatePlaylist(updated)
        }
    }

    suspend fun removeTrackFromPlaylist(playlistId: String, trackId: String) = withContext(Dispatchers.IO) {
        val playlist = playlistDao.getPlaylistByIdOnce(playlistId) ?: return@withContext
        val currentList = playlist.getTrackIdList().toMutableList()
        if (currentList.contains(trackId)) {
            currentList.remove(trackId)
            val updated = playlist.copy(trackIdsJson = currentList.joinToString(","))
            playlistDao.updatePlaylist(updated)
        }
    }

    suspend fun deletePlaylist(playlistId: String) = withContext(Dispatchers.IO) {
        playlistDao.deletePlaylistById(playlistId)
    }

    companion object {
        fun create(database: AppDatabase): MusicRepository {
            return MusicRepository(database.trackDao(), database.playlistDao())
        }
    }
}
