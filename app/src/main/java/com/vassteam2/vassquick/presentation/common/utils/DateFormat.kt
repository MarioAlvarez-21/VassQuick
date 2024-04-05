import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun formatDateToShow(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = inputFormat.parse(dateString)

    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DATE, -1)

    val dateCalendar = Calendar.getInstance()
    date?.let {
        dateCalendar.time = it
    }

    val outputFormatTime = SimpleDateFormat("HH:mm")
    val outputFormatDate = SimpleDateFormat("dd/MM/yy HH:mm")
    outputFormatTime.timeZone = TimeZone.getDefault()
    outputFormatDate.timeZone = TimeZone.getDefault()

    return when {
        isSameDay(dateCalendar, today) -> "Hoy ${outputFormatTime.format(date)}"
        isSameDay(dateCalendar, yesterday) -> "Ayer ${outputFormatTime.format(date)}"
        else -> outputFormatDate.format(date)
    }
}

fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun parseDateToCalendar(dateStr: String): Calendar? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return try {
        val date = format.parse(dateStr)
        Calendar.getInstance().apply {
            time = date
        }
    } catch (e: Exception) {
        Log.e("ChatViewModel", "Error parsing date: $dateStr", e)
        null
    }
}