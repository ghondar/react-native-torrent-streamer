## React-Native-Torrent-Streamer

> Torrent Streamer for react-native

*Only Android support now.*

### - 0.0.7 to 0.1.0 migration

* progress name event changed to status

```Diff
componentWillMount() {
    ...
-    TorrentStreamer.addEventListener('progress', this.onProgress.bind(this))
+    TorrentStreamer.addEventListener('status', this.onStatus.bind(this))
    ...
  }
```

```Diff
-  onStatus({ data }) {
+  onStatus({progress, buffer, downloadSpeed, seeds}) {
    ...
  }
```

* New params on status event

#### Integrate

##### Android

* Install via npm
`npm i react-native-torrent-streamer --save-dev`

* Add dependency to `android/settings.gradle`
```
...
include ':react-native-torrent-streamer'
project(':react-native-torrent-streamer').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-torrent-streamer/android/torrent')
```

* Add `android/app/build.gradle`
```
...
dependencies {
    ...
    compile project(':react-native-torrent-streamer')
}
```
#### If you're using react-native 0.25~0.29, follow these steps

* Register module in `MainActivity.java`
```Java
import com.ghondar.torrentstreamer.*;  // <--- import

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactRootView = new ReactRootView(this);

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new TorrentStreamerPackage())  // <------- here
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

        mReactRootView.startReactApplication(mReactInstanceManager, "doubanbook", null);

        setContentView(mReactRootView);
    }
```

#### If you're using react-native 0.30+, follow these steps

##### Register module in `MainApplication.java`
```Java
import com.ghondar.torrentstreamer.*;  // <--- import

@Override
 protected List<ReactPackage> getPackages() {
   return Arrays.<ReactPackage>asList(
      new MainReactPackage(),
      new TorrentStreamerPackage()  // <------- here
   );
 }
```

#### Usage

```Javascript
import React, { Component } from 'react';
import { AppRegistry, StyleSheet, View, Text, TouchableHighlight } from 'react-native'

import TorrentStreamer from 'react-native-torrent-streamer'

export default class App extends Component<{}> {
  state = {
    progress: 0,
    buffer: 0,
    downloadSpeed: 0,
    seeds: 0
  }

  componentWillMount() {
    TorrentStreamer.addEventListener('error', this.onError)
    TorrentStreamer.addEventListener('status', this.onStatus.bind(this))
    TorrentStreamer.addEventListener('ready', this.onReady.bind(this))
    TorrentStreamer.addEventListener('stop', this.onStop.bind(this))
  }

  onError(e) {
    console.log(e)
  }

  onStatus({progress, buffer, downloadSpeed, seeds}) {
    this.setState({
      progress,
      buffer,
      downloadSpeed,
      seeds
    })
  }

  onReady(data) {
    TorrentStreamer.open(data.url, 'video/mp4')
  }

  onStop(data) {
    console.log('stop')
  }

  render() {
    const { progress, buffer, downloadSpeed, seeds } = this.state

    return (
      <View style={styles.container}>
        <TouchableHighlight
          style={styles.button}
          onPress={this._handleStart.bind(this)}>
            <Text >Start Torrent!</Text>
        </TouchableHighlight>

        <TouchableHighlight
          style={styles.button}
          onPress={this._handleStop.bind(this)}>
            <Text >Stop Torrent!</Text>
        </TouchableHighlight>

        {buffer ? <Text>Buffer: {buffer}</Text> : null}
        {downloadSpeed ? <Text>Download Speed: {(downloadSpeed / 1024).toFixed(2)} Kbs/seg</Text> : null}
        {progress ? <Text>Progress: {parseFloat(progress).toFixed(2)}</Text> : null}
        {seeds ? <Text>Seeds: {seeds}</Text> : null}
      </View>
    )
  }

  _handleStart() {
    TorrentStreamer.start('magnet:?xt=urn:btih:D60795899F8488E7E489BA642DEFBCE1B23C9DA0&dn=Kingsman%3A+The+Secret+Service+%282014%29+%5B720p%5D&tr=http%3A%2F%2Ftracker.yify-torrents.com%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.org%3A80&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337')
  }

  _handleStop() {
    this.setState({
      progress: 0,
      buffer: 0,
      downloadSpeed: 0,
      seeds: 0
    }, () => {
      TorrentStreamer.stop()
    })
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
})
```

#### LICENSE
MIT
