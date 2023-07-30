package com.practicum.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
const val HISTORY_KEY = "history_key"

class SearchActivity : AppCompatActivity() {
    private lateinit var trackList: ArrayList<Track>
    private val KEY_TEXT = ""
    private lateinit var rView: RecyclerView
    private lateinit var historyRecycler: RecyclerView
    private lateinit var drawableNFind: ImageView
    private lateinit var darawableNFindTxt: TextView
    private lateinit var refresh: Button
    private lateinit var loadingproblemText: TextView
    private lateinit var loadingproblemText2: TextView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter
    private lateinit var loadingproblem: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var arrowBack: ImageView
    private lateinit var clearHistoryButton: Button
    private lateinit var clearHistoryText: TextView
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var trackHistoryObj:TrackHistory
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesAPI::class.java)


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        trackHistoryObj = TrackHistory()
        sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        inputEditText = findViewById(R.id.searchUserText)
        clearButton = findViewById(R.id.clearIcon)
        arrowBack = findViewById(R.id.back_arrow)

        drawableNFind = findViewById(R.id.noneFind)
        darawableNFindTxt = findViewById(R.id.nothingfoundText)

        loadingproblemText = findViewById(R.id.loadingproblemText)
        loadingproblemText2 = findViewById(R.id.nothingfoundText_2)
        loadingproblem = findViewById(R.id.loadingproblem)

        clearHistoryButton = findViewById(R.id.clear_history_button)
        clearHistoryText = findViewById(R.id.text_history)

        refresh = findViewById(R.id.refreshButton)
        rView = findViewById(R.id.trackRecycler)
        historyRecycler = findViewById(R.id.historyRecycler)
        trackList = ArrayList()

        trackAdapter = TrackAdapter(trackList){
            trackHistoryObj.editArray(it)
            trackAdapterHistory.notifyDataSetChanged()
        }


        ifSearchOkVisibility()

        trackHistoryObj.trackHistoryList.clear()
        trackHistoryObj.trackHistoryList.addAll(trackHistoryObj.read(App.getSharedPreferences()))
        visibleSettingsHistory(inputEditText.hasFocus())

        arrowBack.setOnClickListener {
            finish()
        }

        trackAdapterHistory = TrackAdapter(trackHistoryObj.trackHistoryList){
            trackHistoryObj.editArray(it)
            trackAdapterHistory.notifyDataSetChanged()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(inputEditText.windowToken, 0) // скрыть клавиатуру
            inputEditText.clearFocus()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
        }



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
                visibleSettingsHistory(inputEditText.hasFocus())
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) {
                    trackAdapterHistory.notifyDataSetChanged()
                }
                visibleSettingsHistory(inputEditText.hasFocus())
            }

            override fun afterTextChanged(s: Editable?) {
                visibleSettingsHistory(inputEditText.hasFocus())
            }

        }

        clearHistoryButton.setOnClickListener {
            trackHistoryObj.trackHistoryList.clear()
            trackHistoryObj.clearAllHistory()
            trackAdapterHistory.notifyDataSetChanged()
            clearHistoryText.visibility = GONE
            clearHistoryButton.visibility = GONE
            historyRecycler.visibility = GONE

        }


        inputEditText.addTextChangedListener(simpleTextWatcher)


        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = trackAdapter




        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = trackAdapterHistory

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    search(inputEditText)
                }
            }
            false
        }
        inputEditText.setOnFocusChangeListener { v, hasFocus ->
            trackAdapterHistory.notifyDataSetChanged()
            visibleSettingsHistory(hasFocus)
        }
    }

    private fun search(inputEditText: EditText) {
        trackList.clear()
        rView.visibility = View.VISIBLE
        iTunesService.search(inputEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        ifSearchOkVisibility()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)

//                            if (trackList.size in 1..4) {
//                                for (i in 0 until 5) {
//                                    trackHistoryObj.editArray(trackList[i])
//                                }
//                            } else {
//                                val tracks = trackList.take(5)
//                                for (track in tracks) {
//                                    trackHistoryObj.editArray(track)
//                                }
//                            }
//                            trackAdapterHistory.notifyDataSetChanged()
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            drawableNFind.visibility = View.VISIBLE
                            darawableNFindTxt.visibility = View.VISIBLE
                            loadingproblem.visibility = GONE
                            loadingproblemText.visibility = GONE
                        }

                    } else {
                        drawableNFind.visibility = GONE
                        darawableNFindTxt.visibility = GONE
                        loadingproblem.visibility = View.VISIBLE
                        loadingproblemText.visibility = View.VISIBLE
                        refresh.setOnClickListener { search(inputEditText) }
                        refresh.visibility = View.VISIBLE
                        rView.visibility = GONE
                        trackAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    loadingproblem.visibility = View.VISIBLE
                    loadingproblemText.visibility = View.VISIBLE
                    refresh.visibility = View.VISIBLE
                    refresh.setOnClickListener { search(inputEditText) }
                }
            }
            )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT, inputEditText.text.toString())
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
        clearHistoryText.visibility =
            if (hasFocus && inputEditText.text.isEmpty() && trackHistoryObj.trackHistoryList.isNotEmpty()) View.VISIBLE else GONE
        clearHistoryButton.visibility =
            if (hasFocus && inputEditText.text.isEmpty() && trackHistoryObj.trackHistoryList.isNotEmpty()) View.VISIBLE else GONE
        historyRecycler.visibility =
            if (hasFocus && inputEditText.text.isEmpty() && trackHistoryObj.trackHistoryList.isNotEmpty()) View.VISIBLE else GONE
    }


    private fun ifSearchOkVisibility() {
        rView.visibility = View.VISIBLE
        loadingproblemText2.visibility = GONE
        drawableNFind.visibility = GONE
        darawableNFindTxt.visibility = GONE
        loadingproblem.visibility = GONE
        loadingproblemText.visibility = GONE
        refresh.visibility = GONE
    }
}