package com.fluffycat.sensorsmanager.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.databinding.MicrophoneFragmentBinding
import com.fluffycat.sensorsmanager.sensors.MicrophoneController
import com.fluffycat.sensorsmanager.utils.REQUEST_RECORD_AUDIO_REQUEST_CODE
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MicrophoneFragment : Fragment() {

    companion object {
        const val chartTitle = "Microphone"
    }

    private val microphoneController: MicrophoneController = MicrophoneController()
    private val lineData: LineData

    private var binding: MicrophoneFragmentBinding? = null

    init {
        val lineDataSet1: LineDataSet = createDataSet(Color.CYAN, chartTitle)
        lineData = LineData(lineDataSet1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return MicrophoneFragmentBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.microphone)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if ((ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO_REQUEST_CODE)
        }

        lifecycleScope.launchWhenResumed {
            microphoneController.microphoneCurrentData.collect {
                if (it != null) {
                    onDataChanged(it)
                }
            }
        }

        binding?.apply {
            microphoneChart.data = lineData
            microphoneSensorInfoLabel.text = microphoneController.getSensorInfo()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    @Suppress("SameParameterValue")
    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onStart() {
        super.onStart()
        if ((ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            microphoneController.startReceivingData()
        }
    }

    override fun onStop() {
        super.onStop()
        microphoneController.stopReceivingData()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_RECORD_AUDIO_REQUEST_CODE && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            microphoneController.startReceivingData()
        }
    }

    private fun onDataChanged(soundSum: Double) {
        val binding = binding ?: return
        val value = soundSum.toFloat()
        binding.microphoneXValueInfoLabel.text = "Sound power: $value"

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), value), 0)

            notifyDataChanged()
            binding.microphoneChart.notifyDataSetChanged()
            binding.microphoneChart.setVisibleXRangeMaximum(100F)
            binding.microphoneChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}
