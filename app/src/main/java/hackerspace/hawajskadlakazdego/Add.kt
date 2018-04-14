package hackerspace.hawajskadlakazdego

import java.util.Calendar

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

fun d(msg: String){
    Log.d("hawajska", msg)
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
    var db: AppDatabase? = null

    fun getHabits(day: Calendar): List<HabitRecord>?{
        val from: Calendar = day.clone() as Calendar
        from.set(Calendar.HOUR, 0)
        from.set(Calendar.MINUTE, 0)
        val to = day.clone() as Calendar
        to.set(Calendar.HOUR, 23)
        to.set(Calendar.MINUTE, 59)

        return this.getDB()?.habitAccess()?._getHabits(from.timeInMillis, to.timeInMillis)
    }
    private var notificationManager : NotificationManager? = null



    fun getDB(): AppDatabase?{
        if(this.db == null){
            this.db = AppDatabase.getInstance(this)
        }
        return this.db
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val text = this.getHabits(java.util.Calendar.getInstance())?.size
        (findViewById(R.id.fatButton) as Button).setText(text?.toString() ?: "dupa")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel("hackerspace.hawajskadlakazdego","Hawajska News", "Your News Channel")


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




