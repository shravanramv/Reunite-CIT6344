package com.example.reuniteapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import com.example.reuniteapp.R  // Replace with your actual package name

class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize search view
        searchView = view.findViewById(R.id.searchView)

        // Set up search view listeners
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text change
                return true
            }
        })

        // Set up Lost Item Card
        val lostItemCard = view.findViewById<View>(R.id.lostItemCard)
        // Add click or other listeners to interact with this card

        // Set up Found Item Card
        val foundItemCard = view.findViewById<View>(R.id.foundItemCard)
        // Add click or other listeners to interact with this card

        return view
    }
}