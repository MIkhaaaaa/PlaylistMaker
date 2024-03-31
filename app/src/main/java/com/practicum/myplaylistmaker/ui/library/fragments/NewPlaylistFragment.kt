package com.practicum.myplaylistmaker.ui.library.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.NewPlaylistBinding
import com.practicum.myplaylistmaker.ui.library.viewModels.NewPlaylistViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {


    private var _newPlaylistBinding: NewPlaylistBinding? = null
    private val newPlaylistBinding: NewPlaylistBinding
        get() = _newPlaylistBinding!!

    private lateinit var bottomNavigator: BottomNavigationView
    var isFileLoaded = false
    private val viewModel: NewPlaylistViewModel by viewModel()
    private var selectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _newPlaylistBinding = NewPlaylistBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.isVisible = false

        newPlaylistBinding.createButton.setOnClickListener {
            if (newPlaylistBinding.playlistNameEditText.text.toString()
                    .isEmpty()
            ) return@setOnClickListener
            createPlaylist()
            val textColor: Int
            val isDarkTheme = viewModel.isAppThemeDark()
            if (isDarkTheme) {
                textColor = Color.BLACK
            } else {
                textColor = Color.WHITE
            }
            val dialogPlaylistName = newPlaylistBinding.playlistNameEditText.text
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setMessage("Плейлист $dialogPlaylistName создан")
                .setNegativeButton("Оk") { dialog, which ->
                    closer()
                }
                .show()
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
        }
        return newPlaylistBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rxPermissions = RxPermissions(this)

        newPlaylistBinding.playlistBackButtonArrow.setOnClickListener {
            onBackClick()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                turnOffCreateButton()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                turnOnCreateButton()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    turnOffCreateButton()
                } else {
                    turnOnCreateButton()
                }
            }
        }
        newPlaylistBinding.playlistNameEditText.addTextChangedListener(simpleTextWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val radius = 8
                    val width = 312
                    val height = 312
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.add_photo)
                        .transform(CenterCrop(), RoundedCorners(radius))
                        .override(width, height)
                        .into(newPlaylistBinding.playlistCover)
                    saveImageToPrivateStorage(uri)

                }
            }

        newPlaylistBinding.playlistCover.setOnClickListener {
            rxPermissions.request(android.Manifest.permission.READ_MEDIA_IMAGES)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackClick()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileCount = filePath.listFiles()?.size ?: 0
        val file = File(filePath, "first_cover_${fileCount + 1}.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        newPlaylistBinding.playlistPlaceHolder.isVisible = false
        isFileLoaded = true
        selectedUri = file.toUri()
    }

    private fun onBackClick() {
        val name = newPlaylistBinding.playlistNameEditText.text
        val descr = newPlaylistBinding.playlistDescriptEditText.text

        if (isFileLoaded || !(name.isNullOrEmpty()) || (!descr.isNullOrEmpty())) {
            val textColor: Int
            val isDarkTheme = viewModel.isAppThemeDark()
            if (isDarkTheme) {
                textColor = Color.BLACK
            } else {
                textColor = Color.WHITE
            }
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { dialog, which ->
                    return@setNegativeButton
                }
                .setPositiveButton("Завершить") { dialog, which ->
                    closer()
                }
                .show()
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
        } else {
            closer()
        }
    }

    private fun closer() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun turnOffCreateButton() {
        newPlaylistBinding.createButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.settingsIconGray))
        newPlaylistBinding.createButton.isEnabled = false
    }

    private fun turnOnCreateButton() {
        newPlaylistBinding.createButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.back1))
        newPlaylistBinding.createButton.isEnabled = true
    }


    private fun createPlaylist() {
        viewModel.addPlayList(
            newPlaylistBinding.playlistNameEditText.text.toString(),
            newPlaylistBinding.playlistDescriptEditText.text.toString(),
            selectedUri.toString(),
        )
    }

    override fun onDestroy() {
        _newPlaylistBinding = null
        super.onDestroy()
    }
}