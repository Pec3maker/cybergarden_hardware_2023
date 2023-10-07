package data.remote.repository

import data.remote.api.ZimbabweApi
import data.models.SensorData
import data.remote.dtos.SensorDataDto
import kotlinx.coroutines.*

class ZimbabweRepository(
    private val zimbabweApi: ZimbabweApi
) {
    suspend fun getSensorInfo(id: Long): SensorData {
        val response = withContext(Dispatchers.IO) {
//            zimbabweApi.getSensorById(id)
            async {
                SensorDataDto()
            }

        }.await()

        return SensorData.from(response)
    }

    suspend fun getSensorsList(): List<SensorData> {
        val response = withContext(Dispatchers.IO) {
//            zimbabweApi.getSensors()
            async {
                listOf(
                    SensorDataDto(),
                    SensorDataDto(),
                    SensorDataDto()
                )
            }
        }.await()

        return response.map {
            SensorData.from(it)
        }
    }
}