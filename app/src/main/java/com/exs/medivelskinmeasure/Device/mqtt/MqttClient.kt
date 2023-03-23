package com.exs.medivelskinmeasure.Device.mqtt

import android.app.Application
import android.content.Context
import android.util.Log
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.CommonUtil
import com.google.gson.Gson
import com.google.gson.JsonParser
import info.mqtt.android.service.Ack
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.*
//import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

//import org.eclipse.paho.android.service.MqttAndroidClient





object MqttClient {
    private val TAG: String = "MqttClient"

    private var _mqttClient: MqttAndroidClient? = null
    private var _deviceSerial: String? = null

    lateinit var listener: MQTTClientInterface

    var mResultImgArray:ArrayList<MQTTMeasuredResultData> = ArrayList()

    interface MQTTClientInterface {
        fun batterState(voltage: Double, level: Double) {}
        fun measuringData(data: MQTTMeasuringRequest) {}
        fun numericResultData(temp: Float, moisture: Int) {}
        fun imageReusltData(data: MQTTMeasuredResultData) {}
        fun measuredFinish() {}
    }

    init {

    }

    fun checkMqttConnection(context: Context): Boolean {
        if (_mqttClient != null) {
            if (_mqttClient!!.isConnected) {
                setConnectionToken(context)
                return true

            } else {
                try {
                    return tryConnection(context)
                } catch (e: MqttException) {
                    resetConnectionData(context)
                    e.printStackTrace()

                    return false
                }
            }

        } else {
            return tryConnection(context)
        }
    }

    private fun tryConnection(context: Context): Boolean {
        val deviceIp = CommonUtil.getPreferenceString(
            context,
            context.getString(R.string.pref_key_device_wifi_ip)
        )

        val deviceSerial = CommonUtil.getPreferenceString(
            context,
            context.getString(R.string.pref_key_device_serial)
        )

        Log.e(TAG, "ip : ${deviceIp} and Serial : ${deviceSerial}")

        _deviceSerial = deviceSerial

        if (deviceSerial != null && deviceIp != null) {
            if (deviceIp.isEmpty() || deviceSerial.isEmpty()) {
                return false
            } else {
                try {
                    val tempClient = MqttAndroidClient(
                        context,
                        "tcp://${deviceIp}:1883",
                        CommonUtil.getPreferenceString(
                            context,
                            context.getString(R.string.pref_key_device_clientId)
                        )!!
                        , Ack.MANUAL_ACK
                    )

                    _mqttClient = tempClient
                    setConnectionToken(context)

                    return true
                } catch (e: MqttException) {
                    resetConnectionData(context)

                    Log.d(TAG, e.localizedMessage)
                    e.printStackTrace()
                    return false
                }
            }
        } else {
            return false
        }
    }

    fun setConnectionToken(context:Context) {
        val token = _mqttClient!!.connect()

        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.w("Mqtt", "Subscribed!")

                subscribeTopic()

                CommonUtil.setPreferenceString(context, context.getString(R.string.pref_key_device_wifi_ip), "")
                CommonUtil.setPreferenceString(context, context.getString(R.string.pref_key_device_serial), "")

            }

