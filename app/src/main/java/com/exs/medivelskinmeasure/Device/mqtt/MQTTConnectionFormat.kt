package com.exs.medivelskinmeasure.Device.mqtt

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object MQTTTopic {
    val MQTTTopicControlRequest: String = "/nodes/WAVU-%s/control/request"
    val MQTTTopicControlResponse: String = "/nodes/WAVU-%s/control/response"
    val MQTTTopicReportRequest: String = "/nodes/WAVU-%s/report/request"
    val MQTTTopicReportResponse: String = "/nodes/WAVU-%s/report/response"
}


object MQTTString {
    val UserInfo: String = "setUserInfo"
    val status: String = "status"
    val result: String = "result"
    val Battery: String = "battery"
    val Measure: String = "measure"
    val StepEnter: String = "enter"
    val StepUseKey: String = "usekey"
    val StepImage: String = "image"
    val StepSensing: String = "sensing"
}

enum class MQTTStep {
    idle, waitUserInfo, waitMeasure, waitImage, measureImage, waitTempMoisture, measureTempMoisture, measureComplete, resultSend, resultSendComplete
}

/**
 * 사용자 정보 설정
 */
data class MQTTConnectionUserInfoRequest(
    val request: String,
    val parameters: MQTTConnectionUserInfoRequestParam
)

data class MQTTConnectionUserInfoRequestParam(
    val userkey: String
)

data class MQTTConnectionUserInfoResponse(
    val response: String,
    val result: String,
    val reason:String?
)


/**
 * 배터리 체크
 */

data class MQTTConnectionBatteryRequest(
    val request: String,
    val parameters: MQTTConnectionBatteryRequestParam
)

data class MQTTConnectionBatteryRequestParam(
    val voltage: Float,
    val level: Int
)

data class MQTTConnectionBatteryResponse(
    val response: String,
    val result: String
)

/**
 * MQTT 상태 체크
 */
data class MQTTStatusResponse(
    val response:String,
    val result:String
)

/**
 * 측정 시작
 */
data class MQTTMeasureRequest(
    val request: String,
    val parameters: MQTTMeasureRequestParam
)

data class MQTTMeasureRequestParam(
    val step: String
)

data class MQTTMeasureResponse(
    val response: String,
    val result: MQTTMeasureResponseResult
)

data class MQTTMeasureResponseResult(
    val step: String,
    val state: String
)

data class MQTTMeasuringRequest(
    val request: String,
    val parameters: MQTTMeasuringRequestParam
)

data class MQTTMeasuringRequestParam(
    val step: Int,
    val message: String,
    val measure_data: MQTTMeasuringRequestData
)

data class MQTTMeasuringRequestData(
    val temp: Float,
    val moisture: Float,
    val image_no: Int
)

@Parcelize
data class MQTTMeasuredResult(
    val request: String,
    val parameters:MQTTMeasuredResultParameter
):Parcelable

@Parcelize
data class MQTTMeasuredResultParameter(
    val userkey: String,
    val datatime: String,
    val final: Int,
    val data_type: Int,
    val data:MQTTMeasuredResultData
):Parcelable

@Parcelize
data class MQTTMeasuredResultData(
    val image_name: String,
    val image_data: String,
    val temp:Float,
    val moisture:Int
):Parcelable