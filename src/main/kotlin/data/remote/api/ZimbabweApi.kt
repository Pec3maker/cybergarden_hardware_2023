package data.remote.api

import data.remote.dtos.*
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ZimbabweApi {
    @POST("/api_jsonrpc.php")
    fun auth(
        @Body body: BaseRequestDto<LoginDto>,
    ): Deferred<BaseResponseDto<String>>

    @POST("/api_jsonrpc.php")
    fun findingSensors(
        @Body body: BaseRequestDto<FindItemsBodyDto>
    ): Deferred<BaseResponseDto<List<SensorDto>>>

    @POST("/api_jsonrpc.php")
    fun getHosts(
        @Body body: BaseRequestDto<GetHostsDto>
    ): Deferred<BaseResponseDto<List<HostDto>>>

    @POST("/api_jsonrpc.php")
    fun findingStations(
        @Body body: BaseRequestDto<FindItemsBodyDto>
    ): Deferred<BaseResponseDto<List<StationDto>>>

    @POST("/api_jsonrpc.php")
    fun getGraphs(
        @Body body: BaseRequestDto<GetGraphsDto>
    ): Deferred<BaseResponseDto<List<GraphDto>>>

    @GET("/chart2.php")
    @Streaming
    fun getGraphImage(
        @Query("graphid") graphId: String,
        @Query("from") from: String = "now-1h",
        @Query("to") to: String = "now",
        @Query("profileIdx") profileIdx: String = "web.graphs.filter",
        @Query("width") width: Long,
        @Query("height") height: Long
    ): Deferred<ResponseBody>
}