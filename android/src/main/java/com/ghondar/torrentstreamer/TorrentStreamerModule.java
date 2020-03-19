package com.ghondar.torrentstreamer;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstream.TorrentOptions;
import com.github.se_bastiaan.torrentstream.TorrentStream;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;

public class TorrentStreamerModule extends ReactContextBaseJavaModule implements TorrentListener {

    private final ReactApplicationContext reactContext;
    private TorrentStream mTorrentStream = null;

    public TorrentStreamerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "TorrentStreamer";
    }

    @ReactMethod
    public void setup(String location, Boolean removeAfterStop) {
        TorrentOptions torrentOptions = new TorrentOptions.Builder()
                .saveLocation(location)
                .maxConnections(200)
                .autoDownload(true)
                .removeFilesAfterStop(removeAfterStop)
                .build();

        mTorrentStream = TorrentStream.init(torrentOptions);
        mTorrentStream.addListener(this);
    }

    @ReactMethod
    public void setup(String location) {
        this.setup(location, true);
    }

    private void setup() {
        if (mTorrentStream == null) {
            this.setup("" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), true);
        }
    }

    @ReactMethod
    public void start(String magnetUrl) {
        this.setup();

        mTorrentStream.startStream(magnetUrl);
    }

    @ReactMethod
    public void stop() {
        if (mTorrentStream != null && mTorrentStream.isStreaming()) {
            mTorrentStream.stopStream();
        }
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        this.reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void onStreamPrepared(Torrent torrent) {
//        Log.d("data", "OnStreamPrepared");

        WritableMap params = Arguments.createMap();
        params.putString("data", "OnStreamPrepared");
        sendEvent("progress", params);
//        torrent.startDownload();
    }

    @Override
    public void onStreamStarted(Torrent torrent) {
//        Log.d("data", "onStreamStarted");

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
//        Log.d("url", torrent.getVideoFile().toString());

        WritableMap params = Arguments.createMap();
        params.putString("url", torrent.getVideoFile().toString());
        params.putString("filename", torrent.getTorrentHandle().name());
        sendEvent("ready", params);
    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus status) {
//        Log.d("buffer", "" + status.bufferProgress);
//        Log.d("download", "" + status.downloadSpeed);
//        Log.d("Progress", "" + status.progress);
//        Log.d("seeds", "" + status.seeds);

        WritableMap params = Arguments.createMap();
        params.putString("buffer", "" + status.bufferProgress);
        params.putString("downloadSpeed", "" + status.downloadSpeed);
        params.putString("progress", "" + status.progress);
        params.putString("seeds", "" + status.seeds);
        sendEvent("status", params);
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
        if (intent.resolveActivity(this.reactContext.getPackageManager()) != null) {
            this.reactContext.startActivity(intent);
        }
    }
}
