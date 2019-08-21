package com.example.music

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RemoteViews
import android.widget.Toast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.activity_music.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class Music : AppCompatActivity() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder:Notification.Builder
    private val channelid="com.example.music"
    private val description = "Test not"
    val player = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        music_constraintLayout.setPadding(0, statusBarHeight(this), 0, 0)

        player.setAudioStreamType(AudioManager.STREAM_MUSIC)


        val URL = "URL"

        val genre = intent.getStringExtra("genre")

        if(genre == "b"){
            music_constraintLayout.setBackgroundResource(R.drawable.backgroudn_list2)
            back()
            state.text="조용한 발라드 어때요?"
        }
        else if(genre == "e"){
            music_constraintLayout.setBackgroundResource(R.drawable.backgroudn_list)
            back()
            state.text="신나는 노래 어때요?"
        }
        else{
            music_constraintLayout.setBackgroundResource(R.drawable.backgroudn_list3)
            back()
            state.text="POP송 은 어때요?"
        }

        val re: Retrofit = Retrofit.Builder()
            .client(OkHttpClient()).
                baseUrl(URL).build()
        val apiservice : Service = re.create(Service::class.java)


        val postCommentStr = apiservice.postRequst("Music_list",genre)
        postCommentStr.enqueue(object : Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val info = response.body()!!.string()
                    val json_contact = JSONObject(info)
                    val jsonarray_info: JSONArray = json_contact.getJSONArray("result")
                    val size:Int = jsonarray_info.length()
                    for (i in 0.. size-1) {
                        val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)
                        /*when (genre) {
                            "b" ->state.text="조용한 발라드 어때요?"
                            "e"-> state.text="신나는 노래 어때요?"
                            "p"-> state.text="외국 느낌을 받아 POP 어때요?"
                        }*/

                        Picasso.with(this@Music)
                            .load("URL"+json_objectdetail.getString("name")+"_"+json_objectdetail.getString("title")+".jpg")
                            .centerCrop()
                            .resize(400, 400)
                            .into(title_image)

                        textTtilte.text = "제목 : "+json_objectdetail.getString("title")
                        textname.text = "이름 : "+json_objectdetail.getString("name")
                        player.setDataSource("URL"+json_objectdetail.getString("name")+"_"+json_objectdetail.getString("title")+".mp3")
                        player.prepare()
                        start_image.setOnClickListener {
                            val intent = Intent(this@Music, Music_Service::class.java)
                            //intent.putExtra("hu",0)
                            //intent.putExtra("title",json_objectdetail.getString("title"))
                            //intent.putExtra("name",json_objectdetail.getString("name"))
                            startService(intent)
                            start_image.visibility = View.GONE
                            stop_image.visibility=View.VISIBLE
                            player.start()
                        }
                        stop_image.setOnClickListener {
                            /*val intent = Intent(this@Music, Music_Service::class.java)
                            intent.putExtra("hu",1)
                            intent.putExtra("title",json_objectdetail.getString("title"))
                            intent.putExtra("name",json_objectdetail.getString("name"))
                            startService(intent)*/
                            start_image.visibility = View.VISIBLE
                            stop_image.visibility=View.GONE
                            player.pause()
                        }

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("실패1", "실패1")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("실패", "실패")

            }
        })
    }

    fun back(){
        val animationDrawable = music_constraintLayout.getBackground() as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this@Music, Music_Service::class.java)
        stopService(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.e("ddd","dwqdwq");
        player.stop()
        finish()
    }
    /*override fun onPause() {
        super.onPause()
        val intent = Intent(this,Music::class.java)
        val pendingIntent = PendingIntent.getActivity(this@Music,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationChannel = NotificationChannel(channelid,description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this,channelid)
                .setContentTitle("랜덤뮤직")
                .setSmallIcon(R.drawable.music_player)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.music_player))
                .setContentIntent(pendingIntent)
        }
        else{
            builder = Notification.Builder(this)
                .setContentTitle("랜덤뮤직")
                .setSmallIcon(R.drawable.music_player)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.music_player))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234,builder.build())
    }*/


}
