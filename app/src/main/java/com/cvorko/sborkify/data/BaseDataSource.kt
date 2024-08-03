package com.cvorko.sborkify.data

import com.spotify.protocol.client.CallResult
import com.spotify.protocol.client.Subscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseDataSource {

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    protected suspend inline fun <reified T> fetch(crossinline fetcher: () -> CallResult<T>): Flow<T> {
        return withContext(Dispatchers.IO) {
            return@withContext fetcher().run {
                callbackFlow<T> {
                    setResultCallback { item ->
                        coroutineScope.launch {
                            send(item)
                        }
                    }
                    awaitClose {
                        this@run.cancel()
                    }
                }
            }
        }
    }

    protected fun <T> Subscription<T>.toFlow() = callbackFlow {
        setEventCallback {
            trySend(it)
        }

        awaitClose {
            cancel()
        }
    }

}

