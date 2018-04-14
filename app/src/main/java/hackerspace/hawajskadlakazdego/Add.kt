package hackerspace.hawajskadlakazdego


import android.app.*
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button


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

class Add : AppCompatActivity() {

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private var channelId = "hackerspace.hawajskadlakazdego"
    private val description = "Test notification"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val navigationNotification = R.id.btn_navigation_notifications

        navigationNotification.setOnClickListener {
            val intent = Intent(this, LauncherActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                        .setContentTitle("Msg me")
                        .setContentText("Test notify")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
            } else {

                builder = Notification.Builder(this)
                        .setContentTitle("Msg me")
                        .setContentText("Test notify")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
            }

                notificationManager.notify(1234, builder.build())


        }

        val c1 = HabbitController(1, 0, findViewById(R.id.fatButton))
        val c2 = HabbitController(1, 0, findViewById(R.id.meatButton))
        val c3 = HabbitController(2, 0, findViewById(R.id.milkButton))
        val c4 = HabbitController(3, 0, findViewById(R.id.grainButton))
        val c5 = HabbitController(5, 0, findViewById(R.id.fruitsButton))
        val c6 = HabbitController(10, 0, findViewById(R.id.workoutButton))
    }

}

private fun Int.setOnClickListener(function: () -> Unit) {

}


