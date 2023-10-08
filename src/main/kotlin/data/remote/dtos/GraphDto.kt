package data.remote.dtos

import data.models.GraphModel
import utils.Transformable

data class GraphDto(
    val graphid: String?,
    val name: String?,
    val height: String?,
    val width: String?,
) : Transformable<GraphModel> {
    override fun convert(): GraphModel =
        GraphModel(
            graphId = graphid ?: "",
            name = name ?: "",
            width = width?.toDouble() ?: 0.0,
            height = height?.toDouble() ?: 0.0
        )
}
