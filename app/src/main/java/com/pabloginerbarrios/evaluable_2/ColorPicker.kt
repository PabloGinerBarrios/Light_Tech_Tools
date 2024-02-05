package com.pabloginerbarrios.evaluable_2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ColorPicker : AppCompatActivity() {

    private lateinit var colorPickerView: ColorPickerView
    private lateinit var l1: LinearLayout
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var colorCode: TextView
    private lateinit var colorName: EditText
    private lateinit var saveButton: CardView
    private lateinit var colorListButton: CardView
    private lateinit var goBackButton: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        initComponents()
        initListeners()

        val brightnessSlideBar = findViewById<BrightnessSlideBar>(R.id.brightnessSlide)
        val alphaSlideBar = findViewById<AlphaSlideBar>(R.id.alphaSlideBar)

        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
        colorPickerView.attachAlphaSlider(alphaSlideBar);

        databaseHandler = DatabaseHandler(this)

        if (ImageSingleton.capturedBitmap != null) {
            Log.d("imagen", "La imagen está en el singleton")
            val bitmapDrawable = BitmapDrawable(resources, ImageSingleton.capturedBitmap)
            val rotatedBitmap = rotateBitmap(bitmapDrawable.bitmap, 90f)
            colorPickerView.setPaletteDrawable(BitmapDrawable(resources, rotatedBitmap));
        }else {
            Log.d("imagen", "La imagen no está en el singleton")
        }
    }

    private fun initComponents() {
        colorPickerView = findViewById(R.id.colorPickerView)
        l1 = findViewById(R.id.colorLayout)
        saveButton = findViewById(R.id.saveButton)
        colorCode = findViewById(R.id.colorCode)
        colorName = findViewById(R.id.colorName)
        colorListButton = findViewById(R.id.listButton)
        goBackButton = findViewById(R.id.goBackButton)
    }

    private fun initListeners() {
        colorPickerView.setColorListener(object : ColorEnvelopeListener {
            override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                l1.setBackgroundColor(envelope.color)
                colorCode.text = "#${envelope.hexCode}"
            }
        })

        saveButton.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    saveButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    if(colorName.text.isNotBlank()) {
                        if(!databaseHandler.isColorNameExists(colorName.text.toString())) {
                            val colorData = ColorData(
                                colorCode.text.toString(),
                                colorName.text.toString(),
                                getCurrentDateTime()
                            )
                            saveColorToDatabase(colorData)
                            Toast.makeText(view.context, "Color \"${colorName.text}\" guardado", Toast.LENGTH_LONG).show()
                            colorName.text.clear()
                            colorName.setHintTextColor(ContextCompat.getColor(view.context, R.color.textColorHint))
                            Log.d("Database", "Color guardado")

                        }else {
                            Toast.makeText(view.context, "Ya existe un color con ese nombre", Toast.LENGTH_LONG).show()
                        }
                    }else {
                        colorName.setHintTextColor(ContextCompat.getColor(view.context, R.color.notSaveBar))
                    }

                }
                MotionEvent.ACTION_UP -> saveButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }

        colorListButton.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    colorListButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, ColorList::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> colorListButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }

        goBackButton.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    goBackButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> goBackButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
    }

    private fun saveColorToDatabase(colorData:ColorData) {
        databaseHandler.addColor(colorData)
    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        // Rotar la imagen 90 grados
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            bitmap,
            0, 0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}