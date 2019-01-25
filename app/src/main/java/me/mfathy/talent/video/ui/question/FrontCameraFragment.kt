package me.mfathy.talent.video.ui.question

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.question_fragment.*
import me.mfathy.talent.video.R
import java.io.File
import java.util.*

/**
 * Created by Mohammed Fathy on 24/01/2019.
 * dev.mfathy@gmail.com
 *
 * FrontCameraFragment to initialise camera preview and recording.
 */
open class FrontCameraFragment : DaggerFragment() {

    companion object {

        const val TAG = "QuestionFragment"

        const val MAX_PREVIEW_WIDTH = 1280
        const val MAX_PREVIEW_HEIGHT = 720

        const val SENSOR_DEFAULT_ORIENTATION_DEGREES = 90
        const val SENSOR_INVERSE_ORIENTATION_DEGREES = 270

        val defaultOrientation = SparseIntArray().apply {
            append(Surface.ROTATION_0, 90)
            append(Surface.ROTATION_90, 0)
            append(Surface.ROTATION_180, 270)
            append(Surface.ROTATION_270, 180)
        }
        val inverseOrientation = SparseIntArray().apply {
            append(Surface.ROTATION_0, 270)
            append(Surface.ROTATION_90, 180)
            append(Surface.ROTATION_180, 90)
            append(Surface.ROTATION_270, 0)
        }
    }

    /**
     * A Camera device to the opened.
     */
    private lateinit var cameraDevice: CameraDevice

    /**
     * A CameraCaptureSession for preview.
     */
    private lateinit var captureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private lateinit var backgroundThread: HandlerThread

    /**
     * A [Handler] for running tasks in the background.
     */
    private lateinit var backgroundHandler: Handler

    /**
     * Whether the app is recording video now
     */
    protected var isRecording = false

    /**
     * deviceStateCallback is called when [CameraDevice] changes its status.
     */
    private val deviceStateCallback = CameraDeviceCallback()

    private val mediaRecorder by lazy {
        MediaRecorder()
    }

