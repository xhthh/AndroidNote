package com.xht.androidnote.module.rxjava;

import android.util.Log;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xht on 2018/6/6.
 */

public class RxJavaActivity extends BaseActivity {
    private static final String TAG = "RxJavaActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    protected void initEventAndData() {
        test1();
    }

    private void test1() {
        Flowable.just("Hello World").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("xht", "s===" + s);
            }
        });
    }


    private void test2() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "create: " + Thread.currentThread().getName());
                e.onNext(1);
                e.onNext(2);
            }
        }).flatMap(new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer integer) throws Exception {
                Log.d(TAG, "flatMap: " + Thread.currentThread().getName());
                return Observable.just(integer);
            }
        }).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "Consumer: " + Thread.currentThread().getName());
                    }
                });

    }

    private void test3() {
        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "create: " + Thread.currentThread().getName());
                e.onNext(1);
                e.onNext(2);
            }
        });
        Observable<Integer> integerObservable1 = integerObservable.flatMap(new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer integer) throws Exception {
                Log.d(TAG, "flatMap: " + Thread.currentThread().getName());
                return Observable.just(integer);
            }
        });
        Observable<Integer> integerObservable2 = integerObservable1.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Disposable disposable = integerObservable2.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Consumer: " + Thread.currentThread().getName());
            }
        });
    }
}
