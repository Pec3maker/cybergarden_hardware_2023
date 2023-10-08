package data.remote.dtos

data class BaseRequestDto<Params>(
    val jsonrpc: String = "2.0",
    val method: String?,
    val params: Params?,
    val id: Long?
)