package data.remote.dtos

import data.models.HostModel
import utils.Transformable

data class HostDto(
    val hostid: String?,
    val host: String?
) : Transformable<HostModel> {
    override fun convert(): HostModel = HostModel(
        hostid = hostid ?: "",
        host = host ?: ""
    )

}
