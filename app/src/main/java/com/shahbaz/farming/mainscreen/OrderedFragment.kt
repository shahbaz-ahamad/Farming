package com.shahbaz.farming.mainscreen

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.OrderAdapter
import com.shahbaz.farming.databinding.FragmentOrderedBinding
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.viewmodel.OrderViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class OrderedFragment : Fragment() {
    private lateinit var binding: FragmentOrderedBinding
    private val orderViewmodel by viewModels<OrderViewmodel>()
    private val orderAdapter by lazy { OrderAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderedBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        orderViewmodel.fetchOrder()
        obseerveFetchOrder()

        orderAdapter.onDownloadBillClick = { order ->
            generatePDF(order)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewOrder.apply {
            adapter = orderAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun obseerveFetchOrder() {
        lifecycleScope.launch {
            orderViewmodel.fetchOrder.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("Order", it.data.toString())
                        orderAdapter.differ.submitList(it.data)
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun generatePDF(order: Order) {
        // Create a new document
        val pdfDocument = PdfDocument()

        // Start a page
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas: Canvas = page.canvas
        val paint = Paint()


        // Example content for the bill
        canvas.drawText("Order ID: ${order.orderId}", 10f, 25f, paint)
        canvas.drawText("Product: ${order.product.title}", 10f, 50f, paint)
        canvas.drawText("Quantity: ${order.product.selectedQuantity}", 10f, 75f, paint)
        canvas.drawText("Price: ${order.product.price}", 10f, 100f, paint)

        // Finish the page
        pdfDocument.finishPage(page)

        // Save the PDF document to external storage
        // Save the PDF document to the "Documents" folder
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "Order_${order.orderId}.pdf"
        )

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                requireContext(),
                "PDF saved at ${file.absolutePath}",
                Toast.LENGTH_SHORT
            ).show()

            sendNotification(file)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error generating PDF", Toast.LENGTH_SHORT).show()
        }

        // Close the document
        pdfDocument.close()
    }

    private fun sendNotification(file: File) {
        val channelId = "my_channel_id"
        val channelName = "PDF NOTIFICATION"


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                channelId,
                channelName,
                importance
            )
            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        //create an intent to open file
        val intent = Intent(Intent.ACTION_VIEW).apply {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_pdf) // Replace with your own icon
            .setContentTitle("PDF Generated")
            .setContentText("Tap to open the PDF")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Remove notification when tapped

        // Show the notification
        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }


    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }
}