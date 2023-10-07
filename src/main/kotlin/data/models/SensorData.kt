package data.models

import data.remote.dtos.SensorDataDto
import java.util.UUID

data class SensorData(
    val id: String,
    val label: String,
    val type: String,
    val isActive: Boolean,
    val lastMeasurementValue: Double,
    val lastMeasurementTimestamp: String,
    val units: String
) {
    companion object {
        // TODO: Real mapping from DTO to SensorData
        fun from(dto: SensorDataDto): SensorData {
            return SensorData(
                id = UUID.randomUUID().toString(),
                label = "влажность Приморский парк",
                type = "датчик влажности",
                isActive = true,
                lastMeasurementValue = 10.0,
                lastMeasurementTimestamp = "05.10.2023 20:23:30",
                units = "ед"
            )
        }
    }

    fun getDescription(): String {
        val isActiveIcon = if(isActive) { "\uD83D\uDFE2" } else {"\uD83D\uDD34" }
        val isActiveLabel = if(isActive) { "Активен" } else { "Отключен" }
        return "\uD83D\uDEDC id: $id\n" +
                "\uD83D\uDEDC Датчик: влажность Приморский парк $label\n" +
                "\uD83D\uDDFFТип: $type\n" +
                "$isActiveIcon Статус: $isActiveLabel\n" +
                "\uD83C\uDFB1 Последние показания: $lastMeasurementValue $units\n" +
                "\uD83D\uDD54 Время последнего обновления: $lastMeasurementTimestamp"
    }
}
