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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Track
import com.example.ui.components.TrackRowItem
import com.example.ui.theme.SpotifyBlack
import com.example.ui.theme.SpotifyCard
import com.example.ui.theme.SpotifyCardElevated
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.SpotifyLightGray
import com.example.ui.theme.SpotifySubtext
import com.example.ui.theme.SpotifyWhite

data class BrowseGenre(
    val title: String,
    val startColor: Color,
    val endColor: Color
)

@Composable
fun SearchScreen(
    searchQuery: String,
    selectedGenre: String?,
    searchResults: List<Track>,
    currentTrack: Track?,
    isPlaying: Boolean,
    onQueryChange: (String) -> Unit,
    onGenreSelect: (String?) -> Unit,
    onTrackSelect: (Track) -> Unit,
    onLikeTrack: (Track) -> Unit,
    onAddToPlaylist: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    val browseGenres = listOf(
        BrowseGenre("Lo-Fi", Color(0xFF00796B), Color(0xFF004D40)),
        BrowseGenre("Synthwave", Color(0xFF7B1FA2), Color(0xFF4A148C)),
        BrowseGenre("Acoustic", Color(0xFFE65100), Color(0xFFBF360C)),
        BrowseGenre("Hip-Hop", Color(0xFFF57F17), Color(0xFF212121)),
        BrowseGenre("Electronic", Color(0xFF0288D1), Color(0xFF01579B)),
        BrowseGenre("Ambient", Color(0xFF512DA8), Color(0xFF311B92)),
        BrowseGenre("Workout", Color(0xFFD32F2F), Color(0xFFB71C1C)),
        BrowseGenre("Pop", Color(0xFFC2185B), Color(0xFF880E4F)),
        BrowseGenre("Podcasts", Color(0xFF388E3C), Color(0xFF1B5E20))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .padding(horizontal = 16.dp)
            .testTag("search_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search",
            color = SpotifyWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "What do you want to listen to?",
                    color = SpotifySubtext,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = SpotifyLightGray
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = SpotifyLightGray
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SpotifyCard,
                unfocusedContainerColor = SpotifyCard,
                focusedTextColor = SpotifyWhite,
                unfocusedTextColor = SpotifyWhite,
                focusedBorderColor = SpotifyGreen,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input")
        )

        // Selected Genre Filter Chip
        if (selectedGenre != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Filtering by: ", color = SpotifySubtext, fontSize = 12.sp)
                AssistChip(
                    onClick = { onGenreSelect(null) },
                    label = { Text(selectedGenre, color = SpotifyWhite) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Filter",
                            tint = SpotifyWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(containerColor = SpotifyCardElevated),
                    border = null,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searchQuery.isNotEmpty() || selectedGenre != null) {
            // Search Results List
            if (searchResults.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No tracks found",
                            color = SpotifyWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try searching for a different song, artist or genre",
                            color = SpotifySubtext,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 120.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Tracks (${searchResults.size})",
                            color = SpotifyWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(searchResults) { track ->
                        TrackRowItem(
                            track = track,
                            isCurrentTrack = currentTrack?.id == track.id,
                            isPlaying = isPlaying,
                            onTrackClick = { onTrackSelect(track) },
                            onLikeClick = { onLikeTrack(track) },
                            onAddToPlaylistClick = { onAddToPlaylist(track) }
                        )
                    }
                }
            }
        } else {
            // Browse All Genres 2-Column Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 120.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Browse all",
                        color = SpotifyWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(browseGenres) { genre ->
                    GenreCard(
                        genre = genre,
                        onClick = { onGenreSelect(genre.title) }
                    )
                }
            }
        }
    }
}

@Composable
fun GenreCard(
    genre: BrowseGenre,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable { onClick() }
            .testTag("genre_card_${genre.title}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(genre.startColor, genre.endColor)
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = genre.title,
                color = SpotifyWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = SpotifyWhite.copy(alpha = 0.35f),
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
