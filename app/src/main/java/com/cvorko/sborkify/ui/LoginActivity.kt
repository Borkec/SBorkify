package com.cvorko.sborkify.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.cvorko.sborkify.di.clientId
import com.cvorko.sborkify.di.dataSourceModule
import com.cvorko.sborkify.di.redirectUri
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val REQUEST_CODE = 1337
class LoginActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()

        initConnection(null)
    }

    private fun initConnection(accessToken: String?) {
        // Set the connection parameters
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        if(accessToken == null) {
            initialAuthorization()
            return
        }

        lifecycleScope.launch {
            try {
                // there has to be a better way of doing this, check if you can inject a lateinit to a koin module
                getKoin().loadModules(listOf(dataSourceModule(connectToSpotify(connectionParams), accessToken)))

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            } catch (e: RuntimeException) {
                initialAuthorization()
            }
        }
    }

    private fun initialAuthorization() {
        val builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this@LoginActivity, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    initConnection(response.accessToken)
                }

                AuthorizationResponse.Type.ERROR -> {
                    Log.w("Authorization request error:", "${response.error}")
                }

                else -> {}
            }
        }
    }
}

private suspend fun Context.connectToSpotify(connectionParams: ConnectionParams): SpotifyAppRemote = suspendCoroutine { cont ->

    SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
        override fun onConnected(appRemote: SpotifyAppRemote) {
            Log.d("MainActivity", "Connected! Yay!")

            cont.resume(appRemote)
        }

        override fun onFailure(throwable: Throwable) {
            Log.e("MainActivity", throwable.message, throwable)
            cont.resumeWithException(throwable)
        }
    })
}