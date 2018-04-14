package hackerspace.hawajskadlakazdego


import android.app.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.view.View
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.graphics.drawable.Icon
import android.util.Log
import javax.xml.transform.Result


class Habbit(max: Int, current: Int =0){
    public val max = max
    public var current = current
}

class HabbitController(max: Int, current: Int = 0, view: Button){
    val data = Habbit(max, current)
    val view = view

    init{
        view.setOnClickListener({
            this.data.current ++
            val ratio = this.data.current.toFloat() / this.data.max.toFloat()
                if(ratio<0.33)
                    this.view.setBackgroundColor( Color.rgb(255, 0, 32))
                else if(ratio < 0.66)
                    this.view.setBackgroundColor( Color.rgb(128, 128, 32))
                else
                    this.view.setBackgroundColor( Color.rgb(0, 255, 32))
        })
    }
}



class NotifyController(view : Button) {
    val view = view

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private var channelId = "hackerspace.hawajskadlakazdego"
    private val description = "Test notification"


//    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        view.setOnClickListener({

        })
    }
}

class Add : AppCompatActivity() {

    private var notificationManager : NotificationManager? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel("hackerspace.hawajskadlakazdego","Hawajska News", "Your News Channel")

        val c1 = HabbitController(1, 0, findViewById(R.id.fatButton))
        val c2 = HabbitController(1, 0, findViewById(R.id.meatButton))
        val c3 = HabbitController(2, 0, findViewById(R.id.milkButton))
        val c4 = HabbitController(3, 0, findViewById(R.id.grainButton))
        val c5 = HabbitController(5, 0, findViewById(R.id.fruitsButton))
        val c6 = HabbitController(10, 0, findViewById(R.id.workoutButton))

        val button = sendNotification(findViewById(R.id.btn_navigation_notifications))



    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        notificationManager?.createNotificationChannel(channel)
    }

    fun sendNotification(view: View) {

        val notificationID = 101
        val resultIntent = Intent(this, Add::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0 , resultIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val channelID = "hackerspace.hawajskadlakazdego"
        val icon: Icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)
        val action: Notification.Action =
                Notification.Action.Builder(icon, "Open", pendingIntent).build()
        val notification = Notification.Builder(this@Add, channelID)
                .setContentTitle("Example Notify")
                .setContentText("This is example")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .setContentIntent(pendingIntent)
                .setActions(action)
                .build()

        notificationManager?.notify(notificationID, notification)
    }

}




