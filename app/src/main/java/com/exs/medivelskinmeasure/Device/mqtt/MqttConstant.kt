package com.exs.medivelskinmeasure.Device.mqtt

object MqttConstant {
    val ClientID = "WAVUApp"
}

enum class MqttDataStep {
    idle, waitUserInfo, waitMeasure, waitImage, measureImage, waitTempMoisture, measureTempMoisture,measureComplete,resultSend,resultSendComplete
}