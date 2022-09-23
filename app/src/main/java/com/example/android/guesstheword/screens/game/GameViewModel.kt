package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 10000L
        private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
        private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
        private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
        private val NO_BUZZ_PATTERN = longArrayOf(0)
    }

    enum class BuzzType(val pattern: LongArray){
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

     private val timer : CountDownTimer

     private val _currentTime = MutableLiveData<Long>()
     private val currentTime : LiveData<Long>
     get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private var _word = MutableLiveData<String>()
     val word : LiveData<String>
     get() = _word

    // The current score
     private var _score = MutableLiveData<Int>()
     val score : LiveData<Int>
     get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
    get() = _eventGameFinish

    init {
        Log.i("GameViewModel","GameViewModel created!")
        _eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0
        _word.value = ""

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(p0: Long) {
                _currentTime.value = p0
            }

            override fun onFinish() {
               onGameFinishComplete()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel","GameViewModel destroyed")
    }

    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            _eventGameFinish.value = true
        }
            _word.value = wordList.removeAt(0)
    }

     fun onSkip() {
             _score.value = (score.value)?.minus(1)
             nextWord()
    }

     fun onCorrect() {
             _score.value = (score.value)?.plus(1)
             nextWord()
    }

    fun onGameFinishComplete(){
        _eventGameFinish.value = true
    }

}