## React-Native-Torrent-Streamer

> Torrent Streamer for react-native

*Only Android support now.*

### - 0.2.0 Release

* Support react-native 0.60.x
* TorrentStream-Android updated to 

#### Integrate

##### Android

* Install via npm
`yarn add react-native-torrent-streamer`

* Add `android/build.gradle`
```Diff
...
allprojects {
  ...
  dependencies {
      ...
+      maven {
+        url("https://jitpack.io")
+      }
  }
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

  componentDidMount() {
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
    TorrentStreamer.start('magnet:?xt=urn:btih:88594aaacbde40ef3e2510c47374ec0aa396c08e&dn=bbb%5Fsunflower%5F1080p%5F30fps%5Fnormal.mp4&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80%2Fannounce&ws=http%3A%2F%2Fdistribution.bbb3d.renderfarming.net%2Fvideo%2Fmp4%2Fbbb%5Fsunflower%5F1080p%5F30fps%5Fnormal.mp4')
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
