package com.cvorko.sborkify.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cvorko.sborkify.data.ContentRepository
import com.cvorko.sborkify.ui.screen.BaseViewState
import com.cvorko.sborkify.ui.screen.ContentViewState
import com.cvorko.sborkify.ui.screen.MainViewState
import com.cvorko.sborkify.ui.screen.ContentItemViewState
import com.spotify.android.appremote.api.ContentApi.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

open class BaseViewModel<VS : BaseViewState>() : ViewModel() {

    protected val viewStateMap = MutableStateFlow<Map<KClass<out VS>, SharedFlow<VS>>>(mapOf())
    val viewStates: StateFlow<Map<KClass<out VS>, SharedFlow<VS>>> = viewStateMap

    protected inline fun <reified V : VS> query(crossinline viewStateMapper: suspend () -> Flow<V>) {
        viewModelScope.launch(Dispatchers.Default) {
            viewStateMap.update { map ->
                map.plus(V::class to
                        viewStateMapper()
                            .shareIn(
                                viewModelScope,
                                SharingStarted.WhileSubscribed(stopTimeoutMillis = 0, replayExpirationMillis = Long.MAX_VALUE),
                                replay = 1
                            )
                )
            }
        }
    }

    @Composable
    inline fun <reified V : VS> getViewState(default: V) =
        viewStates.collectAsState().value
            .getOrDefault(V::class, MutableSharedFlow())
            .collectAsState(initial = default) as State<V>

}