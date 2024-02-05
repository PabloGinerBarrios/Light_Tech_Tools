package com.pabloginerbarrios.evaluable_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var colorPickerButton: androidx.cardview.widget.CardView
    private lateinit var cameraButton: androidx.cardview.widget.CardView
    private lateinit var colorListButton: androidx.cardview.widget.CardView
    private lateinit var dmxCalculatorButton: androidx.cardview.widget.CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImageSingleton.capturedBitmap = null

        initComponents()
        initListeners()
    }

    override fun onRestart() {
        super.onRestart()
        ImageSingleton.capturedBitmap = null
    }

    fun initComponents() {
        colorPickerButton = findViewById(R.id.cardColorPicker)
        cameraButton = findViewById(R.id.cardCamera)
        colorListButton = findViewById(R.id.cardColorList)
        dmxCalculatorButton = findViewById(R.id.cardDmxCalculator)
    }

    fun initListeners() {
        colorPickerButton.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    colorPickerButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, ColorPicker::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> colorPickerButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }

        cameraButton.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cameraButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, Camara::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> cameraButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
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

        dmxCalculatorButton.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    colorPickerButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, DmxCalculator::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> colorPickerButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
    }
}