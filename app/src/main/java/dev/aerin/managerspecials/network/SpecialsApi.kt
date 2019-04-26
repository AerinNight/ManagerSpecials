package dev.aerin.managerspecials.network

import android.net.Uri
import com.google.gson.GsonBuilder
import dev.aerin.managerspecials.models.SpecialsPage
import dev.aerin.managerspecials.utils.UriTypeAdapter
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SpecialsApi {

    companion object {

        private const val ENDPOINT = "https://prestoq.com/"

        fun create(): SpecialsApi {

            val gson = GsonBuilder()
                .registerTypeAdapter(Uri::class.java, UriTypeAdapter())

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .baseUrl(ENDPOINT)
                .build()

            return retrofit.create(SpecialsApi::class.java)
        }
    }

    @GET("android-coding-challenge")
    fun getSpecials(): Single<SpecialsPage>
}
