package data.remote.api

import data.remote.dtos.SensorDataDto
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface ZimbabweApi {
    @GET("sensors")
    fun getSensors(): Deferred<List<SensorDataDto>>

    @GET("sensors/{id}")
    fun getSensorById(
        @Path("id") id: Long
    ): Deferred<SensorDataDto>
}