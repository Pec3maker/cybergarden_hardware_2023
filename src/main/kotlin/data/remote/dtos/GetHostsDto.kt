package data.remote.dtos

data class GetHostsDto(
    val output: List<String>?,
    val selectInterfaces: List<String>?
)
