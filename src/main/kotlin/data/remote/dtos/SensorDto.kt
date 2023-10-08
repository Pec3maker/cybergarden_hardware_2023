package data.remote.dtos

import data.models.SensorModel
import data.models.SensorType
import utils.Transformable
import java.util.*

data class SensorDto(
    val name: String?,
    val lastvalue: String?,
    val lastclock: String?,
    val units: String?
) : Transformable<SensorModel> {
    override fun convert(): SensorModel =
        SensorModel(
            type = SensorType.fromString(name = name),
            value = lastvalue ?: "",
            time = Date(lastclock?.toLong()?.times(1000) ?: 0),
            units = units ?: ""
        )
}
