package com.pabloginerbarrios.evaluable_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class DmxCalculator : AppCompatActivity() {

    private lateinit var btnUpHundred : CardView
    private lateinit var btnUpTen : CardView
    private lateinit var btnUpUnit: CardView
    private lateinit var btnDownHundred : CardView
    private lateinit var btnDownTen : CardView
    private lateinit var btnDownUnit : CardView
    private lateinit var textHundred : TextView
    private lateinit var textTen : TextView
    private lateinit var textUnit : TextView
    private lateinit var imageUpHundred : ImageView
    private lateinit var imageUpDec : ImageView
    private lateinit var imageUpUnit : ImageView
    private lateinit var imageDownHundred : ImageView
    private lateinit var imageDownDec : ImageView
    private lateinit var imageDownUnit : ImageView
    private lateinit var pin1But : LinearLayoutCompat
    private lateinit var pin2But : LinearLayoutCompat
    private lateinit var pin3But : LinearLayoutCompat
    private lateinit var pin4But : LinearLayoutCompat
    private lateinit var pin5But : LinearLayoutCompat
    private lateinit var pin6But : LinearLayoutCompat
    private lateinit var pin7But : LinearLayoutCompat
    private lateinit var pin8But : LinearLayoutCompat
    private lateinit var pin9But : LinearLayoutCompat
    private val pinButtons = mutableListOf<LinearLayoutCompat>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dmx_calculator)

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnUpHundred = findViewById(R.id.btnCentUp)
        btnUpTen = findViewById(R.id.btnDecUp)
        btnUpUnit = findViewById(R.id.btnUnitUp)
        btnDownHundred = findViewById(R.id.btnCentDown)
        btnDownTen = findViewById(R.id.btnDecDown)
        btnDownUnit = findViewById(R.id.btnUnitDown)
        textHundred = findViewById(R.id.centNum)
        textTen = findViewById(R.id.decNum)
        textUnit = findViewById(R.id.unitNum)
        imageUpHundred = findViewById(R.id.imageUpHundred)
        imageUpDec = findViewById(R.id.imageUpDec)
        imageUpUnit = findViewById(R.id.imageUpUnit)
        imageDownHundred = findViewById(R.id.imageDownHundred)
        setColorDown(imageDownHundred)
        imageDownDec = findViewById(R.id.imageDownDec)
        setColorDown(imageDownDec)
        imageDownUnit = findViewById(R.id.imageDownUnit)
        setColorDown(imageDownUnit)
        pin1But = findViewById(R.id.pin1But)
        pinButtons.add(pin1But)
        pin2But = findViewById(R.id.pin2But)
        pinButtons.add(pin2But)
        pin3But = findViewById(R.id.pin3But)
        pinButtons.add(pin3But)
        pin4But = findViewById(R.id.pin4But)
        pinButtons.add(pin4But)
        pin5But = findViewById(R.id.pin5But)
        pinButtons.add(pin5But)
        pin6But = findViewById(R.id.pin6But)
        pinButtons.add(pin6But)
        pin7But = findViewById(R.id.pin7But)
        pinButtons.add(pin7But)
        pin8But = findViewById(R.id.pin8But)
        pinButtons.add(pin8But)
        pin9But = findViewById(R.id.pin9But)
        pinButtons.add(pin9But)
    }

    private fun initListeners() {
        btnUpHundred.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnUpHundred.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    var numberH = textHundred.text.toString().toInt()
                    if(numberH == 0) {
                        setColorUp(imageDownHundred)
                    }
                    if(numberH < 5) {
                        numberH++
                        textHundred.text = numberH.toString()
                        if(numberH == 5) {
                            setColorDown(imageUpHundred)
                            if(textTen.text.toString().toInt() >= 1){
                                textTen.text = "1"
                                setColorDown(imageUpDec)
                            }
                            if(textUnit.text.toString().toInt() >= 1){
                                textUnit.text = "1"
                                setColorDown(imageUpUnit)
                            }
                        }
                    }
                    numberCalc()
                }
                MotionEvent.ACTION_UP -> btnUpHundred.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
        btnUpTen.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnUpTen.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val numberH = textHundred.text.toString().toInt()
                    var numberT = textTen.text.toString().toInt()
                    val numberU = textUnit.text.toString().toInt()
                    if(numberT == 0) {
                        setColorUp(imageDownDec)
                    }
                    if(numberH == 5 && numberT < 1 && numberU > 1){
                        numberT++
                        textTen.text = numberT.toString()
                        textUnit.text = "1"
                        setColorDown(imageUpDec)
                        setColorDown(imageUpUnit)
                    }else if (numberH == 5 && numberT < 1 && numberU <= 1) {
                        numberT++
                        textTen.text = numberT.toString()
                        setColorDown(imageUpDec)
                    }else if (numberH != 5 && numberT < 9) {
                        numberT++
                        textTen.text = numberT.toString()
                    }
                    if(numberT == 9){
                        setColorDown(imageUpDec)
                    }
                    numberCalc()
                }
                MotionEvent.ACTION_UP -> btnUpTen.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
        btnUpUnit.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnUpUnit.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val numberH = textHundred.text.toString().toInt()
                    val numberT = textTen.text.toString().toInt()
                    var numberU = textUnit.text.toString().toInt()
                    if(numberU == 0){
                        setColorUp(imageDownUnit)
                    }
                    if(numberH == 5 && numberT == 1 && numberU < 1){
                        numberU++
                        textUnit.text = numberU.toString()
                        setColorDown(imageUpUnit)
                    }else if(numberH == 5 && numberT < 1 && numberU < 9){
                        numberU++
                        textUnit.text = numberU.toString()
                    }else if (numberH != 5 && numberU < 9) {
                        numberU++
                        textUnit.text = numberU.toString()
                    }
                    if(numberU == 9){
                        setColorDown(imageUpUnit)
                    }
                    numberCalc()
                }
                MotionEvent.ACTION_UP -> btnUpUnit.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
        btnDownHundred.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnDownHundred.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    var number = textHundred.text.toString().toInt()
                    val numberT = textTen.text.toString().toInt()
                    val numberU = textUnit.text.toString().toInt()
                    if((number == 5 && numberT == 1 && numberU == 1)) {
                        setColorUp(imageUpHundred)
                        setColorUp(imageUpDec)
                        setColorUp(imageUpUnit)
                    }else if (number == 5 && numberT == 1) {
                        setColorUp(imageUpHundred)
                        setColorUp(imageUpDec)
                    }else if(number == 5) {
                        setColorUp(imageUpHundred)
                    }
                    if(number > 0) {
                        number--
                        textHundred.text = number.toString()
                        if(number == 0) {
                            setColorDown(imageDownHundred)
                        }
                    }
                    numberCalc()
                }
                MotionEvent.ACTION_UP -> btnDownHundred.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
        btnDownTen.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnDownTen.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val numberH = textHundred.text.toString().toInt()
                    var number = textTen.text.toString().toInt()
                    val numberU = textUnit.text.toString().toInt()
                    if (number == 9) {
                        setColorUp(imageUpDec)
                    }
                    if (numberH == 5 && number == 1 && numberU == 1){
                        setColorUp(imageUpDec)
                        setColorUp(imageUpUnit)
                    }else if (numberH == 5 && number == 1) {
                        setColorUp(imageUpDec)
                    }
                    if(number > 0){
                        number--
                        textTen.text = number.toString()
                        if(number == 0) {
                            setColorDown(imageDownDec)
                        }
                    }
                    numberCalc()
                }
                MotionEvent.ACTION_UP -> btnDownTen.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
        btnDownUnit.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnDownUnit.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val numberH = textHundred.text.toString().toInt()
                    var number = textUnit.text.toString().toInt()
                    if((numberH == 5) || number == 9 ){
                        setColorUp(imageUpUnit)
                    }
                    if(number > 0){
                        number--
                        textUnit.text = number.toString()
                        if(number == 0){
                            setColorDown(imageDownUnit)
                        }
                    }
                    numberCalc()
                }
                MotionEvent.ACTION_UP -> btnDownUnit.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
        pin1But.setOnClickListener {
            changeGravityPin(pin1But)
            calculateNumber()
        }
        pin2But.setOnClickListener {
            changeGravityPin(pin2But)
            calculateNumber()
        }
        pin3But.setOnClickListener {
            changeGravityPin(pin3But)
            calculateNumber()
        }
        pin4But.setOnClickListener {
            changeGravityPin(pin4But)
            calculateNumber()
        }
        pin5But.setOnClickListener {
            changeGravityPin(pin5But)
            calculateNumber()
        }
        pin6But.setOnClickListener {
            changeGravityPin(pin6But)
            calculateNumber()
        }
        pin7But.setOnClickListener {
            changeGravityPin(pin7But)
            calculateNumber()
        }
        pin8But.setOnClickListener {
            changeGravityPin(pin8But)
            calculateNumber()
        }
        pin9But.setOnClickListener {
            changeGravityPin(pin9But)
            calculateNumber()
        }
    }

    private fun setColorDown (image : ImageView) {
        image.setImageAlpha(40)
    }

    private fun setColorUp (image: ImageView) {
        image.setImageAlpha(255)
    }

    private fun numberCalc() {
        val numberH = textHundred.text.toString().toInt()
        val numberT = textTen.text.toString().toInt()
        val numberU = textUnit.text.toString().toInt()

        val number = (numberH * 100) + (numberT * 10) + numberU
        Log.d("numero decimal", number.toString())
        val binaryString = numberToBinary(number)
        Log.d("numero binario", binaryString)
        changeGravityButtons(binaryString)
    }

    private fun numberToBinary(number:Int) : String {
        var binary = ""
        if(number == 0){
            binary = "000000000"
        }else{
            var num = number
            while (num > 0) {
                val remainder = num % 2
                binary = remainder.toString() + binary
                num /= 2
            }
        }
        val paddingZeros = 9 - binary.length
        val paddedBinary = "0".repeat(paddingZeros) + binary
        val reversedBinary = paddedBinary.reversed()

        return reversedBinary
    }

    private fun changeGravityButtons(binaryString:String) {
        for(i in binaryString.indices) {
            if(binaryString[i] == '1') {
                pinButtons[i].gravity = Gravity.TOP
            }else{
                pinButtons[i].gravity = Gravity.BOTTOM
            }
        }
    }

    private fun changeGravityPin(pin : LinearLayoutCompat) {
        if(pin.gravity and Gravity.TOP == Gravity.TOP) {
            Log.d("Gravedad", "La gravedad cambia de top a bottom")
            Log.d("gravedad", pin.gravity.toString())
            pin.gravity = Gravity.BOTTOM
        }else {
            Log.d("Gravdad", "La gravedad cambia de bottom a top")
            Log.d("gravedad", pin.gravity.toString())
            pin.gravity = Gravity.TOP
        }
    }

    private fun calculateNumber() {
        var binary = ""
        for(pin in pinButtons) {
            if(pin.gravity and Gravity.TOP == Gravity.TOP){
                binary+='1'
            }else {
                binary+='0'
            }
        }
        Log.d("binario", binary)
        val realBinary = binary.reversed()
        Log.d("Binario Real", realBinary)
        binaryToDEcimal(realBinary)
    }

    private fun binaryToDEcimal(binary : String) {
        val decimalNumber = Integer.parseInt(binary, 2)
        Log.d("Decimal", decimalNumber.toString())

        val units = decimalNumber % 10
        textUnit.text = units.toString()
        Log.d("unidades", units.toString())

        val tens = (decimalNumber / 10) % 10
        textTen.text = tens.toString()
        Log.d("decenas", tens.toString())

        val hundreds = decimalNumber / 100
        textHundred.text = hundreds.toString()
        Log.d("centenas", hundreds.toString())

        if(hundreds == 5){
            setColorDown(imageUpHundred)
            setColorUp(imageDownHundred)
            if(tens == 1 && units == 1) {
                setColorDown(imageUpDec)
                setColorUp(imageDownDec)
                setColorDown(imageUpUnit)
                setColorUp(imageDownUnit)
            }
            if(tens == 1 && units != 1) {
                setColorDown(imageUpDec)
                setColorUp(imageDownDec)
                setColorUp(imageUpUnit)
                setColorDown(imageDownUnit)
            }
            if(tens != 1 && units == 0) {
                setColorUp(imageUpDec)
                setColorDown(imageDownDec)
                setColorUp(imageUpUnit)
                setColorDown(imageDownUnit)
            }
            if(tens != 1 && units != 0) {
                setColorUp(imageUpDec)
                setColorDown(imageDownDec)
                setColorUp(imageUpUnit)
                setColorUp(imageDownUnit)
            }
        }else if(hundreds == 0) {
            setColorDown(imageDownHundred)
            setColorUp(imageUpHundred)
            if(tens == 0) {
                setColorDown(imageDownDec)
                setColorUp(imageUpDec)
            }else if(tens == 9) {
                setColorDown(imageUpDec)
                setColorUp(imageDownDec)
            }else if(tens > 0) {
                setColorUp(imageDownDec)
                setColorUp(imageUpDec)
            }
            if(units == 0) {
                setColorDown(imageDownUnit)
                setColorUp(imageUpUnit)
            }else if(units == 9) {
                setColorDown(imageUpUnit)
                setColorUp(imageDownUnit)
            }else if(units > 0) {
                setColorUp(imageUpUnit)
                setColorUp(imageDownUnit)
            }
        }else if (hundreds > 0 && hundreds < 5) {
            setColorUp(imageDownHundred)
            setColorUp(imageUpHundred)
            if(tens == 0) {
                setColorDown(imageDownDec)
                setColorUp(imageUpDec)
            }else if(tens == 9) {
                setColorDown(imageUpDec)
                setColorUp(imageDownDec)
            }else if(tens > 0) {
                setColorUp(imageDownDec)
                setColorUp(imageUpDec)
            }
            if(units == 0) {
                setColorDown(imageDownUnit)
                setColorUp(imageUpUnit)
            }else if(units == 9) {
                setColorDown(imageUpUnit)
                setColorUp(imageDownUnit)
            }else if(units > 0) {
                setColorUp(imageUpUnit)
                setColorUp(imageDownUnit)
            }
        }
    }
}