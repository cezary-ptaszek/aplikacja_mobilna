package com.example.projektowanie_mobilne.fragments.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import android.widget.ImageView
import com.example.projektowanie_mobilne.R

class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val imageUrls = listOf(
        "https://picsum.photos/id/24/200/300",
        "https://picsum.photos/id/34/200/300",
        "https://picsum.photos/id/75/200/300",
        "https://picsum.photos/id/78/200/300",
        "https://picsum.photos/id/91/200/300",
        "https://picsum.photos/id/63/200/300",
        "https://picsum.photos/id/3/200/300",
        "https://picsum.photos/id/50/200/300",
        "https://picsum.photos/id/55/200/300",
        "https://picsum.photos/id/60/200/300",
        "https://picsum.photos/id/107/200/300",
        "https://picsum.photos/id/70/200/300"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewGallery)
        recyclerView.layoutManager = GridLayoutManager(context, 3)  // Siatka 3 kolumnowa

        imageAdapter = ImageAdapter(imageUrls) { imageUrl ->
            // Po kliknięciu na zdjęcie, otwieramy pełnoekranowy widok
            val fragment = FullScreenFragment.newInstance(imageUrl)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = imageAdapter
        return view
    }
}

class ImageAdapter(
    private val imageUrls: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Picasso.get().load(imageUrl).into(holder.imageView)
        holder.itemView.setOnClickListener {
            onClick(imageUrl)
        }
    }

    override fun getItemCount(): Int = imageUrls.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewThumbnail)
    }
}
