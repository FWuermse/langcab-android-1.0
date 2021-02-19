import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.langcab.app.Word
import java.io.ByteArrayInputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * Make a GET request and return a parsed object from JSON.
 *
 * @param url URL of the request to make
 * @param method REST method e.G. GET, POST, DELETE
 * @param clazz Object class for parsing Response
 * @param headers Map of request headers
 */
class GsonRequest<T>(
        url: String,
        method: Int,
        private val clazz: Class<T>,
        private val headers: MutableMap<String, String>?,
        private val body: Word,
        private val listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
) : Request<T>(method, url, errorListener) {
    constructor(url: String,
                method: Int,
                clazz: Class<T>,
                headers: MutableMap<String, String>?,
                listener: Response.Listener<T>,
                errorListener: Response.ErrorListener) : this(url, method, clazz, headers, Word(), listener, errorListener)
    private val gson = Gson()


    override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()

    override fun getBody(): ByteArray = gson.toJson(body).toByteArray()

    override fun deliverResponse(response: T) = listener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                    response?.data ?: ByteArray(0),
                    Charset.forName(parseCharset(response?.headers)))
            Response.success(
                    gson.fromJson(json, clazz),
                    parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}
