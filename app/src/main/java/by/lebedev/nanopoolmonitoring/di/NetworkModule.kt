package by.lebedev.nanopoolmonitoring.di

import by.lebedev.nanopoolmonitoring.BuildConfig
import by.lebedev.nanopoolmonitoring.network.CcService
import by.lebedev.nanopoolmonitoring.network.CoinGeckoService
import by.lebedev.nanopoolmonitoring.network.NanopoolService
import by.lebedev.nanopoolmonitoring.network.interceptors.DefaultInterceptor
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.Timeouts
import by.lebedev.nanopoolmonitoring.utils.setupTimeout
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    @Singleton
    @Provides
    fun provideDefaultInterceptor(): DefaultInterceptor {
        return DefaultInterceptor()
    }

    @Singleton
    @Provides
    fun provideMoshiConverter(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

    @Singleton
    @Provides
    fun provideHttpClient(
        defaultInterceptor: DefaultInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(defaultInterceptor).apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }
            .setupTimeout(Timeouts.Default)
            .build()
    }

    @Singleton
    @Nanopool
    @Provides
    fun provideNanopoolRetrofit(
        httpClient: OkHttpClient,
        moshiConverter: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${BuildConfig.API_URL_NANOPOOL}/")
            .addConverterFactory(moshiConverter)
            .client(httpClient)
            .build()
    }

    @Singleton
    @CC
    @Provides
    fun provideCcRetrofit(
        httpClient: OkHttpClient,
        moshiConverter: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL_CC)
            .addConverterFactory(moshiConverter)
            .client(httpClient)
            .build()
    }

    @Singleton
    @CoinGecko
    @Provides
    fun provideCoinGeckoRetrofit(
        httpClient: OkHttpClient,
        moshiConverter: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL_COINGECKO)
            .addConverterFactory(moshiConverter)
            .client(httpClient)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {
    @Singleton
    @Nanopool
    @Provides
    fun provideNanopoolService(
        @Nanopool
        retrofit: Retrofit
    ): NanopoolService = retrofit.create()

    @Singleton
    @CC
    @Provides
    fun provideCcService(
        @CC
        retrofit: Retrofit
    ): CcService = retrofit.create()

    @Singleton
    @CoinGecko
    @Provides
    fun provideCoinGeckoService(
        @CoinGecko
        retrofit: Retrofit
    ): CoinGeckoService = retrofit.create()
}