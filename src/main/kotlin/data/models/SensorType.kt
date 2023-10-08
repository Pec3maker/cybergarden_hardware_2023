package data.models

enum class SensorType {
    TEMPERATURE,
    VIBRATION,
    HUMIDITY,
    UNKNOWN;

    companion object {
        fun fromString(name: String?) = when (name) {
            "Temperature" -> TEMPERATURE
            "Humidity" -> HUMIDITY
            "Vibration" -> VIBRATION
            else -> UNKNOWN
        }
    }
}
