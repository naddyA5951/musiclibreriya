package com.example.data.db

import com.example.data.model.Playlist
import com.example.data.model.Track

object DefaultMusicData {
    val sampleTracks = listOf(
        Track(
            id = "track_1",
            title = "Naveed's Midnight Code",
            artist = "Naveed Ali & The Algorithms",
            album = "Byte Beats Vol. 1",
            durationSeconds = 214,
            coverGradientStart = 0xFF1ED760L,
            coverGradientEnd = 0xFF0D47A1L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            genre = "Lo-Fi",
            lyrics = """
[00:12] Staring at the terminal screen
[00:24] Neon glow and a cup of caffeine
[00:36] Naveed's syntax shining clean
[00:48] Building worlds inside the machine
[01:05] Algorithms dancing in the night
[01:18] Every single test compiling bright
[01:32] We keep on shipping till morning light
            """.trimIndent(),
            isLiked = true,
            playCount = 142,
            isFeatured = true
        ),
        Track(
            id = "track_2",
            title = "Karachi Skyline Drive",
            artist = "South Asian Sunset",
            album = "Coastal Highway",
            durationSeconds = 188,
            coverGradientStart = 0xFFFF7043L,
            coverGradientEnd = 0xFF7B1FA2L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            genre = "Synthwave",
            lyrics = """
[00:08] Warm sea breeze through the open glass
[00:22] Golden hour watching shadows pass
[00:35] Cruising down the coastal line
[00:50] Everything feels just fine
[01:10] City lights reflect on the bay
[01:25] Leaving all the worries miles away
            """.trimIndent(),
            isLiked = true,
            playCount = 98,
            isFeatured = true
        ),
        Track(
            id = "track_3",
            title = "Deep Focus Kotlin",
            artist = "Zen Developer",
            album = "Flow State Sessions",
            durationSeconds = 245,
            coverGradientStart = 0xFF00B0FFL,
            coverGradientEnd = 0xFF1DE9B6L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            genre = "Lo-Fi",
            lyrics = """
[00:15] Pure focus, gentle chime
[00:30] Lost inside the rhythm of time
[00:45] Breathe in, write the flow
[01:00] Watch the ideas gently grow
            """.trimIndent(),
            isLiked = false,
            playCount = 76,
            isFeatured = true
        ),
        Track(
            id = "track_4",
            title = "Lahore Rain & Sitar",
            artist = "Naveed's Heritage Collective",
            album = "Old Walled City",
            durationSeconds = 205,
            coverGradientStart = 0xFFFFD54FL,
            coverGradientEnd = 0xFFD84315L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            genre = "Acoustic",
            lyrics = """
[00:10] Raindrops tap on old wooden eaves
[00:25] Melodies rustle like jasmine leaves
[00:40] Sitar resonant, deep and sweet
[00:55] History walking down every street
            """.trimIndent(),
            isLiked = true,
            playCount = 112,
            isFeatured = false
        ),
        Track(
            id = "track_5",
            title = "Cyberpunk Horizon 2077",
            artist = "Glitch Overdrive",
            album = "Neon Circuitry",
            durationSeconds = 195,
            coverGradientStart = 0xFFE040FBL,
            coverGradientEnd = 0xFF2979FFL,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            genre = "Electronic",
            lyrics = """
[00:14] High tech, low lights
[00:28] Cybernetic endless nights
[00:42] Pulsing signals through the wire
[00:56] Electric dreams fueling the fire
            """.trimIndent(),
            isLiked = false,
            playCount = 63,
            isFeatured = true
        ),
        Track(
            id = "track_6",
            title = "Acoustic Coffee Morning",
            artist = "Oliver & Strings",
            album = "Sunday Morning Sun",
            durationSeconds = 175,
            coverGradientStart = 0xFF8D6E63L,
            coverGradientEnd = 0xFFFFAB91L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
            genre = "Acoustic",
            lyrics = """
[00:09] Steam rising from a porcelain cup
[00:20] Watching the golden morning wake up
[00:32] Acoustic frets under gentle hands
[00:45] Peace across the morning lands
            """.trimIndent(),
            isLiked = true,
            playCount = 85,
            isFeatured = false
        ),
        Track(
            id = "track_7",
            title = "Euphoria Anthem",
            artist = "Nova Galaxy",
            album = "Supernova Stars",
            durationSeconds = 220,
            coverGradientStart = 0xFFFF1744L,
            coverGradientEnd = 0xFFFF9100L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
            genre = "Pop",
            lyrics = """
[00:11] Hands in the air, feeling the beat
[00:24] Bass vibration shaking the street
[00:38] Can you feel it tonight?
[00:51] Shining forever in crystal light
            """.trimIndent(),
            isLiked = false,
            playCount = 54,
            isFeatured = false
        ),
        Track(
            id = "track_8",
            title = "Late Night Ambient Reverie",
            artist = "Lunar Tides",
            album = "Zero Gravity",
            durationSeconds = 230,
            coverGradientStart = 0xFF311B92L,
            coverGradientEnd = 0xFF00E676L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
            genre = "Ambient",
            lyrics = """
[00:15] Stars drift across the velvet sky
[00:30] Quiet whispers floating by
[00:48] Submerged in calm and stillness deep
[01:05] Gentle rest before we sleep
            """.trimIndent(),
            isLiked = true,
            playCount = 92,
            isFeatured = false
        ),
        Track(
            id = "track_9",
            title = "Hip-Hop Cypher 042",
            artist = "Beatmaker Ali",
            album = "Underground Tape",
            durationSeconds = 182,
            coverGradientStart = 0xFF212121L,
            coverGradientEnd = 0xFFFFD600L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
            genre = "Hip-Hop",
            lyrics = """
[00:08] Check the mic one two
[00:20] Naveed's rhythm breaking through
[00:34] Drum patterns tight and clear
[00:46] The best beats you'll hear this year
            """.trimIndent(),
            isLiked = false,
            playCount = 49,
            isFeatured = false
        ),
        Track(
            id = "track_10",
            title = "Chill Workout Boost",
            artist = "Pulse Athletic",
            album = "Peak Heart Rate",
            durationSeconds = 198,
            coverGradientStart = 0xFF00E5FFL,
            coverGradientEnd = 0xFF76FF03L,
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3",
            genre = "Workout",
            lyrics = """
[00:12] Push the pace, step by step
[00:24] Every mile, every rep
[00:36] Strength within, mind on fire
[00:48] Reaching higher and higher
            """.trimIndent(),
            isLiked = false,
            playCount = 38,
            isFeatured = false
        )
    )

