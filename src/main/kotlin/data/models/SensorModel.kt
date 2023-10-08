package data.models

import java.util.Date

data class SensorModel(
    val type: SensorType,
    val value: String,
    val time: Date,
    val units: String
)