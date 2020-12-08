import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun main() {

//    val a = "id=0026219".hmacSha256("123456")
//    print(a)
//    return;

    val client = OkHttpClient.Builder()
        .addNetworkInterceptor(LoggingInterceptor())
        .build();



    val retrofit = Retrofit.Builder()
        .baseUrl(BookApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();

    val service: BookApi = retrofit.create(BookApi::class.java)

//    val call = service.book(JsonRaw("123445", "nnnnnn"))
    val call = service.bookList("0026219")

    call.enqueue(object : Callback<RespondBean> {
        override fun onFailure(call: Call<RespondBean>?, t: Throwable?) {
            print("aaaaaa")
        }

        override fun onResponse(call: Call<RespondBean>?, response: Response<RespondBean>?) {
            if (response != null) {
                if (response.body() != null) {
                    print(String.format("code: %s, message=%s", response.body()!!.code, response.body()!!.message))
                }
            }
        }

    })
}