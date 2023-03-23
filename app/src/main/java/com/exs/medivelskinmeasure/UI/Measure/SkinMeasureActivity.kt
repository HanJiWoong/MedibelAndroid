package com.exs.medivelskinmeasure.UI.Measure

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import com.exs.medivelskinmeasure.Device.mqtt.MQTTMeasuredResultData
import com.exs.medivelskinmeasure.Device.mqtt.MQTTMeasuringRequest
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.Device.mqtt.MqttDataStep
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.CommonUtil.setPreferenceString
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class SkinMeasureActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mCLProgressContent: ConstraintLayout
    private lateinit var mTVSkinMeasureInfo: TextView
    private lateinit var mTVSkinMeasureDesc: TextView

    private lateinit var mVLCVideoLayout: VLCVideoLayout
    private lateinit var libVlc: LibVLC
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var mBtnMeasure: AppCompatButton

    private var mIsComplete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_measure)

        initUI()
        initPlayer()
        setCommonListener()

        MqttClient.requestMeasureStart()
        registMQTTCallback()
    }

    override fun onStart() {
        super.onStart()

        CommonUtil.getPreferenceString(this, getString(R.string.pref_key_device_wifi_ip))?.let {
            startPlayer(it)
        }
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewSkinMeasureTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_skin_measure)

        mCLProgressContent = findViewById(R.id.CLSkinMeasureProgressContent)

        val gd = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.parseColor("#ee7700"),
                Color.parseColor("#ebcc00")

            )
        )
        gd.cornerRadius = 9f

        mCLProgressContent.background = gd

        mTVSkinMeasureInfo = findViewById(R.id.TVSkinMeasureInfo)
        mTVSkinMeasureDesc = findViewById(R.id.TVSkinMeasureInfoDesc)
        mVLCVideoLayout = findViewById(R.id.VLSkinMeasure)

        mBtnMeasure = findViewById(R.id.BtnSkinMeasure)

    }

    private fun initPlayer() {
        libVlc = LibVLC(this, ArrayList<String>().apply {
//            add("--no-drop-late-frames")
//            add("--no-skip-frames")
            add("--drop-late-frames")
            add("--skip-frames")
            add("--rtsp-tcp")
            add("-vvv")
        })
        mediaPlayer = MediaPlayer(libVlc)
        mediaPlayer.attachViews(mVLCVideoLayout, null, false, false)
        mediaPlayer.setEventListener { event ->
            when (event.type) {
                MediaPlayer.Event.Playing -> {
                }
                MediaPlayer.Event.Stopped -> {
                }
            }
        }
    }

    private fun startPlayer(url: String) {
        Media(libVlc, Uri.parse(url)).apply {
            setHWDecoderEnabled(true, false)
            addOption(":network-caching=0")
            addOption(":clock-jitter=0")
            addOption(":clock-synchro=0")
            mediaPlayer.media = this
        }.release()
        mediaPlayer.play()
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mBtnMeasure.setOnClickListener {
            if (!mIsComplete) {
//                mIsComplete = true
//
//                mBtnMeasure.text = getString(R.string.str_ko_skin_measure_complete_btn_title)
//
//                (mCLProgressContent.layoutParams as ConstraintLayout.LayoutParams)
//                    .matchConstraintPercentWidth = 1.0f
//                mCLProgressContent.requestLayout()
//
//                val gd = GradientDrawable(
//                    GradientDrawable.Orientation.LEFT_RIGHT,
//                    intArrayOf(
//                        Color.parseColor("#ee7700"),
//                        Color.parseColor("#ebcc00")
//                    )
//                )
//                gd.cornerRadius = 9f
//
//                mCLProgressContent.background = gd
//
//                mTVSkinMeasureInfo.text = getString(R.string.str_ko_skin_measure_complete)
//                mTVSkinMeasureDesc.text = getString(R.string.str_ko_skin_measure_complete_desc)
//
//                mTitleBar.setHiddenBackBtn(true)
                MqttClient.publishUserInfoSetting(this)


            } else {
                val intent = Intent(this@SkinMeasureActivity, SkinMeasureResultActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }

        }

    }

    private fun registMQTTCallback() {
        MqttClient.listener = object : MqttClient.MQTTClientInterface {
            override fun measuringData(data: MQTTMeasuringRequest) {
                val step = data.parameters.step

                when (step) {
                    MqttDataStep.waitImage.ordinal -> {
                        Toast.makeText(
                            this@SkinMeasureActivity,
                            data.parameters.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    MqttDataStep.measureImage.ordinal -> {
                        runOnUiThread {
                            val no = data.parameters.measure_data.image_no
                            val width:Float = (0.0909 * no).toFloat()

                            val params = mCLProgressContent.layoutParams as ConstraintLayout.LayoutParams

                            params.matchConstraintPercentWidth = width
                            mCLProgressContent.requestLayout()

                        }

                    }
                    MqttDataStep.measureTempMoisture.ordinal -> {

                    }
                }
            }

            override fun numericResultData(temp: Float, moisture: Int) {
                Toast.makeText(
                    this@SkinMeasureActivity,
                    "온도 -> ${temp}, 습도 -> ${moisture}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun imageReusltData(data: MQTTMeasuredResultData) {
                Log.e("TEST","----------------------------------------------------------------------------------??????????????????????????????????????????????????????")

                /**
                 * EMC TEST 를 위해 추가한 코드
                 */
                CoroutineScope(Dispatchers.Default).async {

                    val imgBytes = Base64.decode(data.image_data, 0)
                    val bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        //Q 버전 이상일 경우. (안드로이드 10, API 29 이상일 경우)
                        saveImageOnAboveAndroidQ(bitmap)
                    } else {
                        // Q 버전 이하일 경우. 저장소 권한을 얻어온다.
                        val writePermission = ActivityCompat.checkSelfPermission(
                            this@SkinMeasureActivity,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )

                        if (writePermission == PackageManager.PERMISSION_GRANTED) {
                            saveImageOnUnderAndroidQ(bitmap)
                        } else {
                            val requestExternalStorageCode = 1

                            val permissionStorage = arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )

                            ActivityCompat.requestPermissions(
                                this@SkinMeasureActivity,
                                permissionStorage,
                                requestExternalStorageCode
                            )
                        }
                    }
                }


            }

            override fun measuredFinish() {
                val intent =
                    Intent(this@SkinMeasureActivity, SkinMeasureResultActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }
    }


    /**
     * EMC TEST 를 위해 추가한 코드
     */

    //Android Q (Android 10, API 29 이상에서는 이 메서드를 통해서 이미지를 저장한다.)
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap) {
        val fileName = System.currentTimeMillis().toString() + ".png" // 파일이름 현재시간.png

        /*
        * ContentValues() 객체 생성.
        * ContentValues는 ContentResolver가 처리할 수 있는 값을 저장해둘 목적으로 사용된다.
        * */
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave") // 경로 설정
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName) // 파일이름을 put해준다.
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1) // 현재 is_pending 상태임을 만들어준다.
            // 다른 곳에서 이 데이터를 요구하면 무시하라는 의미로, 해당 저장소를 독점할 수 있다.
        }

        // 이미지를 저장할 uri를 미리 설정해놓는다.
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (uri != null) {
                val image = contentResolver.openFileDescriptor(uri, "w", null)
                // write 모드로 file을 open한다.

                if (image != null) {
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    //비트맵을 FileOutputStream를 통해 compress한다.
                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장소 독점을 해제한다.
                    contentResolver.update(uri, contentValues, null, null)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveImageOnUnderAndroidQ(bitmap: Bitmap) {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/DCIM/imageSave"
        val dir = File(path)

        if (dir.exists().not()) {
            dir.mkdirs() // 폴더 없을경우 폴더 생성
        }

        try {
            val fileItem = File("$dir/$fileName")
            fileItem.createNewFile()
            //0KB 파일 생성.

            val fos = FileOutputStream(fileItem) // 파일 아웃풋 스트림

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            //파일 아웃풋 스트림 객체를 통해서 Bitmap 압축.

            fos.close() // 파일 아웃풋 스트림 객체 close

            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileItem)))
            // 브로드캐스트 수신자에게 파일 미디어 스캔 액션 요청. 그리고 데이터로 추가된 파일에 Uri를 넘겨준다.
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}