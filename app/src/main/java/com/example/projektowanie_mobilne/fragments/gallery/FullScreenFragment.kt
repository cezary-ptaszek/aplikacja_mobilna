package com.example.projektowanie_mobilne.fragments.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.projektowanie_mobilne.R
import com.squareup.picasso.Picasso

class FullScreenFragment : Fragment() {

    companion object {
        private const val IMAGE_URL_KEY = "image_url"

        fun newInstance(imageUrl: String): FullScreenFragment {
            val fragment = FullScreenFragment()
            val args = Bundle()
            args.putString(IMAGE_URL_KEY, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var imageView: ImageView
    private var imageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fullscreen, container, false)
        imageView = view.findViewById(R.id.imageViewFullScreen)

        // Pobranie URL zdjęcia i załadowanie go do ImageView
        imageUrl = arguments?.getString(IMAGE_URL_KEY)
        imageUrl?.let {
            Picasso.get().load(it).into(imageView)
        }

        // Dodanie akcji na kliknięcie w dowolne miejsce na ekranie
        view.setOnClickListener {
            parentFragmentManager.popBackStack() // Powrót do poprzedniego fragmentu
        }

        return view
    }
}
