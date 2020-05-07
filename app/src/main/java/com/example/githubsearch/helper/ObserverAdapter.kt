package com.example.githubsearch.helper

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

abstract class ObserverAdapter<T>: Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(disposable: Disposable?) {
    }

    override fun onNext(it: T) {
    }

    override fun onError(e: Throwable?) {
    }
}