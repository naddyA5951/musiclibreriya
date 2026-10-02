package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Track
import com.example.ui.theme.SpotifyBlack
import com.example.ui.theme.SpotifyCard
import com.example.ui.theme.SpotifyCardElevated
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.SpotifyLightGray
import com.example.ui.theme.SpotifyPureBlack
import com.example.ui.theme.SpotifySubtext
import com.example.ui.theme.SpotifyWhite
import com.example.ui.viewmodel.Screen
import kotlin.math.abs

/**
 * Unified bottom navigation bar component for Naveedify.
 * Features a persistent floating playback bar (with play, pause, previous, skip next, like,
 * animated waveform, and swipe gestures) docked seamlessly atop the Material 3 Navigation Bar.
 */
@Composable
fun NaveedifyBottomBar(
    currentScreen: Screen,
    currentTrack: Track?,
    isPlaying: Boolean,
    progressMs: Int,
    durationMs: Int,
    visualizerBars: List<Float>,
    onNavigate: (Screen) -> Unit,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onLikeClick: () -> Unit,
    onExpandNowPlaying: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .testTag("naveedify_bottom_bar")
    ) {
        // Persistent Playback Control Bar
        AnimatedVisibility(
            visible = currentTrack != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            if (currentTrack != null) {
                PersistentPlaybackBar(
                    track = currentTrack,
                    isPlaying = isPlaying,
                    progressMs = progressMs,
                    durationMs = durationMs,
                    visualizerBars = visualizerBars,
                    onPlayPause = onPlayPause,
                    onSkipNext = onSkipNext,
                    onSkipPrevious = onSkipPrevious,
                    onLikeClick = onLikeClick,
                    onExpandClick = onExpandNowPlaying
                )
            }
        }

        // Material 3 Navigation Tabs
        NavigationBar(
            containerColor = SpotifyPureBlack,
            contentColor = SpotifyWhite,
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .testTag("bottom_nav_bar"),
            tonalElevation = 8.dp
        ) {
            val isHome = currentScreen is Screen.Home
            NavigationBarItem(
                selected = isHome,
                onClick = { onNavigate(Screen.Home) },
                icon = {
                    Icon(
                        imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home"
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SpotifyGreen,
                    selectedTextColor = SpotifyWhite,
                    indicatorColor = SpotifyCard,
                    unselectedIconColor = SpotifyLightGray,
                    unselectedTextColor = SpotifyLightGray
                ),
                modifier = Modifier.testTag("nav_home")
            )

            val isSearch = currentScreen is Screen.Search
            NavigationBarItem(
                selected = isSearch,
                onClick = { onNavigate(Screen.Search) },
                icon = {
                    Icon(
                        imageVector = if (isSearch) Icons.Filled.Search else Icons.Outlined.Search,
                        contentDescription = "Search"
                    )
                },
                label = {
                    Text(
                        text = "Search",
                        fontWeight = if (isSearch) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SpotifyGreen,
                    selectedTextColor = SpotifyWhite,
                    indicatorColor = SpotifyCard,
                    unselectedIconColor = SpotifyLightGray,
                    unselectedTextColor = SpotifyLightGray
                ),
                modifier = Modifier.testTag("nav_search")
            )

            val isLibrary = currentScreen is Screen.Library ||
                    currentScreen is Screen.PlaylistDetail ||
                    currentScreen is Screen.LikedSongsDetail
            NavigationBarItem(
                selected = isLibrary,
                onClick = { onNavigate(Screen.Library) },
                icon = {
                    Icon(
                        imageVector = if (isLibrary) Icons.Filled.LibraryMusic else Icons.Outlined.LibraryMusic,
                        contentDescription = "Your Library"
                    )
                },
                label = {
                    Text(
                        text = "Your Library",
                        fontWeight = if (isLibrary) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SpotifyGreen,
                    selectedTextColor = SpotifyWhite,
                    indicatorColor = SpotifyCard,
                    unselectedIconColor = SpotifyLightGray,
                    unselectedTextColor = SpotifyLightGray
                ),
                modifier = Modifier.testTag("nav_library")
            )

            val isProfile = currentScreen is Screen.Profile
            NavigationBarItem(
                selected = isProfile,
                onClick = { onNavigate(Screen.Profile) },
                icon = {
                    Icon(
                        imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Profile"
                    )
                },
                label = {
                    Text(
                        text = "Naveed",
                        fontWeight = if (isProfile) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SpotifyGreen,
                    selectedTextColor = SpotifyWhite,
                    indicatorColor = SpotifyCard,
                    unselectedIconColor = SpotifyLightGray,
                    unselectedTextColor = SpotifyLightGray
                ),
                modifier = Modifier.testTag("nav_profile")
            )
        }
    }
}

/**
 * High-fidelity persistent playback bar equipped with:
 * - Real-time progress line
 * - Dynamic album artwork
 * - Animated visualizer equalizer icon
 * - Track title & artist
 * - Full suite of controls: Like, Previous, Play/Pause, Skip Next
 * - Horizontal swipe gesture support (Swipe Left = Next, Swipe Right = Previous)
 * - Click to expand to full Now Playing screen
 */
@Composable
fun PersistentPlaybackBar(
    track: Track,
    isPlaying: Boolean,
    progressMs: Int,
    durationMs: Int,
    visualizerBars: List<Float>,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onLikeClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressFraction = if (durationMs > 0) {
        (progressMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 200),
        label = "playback_progress"
    )

    var totalDrag by remember { mutableFloatStateOf(0f) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .shadow(16.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(track.coverGradientStart).copy(alpha = 0.35f),
                        SpotifyCard,
                        SpotifyCardElevated
                    )
                )
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { totalDrag = 0f },
                    onDragEnd = {
                        if (totalDrag < -60f) {
                            onSkipNext()
                        } else if (totalDrag > 60f) {
                            onSkipPrevious()
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        totalDrag += dragAmount
                    }
                )
            }
            .clickable { onExpandClick() }
            .testTag("persistent_player_bar"),
        color = Color.Transparent,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(SpotifyCard.copy(alpha = 0.95f))
        ) {
            // Precision playback progress indicator
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .testTag("persistent_progress_bar"),
                color = SpotifyGreen,
                trackColor = SpotifyCardElevated
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album Art with small equalizer badge
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AlbumArtThumbnail(
                        startColor = Color(track.coverGradientStart),
                        endColor = Color(track.coverGradientEnd),
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        cornerRadius = 6.dp
                    )

                    if (isPlaying) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(SpotifyPureBlack.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = SpotifyGreen,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Track title & artist with tap to expand
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = track.title,
                        color = if (isPlaying) SpotifyGreen else SpotifyWhite,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = track.artist,
                        color = SpotifyLightGray,
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Like / Heart button
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("persistent_like_button")
                ) {
                    Icon(
                        imageVector = if (track.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (track.isLiked) "Unlike" else "Like",
                        tint = if (track.isLiked) SpotifyGreen else SpotifyLightGray,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Previous button
                IconButton(
                    onClick = onSkipPrevious,
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("persistent_skip_prev")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Track",
                        tint = SpotifyLightGray,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Primary Play / Pause Button with tactile circular background
                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying) SpotifyWhite else SpotifyGreen)
                        .testTag("persistent_play_pause")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = SpotifyPureBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Skip Next button
                IconButton(
                    onClick = onSkipNext,
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("persistent_skip_next")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Track",
                        tint = SpotifyLightGray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
