package com.example.cs_topics_project_test.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.cs_topics_project_test.databinding.FragmentUserSettingsBinding
import com.example.cs_topics_project_test.login.SignInActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.ByteArrayOutputStream

class UserSettings : Fragment() {

    private lateinit var binding: FragmentUserSettingsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var imageLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    // mode is whether fragment is being used for sign up or regular settings
    private var mode: String? = null

    companion object {
        private const val ARG_MODE = "mode"
        fun newInstance(mode: String): UserSettings {
            val fragment = UserSettings()
            val bundle = Bundle()
            bundle.putString(ARG_MODE, mode)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // reads "mode" argument (either "signup" or "settings")
        mode = arguments?.getString(ARG_MODE)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // let user upload pfp image
        imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // if user successfully picks img, get image uri, display on pfp
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data!!.data
                imageUri?.let { uri ->
                    binding.profilePicture.setImageURI(uri)
                    // convert img to base64 and save to fb
                    uploadpfp(uri)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentUserSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetpwText.visibility = View.INVISIBLE
        binding.currentPw.visibility = View.INVISIBLE
        binding.newPw.visibility = View.INVISIBLE

        // text that shows user's name/username
        val prevName = binding.textView10
        val prevUsername = binding.usernameDisplay
        val pfp = binding.profilePicture
        val user = firebaseAuth.currentUser

        // set text or default sets name and username to none
        prevName.text = user?.displayName ?: "No Name"
        prevUsername.text = "No username"

        if (mode == "settings" && user != null) {
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                prevUsername.text = document.getString("username") ?: "No username"
                // show pfp if user alr had one
            }
            firestore.collection("users").document(user.uid).collection("profilePicture").document("pfpImg").get()
                .addOnSuccessListener { document ->
                    document.getString("pfp")?.let {
                        val decoded = Base64.decode(it, Base64.DEFAULT)
                        pfp.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.size))
                    }
                }
            // if the user page from settings, show reset pw stuff (but not if accessing from signup)
            binding.resetpwText.visibility = View.VISIBLE
            binding.currentPw.visibility = View.VISIBLE
            binding.newPw.visibility = View.VISIBLE
        }

        // when upload pfp button clicked, open gallery to select pic (dont click save button to upload)
        binding.uploadpfp.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imageLauncher.launch(intent)
        }

        // when save button clicked
        binding.submitUserSettings.setOnClickListener {
            // name is the new inputted name, trim to get rid of leading/trailing whitespace
            val name = binding.newName.text.toString().trim()
            val username = binding.username.text.toString().trim()

            if (user != null) {
                var nameUpdated = false
                var usernameUpdated = false

                // if coming from sign in page, must have name and username filled out
                fun checkIfDone() {
                    if (mode == "signup" && nameUpdated && usernameUpdated) {
                        startActivity(Intent(requireContext(), SignInActivity::class.java))
                        requireActivity().finish()
                    }
                }

                // if user changed name, change saved name in firestore
                if (name.isNotEmpty() && name != user.displayName) {
                    val profileUpdates = userProfileChangeRequest { displayName = name }
                    user.updateProfile(profileUpdates).addOnSuccessListener {
                        // add name to firestore
                        val data = mapOf("name" to name)
                        firestore.collection("users").document(user.uid).set(data, SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Name updated", Toast.LENGTH_SHORT).show()
                                // variable name kind of confusing here but updates the name shown on screen
                                prevName.text = name
                                nameUpdated = true
                                checkIfDone()
                        }
                    }
                }

                // same thing for username
                if (username.isNotEmpty()) {
                    // get current username from firestore (check if unchanged)
                    firestore.collection("users").document(user.uid).get()
                        .addOnSuccessListener { doc ->
                            // if username null assign "" to it temporarily
                            val currentUsername = doc.getString("username") ?: ""
                            if (username != currentUsername) {
                                // check if username unique
                                firestore.collection("users").whereEqualTo("username", username).get()
                                    .addOnSuccessListener { result ->
                                        if (result.isEmpty) {
                                            // add to firestore
                                            val data = mapOf("username" to username)
                                            firestore.collection("users").document(user.uid).set(data, SetOptions.merge())
                                                .addOnSuccessListener {
                                                    Toast.makeText(requireContext(),"Username updated",Toast.LENGTH_SHORT).show()
                                                prevUsername.text = username
                                                usernameUpdated = true
                                                checkIfDone()
                                            }
                                        } else {
                                            Toast.makeText(requireContext(),"Username already taken",Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        }
                }
            }

            // reset pw stuff
            val currentpw = binding.currentPw.text.toString()
            val newpw = binding.newPw.text.toString()
            val email = user?.email
            if (currentpw.isNotEmpty() && newpw.isNotEmpty()) {
                if (email!=null) {
                    val cred = EmailAuthProvider.getCredential(email, currentpw)
                    user.reauthenticate(cred)
                        .addOnSuccessListener {
                        // if current pw is correct (able to reauthenticate), update pw to be new one
                        user.updatePassword(newpw).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Password updated", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Password failed to update", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Current password incorrect", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    // cant use firebase storage for images (bc costs money) so upload image as base64 string in firestore (limit uploads to 1mb)
    private fun uploadpfp(uri: Uri) {
        val stream = requireContext().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(stream)
        val output = ByteArrayOutputStream()
        // compress to jpeg w 80% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
        val base64img = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)

        val user = firebaseAuth.currentUser ?: return // idk if this is needed again but just in case
        val userData = mapOf("pfp" to base64img)

        // save to subcollection bc its a really long string and annoying to scroll through :/
        firestore.collection("users").document(user.uid).collection("profilePicture").document("pfpImg").set(userData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Image uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }
}
