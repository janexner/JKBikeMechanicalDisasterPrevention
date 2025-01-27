package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddComponentViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String> = _name
}