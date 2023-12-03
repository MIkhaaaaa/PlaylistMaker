package com.practicum.myplaylistmaker.ui.search.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.FragmentActivitySearchBinding
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.player.ActivityMediaPlayer
import com.practicum.myplaylistmaker.ui.search.SearchViewModel
import com.practicum.myplaylistmaker.ui.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
        companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private val searchViewModule: SearchViewModel by viewModel()
    private var _binding: FragmentActivitySearchBinding? = null
    private val binding: FragmentActivitySearchBinding
        get() = _binding!!

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks() }
    private var trackList: ArrayList<Track> = ArrayList()
    private val KEY_TEXT = ""
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter
    private var trackHistoryList: ArrayList<Track> = ArrayList()
    private lateinit var bottomNavigator: BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        trackAdapter = TrackAdapter(trackList) {
            searchViewModule.addItem(it)
            val intent = Intent(requireContext(), ActivityMediaPlayer::class.java)
            intent.putExtra("track", it)
            this.startActivity(intent)
        }

        searchViewModule.getStateLiveData().observe(viewLifecycleOwner) { stateLiveData ->

            when (stateLiveData) {
                is SearchScreenState.DefaultSearch -> defaultSearch()
                is SearchScreenState.ConnectionError -> connectionError()
                is SearchScreenState.Loading -> loading()
                is SearchScreenState.NothingFound -> nothingFound()
                is SearchScreenState.SearchOk -> searchIsOk(stateLiveData.data)
                is SearchScreenState.SearchHistory -> searchWithHistory(stateLiveData.historyData)
                else -> {}
            }
        }

        ifSearchOkVisibility()
        binding.refreshButton.setOnClickListener { searchTracks() }

        trackHistoryList.clear()
        searchViewModule.provideHistory().value?.let { trackHistoryList.addAll(it) }
        visibleSettingsHistory(binding.searchUserText.hasFocus())

        trackAdapterHistory = TrackAdapter(trackHistoryList) {
            searchViewModule.addItem(it)
            val intent = Intent(requireContext(), ActivityMediaPlayer::class.java)
            intent.putExtra("track", it)
            this.startActivity(intent)
        }

        binding.clearIcon.setOnClickListener {
            if (clickDebounce()) {
                binding.searchUserText.setText("")
                val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(
                    binding.searchUserText.windowToken,
                    0
                ) // скрыть клавиатуру
                binding.searchUserText.clearFocus()
                searchViewModule.clearTrackList()
                trackAdapter.notifyDataSetChanged()
                bottomNavigator.isVisible = false
                binding.progressbar.isVisible = false
                historyInVisible()
            }
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                visibleSettingsHistory(binding.searchUserText.hasFocus())
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                binding.clearIcon.isVisible = clearButtonVisibility(s)
                if (binding.searchUserText.hasFocus() && binding.searchUserText.text.isEmpty()) {
                    trackAdapterHistory.notifyDataSetChanged()
                }
                visibleSettingsHistory(binding.searchUserText.hasFocus())
            }

            override fun afterTextChanged(s: Editable?) {
                visibleSettingsHistory(binding.searchUserText.hasFocus())
            }

        }

        binding.clearHistoryButton.setOnClickListener {
            if (clickDebounce()) {
                trackHistoryList.clear()
                searchViewModule.clearHistory()
                trackAdapterHistory.notifyDataSetChanged()
                binding.textHistory.isVisible = false
                binding.clearHistoryButton.isVisible = false
                binding.historyRecycler.isVisible = false
            }

        }
        binding.searchUserText.addTextChangedListener(simpleTextWatcher)
        binding.trackRecycler.layoutManager = LinearLayoutManager(context)
        binding.trackRecycler.adapter = trackAdapter

        binding.historyRecycler.layoutManager = LinearLayoutManager(context)
        binding.historyRecycler.adapter = trackAdapterHistory

        binding.searchUserText.setOnFocusChangeListener { _, hasFocus ->
            if (clickDebounce()) {
                trackAdapterHistory.notifyDataSetChanged()
                visibleSettingsHistory(hasFocus)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        trackHistoryList.clear()
        searchViewModule.provideHistory().value?.let { trackHistoryList.addAll(it) }
        trackAdapterHistory.notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun searchTracks() {
        searchViewModule.clearTrackList()
        handler.post {
            searchViewModule.searchRequesting(binding.searchUserText.text.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT, binding.searchUserText.text.toString())
    }


    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun visibleSettingsHistory(hasFocus: Boolean) {
        binding.textHistory.isVisible =
            hasFocus && binding.searchUserText.text.isEmpty() && trackHistoryList.isNotEmpty()
        binding.clearHistoryButton.isVisible =
            hasFocus && binding.searchUserText.text.isEmpty() && trackHistoryList.isNotEmpty()
        binding.historyRecycler.isVisible =
            hasFocus && binding.searchUserText.text.isEmpty() && trackHistoryList.isNotEmpty()
    }


    private fun ifSearchOkVisibility() {
        with(binding) {
            trackRecycler.isVisible = true
            nothingfoundText2.isVisible = false
            noneFind.isVisible = false
            nothingfoundText.isVisible = false
            loadingproblem.isVisible = false
            loadingproblemText.isVisible = false
            refreshButton.isVisible = false
            progressbar.isVisible = false
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        if (binding.searchUserText.text.toString().isNotEmpty()) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun defaultSearch() {
        historyInVisible()
        with(binding) {
            trackRecycler.isVisible = false
            noneFind.isVisible = false
            nothingfoundText.isVisible = false
            loadingproblem.isVisible = false
            loadingproblemText.isVisible = false
            refreshButton.isVisible = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        with(binding) {
            progressbar.isVisible = true
            historyInVisible()
            trackRecycler.isVisible = false
            noneFind.isVisible = false
            nothingfoundText.isVisible = false
            loadingproblem.isVisible = false
            loadingproblemText.isVisible = false
            refreshButton.isVisible = false
        }
        trackAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchIsOk(data: List<Track>) {
        trackList.clear()
        trackList.addAll(data)
        trackAdapter.notifyDataSetChanged()
        with(binding) {
            progressbar.isVisible = false
            historyRecycler.isVisible = false
            trackRecycler.isVisible = true
            noneFind.isVisible = false
            nothingfoundText.isVisible = false
            loadingproblem.isVisible = false
            loadingproblemText.isVisible = false
            refreshButton.isVisible = false
            clearHistoryButton.isVisible = false
        }

        historyInVisible()
    }

    private fun nothingFound() {
        with(binding) {
            textHistory.isVisible = false
            historyRecycler.isVisible = false
            clearHistoryButton.isVisible = true
            trackRecycler.isVisible = false
            noneFind.isVisible = true
            nothingfoundText.isVisible = true
            loadingproblem.isVisible = false
            loadingproblemText.isVisible = false
            refreshButton.isVisible = false
            progressbar.isVisible = false
        }
        historyInVisible()
    }

    private fun connectionError() {
        with(binding) {
            loadingproblem.isVisible = true
            loadingproblemText.isVisible = true
            refreshButton.isVisible = true
            trackRecycler.isVisible = false
            refreshButton.setOnClickListener { searchTracks() }
            progressbar.isVisible = false
        }
        historyInVisible()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchWithHistory(historyData: List<Track>) {
        trackHistoryList.clear()
        trackHistoryList.addAll(historyData)
        trackAdapterHistory.notifyDataSetChanged()
        with(binding) {
            trackRecycler.isVisible = false
            textHistory.isVisible = true
            historyRecycler.isVisible = true
            clearHistoryButton.isVisible = true
            trackRecycler.isVisible = false
            noneFind.isVisible = false
            nothingfoundText.isVisible = false
            refreshButton.isVisible = false
            loadingproblem.isVisible = false
            loadingproblemText.isVisible = false
            progressbar.isVisible = false
        }

    }

    private fun historyInVisible() {
        with(binding) {
            textHistory.isVisible = false
            historyRecycler.isVisible = false
            clearHistoryButton.isVisible = false
        }
    }

}