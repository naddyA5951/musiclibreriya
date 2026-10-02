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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SpotifyBlack
import com.example.ui.theme.SpotifyCard
import com.example.ui.theme.SpotifyCardElevated
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.SpotifyLightGray
import com.example.ui.theme.SpotifyPureBlack
import com.example.ui.theme.SpotifySubtext
import com.example.ui.theme.SpotifyWhite

@Composable
fun ProfileScreen(
    audioQuality: String,
    equalizerPreset: String,
    sleepTimerMinutes: Int?,
    onAudioQualityChange: (String) -> Unit,
    onEqualizerPresetChange: (String) -> Unit,
    onOpenSleepTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isOfflineMode by remember { mutableStateOf(false) }
    var isNormalizeAudio by remember { mutableStateOf(true) }
    var showQualityMenu by remember { mutableStateOf(false) }
    var showEqMenu by remember { mutableStateOf(false) }

    val qualityOptions = listOf("Normal (96 kbps)", "High (160 kbps)", "Very High (320 kbps)")
    val eqPresets = listOf("Bass Boost", "Vocal Booster", "Electronic", "Acoustic", "Flat")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .padding(horizontal = 16.dp)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SpotifyCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1E3A2F), SpotifyCard)
                            )
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "N",
                            color = SpotifyPureBlack,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Naveed Ali",
                        color = SpotifyWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "naveedalicodes1@gmail.com",
                        color = SpotifyLightGray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SpotifyGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SpotifyGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Naveedify Premium Hi-Fi",
                                color = SpotifyGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Listening Stats Summary
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Listening Statistics",
                color = SpotifyWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Minutes Streamed",
                    value = "2,480",
                    subtitle = "This Month",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Top Genre",
                    value = "Lo-Fi",
                    subtitle = "Coding Beats",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Settings Section
        item {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Audio & Playback",
                color = SpotifyWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = SpotifyCard)
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    // Audio Streaming Quality
                    Box {
                        SettingRow(
                            icon = Icons.Default.HighQuality,
                            title = "Streaming Quality",
                            subtitle = audioQuality,
                            onClick = { showQualityMenu = true }
                        )

                        DropdownMenu(
                            expanded = showQualityMenu,
                            onDismissRequest = { showQualityMenu = false },
                            modifier = Modifier.background(SpotifyCardElevated)
                        ) {
                            qualityOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt, color = SpotifyWhite) },
                                    onClick = {
                                        onAudioQualityChange(opt)
                                        showQualityMenu = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = SpotifyCardElevated, thickness = 0.5.dp)

                    // Equalizer
                    Box {
                        SettingRow(
                            icon = Icons.Default.Equalizer,
                            title = "Equalizer",
                            subtitle = equalizerPreset,
                            onClick = { showEqMenu = true }
                        )

                        DropdownMenu(
                            expanded = showEqMenu,
                            onDismissRequest = { showEqMenu = false },
                            modifier = Modifier.background(SpotifyCardElevated)
                        ) {
                            eqPresets.forEach { preset ->
                                DropdownMenuItem(
                                    text = { Text(preset, color = SpotifyWhite) },
                                    onClick = {
                                        onEqualizerPresetChange(preset)
                                        showEqMenu = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = SpotifyCardElevated, thickness = 0.5.dp)

                    // Output Device
                    SettingRow(
                        icon = Icons.Default.Headphones,
                        title = "Output Device",
                        subtitle = "Naveed's Pixel Buds Pro (Connected)",
                        onClick = {}
                    )

                    HorizontalDivider(color = SpotifyCardElevated, thickness = 0.5.dp)

                    // Sleep Timer
                    SettingRow(
                        icon = Icons.Default.Bedtime,
                        title = "Sleep Timer",
                        subtitle = if (sleepTimerMinutes != null) "$sleepTimerMinutes minutes remaining" else "Off",
                        onClick = onOpenSleepTimer
                    )
                }
            }
        }

        // Preferences Toggles
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Playback Preferences",
                color = SpotifyWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = SpotifyCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Normalize Volume",
                                color = SpotifyWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Set the same volume level for all tracks",
                                color = SpotifySubtext,
                                fontSize = 12.sp
                            )
                        }

                        Switch(
                            checked = isNormalizeAudio,
                            onCheckedChange = { isNormalizeAudio = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SpotifyPureBlack,
                                checkedTrackColor = SpotifyGreen,
                                uncheckedThumbColor = SpotifyLightGray,
                                uncheckedTrackColor = SpotifyCardElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Offline Mode",
                                color = SpotifyWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Only play downloaded music & cached tracks",
                                color = SpotifySubtext,
                                fontSize = 12.sp
                            )
                        }

                        Switch(
                            checked = isOfflineMode,
                            onCheckedChange = { isOfflineMode = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SpotifyPureBlack,
                                checkedTrackColor = SpotifyGreen,
                                uncheckedThumbColor = SpotifyLightGray,
                                uncheckedTrackColor = SpotifyCardElevated
                            )
                        )
                    }
                }
            }
        }

        // About & Version
        item {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = SpotifySubtext,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Naveedify v1.0.0 • Personalized for Naveed Ali",
                    color = SpotifySubtext,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SpotifyCard)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = SpotifySubtext, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = SpotifyGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = SpotifyLightGray, fontSize = 11.sp)
        }
    }
}

@Composable
fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SpotifyLightGray,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = SpotifyWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                color = SpotifySubtext,
                fontSize = 12.sp
            )
        }
    }
}
