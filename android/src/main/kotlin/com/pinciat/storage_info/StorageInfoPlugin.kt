package com.pinciat.storage_info

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** StorageInfoPlugin */
class StorageInfoPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "storage_info")
        channel.setMethodCallHandler(this)
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "storage_info")
            channel.setMethodCallHandler(StorageInfoPlugin())
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getStorageFreeSpace" -> result.success(getStorageFreeSpace())
            "getStorageUsedSpace" -> result.success(getStorageUsedSpace())
            "getStorageTotalSpace" -> result.success(getStorageTotalSpace())

            "getRomTotalSpace" -> result.success(getRomTotalSpace())
            "getRomFreeSpace" -> result.success(getRomFreeSpace())
            "getRomUsedSpace" -> result.success(getRomUsedSpace())

            else -> result.notImplemented()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getStorageTotalSpace(): Long {
        val path = Environment.getDataDirectory()
        println("xxx data directory $path")
        val stat = StatFs(path.path)
        return stat.totalBytes
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getStorageFreeSpace(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        return stat.availableBytes
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getStorageUsedSpace(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        return stat.totalBytes - stat.availableBytes
    }

//    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
//    fun getStorageUsedPercent(): Double {
//        val path = Environment.getDataDirectory()
//        val stat = StatFs(path.path)
//        return (stat.totalBytes - stat.availableBytes) * 100f / stat.totalBytes;
//    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getRomTotalSpace(): Long {
        val mActivityManager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        mActivityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getRomFreeSpace(): Long {
        val mActivityManager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        mActivityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getRomUsedSpace(): Long {
        val mActivityManager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        mActivityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem - memoryInfo.availMem;
    }

    fun roundNumberTo2DecimalPlace(value: Double): Double {
        val number2digits: Double = String.format("%.2f", value).toDouble()
        return number2digits
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
