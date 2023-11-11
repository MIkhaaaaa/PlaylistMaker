package com.practicum.myplaylistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.ActivitySearchBinding
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.player.ActivityMediaPlayer
import com.practicum.myplaylistmaker.ui.search.adapter.TrackAdapter
import com.practicum.myplaylistmaker.ui.search.liveData.ScreenState

const val HISTORY_KEY = "history_key"

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks() }
    private var trackList: ArrayList<Track> = ArrayList()
    private val KEY_TEXT = ""
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter
    private var trackHistoryList: ArrayList<Track> = ArrayList()
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressbar) }
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModule: SearchViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchViewModule = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        trackAdapter = TrackAdapter(trackList) {
            searchViewModule.addItem(it)
            val intent = Intent(this, ActivityMediaPlayer::class.java)
            intent.putExtra("track", it)
            this.startActivity(intent)
        }

        searchViewModule.getStateLiveData().observe(this) { stateLiveData ->

            when (stateLiveData) {
                is ScreenState.DefaultSearch -> defaultSearch()
                is ScreenState.ConnectionError -> connectionError()
                is ScreenState.Loading -> loading()
                is ScreenState.NothingFound -> nothingFound()
                is ScreenState.SearchOk -> searchIsOk(stateLiveData.data)
                is ScreenState.SearchHistory -> searchWithHistory(stateLiveData.historyData)
                else -> {}
            }
        }

        ifSearchOkVisibility()
        binding.refreshButton.setOnClickListener { searchTracks() }

        trackHistoryList.clear()
        searchViewModule.provideHistory().value?.let { trackHistoryList.addAll(it) }
        visibleSettingsHistory(binding.searchUserText.hasFocus())

        binding.backArrow.setOnClickListener {
            if (clickDebounce()) {
                finish()
            }
        }

        trackAdapterHistory = TrackAdapter(trackHistoryList) {
            searchViewModule.addItem(it)
            val intent = Intent(this, ActivityMediaPlayer::class.java)
            intent.putExtra("track", it)
            this.startActivity(intent)
        }

        binding.clearIcon.setOnClickListener {
            if (clickDebounce()) {
                binding.searchUserText.setText("")
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(
                    binding.searchUserText.windowToken,
                    0
                ) // скрыть клавиатуру
                binding.searchUserText.clearFocus()
                searchViewModule.clearTrackList()
                trackAdapter.notifyDataSetChanged()
                progressBar.isVisible = false
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
        binding.trackRecycler.layoutManager = LinearLayoutManager(this)
        binding.trackRecycler.adapter = trackAdapter

        binding.historyRecycler.layoutManager = LinearLayoutManager(this)
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
        searchViewModule.provideHistory().value?.let { trackHistoryList.addAll(it)}
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_TEXT, "")
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
        binding.trackRecycler.isVisible = true
        binding.nothingfoundText2.isVisible = false
        binding.noneFind.isVisible = false
        binding.nothingfoundText.isVisible = false
        binding.loadingproblem.isVisible = false
        binding.loadingproblemText.isVisible = false
        binding.refreshButton.isVisible = false
        progressBar.isVisible = false
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
        binding.trackRecycler.isVisible = false
        binding.noneFind.isVisible = false
        binding.nothingfoundText.isVisible = false
        binding.loadingproblem.isVisible = false
        binding.loadingproblemText.isVisible = false
        binding.refreshButton.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        binding.progressbar.isVisible = true
        historyInVisible()
        binding.trackRecycler.isVisible = false
        binding.noneFind.isVisible = false
        binding.nothingfoundText.isVisible = false
        binding.loadingproblem.isVisible = false
        binding.loadingproblemText.isVisible = false
        binding.refreshButton.isVisible = false
        trackAdapter.notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchIsOk(data: List<Track>) {
        trackList.clear()
        trackList.addAll(data)
        trackAdapter.notifyDataSetChanged()
        binding.progressbar.isVisible = false
        binding.historyRecycler.isVisible = false
        binding.trackRecycler.isVisible = true
        binding.noneFind.isVisible = false
        binding.nothingfoundText.isVisible = false
        binding.loadingproblem.isVisible = false
        binding.loadingproblemText.isVisible = false
        binding.refreshButton.isVisible = false
        binding.clearHistoryButton.isVisible = false
        historyInVisible()
    }

    private fun nothingFound() {
        binding.textHistory.isVisible = false
        binding.historyRecycler.isVisible = false
        binding.clearHistoryButton.isVisible = true
        binding.trackRecycler.isVisible = false
        binding.noneFind.isVisible = true
        binding.nothingfoundText.isVisible = true
        binding.loadingproblem.isVisible = false
        binding.loadingproblemText.isVisible = false
        binding.refreshButton.isVisible = false
        historyInVisible()
    }

    private fun connectionError() {
        binding.loadingproblem.isVisible = true
        binding.loadingproblemText.isVisible = true
        binding.refreshButton.isVisible = true
        binding.trackRecycler.isVisible = false
        binding.refreshButton.setOnClickListener { searchTracks() }
        binding.progressbar.isVisible = false
        historyInVisible()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchWithHistory(historyData: List<Track>) {
        trackHistoryList.clear()
        trackHistoryList.addAll(historyData)
        trackAdapterHistory.notifyDataSetChanged()
        binding.trackRecycler.isVisible = false
        binding.textHistory.isVisible = true
        binding.historyRecycler.isVisible = true
        binding.clearHistoryButton.isVisible = true
        binding.trackRecycler.isVisible = false
        binding.noneFind.isVisible = false
        binding.nothingfoundText.isVisible = false
        binding.refreshButton.isVisible = false
        binding.loadingproblem.isVisible = false
        binding.loadingproblemText.isVisible = false
        binding.progressbar.isVisible = false
    }

    private fun historyInVisible() {
        binding.textHistory.isVisible = false
        binding.historyRecycler.isVisible = false
        binding.clearHistoryButton.isVisible = false
    }
}