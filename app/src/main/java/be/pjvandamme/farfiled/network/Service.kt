package be.pjvandamme.farfiled.network

import be.pjvandamme.farfiled.model.Face
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val ADORABLE_BASE_URL = "https://api.adorable.io/"
private const val UIFACES_BASE_URL = "https://uifaces.co/"
private const val UIFACES_API_KEY = "X-API-KEY: 1ec86cd6e5a2045ee4f210ccc68c84"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/*
 *  ADORABLE
 */

private val adorableRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(ADORABLE_BASE_URL)
    .build()

interface AdorableAvatarApiService{
    @GET("avatars/list")
    fun getFacialFeatures():
            Deferred<AdorableAvatarFeatures>
}

object AdorableAvatarApi{
    val retrofitService: AdorableAvatarApiService by lazy{
        adorableRetrofit.create(AdorableAvatarApiService::class.java)
    }
}


/*
 *  UI FACES
 */

private val uiFacesRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(UIFACES_BASE_URL)
    .build()

enum class GENDER{
    MALE,
    FEMALE
}

enum class HAIRCOLOR{
    BLACK,
    BROWN,
    BLOND,
    RED
}

enum class EMOTION{
    HAPPINES,
    NEUTRAL
}

interface UIFacesApiService{
    @Headers(UIFACES_API_KEY)
    @GET("api")
    fun getUIFaces(@Query("limit") limit: Int,
                   @Query("gender[]") gender: GENDER?,
                   @Query("from_age") from_age: Int?,
                   @Query("to_age") to_age: Int?,
                   @Query("hairColor[]") hairColor: HAIRCOLOR?,
                   @Query("emotion[]") emotion: EMOTION?):
        Deferred<List<NetworkFace>>

    @Headers("X-API-KEY: 1ec86cd6e5a2045ee4f210ccc68c84")
    @GET("api?random")
    fun getRandomUIFaces(@Query("limit") limit: Int):
            Deferred<List<NetworkFace>>
}

object UIFacesApi{
    val retrofitService: UIFacesApiService by lazy{
        uiFacesRetrofit.create(UIFacesApiService::class.java)
    }
}