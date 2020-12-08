
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.lang.StringBuilder
import java.util.*
import kotlin.String
import kotlin.collections.HashMap


internal class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()


        if (request.method.toUpperCase() == "GET") {
            print("It's GET Request")
            val params = request.url.queryParameterNames
            val map = HashMap<String, String>()
            map["platform"] = "android"
            map["time"] = (System.currentTimeMillis() / 1000).toInt().toString()
            for (p in params) {
                map[p] = request.url.queryParameter(p) ?: ""
            }

            val sign = this.getSignStringBuild(map).hmacSha256("123456")

            map["sign"] = sign

            val query = queryBuild(map)
            val newUrl = request.url.newBuilder().query(query).build()

            var newRequest = request.newBuilder().url(newUrl).build()

            print(
                String.format(
                    "Sending request %s on %s%n%s",
                    newRequest.url, chain.connection(), newRequest.headers
                )
            )

            return chain.proceed(newRequest)

        } else if (request.method.toUpperCase() == "POST") {
            print("It's POST Request")
            // print(request.body)
            val buffer = Buffer()
            val requestBody = request.body as RequestBody
            requestBody.writeTo(buffer)
            val oldParamsJson: String = buffer.readUtf8()
            val gson = Gson()
            val type = object: TypeToken<HashMap<String, Any>>(){}.type
            var map: HashMap<String, Any> = gson.fromJson(oldParamsJson, type) //原始参数


            map["sign"] = this.postSignStringBuild(map).hmacSha256("123456") //重新组装

            val newJsonParams: String = gson.toJson(map) //装换成json字符串

            val builder = request.newBuilder().post(newJsonParams.toRequestBody(requestBody.contentType()));
            builder.header("Content-Length", newJsonParams.toByteArray().size.toString())

            return chain.proceed(builder.build())

        }

        return chain.proceed(request)
    }

    private fun getSignStringBuild(map: HashMap<String, String>) : String {
        val keys = TreeSet(map.keys)
        val paramsStr = StringBuilder()
        for (key in keys) {
            val v = map[key].toString()
            paramsStr.append("$key=$v&")
        }
        val result = paramsStr.toString()
        return result.trimEnd('&')
    }

    private fun postSignStringBuild(map: HashMap<String, Any>) : String {
        val keys = TreeSet(map.keys)
        val paramsStr = StringBuilder()
        for (key in keys) {
            val v = map[key].toString()
            paramsStr.append("$key=$v&")
        }
        val result = paramsStr.toString()
        return result.trimEnd('&')
    }



    private fun queryBuild(map: HashMap<String, String>): String {
        var paramsStr = StringBuilder()
        for (k in map.keys) {
            val v = map[k]
            paramsStr.append("$k=$v&")
        }
        val result = paramsStr.toString()
        return result.trimEnd('&')
    }
}
