package com.xht.androidnote.module.okhttp;

import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by xht on 2018/4/28.
 */

public class CountingRequestBody extends RequestBody {

    private RequestBody delegate;
    private Listener mListener;
    private CountingSink mCountingSink;

    public CountingRequestBody(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        mListener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        mCountingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(mCountingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {
        private long bytesWritten;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            mListener.onRequestProgress(bytesWritten, contentLength());
        }
    }

    public interface Listener {
        void onRequestProgress(long byteWrited, long contentLength);
    }
}
