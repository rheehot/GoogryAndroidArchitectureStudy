package com.ironelder.androidarchitecture.view.mainview

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.ironelder.androidarchitecture.data.ResultItem
import com.ironelder.androidarchitecture.data.TotalModel
import com.ironelder.androidarchitecture.data.database.SearchResultDatabase
import com.ironelder.androidarchitecture.data.repository.SearchDataRepositoryImpl
import com.ironelder.androidarchitecture.view.baseview.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val LOG_TAG = MainPresenter::class.java.toString()

    override fun searchWithAdapter(
        context: Context, type: String, query: String?
    ) {
        SearchDataRepositoryImpl.getRemoteSearchData(
            type,
            query,
            SearchResultDatabase.getInstance(context)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showLoading()
            }
            .doAfterTerminate {
                view.hideLoading()
            }
            .subscribe(::onSuccess, ::onError)
            .addDisposable()
    }

    private fun onSuccess(result: TotalModel) {
        if (result.items.isNullOrEmpty()) {
            view.showNoSearchData()
        } else {
            view.onDataChanged(result.items)
        }
    }

    private fun onError(t: Throwable) {
        view.showErrorMessage(t.message)
    }

    override fun getSearchResultToRoom(context: Context, type: String) {
        SearchDataRepositoryImpl.getLocalSearchData(type, SearchResultDatabase.getInstance(context))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
                view.onLoadFromDatabase(
                    it.search,
                    Gson().fromJson(
                        it.result,
                        Array<ResultItem>::class.java
                    ).toCollection(ArrayList())
                )
            }, { t: Throwable? -> Log.w(LOG_TAG, t?.message ?: "DataBase $type Not Save Data") })
            ?.addDisposable()
    }

}