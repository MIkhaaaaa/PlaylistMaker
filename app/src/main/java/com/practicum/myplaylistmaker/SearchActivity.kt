package com.practicum.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils
import com.practicum.myplaylistmaker.databinding.ActivitySearchBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
const val HISTORY_KEY = "history_key"

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }
    private var trackList: ArrayList<Track> = ArrayList()
    private val KEY_TEXT = ""
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter
    private val trackHistoryObj = TrackHistory()
    private val progressBar: ProgressBar by lazy {findViewById(R.id.progressbar)}
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    private lateinit var searchBinding: ActivitySearchBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        trackAdapter = TrackAdapter(trackList){
            trackHistoryObj.editArray(it)
            trackAdapterHistory.notifyDataSetChanged()
            val intent = Intent(this, ActivityMediaPlayer::class.java)
            intent.putExtra("track",it)
            this.startActivity(intent)
        }

        ifSearchOkVisibility()

        trackHistoryObj.trackHistoryList.clear()
        trackHistoryObj.trackHistoryList.addAll(trackHistoryObj.read(App.getSharedPreferences()))
        visibleSettingsHistory(searchBinding.searchUserText.hasFocus())

        searchBinding.backArrow.setOnClickListener {
            if (clickDebounce()) {
                finish()
            }
        }

        trackAdapterHistory = TrackAdapter(trackHistoryObj.trackHistoryList){
            trackHistoryObj.editArray(it)
            trackAdapterHistory.notifyDataSetChanged()
            val intent = Intent(this, ActivityMediaPlayer::class.java)
            intent.putExtra("track",it)
            this.startActivity(intent)
        }

        searchBinding.clearIcon.setOnClickListener {
            if (clickDebounce()){
            searchBinding.searchUserText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(searchBinding.searchUserText.windowToken, 0) // скрыть клавиатуру
            searchBinding.searchUserText.clearFocus()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            }
        }



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                visibleSettingsHistory(searchBinding.searchUserText.hasFocus())
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                searchBinding.clearIcon.visibility = clearButtonVisibility(s)
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
                trackHistoryObj.trackHistoryList.clear()
                trackHistoryObj.clearAllHistory()
                trackAdapterHistory.notifyDataSetChanged()
                searchBinding.textHistory.visibility = GONE
                searchBinding.clearHistoryButton.visibility = GONE
                searchBinding.historyRecycler.visibility = GONE
            }

        }
        searchBinding.searchUserText.addTextChangedListener(simpleTextWatcher)
        searchBinding.trackRecycler.layoutManager = LinearLayoutManager(this)
        searchBinding.trackRecycler.adapter = trackAdapter

        searchBinding.historyRecycler.layoutManager = LinearLayoutManager(this)
        searchBinding.historyRecycler.adapter = trackAdapterHistory

        searchBinding.searchUserText.setOnFocusChangeListener { v, hasFocus ->
            if (clickDebounce()) {
                trackAdapterHistory.notifyDataSetChanged()
                visibleSettingsHistory(hasFocus)
            }
        }
    }


    private fun search() {
        trackList.clear()
        if (searchBinding.searchUserText.text.toString().isNotEmpty()) {
            searchBinding.trackRecycler.visibility = View.VISIBLE
        } else {
            searchBinding.trackRecycler.visibility = GONE
            searchBinding.noneFind.visibility = GONE
        }
        iTunesService.search(searchBinding.searchUserText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        progressBar.visibility = GONE
                        trackList.clear()
                        ifSearchOkVisibility()

                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        }

                        if (trackList.isEmpty() && searchBinding.searchUserText.text.toString().isNotEmpty()) {
                            searchBinding.noneFind.visibility = View.VISIBLE
                            searchBinding.nothingfoundText.visibility = View.VISIBLE
                            searchBinding.loadingproblem.visibility = GONE
                            searchBinding.loadingproblemText.visibility = GONE
                        }

                    } else {
                        searchBinding.noneFind.visibility = GONE
                        progressBar.visibility = GONE
                        searchBinding.nothingfoundText.visibility = GONE
                        searchBinding.loadingproblem.visibility = View.VISIBLE
                        searchBinding.loadingproblemText.visibility = View.VISIBLE
                        searchBinding.refreshButton.setOnClickListener { search() }
                        searchBinding.refreshButton.visibility = View.VISIBLE
                        searchBinding.trackRecycler.visibility = GONE
                        trackAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    progressBar.visibility = GONE
                    searchBinding.loadingproblem.visibility = View.VISIBLE
                    searchBinding.loadingproblemText.visibility = View.VISIBLE
                    searchBinding.refreshButton.visibility = View.VISIBLE
                    searchBinding.refreshButton.setOnClickListener { search() }
                }
            }
            )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT, searchBinding.searchUserText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_TEXT, "")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            View.VISIBLE
        }
    }

    private fun visibleSettingsHistory(hasFocus: Boolean) {
        searchBinding.textHistory.visibility =
            if (hasFocus && searchBinding.searchUserText.text.isEmpty() && trackHistoryObj.trackHistoryList.isNotEmpty()) View.VISIBLE else GONE
        searchBinding.clearHistoryButton.visibility =
            if (hasFocus && searchBinding.searchUserText.text.isEmpty() && trackHistoryObj.trackHistoryList.isNotEmpty()) View.VISIBLE else GONE
        searchBinding.historyRecycler.visibility =
            if (hasFocus && searchBinding.searchUserText.text.isEmpty() && trackHistoryObj.trackHistoryList.isNotEmpty()) View.VISIBLE else GONE
    }


    private fun ifSearchOkVisibility() {
        searchBinding.trackRecycler.visibility = View.VISIBLE
        searchBinding.nothingfoundText2.visibility = GONE
        searchBinding.noneFind.visibility = GONE
        searchBinding.nothingfoundText.visibility = GONE
        searchBinding.loadingproblem.visibility = GONE
        searchBinding.loadingproblemText.visibility = GONE
        searchBinding.refreshButton.visibility = GONE
        progressBar.visibility = GONE
    }
    private fun clickDebounce() : Boolean {
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