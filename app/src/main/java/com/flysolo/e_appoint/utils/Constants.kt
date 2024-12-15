package com.flysolo.e_appoint.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.flysolo.e_appoint.models.appointments.AppointmentStatus
import com.flysolo.e_appoint.models.appointments.Appointments
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.Pie
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun generateRandomString(length: Int = 15): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var result = ""

    for (i in 0 until length) {
        val randomIndex = (Math.random() * characters.length).toInt()
        result += characters[randomIndex]
    }

    return result
}


fun generateRandomNumberString(length: Int = 15): String {
    val characters = "0123456789"
    var result = ""

    for (i in 0 until length) {
        val randomIndex = (Math.random() * characters.length).toInt()
        result += characters[randomIndex]
    }

    return result
}


fun Double.toPhp(): String {
    return "₱ %.2f".format(this)
}



fun Date.display(): String {
    val formatter = SimpleDateFormat("MMM dd, hh:mm aa", Locale.getDefault())
    return formatter.format(this)
}

fun Context.shortToast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}



fun List<Appointments>.toPieData(): List<Pie> {
    return AppointmentStatus.entries.map { status ->
        Pie(
            label = status.name,
            data = this.filter { it.status == status }.size.toDouble(),
            color = status.getColor() ?: Color.Black,
            selectedColor = status.getColor().copy(alpha = 0.7f) ?: Color.Gray
        )
    }
}




data class LineChartData(
    val label : String,
    val data : List<Bars.Data>,
    val color : Color,
)



fun AppointmentStatus.getColor(): Color {
    return when (this) {
        AppointmentStatus.PENDING -> Color(0xFFFFA500) // Orange
        AppointmentStatus.CONFIRMED -> Color(0xFF00FF00) // Green
        AppointmentStatus.DONE -> Color(0xFF00FFFF) // Cyan
        AppointmentStatus.CANCELLED -> Color(0xFFFF4500) // Red-Orange
        AppointmentStatus.DECLINED -> Color(0xFF808080) // Gray
    }
}

fun List<Appointments>.toLineChartByStatusAndMonths(): List<LineChartData> {

    val statusColors = mapOf(
        AppointmentStatus.PENDING to Color(0xFFFFA500),
        AppointmentStatus.CONFIRMED to Color(0xFF00FF00),
        AppointmentStatus.DONE to Color(0xFF00FFFF),
        AppointmentStatus.CANCELLED to Color(0xFFFF4500),
        AppointmentStatus.DECLINED to Color(0xFF808080)
    )


    val groupedData = this.groupBy { it.status }.mapValues { entry ->
        entry.value.groupBy {
            val calendar = Calendar.getInstance()
            calendar.time = it.createdAt
            "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}"
        }.mapValues { monthGroup ->
            monthGroup.value.size.toDouble()
        }
    }


    val lineChartDataList = mutableListOf<LineChartData>()
    for ((status, monthlyData) in groupedData) {
        // Sort data by month-year keys to ensure chronological order
        val sortedData = monthlyData.toSortedMap(compareBy { it })

        // Convert sorted data to a list of Bars.Data
        val barDataList = sortedData.map { (monthYear, count) ->
            Bars.Data(
                label = monthYear,
                value = count,
                color = SolidColor(statusColors[status] ?: Color.Black)
            )
        }

        lineChartDataList.add(
            LineChartData(
                label = status.name,
                data = barDataList,
                color = statusColors[status] ?: Color.Black // Default to black if color not found
            )
        )
    }

    return lineChartDataList
}

fun List<Appointments>.toBarData(): List<Bars> {
    // Map colors to statuses
    val statusColors = mapOf(
        AppointmentStatus.PENDING to AppointmentStatus.PENDING.getColor(),
        AppointmentStatus.CONFIRMED to AppointmentStatus.CONFIRMED.getColor(),
        AppointmentStatus.DONE to AppointmentStatus.DONE.getColor(),
        AppointmentStatus.CANCELLED to AppointmentStatus.CANCELLED.getColor(),
        AppointmentStatus.DECLINED to AppointmentStatus.DECLINED.getColor()
    )

    // Group appointments by status and month
    val groupedData = this.groupBy { appointment ->
        val calendar = Calendar.getInstance().apply { time = appointment.createdAt }
        // Get the abbreviated month name (MMM format)
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) // MMM format
        month // Only use the month name (e.g., "Jan", "Feb")
    }.mapValues { entry ->
        entry.value.groupBy { it.status }.mapValues { statusGroup ->
            statusGroup.value.size.toDouble() // Count of appointments per status
        }
    }

    // Create Bars for each month with corresponding status counts
    return groupedData.map { (month, statusCounts) ->
        Bars(
            label = month, // Use the month name (e.g., "Jan")
            values = AppointmentStatus.values().map { status ->
                Bars.Data(
                    label = status.name,
                    value = statusCounts[status] ?: 0.0,
                    color = SolidColor(statusColors[status] ?: Color.Black)
                )
            }
        )
    }
}
