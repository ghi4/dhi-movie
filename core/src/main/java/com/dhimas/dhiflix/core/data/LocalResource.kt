package com.dhimas.dhiflix.core.data

import kotlinx.coroutines.flow.*

abstract class LocalResource<ResultType> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        emitAll(loadFromDB().map {
            Resource.Success(it)
        })
    }

    protected abstract fun loadFromDB(): Flow<ResultType>

    fun asFlow(): Flow<Resource<ResultType>> = result
}