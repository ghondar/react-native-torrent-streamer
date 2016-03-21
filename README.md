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

#### Usage
```Javascript
var RCTDeviceEventEmitter = require('RCTDeviceEventEmitter');
var Subscribable = require('Subscribable');
var TorrentStreamer = require('react-native-torrent-streamer');


var doubanbook = React.createClass({

  mixins: [Subscribable.Mixin],

  componentWillMount: function() {
    this.addListenerOn(RCTDeviceEventEmitter, 'error', this.onError)
    this.addListenerOn(RCTDeviceEventEmitter, 'progress', this.onProgress.bind(this))
    this.addListenerOn(RCTDeviceEventEmitter, 'ready', this.onReady.bind(this))
    this.addListenerOn(RCTDeviceEventEmitter, 'stop', this.onStop.bind(this))
  },

  onError: function(err) {
    console.log(err)
  },

  onProgress: function(progress) {
    console.log(progress.data)
  },

  onReady: function(data) {
    console.log("onReady")
    TorrentStreamer.open(data.url, "video/mp4")
  },

  onStop: function() {
    console.log("onStop")
  },

  start: function() {
    TorrentStreamer.start('magnet:?xt=urn:btih:D60795899F8488E7E489BA642DEFBCE1B23C9DA0&dn=Kingsman%3A+The+Secret+Service+%282014%29+%5B720p%5D&tr=http%3A%2F%2Ftracker.yify-torrents.com%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.org%3A80&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337')
  },

  stop: function() {
    TorrentStreamer.stop()
  },

  render: function() {
    return (
      <View style={styles.container}>

        <TouchableHighlight
          style={styles.button}
          onPress={this.start}>
            <Text >Start Torrent!</Text>
        </TouchableHighlight>

        <TouchableHighlight
          style={styles.button}
          onPress={this.stop}>
            <Text >Stop Torrent!</Text>
        </TouchableHighlight>

      </View>
    );
  }
})

```

#### LICENSE
MIT
