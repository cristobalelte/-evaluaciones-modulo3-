import com.example.ama.core.network.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
// ...
//Este archivo no se usa con el ProductData.kt
object NetworkModule {
    private val moshi = Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
//        .baseUrl("http://54.243.16.169:3000/")    //http://44.222.218.77:3000/api#/ debe terminar con /
        .baseUrl("http://3.128.184.226:3000/")
        //Ultima version: http://44.222.218.77:3000/api#/
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(OkHttpClient.Builder().build())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}