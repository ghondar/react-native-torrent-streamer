### React-Native-Torrent-Streamer

> Torrent Streamer for react-native

*Only Android support now.*

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
import React, { Component } from 'react'
import { AppRegistry, StyleSheet, View, Text, TouchableHighlight } from 'react-native'

import TorrentStreamer from 'react-native-torrent-streamer'

class Example extends Component {
  constructor(props, context) {
    super(props, context)
    this.state = {
      progress: 0,
      title   : ''
    }
  }

  componentWillMount() {
    TorrentStreamer.addEventListener('error', this.onError)
    TorrentStreamer.addEventListener('progress', this.onProgress.bind(this))
    TorrentStreamer.addEventListener('ready', this.onReady.bind(this))
    TorrentStreamer.addEventListener('stop', this.onStop.bind(this))
  }


  componentWillUnmount() {
    TorrentStreamer.removeEventListener('error', this.onError)
    TorrentStreamer.removeEventListener('progress', this.onProgress.bind(this))
    TorrentStreamer.removeEventListener('ready', this.onReady.bind(this))
    TorrentStreamer.removeEventListener('stop', this.onStop.bind(this))
    TorrentStreamer.stop()
  }

  onStart() {
    TorrentStreamer.start('magnet:?xt=urn:btih:D60795899F8488E7E489BA642DEFBCE1B23C9DA0&dn=Kingsman%3A+The+Secret+Service+%282014%29+%5B720p%5D&tr=http%3A%2F%2Ftracker.yify-torrents.com%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.org%3A80&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337')
  }

  onError(e) {
    console.log(e)
  }

  onProgress(progress) {
    if(progress.data != this.state.progress) {
      this.setState({
        progress: parseInt(progress.data) ? parseInt(progress.data) : 0,
        title   : typeof progress.data === 'string' && progress.data
      })
    }
  }

  onReady(data) {
    TorrentStreamer.open(data.url, 'video/mp4')
  }

  onStop(data) {
    console.log('stop')
  }

  render() {
    const { progress } = this.state

    return (
      <View style={styles.container}>

        <TouchableHighlight
          style={styles.button}
          onPress={this.onStart}>
            <Text >Start Torrent!</Text>
        </TouchableHighlight>

        <TouchableHighlight
          style={styles.button}
          onPress={TorrentStreamer.stop}>
            <Text >Stop Torrent!</Text>
        </TouchableHighlight>

        <Text>{progress}</Text>

      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
})

export default Example

AppRegistry.registerComponent('example', () => Example)
```

#### LICENSE
MIT
