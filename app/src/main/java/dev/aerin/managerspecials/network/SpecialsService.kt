package dev.aerin.managerspecials.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.aerin.managerspecials.models.SpecialsPage
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SpecialsService : ViewModel() {

    private val errorPipe by lazy {
        PublishSubject.create<String>()
    }

    private val specialsData by lazy {
        SpecialsLiveData(errorPipe)
    }

    fun getErrorPipe(): Observable<String> {
        return errorPipe
    }

    fun getSpecialsData(): LiveData<SpecialsPage> {
        return specialsData
    }

    fun refresh() {
        specialsData.refresh()
    }
}