    val samplePlaylists = listOf(
        Playlist(
            id = "playlist_naveed_mix",
            title = "Made for Naveed",
            description = "Your personal daily soundtrack updated with your favorite coding & chill tracks.",
            gradientStart = 0xFF1ED760L,
            gradientEnd = 0xFF121212L,
            isCustom = false,
            trackIdsJson = "track_1,track_2,track_3,track_4,track_8"
        ),
        Playlist(
            id = "playlist_coding",
            title = "Kotlin & Code Flow",
            description = "Deep focus lo-fi beats, ambient electronics, and zero distractions.",
            gradientStart = 0xFF00B0FFL,
            gradientEnd = 0xFF311B92L,
            isCustom = false,
            trackIdsJson = "track_1,track_3,track_5,track_8"
        ),
        Playlist(
            id = "playlist_night_drive",
            title = "Late Night Drive",
            description = "Cruising city lights with retro synthwave and atmospheric soundscapes.",
            gradientStart = 0xFFFF7043L,
            gradientEnd = 0xFF4A148CL,
            isCustom = false,
            trackIdsJson = "track_2,track_5,track_9"
        ),
        Playlist(
            id = "playlist_acoustic",
            title = "Chai & Acoustic Strings",
            description = "Warm acoustic guitars, morning rain, and serene melodies.",
            gradientStart = 0xFFFFD54FL,
            gradientEnd = 0xFF3E2723L,
            isCustom = false,
            trackIdsJson = "track_4,track_6,track_8"
        ),
        Playlist(
            id = "playlist_workout",
            title = "Workout Energy",
            description = "High tempo electronic and upbeat rhythms to power your sessions.",
            gradientStart = 0xFFFF1744L,
            gradientEnd = 0xFF00E5FFL,
            isCustom = false,
            trackIdsJson = "track_5,track_7,track_10"
        )
    )
}
