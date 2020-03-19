import { DeviceEventEmitter, NativeModules } from 'react-native';

const { TorrentStreamer } = NativeModules;

const TORRENT_STREAMER_DOWNLOAD_EVENTS = {
  error: 'error',
  progress: 'progress',
  status: 'status',
  ready: 'ready',
  stop: 'stop'
};

const _TorrentStreamerDownloadHandlers = {};

const TorrentStreamerPackage = {
  addEventListener: (type, handler) => {
    _TorrentStreamerDownloadHandlers[handler] = DeviceEventEmitter.addListener(
      TORRENT_STREAMER_DOWNLOAD_EVENTS[type],
      (torrentStreamerData) => {
        handler(torrentStreamerData);
      }
    );
  },
  removeEventListener: (type, handler) =>{
    if (!_TorrentStreamerDownloadHandlers[handler]) {
      return;
    }
    _TorrentStreamerDownloadHandlers[handler].remove();
    _TorrentStreamerDownloadHandlers[handler] = null;
  },
  setup: (location, removeAfterStop)=>{
    removeAfterStop = removeAfterStop || true
    
    TorrentStreamer.setup(location, removeAfterStop);
  },
  start: url => {
    TorrentStreamer.start(url);
  },
  stop: () => {
    TorrentStreamer.stop()
  },
  open: (path, type) =>{
    TorrentStreamer.open(path, type);
  }
}

export default TorrentStreamerPackage