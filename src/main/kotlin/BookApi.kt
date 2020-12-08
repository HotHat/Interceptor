import retrofit2.Call
import retrofit2.http.*

interface BookApi {
    companion object {
        const val BASE_URL = "http://api.tp.test/"
    }

    @GET("/book-chapter-list")
    fun bookList(@Query("id") id: String): Call<RespondBean>

    @POST("/book-chapter-list")
    fun book(@Body raw: JsonRaw): Call<RespondBean>

}