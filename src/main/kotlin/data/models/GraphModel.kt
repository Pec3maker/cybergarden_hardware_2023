package data.models

import data.remote.api.ZimbabweApi

data class GraphModel(
    val graphId: String,
    val name: String,
    val width: Double,
    val height: Double
)

suspend fun GraphModel.getImage(api: ZimbabweApi): ByteArray {
    return api.getGraphImage(
        graphId = this.graphId ?: "",
        width = this.width.toLong() ?: 0,
        height = this.height.toLong() ?: 0
    ).await().bytes()
}
