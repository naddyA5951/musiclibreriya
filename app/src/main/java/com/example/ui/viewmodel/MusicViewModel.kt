package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.Playlist
import com.example.data.model.Track
import com.example.data.repository.MusicRepository
import com.example.player.MusicPlayerManager
import com.example.player.RepeatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Search : Screen()
    object Library : Screen()
    object Profile : Screen()
    data class PlaylistDetail(val playlistId: String) : Screen()
    object LikedSongsDetail : Screen()
}

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val repository = MusicRepository.create(database)
    val playerManager = MusicPlayerManager(application)

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Full screen now playing expansion
    private val _isNowPlayingExpanded = MutableStateFlow(false)
    val isNowPlayingExpanded: StateFlow<Boolean> = _isNowPlayingExpanded.asStateFlow()

    // Search query & selected genre
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre: StateFlow<String?> = _selectedGenre.asStateFlow()

    // Home feed filter (All, Music, Podcasts)
    private val _homeFilter = MutableStateFlow("All")
    val homeFilter: StateFlow<String> = _homeFilter.asStateFlow()

    // Dialog states
    private val _showCreatePlaylistDialog = MutableStateFlow(false)
    val showCreatePlaylistDialog: StateFlow<Boolean> = _showCreatePlaylistDialog.asStateFlow()

    private val _trackToAddToPlaylist = MutableStateFlow<Track?>(null)
    val trackToAddToPlaylist: StateFlow<Track?> = _trackToAddToPlaylist.asStateFlow()

    private val _showSleepTimerDialog = MutableStateFlow(false)
    val showSleepTimerDialog: StateFlow<Boolean> = _showSleepTimerDialog.asStateFlow()

    private val _showLyrics = MutableStateFlow(false)
    val showLyrics: StateFlow<Boolean> = _showLyrics.asStateFlow()

    // Audio settings
    private val _audioQuality = MutableStateFlow("Very High (320 kbps)")
    val audioQuality: StateFlow<String> = _audioQuality.asStateFlow()

    private val _equalizerPreset = MutableStateFlow("Bass Boost")
    val equalizerPreset: StateFlow<String> = _equalizerPreset.asStateFlow()

    // Reactive streams from repository
    val allTracks: StateFlow<List<Track>> = repository.allTracks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val likedTracks: StateFlow<List<Track>> = repository.likedTracks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allPlaylists: StateFlow<List<Playlist>> = repository.allPlaylists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filtered search tracks
    val searchResults: StateFlow<List<Track>> = combine(
        allTracks,
        _searchQuery,
        _selectedGenre
    ) { tracks, query, genre ->
        var list = tracks
        if (!genre.isNullOrBlank()) {
            list = list.filter { it.genre.equals(genre, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            list = list.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.artist.contains(query, ignoreCase = true) ||
                        it.album.contains(query, ignoreCase = true) ||
                        it.genre.contains(query, ignoreCase = true)
            }
        }
        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Player forwarding states
    val currentTrack: StateFlow<Track?> = playerManager.currentTrack
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val currentPositionMs: StateFlow<Int> = playerManager.currentPositionMs
    val durationMs: StateFlow<Int> = playerManager.durationMs
    val isShuffle: StateFlow<Boolean> = playerManager.isShuffle
    val repeatMode: StateFlow<RepeatMode> = playerManager.repeatMode
    val visualizerBars: StateFlow<List<Float>> = playerManager.visualizerBars
    val sleepTimerMinutes: StateFlow<Int?> = playerManager.sleepTimerMinutes

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setNowPlayingExpanded(expanded: Boolean) {
        _isNowPlayingExpanded.value = expanded
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedGenre(genre: String?) {
        _selectedGenre.value = genre
    }

    fun setHomeFilter(filter: String) {
        _homeFilter.value = filter
    }

    fun playTrack(track: Track, queue: List<Track>? = null) {
        val finalQueue = queue ?: allTracks.value
        playerManager.playTrack(track, finalQueue)
        viewModelScope.launch {
            repository.recordTrackPlayed(track.id)
        }
    }

    fun playPlaylist(playlist: Playlist) {
        val tracksMap = allTracks.value.associateBy { it.id }
        val tracks = playlist.getTrackIdList().mapNotNull { tracksMap[it] }
        if (tracks.isNotEmpty()) {
            playTrack(tracks.first(), tracks)
        }
    }

    fun togglePlayPause() {
        playerManager.togglePlayPause()
    }

    fun seekTo(positionMs: Int) {
        playerManager.seekTo(positionMs)
    }

    fun skipNext() {
        playerManager.skipNext()
    }

    fun skipPrevious() {
        playerManager.skipPrevious()
    }

    fun toggleShuffle() {
        playerManager.toggleShuffle()
    }

    fun toggleRepeat() {
        playerManager.toggleRepeat()
    }

    fun toggleLike(track: Track) {
        viewModelScope.launch {
            repository.toggleLike(track.id, track.isLiked)
        }
    }

    fun openCreatePlaylistDialog() {
        _showCreatePlaylistDialog.value = true
    }

    fun closeCreatePlaylistDialog() {
        _showCreatePlaylistDialog.value = false
    }

    fun createPlaylist(title: String, description: String, colorHex: Long) {
        viewModelScope.launch {
            val endHex = 0xFF121212
            repository.createPlaylist(title, description, colorHex, endHex)
            closeCreatePlaylistDialog()
        }
    }

    fun openAddToPlaylist(track: Track) {
        _trackToAddToPlaylist.value = track
    }

    fun closeAddToPlaylist() {
        _trackToAddToPlaylist.value = null
    }

    fun addTrackToPlaylist(playlistId: String, trackId: String) {
        viewModelScope.launch {
            repository.addTrackToPlaylist(playlistId, trackId)
            closeAddToPlaylist()
        }
    }

    fun removeTrackFromPlaylist(playlistId: String, trackId: String) {
        viewModelScope.launch {
            repository.removeTrackFromPlaylist(playlistId, trackId)
        }
    }

    fun deletePlaylist(playlistId: String) {
        viewModelScope.launch {
            repository.deletePlaylist(playlistId)
            if (_currentScreen.value is Screen.PlaylistDetail) {
                _currentScreen.value = Screen.Library
            }
        }
    }

    fun openSleepTimerDialog() {
        _showSleepTimerDialog.value = true
    }

    fun closeSleepTimerDialog() {
        _showSleepTimerDialog.value = false
    }

    fun setSleepTimer(minutes: Int?) {
        playerManager.setSleepTimer(minutes)
        closeSleepTimerDialog()
    }

    fun toggleLyrics() {
        _showLyrics.value = !_showLyrics.value
    }

    fun setAudioQuality(quality: String) {
        _audioQuality.value = quality
    }

    fun setEqualizerPreset(preset: String) {
        _equalizerPreset.value = preset
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
