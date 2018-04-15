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
    var habitViews: Map<Habit, HabitViewController>? = null

    fun getHabits(day: Calendar): List<HabitRecord>?{
        val from: Calendar = day.clone() as Calendar
        from.set(Calendar.HOUR, 0)
        from.set(Calendar.MINUTE, 0)
        val to = day.clone() as Calendar
        to.set(Calendar.HOUR, 23)
        to.set(Calendar.MINUTE, 59)

        return this.getDB()?.habitAccess()?.getHabitRecords(from.timeInMillis, to.timeInMillis)
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
        initViews()


    }

    private fun initViews() {

        val dbAbstraction = DatabaseAbstraction(this.getDB()!!)
        val hvFat = HabitViewController(findViewById(R.id.fatButton),
                Habit.Fat, dbAbstraction)
        val hvMeat = HabitViewController(findViewById(R.id.meatButton),
                Habit.Meat, dbAbstraction)
        val hvMilk = HabitViewController(findViewById(R.id.milkButton),
                Habit.Milk, dbAbstraction)
        val hvGrain = HabitViewController(findViewById(R.id.grainButton),
                Habit.Grain, dbAbstraction)
        val hvFruits = HabitViewController(findViewById(R.id.fruitsButton),
                Habit.Fruits, dbAbstraction)
        val hvWorkout = HabitViewController(findViewById(R.id.workoutButton),
                Habit.Workout, dbAbstraction)

        this.habitViews = mapOf(
                Habit.Fat to hvFat,
                Habit.Meat to hvMeat,
                Habit.Milk to hvMilk,
                Habit.Grain to hvGrain,
                Habit.Fruits to hvFruits,
                Habit.Workout to hvWorkout
        )

        this.habitViews?.forEach {
            it.value.redraw()
        }
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


        val resultIntent = Intent(this, Add::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0 , resultIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val result1 = Intent(this, Button::class.java)

        val pendingIntent1 = PendingIntent.getActivity(this, 0 , result1, PendingIntent.FLAG_CANCEL_CURRENT)

        val channelID = "hackerspace.hawajskadlakazdego"

        val icon: Icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)
        val action: Notification.Action =
                Notification.Action.Builder(icon, "Open", pendingIntent).build()
        val prevPendingIntent: Notification.Action =
                Notification.Action.Builder(icon, "Open", pendingIntent).build()

        val pausePendingIntent: Notification.Action =
                Notification.Action.Builder(icon, "Open", pendingIntent).build()

        val nextPendingIntent: Notification.Action =
                Notification.Action.Builder(icon, "Open", pendingIntent).build()




        val GROUP_KEY_NOTIFY = "group_key_notify"

        var builderSummary: Notification.Builder = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("A Bundle Example")
                .setContentText("You have 3 new messages")
                .setGroup(GROUP_KEY_NOTIFY)
                .setGroupSummary(true)

        var builder1: Notification.Builder = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Message Fat")
                .setContentText("")
                .setGroup(GROUP_KEY_NOTIFY)

        var builder2: Notification.Builder = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Message")
                .setContentText("You have a new message from Caitlyn")
                .setActions(action)
                .setGroup(GROUP_KEY_NOTIFY)

        var builder3: Notification.Builder = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Message")
                .setContentText("You have a new message from Jason")
                .setActions(action)
                .setGroup(GROUP_KEY_NOTIFY)

        var builder4: Notification.Builder = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Message")
                .setContentText("You have a new message from Kassidy")
                .setActions(action)
                .setGroup(GROUP_KEY_NOTIFY)

        var builder5: Notification.Builder = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Message")
                .setContentText("You have a new message from Caitlyn")
                .setActions(action)
                .setGroup(GROUP_KEY_NOTIFY)

        var builder6: Notification.Builder = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Message")
                .setContentText("You have a new message from Jason")
                .setActions(action)
                .setGroup(GROUP_KEY_NOTIFY)

        var notificationId0 = 100
        var notificationId1 = 101
        var notificationId2 = 102
        var notificationId3 = 103
        var notificationId4 = 104
        var notificationId5 = 105
        var notificationId6 = 106

        notificationManager?.notify(notificationId1, builder1.build())
        notificationManager?.notify(notificationId2, builder2.build())
        notificationManager?.notify(notificationId3, builder3.build())
        notificationManager?.notify(notificationId4, builder4.build())
        notificationManager?.notify(notificationId5, builder5.build())
        notificationManager?.notify(notificationId6, builder6.build())
        notificationManager?.notify(notificationId0, builderSummary.build())
    }



}




