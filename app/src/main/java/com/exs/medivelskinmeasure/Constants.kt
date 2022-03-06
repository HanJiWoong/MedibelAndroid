package com.exs.medivelskinmeasure

class Constants {
    companion object {
        var measureMode:MeasureMode = MeasureMode.None
    }
}

enum class MeasureMode {
    None,
    Hospital,
    BeautyShop,
    AnimalHospital,
    HumanClinical,
    AnimalClinical,
    Clinical
}