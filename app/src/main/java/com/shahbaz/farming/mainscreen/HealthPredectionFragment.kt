package com.shahbaz.farming.mainscreen

import android.app.Activity
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentHealthPredectionBinding
import com.shahbaz.farming.util.hideBottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

@AndroidEntryPoint
class HealthPredectionFragment : Fragment() {

    private lateinit var binding: FragmentHealthPredectionBinding
    private lateinit var tfliteInterpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHealthPredectionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the TensorFlow Lite interpreter
        tfliteInterpreter = loadModelFile()

        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val uri = data?.data
                    if (uri != null) {
                        // Load the image as a Bitmap
                        val inputStream = requireContext().contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageView.setImageBitmap(bitmap)

                        // Preprocess the image and run inference
                        val resultText = classifyImage(bitmap)
                        binding.textViewResult.text = resultText
                    }
                }
            }


        binding.buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }

    }

    private fun loadModelFile(): Interpreter {
        val assetManager: AssetManager = requireContext().assets
        val fileDescriptor: AssetFileDescriptor = assetManager.openFd("model_unquant.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val modelBuffer: MappedByteBuffer =
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        return Interpreter(modelBuffer)
    }

    // Function to classify the image
    private fun classifyImage(bitmap: Bitmap): String {
        // Resize the image to the model input size (e.g., 224x224)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        // Preprocess the image
        val inputTensorBuffer =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)
        inputTensorBuffer.loadBuffer(inputBuffer)

        // Create output buffer
// Create output buffer for 12 classes
        val outputTensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 12), DataType.FLOAT32)

        // Run the model
        tfliteInterpreter.run(inputTensorBuffer.buffer, outputTensorBuffer.buffer.rewind())

        // Get prediction result
        val outputs = outputTensorBuffer.floatArray
        val healthyProbability = outputs[0]
        val unhealthyProbability = outputs[1]

        // Determine the prediction
        val result = if (healthyProbability < unhealthyProbability) {
            "Healthy"
        } else {
            "Unhealthy"
        }

        val confidence =
            if (healthyProbability > unhealthyProbability) healthyProbability else unhealthyProbability
        return "Predicted class: $result with confidence: $confidence"
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer =
            ByteBuffer.allocateDirect(4 * 224 * 224 * 3) // Float32, 3 channels, 224x224
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(224 * 224)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        return byteBuffer
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }
}