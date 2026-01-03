import android.util.Log
import retrofit2.HttpException

suspend inline fun <T> safeApiCall(
    tag: String,
    crossinline block: suspend () -> T
): T? {
    return try {
        block()
    } catch (e: HttpException) {
        val body = e.response()?.errorBody()?.string()
        Log.e(tag, "HTTP ${e.code()} body=$body", e)
        null
    } catch (e: Exception) {
        Log.e(tag, "Error", e)
        null
    }
}
