package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.data.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class RepeatMode {
    OFF, ALL, ONE
}

class MusicPlayerManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var mediaPlayer: MediaPlayer? = null
    private var tickerJob: Job? = null
    private var sleepTimerJob: Job? = null

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0)
    val currentPositionMs: StateFlow<Int> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0)
    val durationMs: StateFlow<Int> = _durationMs.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue.asStateFlow()

    private val _currentIndex = MutableStateFlow(-1)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _visualizerBars = MutableStateFlow<List<Float>>(listOf(0.2f, 0.4f, 0.7f, 0.9f, 0.5f, 0.8f, 0.3f))
    val visualizerBars: StateFlow<List<Float>> = _visualizerBars.asStateFlow()

    private val _sleepTimerMinutes = MutableStateFlow<Int?>(null)
    val sleepTimerMinutes: StateFlow<Int?> = _sleepTimerMinutes.asStateFlow()

    // Internal simulation mode in case media streaming has latency/offline
    private var isSimulatedPlayback = false

    fun playTrack(track: Track, newQueue: List<Track>? = null) {
        if (newQueue != null && newQueue.isNotEmpty()) {
            _queue.value = newQueue
            _currentIndex.value = newQueue.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
        } else if (!_queue.value.any { it.id == track.id }) {
            _queue.value = _queue.value + track
            _currentIndex.value = _queue.value.lastIndex
        } else {
            _currentIndex.value = _queue.value.indexOfFirst { it.id == track.id }
        }

        _currentTrack.value = track
        val totalMs = track.durationSeconds * 1000
        _durationMs.value = totalMs
        _currentPositionMs.value = 0

        startMediaPlayer(track)
    }

    private fun startMediaPlayer(track: Track) {
        releaseMediaPlayer()
        isSimulatedPlayback = false

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(track.audioUrl)
                setOnPreparedListener { mp ->
                    try {
                        mp.start()
                        _isPlaying.value = true
                        _durationMs.value = if (mp.duration > 0) mp.duration else track.durationSeconds * 1000
                        startTicker()
                    } catch (e: Exception) {
                        fallbackToSimulated(track)
                    }
                }
                setOnCompletionListener {
                    handleTrackCompletion()
                }
                setOnErrorListener { _, what, extra ->
                    Log.w("NaveedifyPlayer", "MediaPlayer error $what / $extra, using simulated playback")
                    fallbackToSimulated(track)
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            fallbackToSimulated(track)
        }
    }

    private fun fallbackToSimulated(track: Track) {
        releaseMediaPlayer()
        isSimulatedPlayback = true
        _isPlaying.value = true
        _durationMs.value = track.durationSeconds * 1000
        startTicker()
    }

    fun togglePlayPause() {
        val track = _currentTrack.value ?: return

        if (_isPlaying.value) {
            pause()
        } else {
            resume(track)
        }
    }

    fun pause() {
        _isPlaying.value = false
        stopTicker()
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            Log.e("NaveedifyPlayer", "Pause error", e)
        }
    }

    fun resume(track: Track? = _currentTrack.value) {
        if (track == null) return

        if (mediaPlayer != null && !isSimulatedPlayback) {
            try {
                mediaPlayer?.start()
                _isPlaying.value = true
                startTicker()
                return
            } catch (e: Exception) {
                fallbackToSimulated(track)
            }
        } else {
            _isPlaying.value = true
            startTicker()
        }
    }

    fun seekTo(positionMs: Int) {
        val clamped = positionMs.coerceIn(0, _durationMs.value.coerceAtLeast(1000))
        _currentPositionMs.value = clamped
        try {
            if (mediaPlayer != null && !isSimulatedPlayback) {
                mediaPlayer?.seekTo(clamped)
            }
        } catch (e: Exception) {
            Log.e("NaveedifyPlayer", "Seek error", e)
        }
    }

    fun skipNext() {
        val q = _queue.value
        if (q.isEmpty()) return

        var nextIndex = _currentIndex.value + 1
        if (_isShuffle.value && q.size > 1) {
            nextIndex = Random.nextInt(q.size)
        } else if (nextIndex >= q.size) {
            if (_repeatMode.value == RepeatMode.ALL) {
                nextIndex = 0
            } else {
                pause()
                seekTo(0)
                return
            }
        }

        if (nextIndex in q.indices) {
            _currentIndex.value = nextIndex
            playTrack(q[nextIndex], q)
        }
    }

    fun skipPrevious() {
        val q = _queue.value
        if (q.isEmpty()) return

        // If played more than 3 seconds, restart current track
        if (_currentPositionMs.value > 3000) {
            seekTo(0)
            return
        }

        var prevIndex = _currentIndex.value - 1
        if (prevIndex < 0) {
            prevIndex = if (_repeatMode.value == RepeatMode.ALL) q.lastIndex else 0
        }

        if (prevIndex in q.indices) {
            _currentIndex.value = prevIndex
            playTrack(q[prevIndex], q)
        }
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun toggleRepeat() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }

    fun setSleepTimer(minutes: Int?) {
        sleepTimerJob?.cancel()
        _sleepTimerMinutes.value = minutes

        if (minutes != null && minutes > 0) {
            sleepTimerJob = scope.launch {
                delay(minutes * 60 * 1000L)
                pause()
                _sleepTimerMinutes.value = null
            }
        }
    }

    private fun handleTrackCompletion() {
        when (_repeatMode.value) {
            RepeatMode.ONE -> {
                seekTo(0)
                _currentTrack.value?.let { resume(it) }
            }
            RepeatMode.ALL -> {
                skipNext()
            }
            RepeatMode.OFF -> {
                if (_currentIndex.value < _queue.value.lastIndex) {
                    skipNext()
                } else {
                    pause()
                    seekTo(0)
                }
            }
        }
    }

    private fun startTicker() {
        stopTicker()
        tickerJob = scope.launch {
            while (isActive && _isPlaying.value) {
                delay(250)
                if (mediaPlayer != null && !isSimulatedPlayback) {
                    try {
                        if (mediaPlayer?.isPlaying == true) {
                            _currentPositionMs.value = mediaPlayer?.currentPosition ?: 0
                            val dur = mediaPlayer?.duration ?: 0
                            if (dur > 0) _durationMs.value = dur
                        }
                    } catch (e: Exception) {
                        isSimulatedPlayback = true
                    }
                } else if (isSimulatedPlayback) {
                    val next = _currentPositionMs.value + 250
                    if (next >= _durationMs.value && _durationMs.value > 0) {
                        handleTrackCompletion()
                    } else {
                        _currentPositionMs.value = next
                    }
                }

                // Animate visualizer bars
                _visualizerBars.value = List(7) {
                    Random.nextFloat().coerceIn(0.15f, 1.0f)
                }
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun releaseMediaPlayer() {
        stopTicker()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.e("NaveedifyPlayer", "Release error", e)
        }
        mediaPlayer = null
    }

    fun release() {
        releaseMediaPlayer()
        sleepTimerJob?.cancel()
    }
}
