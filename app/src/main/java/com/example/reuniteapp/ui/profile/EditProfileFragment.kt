package com.example.reuniteapp.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.UserProfileDao
import com.example.reuniteapp.databinding.FragmentEditProfileBinding
import com.example.reuniteapp.models.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userProfileDao: UserProfileDao
    private var currentUserId: Int = -1
    private var profilePicBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize userProfileDao
        val database = AppDatabase.getDatabase(requireContext())
        userProfileDao = database.userProfileDao()

        // Load user profile data
        loadUserProfile()

        // Set click listeners
        binding.saveButton.setOnClickListener {
            saveUserProfile()
        }

        binding.cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.editProfileImageButton.setOnClickListener {
            // Launch image picker to select profile picture
            pickProfileImage.launch("image/*")
        }
    }

    private fun loadUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            currentUserId = getUserIdFromSharedPreferences()
            if (currentUserId != -1) {
                try {
                    val userProfile = userProfileDao.getUserProfileById(currentUserId)
                    if (userProfile != null) {
                        updateUI(userProfile)
                        Log.d(TAG, "User profile loaded successfully")
                    } else {
                        Log.e(TAG, "User profile not found for ID: $currentUserId")
                        Toast.makeText(requireContext(), "User profile not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error retrieving user profile", e)
                    Toast.makeText(requireContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e(TAG, "User ID not found in SharedPreferences")
                Toast.makeText(requireContext(), "Please log in to view your profile", Toast.LENGTH_LONG).show()
                // TODO: Navigate to login screen if not logged in
            }
        }
    }

    private fun updateUI(userProfile: UserProfile) {
        binding.nameEditText.setText(userProfile.name)
        binding.emailEditText.setText(userProfile.email)
        binding.contactEditText.setText(userProfile.contactNumber)

        // Load profile image if available
        loadProfileImage(userProfile.profileImageUri)
    }

    private fun saveUserProfile() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val contactNumber = binding.contactEditText.text.toString()
        val userId = getUserIdFromSharedPreferences()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (userId != -1) {
                    // Load current user profile
                    val userProfile = userProfileDao.getUserProfileById(userId)
                    if (userProfile != null) {
                        // Update the fields
                        userProfile.name = name
                        userProfile.email = email
                        userProfile.contactNumber = contactNumber

                        // Save updated profile
                        userProfileDao.updateUserProfile(userProfile)
                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        // TODO: Navigate back to profile fragment or handle navigation as needed
                    } else {
                        Log.e(TAG, "User profile not found for ID: $userId")
                        Toast.makeText(requireContext(), "User profile not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "User ID not found in SharedPreferences")
                    Toast.makeText(requireContext(), "Failed to update user profile: User ID not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user profile", e)
                Toast.makeText(requireContext(), "Failed to update user profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("USER_ID", -1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadProfileImage(imageUri: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (imageUri.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    val imageBitmap = decodeImageFromFile(imageUri)
                    profilePicBitmap = imageBitmap
                }
                binding.profileImageView.setImageBitmap(profilePicBitmap)
            } else {
                binding.profileImageView.setImageResource(R.drawable.default_profile_image)
            }
        }
    }

    private fun decodeImageFromFile(imageUri: String): Bitmap? {
        return try {
            val imageStream = requireContext().contentResolver.openInputStream(android.net.Uri.parse(imageUri))
            BitmapFactory.decodeStream(imageStream)
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding image from file", e)
            null
        }
    }

    companion object {
        private const val TAG = "EditProfileFragment"
    }

    // ActivityResultLauncher for image picker
    private val pickProfileImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the selected image URI here
        uri?.let {
            // Update profilePicUri with the selected image URI
            val imageUriString = it.toString()
            profilePicBitmap = decodeImageFromFile(imageUriString)
            binding.profileImageView.setImageBitmap(profilePicBitmap)
        }
    }
}