package com.fluffycat.sensorsmanager.sensors

import android.hardware.SensorEvent
import android.media.*
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

class MicrophoneController : ISensorController {

    override val sensorCurrentData = MutableLiveData<SensorEvent>()
    override val additionalData = MutableLiveData<Int>()

    val microphoneCurrentData = MutableLiveData<Double>()

    private var threadMicReader: ThreadMicReader? = null

    override fun startReceivingData() {
        threadMicReader?.interrupt()
        threadMicReader = ThreadMicReader(this).also { threadMicReader -> threadMicReader.start() }
    }

    override fun stopReceivingData() {
        threadMicReader?.interrupt()
        threadMicReader = null
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        // empty
    }

    override fun onAdditionalDataChanged(accuracy: Int) {
        // empty
    }

    fun onSensorDataReceived(sum: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            microphoneCurrentData.value = sum
        }
    }

    override fun getSensorInfo(): String = "Microphone"
}

class ThreadMicReader(private val microphoneController: MicrophoneController) : Thread() {

    private val soundDataBuffer: ByteArray = ByteArray(1400)
    private var audioTrack: AudioTrack? = null
    override fun run() {
        val recorder = createRecorder()
        recorder.startRecording()
        var sum = -128.0

        while (true) {
            recorder.read(soundDataBuffer, 0, soundDataBuffer.size)

            // Get max sound value from buffer
            var i = 0
            while (i * 2 + 1 < soundDataBuffer.size) {
                sum = max(soundDataBuffer[i * 2 + 1].toDouble(), sum)
                i++
            }
            if (sum > 0 && sum < 30) sum *= 2.0 else if (sum >= 30 && sum < 50) sum *= 1.5
            microphoneController.onSensorDataReceived(sum)
            sum = -128.0
        }
    }

    private fun createRecorder(): AudioRecord {
        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        audioTrack = AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize,  //buffer length in bytes
                AudioTrack.MODE_STREAM)
        return AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minBufferSize)
    }
}