package com.example.gratefulnote.addgratitudefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao

class AddGratetitudeViewModel(private val dataSource : PositiveEmotionDatabaseDao) : ViewModel() {
    // TODO: Implement the ViewModel
    private val _navigateToMainFragment = MutableLiveData<Boolean>(false)
    val navigateToMainFragment : LiveData<Boolean>
        get() = _navigateToMainFragment

    fun onClickSubmit(){
        _navigateToMainFragment.value = true
    }

    fun doneNavigating(){
        _navigateToMainFragment.value = false
    }
}