package com.pabloginerbarrios.evaluable_2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.camera2.CameraCharacteristics
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.pabloginerbarrios.evaluable_2.databinding.ActivityCamaraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Camara : AppCompatActivity() {

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val REQUEST_CODE_PERMISSIONS = 10
    }

    lateinit var binding: ActivityCamaraBinding
    private var preview : Preview? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK //Variable para la cámara seleccionada. Empieza con la trasera.
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCamaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButtonListeners()

        cameraExecutor = Executors.newSingleThreadExecutor()

        //Condicional para el control de permisos. Si están los permisos dados se inicia la cámara. Si no, se inicia la función requestPermissions para pedirlos.
        if(allPermissonsGranted()) startCamera()
        else ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    private fun initButtonListeners() {
        binding.photoCaptureButton.setOnClickListener { takePhoto() } //Funcionalidad del botón en la función takePhoto()

        //Funcionalidad para el botón de cambio entre cámaras.
        binding.cameraSwitchButtton.setOnClickListener {
            lensFacing = if(CameraSelector.LENS_FACING_FRONT == lensFacing){
                CameraSelector.LENS_FACING_BACK
            }else {
                CameraSelector.LENS_FACING_FRONT
            }
            bindCamera()
        }

        binding.goBackButtonCamera.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.goBackButtonCamera.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> binding.goBackButtonCamera.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }

        binding.colorPickerButtonCamera.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.colorPickerButtonCamera.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, ColorPicker::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> binding.colorPickerButtonCamera.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }

        binding.colorListButtonCamera.setOnTouchListener { view, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.colorListButtonCamera.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.pressedButtonColor))
                    val intent = Intent(this, ColorList::class.java)
                    startActivity(intent)
                }
                MotionEvent.ACTION_UP -> binding.colorListButtonCamera.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.cardBackground))
            }
            true
        }
    }

    //Función para lo que ha de hacer la aplicación al regreso de la función requestPermissions dependiendo del resultado de la misma.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //Si el permiso que devuelve es el que se le ha pedido y se tienen todos los permisos, se inicia la cámara. Si no, se muestra un Toast y se finaliza la activity.
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if(allPermissonsGranted()) startCamera()
            else {
                Toast.makeText(this, "Debes proporcionar permisos si quieres tomar fotos.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    //Función para recorrer el array de permisos y comprobar que se tienen todos.
    private fun allPermissonsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    //Función que vincula los elementos de hardware del dlispositivo con los elementos de software de la aplicación para poder usar la cámara.
    private fun bindCamera() {
        val rotation = 0

        //Intento iniciar el cameraProvider. Si hay algún error muestro un mensaje por el log.
        val cameraProvider = cameraProvider ?: run {
            Log.e("Camera error", "Proveedor de cámara nulo al intentar vincular la cámara.")
            return
        }

        try {
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
            val targetResolution = Size(binding.viewFinder.width ?: 0, binding.viewFinder.height ?: 0)

            preview = Preview.Builder()
                .setTargetResolution(targetResolution)
                .setTargetRotation(rotation)
                .build()

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetResolution(targetResolution)
                .setTargetRotation(rotation)
                .build()

            cameraProvider.unbindAll() //Se desvinculan las cámaras anteriormente vinculadas para evitar errores. 
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }catch(exc: Exception) {
            Log.e("Camera error", "Fallo al vincular la cámara", exc)
        }
    }

    //Función que inicia la cámara para poder previsualizar la imagen en la pantalla del dispositivo.
    private fun startCamera() {
        val cameraProviderFinnaly = ProcessCameraProvider.getInstance(this)
        cameraProviderFinnaly.addListener(Runnable {
            cameraProvider = cameraProviderFinnaly.get()
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("No tenemos cámara.")
            }

            manageSwitchButton()

            bindCamera()
        }, ContextCompat.getMainExecutor(this))
    }

    //Función que devuelve true si el dispositivo tiene cámara trasera.
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    //Función que devuelve true si el dispositivo tiene cámara delantera.
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    //Función que deshabilita el botón de cambio de cámara si el dispositivo solo tiene una cámara o no puede recabar info de las cámaras.
    private fun manageSwitchButton() {
        val switchButton = binding.cameraSwitchButtton
        try {
            switchButton.isEnabled = hasBackCamera() && hasFrontCamera() //El botón solo estará enabled si cumple las dos condiciones.
        }catch(exc: CameraInfoUnavailableException) {
            switchButton.isEnabled = false
        }
    }

    //Función para tomar una foto y pasarla al colorPicker.
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val tempFile = File.createTempFile("tempImage", ".jpg", cacheDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object:ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults){
                    ImageSingleton.capturedBitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
                    tempFile.delete()
                    openColorPicker()
                    var clMain = findViewById<ConstraintLayout>(R.id.clMain)
                    Snackbar.make(clMain, "Imagen guardada con éxito", Snackbar.LENGTH_LONG).setAction("OK"){
                        clMain.setBackgroundColor(Color.CYAN)
                    }.show()
                }
                override fun onError(exception: ImageCaptureException) {
                    var clMain = findViewById<ConstraintLayout>(R.id.clMain)
                    Snackbar.make(clMain, "Error al guardar la imagen", Snackbar.LENGTH_LONG).setAction("OK"){
                        clMain.setBackgroundColor(Color.CYAN)
                    }.show()
                }
            }
        )
    }

    private fun openColorPicker() {
        var intent = Intent(this, ColorPicker::class.java)
        startActivity(intent)
    }

    private fun closeCamera() {
        preview?.let {
            it.setSurfaceProvider(null)
        }

        imageCapture = null

        cameraProvider?.unbindAll()
    }

    override fun onResume() {
        super.onResume()
        bindCamera()
    }

    override fun onPause() {
        super.onPause()
        closeCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

object ImageSingleton {
    var capturedBitmap: Bitmap? = null
}