    /**
     * Output file for video
     */
    lateinit var currentVideoFilePath: String
    private val cameraManager by lazy {
        activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    /**
     * handler for several lifecycle events on a [TextureView].
     */
    val surfaceListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) = Unit

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?) = true

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            Log.d(TAG, "Texture Surface width: $width height: $height")
            openCamera()
        }
    }

    private fun <T> cameraCharacteristics(cameraId: String, key: CameraCharacteristics.Key<T>): T? {
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        return when (key) {
            CameraCharacteristics.LENS_FACING -> characteristics.get(key)
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP -> characteristics.get(key)
            CameraCharacteristics.SENSOR_ORIENTATION -> characteristics.get(key)
            else -> throw  IllegalArgumentException("Key is not recognized")
        }
    }

    private fun cameraId(lens: Int): String {
        var deviceId = listOf<String>()
        try {
            val cameraIdList = cameraManager.cameraIdList
            deviceId = cameraIdList.filter { lens == cameraCharacteristics(it, CameraCharacteristics.LENS_FACING) }
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }
        return deviceId[0]
    }

    @SuppressLint("MissingPermission")
    private fun connectCamera() {
        val deviceId = cameraId(CameraCharacteristics.LENS_FACING_FRONT)
        Log.d(TAG, "Connected camera deviceId: $deviceId")

        try {
            cameraManager.openCamera(deviceId, deviceStateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: InterruptedException) {
            Log.e(TAG, "Open camera device interrupted while opened")
        }
    }

    fun openCamera() {
        connectCamera()
    }

    fun closeCamera() {
        if (this::captureSession.isInitialized)
            captureSession.close()
        if (this::cameraDevice.isInitialized)
            cameraDevice.close()
    }

    protected fun startBackgroundThread() {
        backgroundThread = HandlerThread("QuestionCamera").also { it.start() }
        backgroundHandler = Handler(backgroundThread.looper)
    }

    protected fun stopBackgroundThread() {
        backgroundThread.quitSafely()
        try {
            backgroundThread.join()
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }

    fun previewSession() {
        val surfaceTexture = previewTextureView.surfaceTexture
        surfaceTexture.setDefaultBufferSize(MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT)
        val surface = Surface(surfaceTexture)

        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)

        cameraDevice.createCaptureSession(
            Arrays.asList(surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession?) {
                    Log.e(TAG, "Failed to create capture session!")
                }

                override fun onConfigured(session: CameraCaptureSession?) {
                    if (session != null) {
                        captureSession = session
                        captureRequestBuilder.set(
                            CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                        )
                        captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null)
                    }
                }

            }, backgroundHandler
        )
    }

    fun recordSession(videoId: String) {

        setupMediaRecorder(videoId)

        val surfaceTexture = previewTextureView.surfaceTexture
        surfaceTexture.setDefaultBufferSize(MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT)
        val textureSurface = Surface(surfaceTexture)
        val recordSurface = mediaRecorder.surface

        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
        captureRequestBuilder.addTarget(textureSurface)
        captureRequestBuilder.addTarget(recordSurface)

        val surfaces = ArrayList<Surface>().apply {
            add(textureSurface)
            add(recordSurface)
        }

        cameraDevice.createCaptureSession(
            surfaces,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession?) {
                    Log.e(TAG, "Failed to create record session!")
                }

                override fun onConfigured(session: CameraCaptureSession?) {
                    if (session != null) {
                        captureSession = session
                        captureRequestBuilder.set(
                            CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                        )
                        captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null)
                        isRecording = true
                        mediaRecorder.start()
                    }
                }

            }, backgroundHandler
        )
    }

    private fun createVideoFileName(qid: String): String {
        return "VIDEO_ANSWER_OF_$qid.mp4"
    }

    private fun createVideoFile(qid: String): File {
        val videoFile = File(context?.filesDir, createVideoFileName(qid))
        currentVideoFilePath = videoFile.absolutePath
        return videoFile
    }

    private fun setupMediaRecorder(videoId: String) {
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        val sensorOrientation = cameraCharacteristics(
            cameraId(CameraCharacteristics.LENS_FACING_FRONT),
            CameraCharacteristics.SENSOR_ORIENTATION
        )
        when (sensorOrientation) {
            SENSOR_DEFAULT_ORIENTATION_DEGREES ->
                mediaRecorder.setOrientationHint(defaultOrientation.get(rotation!!))
            SENSOR_INVERSE_ORIENTATION_DEGREES ->
                mediaRecorder.setOrientationHint(inverseOrientation.get(rotation!!))
        }
        mediaRecorder.apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setOutputFile(createVideoFile(videoId))
            else setOutputFile(createVideoFile(videoId).absolutePath)
            setVideoEncodingBitRate(10000000)
            setVideoFrameRate(30)
            setVideoSize(1920, 1080)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
        }
    }

    fun stopMediaRecorder() {
        mediaRecorder.apply {
            try {
                stop()
                reset()
            } catch (e: IllegalStateException) {
                Log.e(TAG, e.toString())
            }
        }
    }

    /**
     * CameraDeviceCallback is called when [CameraDevice] changes its status.
     */
    inner class CameraDeviceCallback : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {
            Log.d(TAG, "Camera device opened")

            if (camera != null) {
                cameraDevice = camera
                previewSession()
            }
        }

        override fun onDisconnected(camera: CameraDevice?) {
            Log.d(TAG, "Camera device disconnected")
            camera?.close()
        }

        override fun onError(camera: CameraDevice?, error: Int) {
            Log.e(TAG, "Camera device error: $error")
            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.error_title))
                .setMessage(getString(R.string.error_camera_device))
                .setPositiveButton(
                    getString(R.string.ok)
                ) { dialog, _ ->
                    dialog.dismiss()
                    this@FrontCameraFragment.activity?.finish()
                }
                .show()

        }
    }

}