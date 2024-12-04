package com.example.projektowanie_mobilne.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projektowanie_mobilne.R
import java.io.IOException

class MusicFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private var musicFiles = listOf(
        R.raw.music1,
        R.raw.music2,
        R.raw.music3
    )
    private var currentTrackIndex = 0
    private lateinit var titleTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_music, container, false)

        titleTextView = binding.findViewById(R.id.musicTitle)
        durationTextView = binding.findViewById(R.id.musicDuration)
        playButton = binding.findViewById(R.id.playButton)
        pauseButton = binding.findViewById(R.id.pauseButton)
        stopButton = binding.findViewById(R.id.stopButton)
        prevButton = binding.findViewById(R.id.prevButton)
        nextButton = binding.findViewById(R.id.nextButton)

        // Przyciski
        playButton.setOnClickListener { playMusic() }
        pauseButton.setOnClickListener { pauseMusic() }
        stopButton.setOnClickListener { stopMusic() }
        prevButton.setOnClickListener { previousTrack() }
        nextButton.setOnClickListener { nextTrack() }

        // Ustawienia początkowe
        updateTrackInfo()

        return binding
    }

    private fun playMusic() {
        try {
            mediaPlayer = MediaPlayer.create(requireContext(), musicFiles[currentTrackIndex])
            mediaPlayer?.apply {
                start()

                // Zaktualizuj tytuł i czas trwania
                updateTrackInfo()

                // Włącz przyciski
                playButton.isEnabled = false
                pauseButton.isEnabled = true
                stopButton.isEnabled = true
                prevButton.isEnabled = true
                nextButton.isEnabled = true

                // Zaktualizuj czas trwania
                val duration = this.duration / 1000
                updateDuration(duration)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        playButton.isEnabled = true
        pauseButton.isEnabled = false
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        playButton.isEnabled = true
        pauseButton.isEnabled = false
        stopButton.isEnabled = false
        prevButton.isEnabled = false
        nextButton.isEnabled = true
        durationTextView.text = "0:00"
    }

    private fun updateTrackInfo() {
        // Ustawienie tytułu muzyki
        val currentTrack = musicFiles[currentTrackIndex]
        titleTextView.text = when (currentTrack) {
            R.raw.music1 -> "Muzyka 1"
            R.raw.music2 -> "Muzyka 2"
            R.raw.music3 -> "Muzyka 3"
            else -> "Tytuł utworu"
        }
    }

    private fun updateDuration(seconds: Int) {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        durationTextView.text = String.format("%02d:%02d", minutes, remainingSeconds)
    }

    // Obsługuje przycisk "Next"
    private fun nextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % musicFiles.size
        stopMusic()  // Zatrzymaj aktualny utwór przed przejściem do następnego
        playMusic()  // Rozpocznij odtwarzanie następnego utworu
    }

    // Obsługuje przycisk "Previous"
    private fun previousTrack() {
        currentTrackIndex = if (currentTrackIndex == 0) {
            musicFiles.size - 1
        } else {
            currentTrackIndex - 1
        }
        stopMusic()  // Zatrzymaj aktualny utwór przed przejściem do poprzedniego
        playMusic()  // Rozpocznij odtwarzanie poprzedniego utworu
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.release()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
    }
}


