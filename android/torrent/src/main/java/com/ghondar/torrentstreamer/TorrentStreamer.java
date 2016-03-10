package com.ghondar.torrentstreamer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.github.sv244.torrentstream.StreamStatus;
import com.github.sv244.torrentstream.Torrent;
import com.github.sv244.torrentstream.TorrentOptions;
import com.github.sv244.torrentstream.TorrentStream;
import com.github.sv244.torrentstream.listeners.TorrentListener;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class TorrentStreamer extends ReactContextBaseJavaModule implements TorrentListener {

    private TorrentStream mTorrentStream;
    private ReactApplicationContext context;

    public TorrentStreamer(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;

        TorrentOptions torrentOptions = new TorrentOptions();
        torrentOptions.setSaveLocation(reactContext.getExternalCacheDir());
        torrentOptions.setMaxConnections(200);
        torrentOptions.setMaxDownloadSpeed(0);
        torrentOptions.setMaxUploadSpeed(0);
        torrentOptions.setRemoveFilesAfterStop(true);

        mTorrentStream = TorrentStream.init(torrentOptions);
        mTorrentStream.addListener(this);
    }

    @Override
    public String getName() {
        return "TorrentStreamer";
    }

    @ReactMethod
    public Bool isStreaming() {
        return mTorrentStream.isStreaming();
    }

    @ReactMethod
    public Bool stop() {
        if(mTorrentStream.isStreaming()) {
            mTorrentStream.stopStream();
        }
    }

    @ReactMethod
    public void start(String magnetUrl) {
        mTorrentStream.startStream(magnetUrl);
    }

    private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        this.context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void onStreamPrepared(Torrent torrent) {
        WritableMap params = Arguments.createMap();
        params.putString("data", "OnStreamPrepared");
        sendEvent("progress", params);
        torrent.startDownload();
    }

    @Override
    public void onStreamStarted(Torrent torrent) {
        WritableMap params = Arguments.createMap();
        params.putString("data", "onStreamStarted");
        sendEvent("progress", params);
    }

    @Override
    public void onStreamError(Torrent torrent, Exception e) {
        WritableMap params = Arguments.createMap();
        params.putString("msg", e.getMessage());
        sendEvent("error", params);
    }

    @Override
    public void onStreamReady(Torrent torrent) {
        WritableMap params = Arguments.createMap();
        params.putString("url", torrent.getVideoFile().toString());
        sendEvent("ready", params);
    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus status) {

        if(status.bufferProgress <= 100) {
            WritableMap params = Arguments.createMap();
            params.putString("data", ""+status.bufferProgress);
            sendEvent("progress", params);
        }
    }

    @Override
    public void onStreamStopped() {
        WritableMap params = Arguments.createMap();
        params.putString("msg", "OnStreamStoped");
        sendEvent("stop", params);
    }

    @ReactMethod
    public void open(String url, String type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Check that an app exists to receive the intent
        if (intent.resolveActivity(this.context.getPackageManager()) != null) {
            this.context.startActivity(intent);
        }
    }
}
