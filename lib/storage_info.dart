import 'dart:async';

import 'package:flutter/services.dart';

class StorageInfo {
  static const MethodChannel _channel = const MethodChannel('storage_info');

  // Storage space in bytes
  static Future<int> get getStorageFreeSpace async {
    final int freeSpace = await _channel.invokeMethod('getStorageFreeSpace');
    return freeSpace;
  }

  static Future<int> get getStorageTotalSpace async {
    final int totalSpace = await _channel.invokeMethod('getStorageTotalSpace');
    return totalSpace;
  }

  static Future<int> get getStorageUsedSpace async {
    final int usedSpace = await _channel.invokeMethod('getStorageUsedSpace');
    return usedSpace;
  }

  // rom in bytes
  static Future<int> get getRomTotalSpace async {
    final int totalSpace = await _channel.invokeMethod('getRomTotalSpace');
    return totalSpace;
  }

  static Future<int> get getRomFreeSpace async {
    final int freeSpace = await _channel.invokeMethod('getRomFreeSpace');
    return freeSpace;
  }

  static Future<int> get getRomUsedSpace async {
    final int usedSpace = await _channel.invokeMethod('getRomUsedSpace');
    return usedSpace;
  }
}
