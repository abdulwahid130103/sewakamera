package com.polinema.sewakamera.Helper

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.R
import org.json.JSONArray
import org.json.JSONException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataHelper(private val context: Context) {


    fun showConfirmationDialog(
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        positiveAction: DialogInterface.OnClickListener,
        negativeAction: DialogInterface.OnClickListener?
    ) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, positiveAction)
            .setNegativeButton(negativeText, negativeAction)
            .create()
        dialog.show()
    }

    fun formatRupiah(value: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(value.toLong())
    }

    fun unformatRupiah(formattedValue: String): Int {
        val numericOnly = formattedValue.replace(Regex("[^\\d]"), "")
        return numericOnly.toInt()
    }


    fun toast_custom_alert(ctx: Context,pesan: String, message: String) {
        val toast = Toast(ctx)
        val layout: View = View.inflate(ctx, R.layout.custom_toast_alert, null)
        val messageToast = layout.findViewById<TextView>(R.id.messageToast)
        val imageToast = layout.findViewById<ImageView>(R.id.imageToast)
        val backgroundContainer = layout.findViewById<LinearLayout>(R.id.custom_toast_container)



        val background = backgroundContainer.background
        if (background is GradientDrawable) {
            if(pesan == "success"){
                messageToast.text = message
                imageToast.setImageResource(R.drawable.baseline_done_24)
                background.setColor(Color.parseColor("#00916e"))
            }else if(pesan == "warning"){
                messageToast.text = message
                imageToast.setImageResource(R.drawable.baseline_warning_24)
                background.setColor(Color.parseColor("#f7b801"))
            }else if(pesan == "notifikasi"){
                messageToast.text = message
                imageToast.setImageResource(R.drawable.baseline_notifications_24)
                background.setColor(Color.parseColor("#0077b6"))
            }else if(pesan == "error"){
                messageToast.text = message
                imageToast.setImageResource(R.drawable.baseline_clear_24)
                background.setColor(Color.parseColor("#c1121f"))
            }
        } else {
            Log.e("ToastError", "Background is not a GradientDrawable")
        }

        toast.apply {
            setGravity(Gravity.TOP, 0, 100)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }

    fun showDatePicker(context: Context, onDateSetListener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, onDateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis // Set the minimum date to today
        datePickerDialog.show()
    }

    fun calculateEndDate(startDateStr: String, daysToAdd: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val startDate = dateFormat.parse(startDateStr)
            startDate?.let {
                val calendar = Calendar.getInstance()
                calendar.time = startDate
                calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)
                return dateFormat.format(calendar.time)
            } ?: run {
                Log.e("calculateEndDate", "Failed to parse the start date: $startDateStr")
            }
        } catch (e: Exception) {
            Log.e("calculateEndDate", "Error processing the date", e)
        }
        return "Invalid date"
    }

    fun showTimePicker(context: Context, onTimeSetListener: TimePickerDialog.OnTimeSetListener) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val defaultHour = 7
        val defaultMinute = 0

        TimePickerDialog(context, onTimeSetListener, defaultHour, defaultMinute, true).show()
    }

    fun setDefaultTime(editText: EditText) {
        editText.setText("07.00")
    }

    fun pantauInputan(editText: EditText, onUpdate: Runnable) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nothing needed here
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().isNotEmpty()) {
                    try {
                        onUpdate.run()
                    } catch (e: NumberFormatException) {
                        Log.e("NumberFormatException", "Error parsing integer from jumlahHariBooking")
                    }
                }
            }
        })
    }


    fun setDefaultHari(editText: EditText) {
        editText.setText("1")
    }

    fun setDefaultDate(editText: EditText) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1) // Add 1 day to the current date

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        editText.setText(formattedDate)
    }


}