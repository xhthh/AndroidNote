package com.xht.androidnote.module.rxjava;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xht on 2018/6/6.
 */

public class RxJavaActivity extends BaseActivity {
    private static final String TAG = "RxJava";
    @BindView(R.id.btn_test0)
    Button mBtnTest0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick(R.id.btn_test0)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test0:
                //                test0();
                //                test1();
                                test2();
//                test3();
                break;
        }
    }

    private void test3() {

    }

    /**
     * 线程切换
     * 默认 发送和接收是在同一个线程
     * <p>
     * 每调用一次observeOn() 线程便会切换一次, 因此如果我们有类似的需求时, 便可知道如何处理了.
     * <p>
     * 在RxJava中, 已经内置了很多线程选项供我们选择, 例如有
     * ①Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
     * ②Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
     * ③Schedulers.newThread() 代表一个常规的新线程
     * ④AndroidSchedulers.mainThread()  代表Android的主线程
     */
    private void test2() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "test2()--observable--subscribe()---thread is " + Thread.currentThread().getName());
                Log.i(TAG, "test2()--observable--subscribe()---emitter 1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "test2()--consumer--accept()---thread is " + Thread.currentThread().getName());
                Log.i(TAG, "test2()--consumer--accept()---onNext==" + integer);
            }
        };

        // 默认是在同一个线程
        //observable.subscribe(consumer);

        //subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.
        //多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
        //多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
        /*observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);*/

        /*observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(consumer);*/


        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "test2()---After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "test2()---After observeOn(io), current thread is : " + Thread.currentThread().getName());
                    }
                })
                .subscribe(consumer);
    }


    /**
     * dispose  会将两根管道切断, 从而导致下游收不到事件.
     * <p>
     * 调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.
     */
    private void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "test1()---subscribe()---emitter 1");
                emitter.onNext(1);
                Log.i(TAG, "test1()---subscribe()---emitter 2");
                emitter.onNext(2);
                Log.i(TAG, "test1()---subscribe()---emitter 3");
                emitter.onNext(3);
                Log.i(TAG, "test1()---subscribe()---emitter onComplete");
                emitter.onComplete();
                Log.i(TAG, "test1()---subscribe()---emitter 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "test1()---onSubscribe()");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "test1()---onNext()---onNext()---" + integer);
                i++;
                if (i == 2) {
                    Log.i(TAG, "test1()---onNext()---dispose");
                    mDisposable.dispose();
                    Log.i(TAG, "test1()---onNext()---isDispose==" + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "test1()---onError()");

            }

            @Override
            public void onComplete() {
                Log.i(TAG, "test1()---onComplete()");
            }
        });
    }


    /**
     * 最基本用法    只有当上游和下游建立连接之后, 上游才会开始发送事件. 也就是调用了subscribe() 方法之后才开始发送事件.
     * 链式调用
     * 发送事件的规则
     * ①上游可以发送无限个onNext, 下游也可以接收无限个onNext.
     * ②当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
     * ③当上游发送了一个onError后,  上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
     * ④上游可以不发送onComplete或onError.
     * ⑤最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError,  也不能先发一个onComplete, 然后再发一个onError, 反之亦然。
     * 发送一个 onComplete() 和一个 onError() 会崩溃；发送两个 onError() 第二个会导致程序会崩溃
     */
    private void test0() {
        //创建一个上游 Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });

        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "test0()---onSubscribe()");
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "test0()---onNext()---" + integer);

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "test0()---onError()");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "test0()---onComplete()");
            }
        };

        // 只有当上游和下游建立连接之后, 上游才会开始发送事件. 也就是调用了subscribe() 方法之后才开始发送事件.
        //observable.subscribe(observer);

        //链式调用
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(3);
                emitter.onNext(2);
                emitter.onNext(1);
                emitter.onComplete();
                //                emitter.onError(new Throwable("错误"));
                //                emitter.onError(new Throwable("错误"));

                // 发送一个 onComplete() 和一个 onError() 会崩溃
                // 发送两个 onError() 第二个会导致程序会崩溃
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "test0()---onSubscribe()");
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "test0()---onNext()---" + integer);

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "test0()---onError()---" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "test0()---onComplete()");
            }
        });
    }
}
