package dev.aerin.managerspecials.network

import androidx.lifecycle.MutableLiveData
import dev.aerin.managerspecials.models.SpecialsPage
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class SpecialsLiveData(private val errorPipe: PublishSubject<String>) : MutableLiveData<SpecialsPage>() {

    private val api by lazy {
        SpecialsApi.create()
    }
    private var request: Disposable? = null


    override fun onActive() { // Observers went from 0 to 1 - some activity needs us
        super.onActive()
        refresh()
    }

    fun refresh() {
        request?.dispose()
        request = api.getSpecials()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {specials -> postValue(specials)},
                {error -> errorPipe.onNext(error.message ?: "")}
            )
        /*
        Typically in a production app I'd return some kind of bundled error code and user-friendly message with info on
        how they might resolve their issue locally, like turning on data if the problem is we aren't connected. However,
        for the sake of this coding test, I'm just going to return the exception message instead, showing off the error
        handling mechanism without needing to build a whole suite of exception checking and pretty error messages in
        localizable formats, which seems kind of out-of-scope.
         */
    }

    override fun onInactive() { // Observers went from 1 to 0 - all activities are gone or dormant
        super.onInactive()
        request?.dispose()
    }
}
