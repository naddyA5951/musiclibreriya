package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddToPlaylistDialog
import com.example.ui.components.CreatePlaylistDialog
import com.example.ui.components.NaveedifyBottomBar
import com.example.ui.components.SleepTimerDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.LikedSongsScreen
import com.example.ui.screens.NowPlayingScreen
import com.example.ui.screens.PlaylistDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.NaveedifyTheme
import com.example.ui.theme.SpotifyBlack
import com.example.ui.theme.SpotifyCard
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.SpotifyLightGray
import com.example.ui.theme.SpotifyPureBlack
import com.example.ui.theme.SpotifyWhite
import com.example.ui.viewmodel.MusicViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NaveedifyTheme {
                NaveedifyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NaveedifyApp(viewModel: MusicViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isNowPlayingExpanded by viewModel.isNowPlayingExpanded.collectAsStateWithLifecycle()

    val allTracks by viewModel.allTracks.collectAsStateWithLifecycle()
    val likedTracks by viewModel.likedTracks.collectAsStateWithLifecycle()
    val allPlaylists by viewModel.allPlaylists.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    val homeFilter by viewModel.homeFilter.collectAsStateWithLifecycle()

    val currentTrack by viewModel.currentTrack.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentPositionMs by viewModel.currentPositionMs.collectAsStateWithLifecycle()
    val durationMs by viewModel.durationMs.collectAsStateWithLifecycle()
    val isShuffle by viewModel.isShuffle.collectAsStateWithLifecycle()
    val repeatMode by viewModel.repeatMode.collectAsStateWithLifecycle()
    val visualizerBars by viewModel.visualizerBars.collectAsStateWithLifecycle()
    val sleepTimerMinutes by viewModel.sleepTimerMinutes.collectAsStateWithLifecycle()
    val showLyrics by viewModel.showLyrics.collectAsStateWithLifecycle()

    val audioQuality by viewModel.audioQuality.collectAsStateWithLifecycle()
    val equalizerPreset by viewModel.equalizerPreset.collectAsStateWithLifecycle()

    val showCreatePlaylistDialog by viewModel.showCreatePlaylistDialog.collectAsStateWithLifecycle()
    val trackToAddToPlaylist by viewModel.trackToAddToPlaylist.collectAsStateWithLifecycle()
    val showSleepTimerDialog by viewModel.showSleepTimerDialog.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (!isNowPlayingExpanded) {
                NaveedifyBottomBar(
                    currentScreen = currentScreen,
                    currentTrack = currentTrack,
                    isPlaying = isPlaying,
                    progressMs = currentPositionMs,
                    durationMs = durationMs,
                    visualizerBars = visualizerBars,
                    onNavigate = { viewModel.navigateTo(it) },
                    onPlayPause = { viewModel.togglePlayPause() },
                    onSkipNext = { viewModel.skipNext() },
                    onSkipPrevious = { viewModel.skipPrevious() },
                    onLikeClick = { currentTrack?.let { viewModel.toggleLike(it) } },
                    onExpandNowPlaying = { viewModel.setNowPlayingExpanded(true) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SpotifyBlack)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(
                        allTracks = allTracks,
                        playlists = allPlaylists,
                        likedTracks = likedTracks,
                        currentTrack = currentTrack,
                        isPlaying = isPlaying,
                        homeFilter = homeFilter,
                        onFilterSelect = { viewModel.setHomeFilter(it) },
                        onTrackSelect = { track, list -> viewModel.playTrack(track, list) },
                        onPlaylistSelect = { viewModel.navigateTo(Screen.PlaylistDetail(it)) },
                        onLikedSongsSelect = { viewModel.navigateTo(Screen.LikedSongsDetail) },
                        onLikeTrack = { viewModel.toggleLike(it) },
                        onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                        onProfileClick = { viewModel.navigateTo(Screen.Profile) }
                    )
                }

                is Screen.Search -> {
                    SearchScreen(
                        searchQuery = searchQuery,
                        selectedGenre = selectedGenre,
                        searchResults = searchResults,
                        currentTrack = currentTrack,
                        isPlaying = isPlaying,
                        onQueryChange = { viewModel.setSearchQuery(it) },
                        onGenreSelect = { viewModel.setSelectedGenre(it) },
                        onTrackSelect = { viewModel.playTrack(it, searchResults) },
                        onLikeTrack = { viewModel.toggleLike(it) },
                        onAddToPlaylist = { viewModel.openAddToPlaylist(it) }
                    )
                }

                is Screen.Library -> {
                    LibraryScreen(
                        playlists = allPlaylists,
                        likedTracks = likedTracks,
                        onCreatePlaylistClick = { viewModel.openCreatePlaylistDialog() },
                        onPlaylistClick = { viewModel.navigateTo(Screen.PlaylistDetail(it)) },
                        onLikedSongsClick = { viewModel.navigateTo(Screen.LikedSongsDetail) },
                        onDeletePlaylist = { viewModel.deletePlaylist(it) }
                    )
                }

                is Screen.PlaylistDetail -> {
                    val playlist = allPlaylists.find { it.id == screen.playlistId }
                    val tracksMap = allTracks.associateBy { it.id }
                    val playlistTracks = playlist?.getTrackIdList()?.mapNotNull { tracksMap[it] } ?: emptyList()

                    if (playlist != null) {
                        PlaylistDetailScreen(
                            playlist = playlist,
                            tracks = playlistTracks,
                            currentTrack = currentTrack,
                            isPlaying = isPlaying,
                            onBack = { viewModel.navigateTo(Screen.Library) },
                            onTrackSelect = { track, list -> viewModel.playTrack(track, list) },
                            onPlayAll = { viewModel.playPlaylist(playlist) },
                            onShuffleAll = {
                                viewModel.toggleShuffle()
                                viewModel.playPlaylist(playlist)
                            },
                            onLikeTrack = { viewModel.toggleLike(it) },
                            onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                            onRemoveFromPlaylist = if (playlist.isCustom) {
                                { track -> viewModel.removeTrackFromPlaylist(playlist.id, track.id) }
                            } else null
                        )
                    }
                }

                is Screen.LikedSongsDetail -> {
                    LikedSongsScreen(
                        likedTracks = likedTracks,
                        currentTrack = currentTrack,
                        isPlaying = isPlaying,
                        onBack = { viewModel.navigateTo(Screen.Library) },
                        onTrackSelect = { track, list -> viewModel.playTrack(track, list) },
                        onPlayAll = {
                            if (likedTracks.isNotEmpty()) {
                                viewModel.playTrack(likedTracks.first(), likedTracks)
                            }
                        },
                        onShuffleAll = {
                            if (likedTracks.isNotEmpty()) {
                                viewModel.toggleShuffle()
                                viewModel.playTrack(likedTracks.first(), likedTracks)
                            }
                        },
                        onLikeTrack = { viewModel.toggleLike(it) },
                        onAddToPlaylist = { viewModel.openAddToPlaylist(it) }
                    )
                }

                is Screen.Profile -> {
                    ProfileScreen(
                        audioQuality = audioQuality,
                        equalizerPreset = equalizerPreset,
                        sleepTimerMinutes = sleepTimerMinutes,
                        onAudioQualityChange = { viewModel.setAudioQuality(it) },
                        onEqualizerPresetChange = { viewModel.setEqualizerPreset(it) },
                        onOpenSleepTimer = { viewModel.openSleepTimerDialog() }
                    )
                }
            }

            // Full Screen Now Playing Sheet with Slide In animation
            AnimatedVisibility(
                visible = isNowPlayingExpanded,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                NowPlayingScreen(
                    track = currentTrack,
                    isPlaying = isPlaying,
                    currentPositionMs = currentPositionMs,
                    durationMs = durationMs,
                    isShuffle = isShuffle,
                    repeatMode = repeatMode,
                    visualizerBars = visualizerBars,
                    sleepTimerMinutes = sleepTimerMinutes,
                    showLyrics = showLyrics,
                    onToggleLyrics = { viewModel.toggleLyrics() },
                    onCollapse = { viewModel.setNowPlayingExpanded(false) },
                    onPlayPause = { viewModel.togglePlayPause() },
                    onSeek = { viewModel.seekTo(it) },
                    onSkipNext = { viewModel.skipNext() },
                    onSkipPrevious = { viewModel.skipPrevious() },
                    onToggleShuffle = { viewModel.toggleShuffle() },
                    onToggleRepeat = { viewModel.toggleRepeat() },
                    onLikeClick = { currentTrack?.let { viewModel.toggleLike(it) } },
                    onOpenSleepTimer = { viewModel.openSleepTimerDialog() }
                )
            }
        }
    }

    // Modal Dialogs
    if (showCreatePlaylistDialog) {
        CreatePlaylistDialog(
            onDismiss = { viewModel.closeCreatePlaylistDialog() },
            onConfirm = { title, desc, color ->
                viewModel.createPlaylist(title, desc, color)
            }
        )
    }

    trackToAddToPlaylist?.let { track ->
        AddToPlaylistDialog(
            track = track,
            playlists = allPlaylists,
            onPlaylistSelected = { playlistId ->
                viewModel.addTrackToPlaylist(playlistId, track.id)
            },
            onCreateNewPlaylist = {
                viewModel.closeAddToPlaylist()
                viewModel.openCreatePlaylistDialog()
            },
            onDismiss = { viewModel.closeAddToPlaylist() }
        )
    }

    if (showSleepTimerDialog) {
        SleepTimerDialog(
            currentMinutes = sleepTimerMinutes,
            onSetTimer = { viewModel.setSleepTimer(it) },
            onDismiss = { viewModel.closeSleepTimerDialog() }
        )
    }
}
