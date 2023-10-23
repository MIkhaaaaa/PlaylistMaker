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
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.myplaylistmaker.util.Creator
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.ActivitySearchBinding
import com.practicum.myplaylistmaker.domain.api.TracksInteractor
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.ui.search.adapter.TrackAdapter
import com.practicum.myplaylistmaker.ui.player.ActivityMediaPlayer

const val HISTORY_KEY = "history_key"

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTwo() }
    private var trackList: ArrayList<Track> = ArrayList()
    private val KEY_TEXT = ""
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter
    private var trackHistoryList: ArrayList<Track> = ArrayList()
    private val historyCreator = Creator.provideSharedPreferenceInteractor()
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressbar) }
    private lateinit var searchBinding: ActivitySearchBinding
    private val creator = Creator.provideTracksIteractor(this)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        trackAdapter = TrackAdapter(trackList) {
            trackHistoryList.clear()
            trackHistoryList.addAll(historyCreator.editArray(it))
            trackAdapterHistory.notifyDataSetChanged()
            val intent = Intent(this, ActivityMediaPlayer::class.java)
            intent.putExtra("track", it)
            this.startActivity(intent)
        }

        ifSearchOkVisibility()
        searchBinding.refreshButton.setOnClickListener { searchTwo() }

        trackHistoryList.clear()
        trackHistoryList.addAll(historyCreator.read(historyCreator.getSharedPreferences()))
        visibleSettingsHistory(searchBinding.searchUserText.hasFocus())

        searchBinding.backArrow.setOnClickListener {
            if (clickDebounce()) {
                finish()
            }
        }

        trackAdapterHistory = TrackAdapter(trackHistoryList) {
            trackHistoryList.clear()
            trackHistoryList.addAll(historyCreator.editArray(it))
            trackAdapterHistory.notifyDataSetChanged()
            val intent = Intent(this, ActivityMediaPlayer::class.java)
            intent.putExtra("track", it)
            this.startActivity(intent)
        }

        searchBinding.clearIcon.setOnClickListener {
            if (clickDebounce()) {
                searchBinding.searchUserText.setText("")
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(
                    searchBinding.searchUserText.windowToken,
                    0
                ) // скрыть клавиатуру
                searchBinding.searchUserText.clearFocus()
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
                progressBar.isVisible = false
            }
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                visibleSettingsHistory(searchBinding.searchUserText.hasFocus())
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                searchBinding.clearIcon.isVisible = clearButtonVisibility(s)
                if (searchBinding.searchUserText.hasFocus() && searchBinding.searchUserText.text.isEmpty()) {
                    trackAdapterHistory.notifyDataSetChanged()
                }
                visibleSettingsHistory(searchBinding.searchUserText.hasFocus())
            }

            override fun afterTextChanged(s: Editable?) {
                visibleSettingsHistory(searchBinding.searchUserText.hasFocus())
            }

        }

        searchBinding.clearHistoryButton.setOnClickListener {
            if (clickDebounce()) {
                trackHistoryList.clear()
                historyCreator.clearAllHistory()
                trackAdapterHistory.notifyDataSetChanged()
                searchBinding.textHistory.isVisible = false
                searchBinding.clearHistoryButton.isVisible = false
                searchBinding.historyRecycler.isVisible = false
            }

        }
        searchBinding.searchUserText.addTextChangedListener(simpleTextWatcher)
        searchBinding.trackRecycler.layoutManager = LinearLayoutManager(this)
        searchBinding.trackRecycler.adapter = trackAdapter

        searchBinding.historyRecycler.layoutManager = LinearLayoutManager(this)
        searchBinding.historyRecycler.adapter = trackAdapterHistory

        searchBinding.searchUserText.setOnFocusChangeListener { _, hasFocus ->
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
        trackHistoryList.addAll(historyCreator.read(historyCreator.getSharedPreferences()))
        trackAdapterHistory.notifyDataSetChanged()
    }



    private fun searchTwo() {
        progressBar.isVisible = true
        creator.searchTracks(
            searchBinding.searchUserText.text.toString(),
            object : TracksInteractor.TracksConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(foundTrack: ArrayList<Track>?, errorMessage: String?) {
                    handler.post {

                        if (searchBinding.searchUserText.text.toString().isNotEmpty()) {
                            searchBinding.trackRecycler.isVisible = true
                        } else {
                            searchBinding.trackRecycler.isVisible = false
                            searchBinding.noneFind.isVisible = false
                        }
                        searchBinding.progressbar.isVisible = false
                        trackList.clear()
                        if (foundTrack != null) {
                            trackList.addAll(foundTrack)
                            ifSearchOkVisibility()
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (errorMessage != null){
                            progressBar.isVisible = false
                            searchBinding.loadingproblem.isVisible = true
                            searchBinding.loadingproblemText.isVisible = true
                            searchBinding.refreshButton.isVisible = true
                        }
                        if (trackList.isEmpty()) {
                            searchBinding.noneFind.isVisible = false
                            progressBar.isVisible = false
                            searchBinding.nothingfoundText.isVisible = false
                            searchBinding.loadingproblem.isVisible = true
                            searchBinding.loadingproblemText.isVisible = true

                            searchBinding.refreshButton.isVisible = true
                            searchBinding.trackRecycler.isVisible = false
                            trackAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT, searchBinding.searchUserText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_TEXT, "")
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun visibleSettingsHistory(hasFocus: Boolean) {
        searchBinding.textHistory.isVisible =
            hasFocus && searchBinding.searchUserText.text.isEmpty() && trackHistoryList.isNotEmpty()
        searchBinding.clearHistoryButton.isVisible =
            hasFocus && searchBinding.searchUserText.text.isEmpty() && trackHistoryList.isNotEmpty()
        searchBinding.historyRecycler.isVisible =
            hasFocus && searchBinding.searchUserText.text.isEmpty() && trackHistoryList.isNotEmpty()
    }


    private fun ifSearchOkVisibility() {
        searchBinding.trackRecycler.isVisible = true
        searchBinding.nothingfoundText2.isVisible = false
        searchBinding.noneFind.isVisible = false
        searchBinding.nothingfoundText.isVisible = false
        searchBinding.loadingproblem.isVisible = false
        searchBinding.loadingproblemText.isVisible = false
        searchBinding.refreshButton.isVisible = false
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
        if (searchBinding.searchUserText.text.toString().isNotEmpty()) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }
}