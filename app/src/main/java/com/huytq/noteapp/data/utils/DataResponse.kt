package com.huytq.noteapp.data.utils

sealed class DataResponse<T> constructor(val loadDataStatus: LoadDataStatus) {
    class DataEmptyResponse<T> : DataResponse<T>(LoadDataStatus.IDLE)
    class DataErrorResponse<T>(val throwable: Throwable) : DataResponse<T>(LoadDataStatus.ERROR)
    data class DataSuccessResponse<T>(val body: T) : DataResponse<T>(LoadDataStatus.SUCCESS)
    class DataLoadingResponse<T> : DataResponse<T>(LoadDataStatus.LOADING)
    class DataExpireResponse<T> : DataResponse<T>(LoadDataStatus.EXPIRE)
}