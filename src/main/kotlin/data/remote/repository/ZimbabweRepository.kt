package data.remote.repository

import data.models.*
import data.remote.api.ZimbabweApi
import data.remote.dtos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.GlobalTokenSaver
import utils.transform

class ZimbabweRepository(
    private val zimbabweApi: ZimbabweApi
) {
    suspend fun getStationsList(): List<StationModel> {
        val response = withContext(Dispatchers.IO) {
            val body = BaseRequestDto(
                method = "item.get",
                params = FindItemsBodyDto(
                    output = "extend",
                    hostids = "10602"
                ),
                id = 1
            )
            zimbabweApi.findingStations(body)
        }.await()

        val stationsList = response.result?.transform()?.filter { it.name == "Station" } ?: emptyList()

        return stationsList
    }

    suspend fun getSensorsList(): List<SensorModel> {
        val hostid = getHosts().first().hostid

        val response = withContext(Dispatchers.IO) {
            val body = BaseRequestDto(
                method = "item.get",
                params = FindItemsBodyDto(
                    output = "extend",
                    hostids = hostid
                ),
                id = 1
            )
            zimbabweApi.findingSensors(body)
        }.await()

        return response.result?.transform()?.filter { it.type != SensorType.UNKNOWN } ?: emptyList()
    }

    suspend fun getGraph(graph: GraphModel): ByteArray {
        return zimbabweApi.getGraphImage(
            graphId = graph.graphId ?: "",
            width = graph.width.toLong() ?: 0,
            height = graph.height.toLong() ?: 0
        ).await().bytes()
    }


    suspend fun getGraphs(): List<GraphModel> {
        val hostid = getHosts().firstOrNull()?.hostid

        val response = withContext(Dispatchers.IO) {
            val body = BaseRequestDto(
                method = "graph.get",
                params = GetGraphsDto(
                    output = "extend",
                    hostids = hostid,
                    sortfield = "name"
                ),
                id = 1
            )
            zimbabweApi.getGraphs(body)
        }.await().result?.transform() ?: emptyList()

        return response
    }

    suspend fun getHosts(): List<HostModel> {
        val response = withContext(Dispatchers.IO) {
            val body = BaseRequestDto(
                method = "host.get",
                params = GetHostsDto(
                    output = listOf("hostid", "host"),
                    selectInterfaces = listOf("interfaceid", "ip")
                ),
                id = 2
            )
            zimbabweApi.getHosts(body)
        }.await()

        return response.result?.transform() ?: emptyList()
    }

    suspend fun auth() {
        val response = withContext(Dispatchers.IO) {
            val body = BaseRequestDto(
                method = "user.login",
                params = LoginDto(
                    username = "Admin",
                    password = "zabbix"
                ),
                id = 1
            )
            zimbabweApi.auth(body)
        }.await()

        GlobalTokenSaver.token = response.result
    }
}