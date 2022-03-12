package com.exs.medivelskinmeasure.Device

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.logging.Handler

object BluetoothObject {

    private val TAG: String? = this.javaClass.name

    /**
     * 장비의 블루투스 기능을 가져오기 위한 Adapter
     */
    var btAdapter: BluetoothAdapter


    /**
     * 장비의 데이터 통신을 위한 소켓, 입출력 스트림
     */
    var mBtSocket: BluetoothSocket? = null
    var mOutputStream: OutputStream? = null
    var mInputStream: InputStream? = null

    var mWorkThread: Thread? = null
    var mArrReadBuffer: ByteArray? = null
    var mPosReadBuffer: Int = 0

    init {
        btAdapter = BluetoothAdapter.getDefaultAdapter()

    }


    /**
     * 블루투스 어댑터의 상태를 체크
     */
    fun checkBtState(): Boolean {
        if (btAdapter != null) {
            return btAdapter.isEnabled
        }

        return false
    }

    /**
     * 블루투스 활성화 요청
     */
    @SuppressLint("MissingPermission")
    fun requestBtEnable(context: Context, requestCode: Int) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        (context as Activity).startActivityForResult(enableBtIntent, requestCode)
    }

    /**
     * 페어링된 장비 검색
     */
    @SuppressLint("MissingPermission")
    fun requestPairedDevices(): Set<BluetoothDevice>? {
        return btAdapter?.bondedDevices
    }


//    /**
//     * 블루투스 장비 연결
//     */
//    fun connectDevice(context: Context, device: BluetoothDevice) {
//        try {
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//
//
//
//                mBtSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(device.uuids.get(0).toString()))
//
//                mBtSocket?.let {
//                    it.connect()
//
//                    mOutputStream = it.outputStream
//                    mInputStream = it.inputStream
//
//                    receiveData()
//                }
//
//                if (mBtSocket == null) {
//                    Log.e(TAG, "장비와의 소켓이 열리지 않았음.")
//                }
//
//            } else {
//                Log.e(TAG, "BLUETOOTH_CONNECT 권한이 없습니다.")
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun receiveData() {
//        // 데이터를 수신하기 위한 버퍼를 생성
//        mPosReadBuffer = 0
//        mArrReadBuffer = ByteArray(1024)
//
//
//        // 데이터를 수신하기 위한 쓰레드 생성
//        mWorkThread = Thread {
//            while (Thread.currentThread().isInterrupted) {
//                try {
//
//                    // 데이터를 수신했는지 확인합니다.
//                    val byteAvailable: Int = mInputStream!!.available()
//
//                    // 데이터가 수신 된 경우
//                    if (byteAvailable > 0) {
//
//                        // 입력 스트림에서 바이트 단위로 읽어 옵니다.
//                        val bytes = ByteArray(byteAvailable)
//                        mInputStream!!.read(bytes)
//
//                        // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.
//                        for (i in 0 until byteAvailable) {
//                            val tempByte = bytes[i]
//
//                            // 개행문자를 기준으로 받음(한줄)
//                            if (tempByte == '\n'.toByte()) {
//
//                                // readBuffer 배열을 encodedBytes로 복사
//                                val encodedBytes = ByteArray(mPosReadBuffer)
//                                System.arraycopy(
//                                    mArrReadBuffer,
//                                    0,
//                                    encodedBytes,
//                                    0,
//                                    encodedBytes.size
//                                )
//
//                                // 인코딩 된 바이트 배열을 문자열로 변환
//                                val text = String(encodedBytes, Charset.forName("UTF-8"))
//                                mPosReadBuffer = 0
//
//                            } else {
//                                mArrReadBuffer!![mPosReadBuffer++] = tempByte
//                            }
//                        }
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                try {
//
//                    // 1초마다 받아옴
//                    Thread.sleep(1000)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        mWorkThread!!.start()
//    }
//
//    fun sendData(text:String) {
//
//        mOutputStream?.let {
//            var sendText = text
//
//            sendText += "\n"
//            it.write(sendText.toByteArray())
//        }
//
//    }
}

