var { DeviceEventEmitter, NativeModules } = require('react-native');
var { TorrentStreamerAndroid } = NativeModules;

var TORRENT_STREAMER_DOWNLOAD_EVENTS = {
  error: 'error',
  progress: 'progress',
  ready: 'ready',
  stop: 'stop'
};

var _TorrentStreamerDownloadHandlers = {};

var TorrentStreamer = {
  addEventListener: function(type, handler) {
    _TorrentStreamerDownloadHandlers[handler] = DeviceEventEmitter.addListener(
      TORRENT_STREAMER_DOWNLOAD_EVENTS[type],
      (torrentStreamerData) => {
        handler(torrentStreamerData);
      }
    );
  },
  removeEventListener: function(type, handler) {
    if (!_TorrentStreamerDownloadHandlers[handler]) {
      return;
    }
    _TorrentStreamerDownloadHandlers[handler].remove();
    _TorrentStreamerDownloadHandlers[handler] = null;
  },
  start: function(url){
    TorrentStreamerAndroid.start(url);
  },
  stop: TorrentStreamerAndroid.stop,
  open: function(path, type) {
    TorrentStreamerAndroid.open(path, type);
  }
}

module.exports = TorrentStreamer;