            override fun onFailure(
                asyncActionToken: IMqttToken,
                exception: Throwable
            ) {
                Log.w("Mqtt", "Subscribed fail!")
                disconnectMqtt()
                subscribeTopic()
            }
        }

        settingCallback(token)
    }

    fun resetConnectionData(context: Context) {
        CommonUtil.setPreferenceString(
            context,
            context.getString(R.string.pref_key_device_wifi_ip),
            ""
        )

        CommonUtil.setPreferenceString(
            context,
            context.getString(R.string.pref_key_device_serial),
            ""
        )

        disconnectMqtt()
    }

    fun disconnectMqtt() {
        _mqttClient?.disconnect()
        _mqttClient = null
    }

    fun existConnectionData(context: Context): Boolean {
        val deviceIp = CommonUtil.getPreferenceString(
            context,
            context.getString(R.string.pref_key_device_wifi_ip)
        )

        val deviceSerial = CommonUtil.getPreferenceString(
            context,
            context.getString(R.string.pref_key_device_serial)
        )

        if (deviceSerial != null && deviceIp != null) {
            if (deviceIp.isEmpty() || deviceSerial.isEmpty()) {
                return false
            } else {
                return true
            }
        } else {
            return false
        }
    }

    fun subscribeTopic() {

        try {
            // 레포트 정보
            _mqttClient?.let { client ->
                val topic: String =
                    String.format(MQTTTopic.MQTTTopicReportRequest, _deviceSerial)


                val runnable = Runnable {
                    client.subscribe(topic, 0)
                }

                runnable.run()

            }

            // 컨트롤 응답
            _mqttClient?.let { client ->
                val topic: String = String.format(MQTTTopic.MQTTTopicControlResponse, _deviceSerial)

//                withContext(Dispatchers.IO) {
//
//                }

                val runnable = Runnable {
                    client.subscribe(topic, 0)
                }

                runnable.run()
            }

        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun settingCallback(token: IMqttToken) {
        token.client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.e(TAG, "Connection Lost => ${cause.toString()}")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.e(
                    TAG,
                    "topic => ${topic}, ${message}"
                )

                val string = message!!.toString()

                val element = JsonParser.parseString(string)
                val request = element.asJsonObject.get("request").asString

                if (request.equals(MQTTString.Battery)) {

                    val gsonObj = Gson().fromJson<MQTTConnectionBatteryRequest>(
                        string,
                        MQTTConnectionBatteryRequest::class.java
                    )

                    listener.batterState(
                        gsonObj.parameters.voltage.toDouble(),
                        gsonObj.parameters.level.toDouble()
                    )
                    responseReportBattery()
                } else if (request.equals(MQTTString.status)) {
                    val gsonObj = Gson().fromJson<MQTTMeasuringRequest>(
                        string,
                        MQTTMeasuringRequest::class.java
                    )

                    if (gsonObj.parameters.step == MQTTStep.waitImage.ordinal || gsonObj.parameters.step == MQTTStep.measureImage.ordinal || gsonObj.parameters.step == MQTTStep.measureTempMoisture.ordinal || gsonObj.parameters.step == MQTTStep.resultSend.ordinal) {
                        listener.measuringData(gsonObj)
                    } else if (gsonObj.parameters.step == MQTTStep.idle.ordinal) {
                        listener.measuredFinish()
                    }


                    responseMeasuring()

                } else if (request.equals(MQTTString.result)) {
                    responseMeasureEnd()

                    val gsonObj = Gson().fromJson<MQTTMeasuredResult>(
                        string,
                        MQTTMeasuredResult::class.java
                    )

                    if (gsonObj.parameters.data.image_name.isEmpty()) {
                        listener.numericResultData(
                            gsonObj.parameters.data.temp,
                            gsonObj.parameters.data.moisture
                        )
                    } else {
                        mResultImgArray.add(gsonObj.parameters.data)
//                        listener.imageReusltData(gsonObj.parameters.data)
                    }
                }

            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }

        })
    }

    /**
     * 유저 정보 설정 관련 메소드
     */

    // publish
    fun publishUserInfoSetting(context: Context) {
        CoroutineScope(Dispatchers.IO).async {
            try {
                _mqttClient?.let { client ->
                    val topic: String =
                        String.format(MQTTTopic.MQTTTopicControlRequest, _deviceSerial)

                    val request: MQTTConnectionUserInfoRequest = MQTTConnectionUserInfoRequest(
                        MQTTString.UserInfo, MQTTConnectionUserInfoRequestParam(
                            CommonUtil.getUUID(context).substring(0, 31)
                        )
                    )

                    val jsonString: String = Gson().toJson(request)
                    client.publish(topic, MqttMessage(jsonString.toByteArray()))
                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }

    }

    /**
     * 배터리의 상태를 주기적으로 받기 위한 레포트
     */

    // response 메소드
    fun responseReportBattery() {
        CoroutineScope(Dispatchers.IO).async {
            try {

                _mqttClient?.let { client ->
                    val topic: String =
                        String.format(MQTTTopic.MQTTTopicReportResponse, _deviceSerial)

                    client.publish(
                        topic, MqttMessage(
                            Gson().toJson(MQTTConnectionBatteryResponse(MQTTString.Battery, "OK"))
                                .toByteArray()
                        )
                    )

                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 측정 시작 요청
     */
    fun requestMeasureStart() {
        CoroutineScope(Dispatchers.IO).async {
            try {
                _mqttClient?.let { client ->
                    mResultImgArray.clear()

                    val topic: String =
                        String.format(MQTTTopic.MQTTTopicControlRequest, _deviceSerial)

                    val param = MQTTMeasureRequestParam(MQTTString.StepImage)
                    val request = MQTTMeasureRequest(MQTTString.Measure, param)



                    client.publish(
                        topic, MqttMessage(
                            Gson().toJson(request)
                                .toByteArray()
                        )
                    )
                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 측정 진행
     */
    fun responseMeasuring() {
        CoroutineScope(Dispatchers.IO).async {
            try {
                _mqttClient?.let { client ->
                    val topic: String =
                        String.format(MQTTTopic.MQTTTopicReportResponse, _deviceSerial)

                    client.publish(
                        topic, MqttMessage(
                            Gson().toJson(MQTTConnectionBatteryResponse(MQTTString.result, "OK"))
                                .toByteArray()
                        )
                    )

                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 측정 종료
     */
    fun responseMeasureEnd() {
        CoroutineScope(Dispatchers.IO).async {
            try {
                _mqttClient?.let { client ->
                    val topic: String =
                        String.format(MQTTTopic.MQTTTopicReportResponse, _deviceSerial)

                    client.publish(
                        topic, MqttMessage(
                            Gson().toJson(MQTTConnectionBatteryResponse(MQTTString.result, "OK"))
                                .toByteArray()
                        )
                    )

                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }
}