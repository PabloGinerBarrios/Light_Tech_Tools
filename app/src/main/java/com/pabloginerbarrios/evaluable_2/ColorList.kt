package com.pabloginerbarrios.evaluable_2

import android.content.Intent
import android.graphics.ColorSpace
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ListView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class ColorList : AppCompatActivity() {

    private lateinit var colorListView: ListView
    private lateinit var colorListAdapter: ColorListAdapter
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var deleteButton: CardView
    private lateinit var goBackButton: CardView
    private lateinit var colorPickerButton: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_list)

        databaseHandler = DatabaseHandler(this)
        val colorList = databaseHandler.getAllColors().toMutableList()

        colorListAdapter = ColorListAdapter(this, colorList)

        initComponents()
        colorListView.adapter = colorListAdapter
        initListeners()
    }

    private fun initComponents() {
        colorListView = findViewById(R.id.colorList)
        deleteButton = findViewById(R.id.deleteButton)
        goBackButton = findViewById(R.id.goBackButton)
        colorPickerButton = findViewById(R.id.colorPickerButton)
    }

    private fun initListeners() {
        colorListView.setOnItemClickListener { _, _, position, _ ->
            val selectedColor = colorListAdapter.getItem(position)
            selectedColor?.isSelected = selectedColor?.isSelected!!
            colorListAdapter.notifyDataSetChanged()
        }

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

        deleteButton.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    deleteButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    deleteSelectedColors()
                    Toast.makeText(view.context, "Color eliminado", Toast.LENGTH_LONG).show()
                }
                MotionEvent.ACTION_UP -> deleteButton.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
    }

    private fun deleteSelectedColors() {
        val selectedColors = colorListAdapter.getSelectedColors()
        for(selectedColor in selectedColors) {
            databaseHandler.deleteColor(selectedColor)
        }

        colorListAdapter.removeSelectedColors(databaseHandler)
    }
}