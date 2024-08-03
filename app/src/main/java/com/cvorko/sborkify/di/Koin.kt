package com.cvorko.sborkify.di

import com.cvorko.sborkify.data.CallDataSource
import com.cvorko.sborkify.data.CallDataSourceImpl
import com.cvorko.sborkify.data.ContentDataSource
import com.cvorko.sborkify.data.ContentDataSourceImpl
import com.cvorko.sborkify.data.ContentRepository
import com.cvorko.sborkify.data.ContentRepositoryImpl
import com.cvorko.sborkify.data.ImageDataSource
import com.cvorko.sborkify.data.ImageDataSourceImpl
import com.cvorko.sborkify.data.PlayerController
import com.cvorko.sborkify.data.PlayerControllerImpl
import com.cvorko.sborkify.data.SpotifyAppService
import com.cvorko.sborkify.ui.viewmodel.AlbumViewModel
import com.cvorko.sborkify.ui.viewmodel.MainActivityViewModel
import com.cvorko.sborkify.ui.viewmodel.PlayerViewModel
import com.google.gson.GsonBuilder
import com.spotify.android.appremote.api.SpotifyAppRemote
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val clientId = "242263caaadd42fa822829c78180b524"
const val redirectUri = "https://com.cvorko.sborkify/callback"
val BASE_URL = "https://api.spotify.com/v1/"

val appModule = module {
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { parameters -> AlbumViewModel(playlistId = parameters.get(), get(), get()) }
    viewModel { PlayerViewModel(get(), get()) }
}

fun dataSourceModule(spotifyAppRemote: SpotifyAppRemote, accessToken: String) = module {
    single<ImageDataSource> { ImageDataSourceImpl(spotifyAppRemote.imagesApi) }
    single<ContentDataSource> { ContentDataSourceImpl(spotifyAppRemote.contentApi) }
    single<PlayerController> { PlayerControllerImpl(spotifyAppRemote.playerApi) }
    single<ContentRepository> { ContentRepositoryImpl(get(), get()) }
    single<CallDataSource> { CallDataSourceImpl(get()) }

    single<SpotifyAppService> {

        val client = OkHttpClient.Builder()
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(this)
        }
        client.addInterceptor(createInterceptor(accessToken))
        client.followRedirects(false)

        val gson = GsonBuilder().setLenient().create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create<SpotifyAppService>()
    }
}

fun createInterceptor(token: String) = Interceptor { chain ->
    val request: Request = chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
    chain.proceed(request)
}

