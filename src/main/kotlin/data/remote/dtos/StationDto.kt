package data.remote.dtos

import data.models.StationModel
import utils.Transformable

data class StationDto(
    val id: String?,
    val name: String?
) : Transformable<StationModel> {
    override fun convert(): StationModel =
        StationModel(
            id = id ?: "",
            name = name ?: ""
        )
}