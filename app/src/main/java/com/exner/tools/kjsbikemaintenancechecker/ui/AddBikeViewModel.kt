package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddBikeViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String> = _name

    fun updateName(newName: String) {
        _name.value = newName
    }

    private val _buildDate: MutableLiveData<LocalDateTime> = MutableLiveData()
    val buildDate: LiveData<LocalDateTime> = _buildDate

    fun updateBuildDate(newBuildDate: LocalDateTime) {
        _buildDate.value = newBuildDate
    }

    private val _mileage: MutableLiveData<Int> = MutableLiveData()
    val mileage: LiveData<Int> = _mileage

    fun updateMileage(newMileage: Int) {
        _mileage.value = newMileage
    }

    private val _lastUsedDate: MutableLiveData<LocalDateTime> = MutableLiveData()
    val lastUsedDate: LiveData<LocalDateTime> = _lastUsedDate

    fun updateLastUsedDate(newLastUsedDate: LocalDateTime) {
        _lastUsedDate.value = newLastUsedDate
    }
}