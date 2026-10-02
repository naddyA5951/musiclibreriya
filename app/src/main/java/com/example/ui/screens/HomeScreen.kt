package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Playlist
import com.example.data.model.Track
import com.example.ui.components.AlbumArtThumbnail
import com.example.ui.components.TrackRowItem
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.SpotifyBlack
import com.example.ui.theme.SpotifyCard
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.SpotifyLightGray
import com.example.ui.theme.SpotifyPureBlack
import com.example.ui.theme.SpotifySubtext
import com.example.ui.theme.SpotifyWhite
import java.util.Calendar

@Composable
fun HomeScreen(
    allTracks: List<Track>,
    playlists: List<Playlist>,
    likedTracks: List<Track>,
    currentTrack: Track?,
    isPlaying: Boolean,
    homeFilter: String,
    onFilterSelect: (String) -> Unit,
    onTrackSelect: (Track, List<Track>) -> Unit,
    onPlaylistSelect: (String) -> Unit,
    onLikedSongsSelect: () -> Unit,
    onLikeTrack: (Track) -> Unit,
    onAddToPlaylist: (Track) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good morning, Naveed"
            in 12..17 -> "Good afternoon, Naveed"
            else -> "Good evening, Naveed"
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1E3A2F),
                                SpotifyBlack
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = greeting,
                        color = SpotifyWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SpotifyGreen)
                                .clickable { onProfileClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "N",
                                color = SpotifyPureBlack,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Filter chips: All, Music, Podcasts
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("All", "Music", "Podcasts").forEach { filter ->
                        val isSelected = homeFilter == filter
                        AssistChip(
                            onClick = { onFilterSelect(filter) },
                            label = {
                                Text(
                                    text = filter,
                                    color = if (isSelected) SpotifyPureBlack else SpotifyWhite,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) SpotifyGreen else SpotifyCard
                            ),
                            border = null,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Spotify-style 2x3 quick grid
                QuickAccessGrid(
                    playlists = playlists,
                    likedCount = likedTracks.size,
                    onLikedSongsSelect = onLikedSongsSelect,
                    onPlaylistSelect = onPlaylistSelect
                )
            }
        }

        // Section: Made for Naveed Ali
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Made for Naveed Ali",
                    color = SpotifyWhite,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(playlists) { playlist ->
                        PlaylistCarouselCard(
                            playlist = playlist,
                            onClick = { onPlaylistSelect(playlist.id) }
                        )
                    }
                }
            }
        }

        // Section: Naveed's Daily Mix (Tracks)
        item {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Naveed's Heavy Rotation",
                            color = SpotifyWhite,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Your most played tracks & recommendations",
                            color = SpotifySubtext,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                val featuredTracks = allTracks.take(6)
                featuredTracks.forEach { track ->
                    TrackRowItem(
                        track = track,
                        isCurrentTrack = currentTrack?.id == track.id,
                        isPlaying = isPlaying,
                        onTrackClick = { onTrackSelect(track, allTracks) },
                        onLikeClick = { onLikeTrack(track) },
                        onAddToPlaylistClick = { onAddToPlaylist(track) }
                    )
                }
            }
        }

        // Section: Discover New Horizons
        item {
            Column(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)) {
                Text(
                    text = "Discover Weekly",
                    color = SpotifyWhite,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(allTracks.drop(3)) { track ->
                        TrackCarouselCard(
                            track = track,
                            onClick = { onTrackSelect(track, allTracks) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickAccessGrid(
    playlists: List<Playlist>,
    likedCount: Int,
    onLikedSongsSelect: () -> Unit,
    onPlaylistSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Row 1: Liked Songs + Playlist 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickGridItem(
                title = "Liked Songs",
                startColor = AccentPurple,
                endColor = Color(0xFF1E88E5),
                isLikedCard = true,
                onClick = onLikedSongsSelect,
                modifier = Modifier.weight(1f)
            )

            val p1 = playlists.getOrNull(0)
            if (p1 != null) {
                QuickGridItem(
                    title = p1.title,
                    startColor = Color(p1.gradientStart),
                    endColor = Color(p1.gradientEnd),
                    onClick = { onPlaylistSelect(p1.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 2: Playlist 2 + Playlist 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val p2 = playlists.getOrNull(1)
            val p3 = playlists.getOrNull(2)

            if (p2 != null) {
                QuickGridItem(
                    title = p2.title,
                    startColor = Color(p2.gradientStart),
                    endColor = Color(p2.gradientEnd),
                    onClick = { onPlaylistSelect(p2.id) },
                    modifier = Modifier.weight(1f)
                )
            }

            if (p3 != null) {
                QuickGridItem(
                    title = p3.title,
                    startColor = Color(p3.gradientStart),
                    endColor = Color(p3.gradientEnd),
                    onClick = { onPlaylistSelect(p3.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 3: Playlist 4 + Playlist 5
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val p4 = playlists.getOrNull(3)
            val p5 = playlists.getOrNull(4)

            if (p4 != null) {
                QuickGridItem(
                    title = p4.title,
                    startColor = Color(p4.gradientStart),
                    endColor = Color(p4.gradientEnd),
                    onClick = { onPlaylistSelect(p4.id) },
                    modifier = Modifier.weight(1f)
                )
            }

            if (p5 != null) {
                QuickGridItem(
                    title = p5.title,
                    startColor = Color(p5.gradientStart),
                    endColor = Color(p5.gradientEnd),
                    onClick = { onPlaylistSelect(p5.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun QuickGridItem(
    title: String,
    startColor: Color,
    endColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLikedCard: Boolean = false
) {
    Card(
        modifier = modifier
            .height(54.dp)
            .clickable { onClick() }
            .testTag("quick_item_$title"),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = SpotifyCard)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Brush.linearGradient(listOf(startColor, endColor))),
                contentAlignment = Alignment.Center
            ) {
                if (isLikedCard) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = SpotifyWhite,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    AlbumArtThumbnail(
                        startColor = startColor,
                        endColor = endColor,
                        modifier = Modifier.size(54.dp),
                        cornerRadius = 0.dp
                    )
                }
            }

            Text(
                text = title,
                color = SpotifyWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun PlaylistCarouselCard(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(148.dp)
            .clickable { onClick() }
            .testTag("playlist_card_${playlist.id}")
    ) {
        AlbumArtThumbnail(
            startColor = Color(playlist.gradientStart),
            endColor = Color(playlist.gradientEnd),
            modifier = Modifier
                .size(148.dp)
                .clip(RoundedCornerShape(8.dp)),
            initials = playlist.title
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = playlist.title,
            color = SpotifyWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = playlist.description,
            color = SpotifySubtext,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TrackCarouselCard(
    track: Track,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(136.dp)
            .clickable { onClick() }
            .testTag("track_card_${track.id}")
    ) {
        AlbumArtThumbnail(
            startColor = Color(track.coverGradientStart),
            endColor = Color(track.coverGradientEnd),
            modifier = Modifier
                .size(136.dp)
                .clip(RoundedCornerShape(8.dp)),
            initials = track.genre
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = track.title,
            color = SpotifyWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = track.artist,
            color = SpotifySubtext,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
