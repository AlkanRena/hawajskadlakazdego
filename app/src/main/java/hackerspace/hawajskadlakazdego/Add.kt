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
import android.provider.ContactsContract
import android.app.Activity
import android.net.Uri
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.design.internal.BottomNavigationItemView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.util.*




fun d(msg: String){
    Log.d("hawajska", msg)
}


class Add : AppCompatActivity() {
    var db: AppDatabase? = null
    var habitViews: Map<Habit, HabitViewController>? = null

    private var notificationManager : NotificationManager? = null

    fun getDB(): AppDatabase?{
        if(this.db == null){
            this.db = AppDatabase.getInstance(this)
        }
        return this.db
    }

    val PERMISSION_REQUEST = 1
    val PERMISSION_REQUEST_ON_SHARE = 2
    val PICK_CONTACT = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel("hackerspace.hawajskadlakazdego","Hawajska News", "Your News Channel")


        rebuildViewControllers({ Calendar.getInstance()})
        val button = sendNotification(findViewById(R.id.workoutButton))

        val prevDayIcon = findViewById<View>(R.id.navigation_today)
        val nextDayIcon = findViewById<View>(R.id.navigation_yesterday)
        prevDayIcon.setOnClickListener({
            this.rebuildViewControllers { java.util.Calendar.getInstance() }
            (prevDayIcon as BottomNavigationItemView).setChecked(true)
            (nextDayIcon as BottomNavigationItemView).setChecked(false)
            //nextDayIcon.
        })
        nextDayIcon.setOnClickListener({
            this.rebuildViewControllers {
            var timeProvider = java.util.Calendar.getInstance()
            timeProvider.add(Calendar.DATE, -1)
            timeProvider}
            (prevDayIcon as BottomNavigationItemView).setChecked(false)
            (nextDayIcon as BottomNavigationItemView).setChecked(true)

        })

        val permissions = arrayOf(android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_CONTACTS)
        findViewById<View>(R.id.navigation_share).setOnClickListener({
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_ON_SHARE)
            }
            else
                share()

        })

        val donate = findViewById<View>(R.id.donate)
        donate.setOnClickListener({

            setContentView(R.layout.activity_donate)

        })

    }


    fun share(){
        // Open android contacts to let the user choose a friend to call
        val intent = Intent(
                Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI)
        this.startActivityForResult(intent, PICK_CONTACT)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                           permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_ON_SHARE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    share()
                } //else nothing to do
            }
        }
    }

    public override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)

        when (reqCode) {
            PICK_CONTACT -> if (resultCode == Activity.RESULT_OK) {
                // Get selected contact phone number and call it
                try {
                    val returnUri = data!!.data!!
                    val cursor = contentResolver.query(returnUri, null, null, null, null)

                    if (cursor!!.moveToNext()) {
                        val columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                        val contactID = cursor.getString(columnIndex_ID)

                        val columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                        val stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER)

                        if (stringHasPhoneNumber.equals("1", ignoreCase = true)) {
                            val cursorNum = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, null, null)

                            //Get the first phone number
                            if (cursorNum!!.moveToNext()) {
                                val columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                val stringNumber = cursorNum.getString(columnIndex_number)
                                d(stringNumber)
                                val phoneIntent = Intent(Intent.ACTION_CALL)
                                phoneIntent.setData(Uri.parse("tel:" + stringNumber));
                                try {
                                    this.startActivity(phoneIntent)
                                } catch (e: Exception) {

                                }
                            }

                        } else {
                            d("NO Phone Number")
                        }


                    } else {
                        Toast.makeText(applicationContext, "NO data!", Toast.LENGTH_LONG).show()
                    }
                }
                catch (e:Exception){}


            }
        }
    }

    private fun rebuildViewControllers(timeProvider: ()->Calendar) {

        val dbAbstraction = DatabaseAbstraction(this.getDB()!!)
        val hvFat = HabitViewController(findViewById(R.id.fatButton),
                findViewById(R.id.fatLabel),
                Habit.Fat, dbAbstraction, timeProvider)
        val hvMeat = HabitViewController(findViewById(R.id.meatButton),
                findViewById(R.id.meatLabel),
                Habit.Meat, dbAbstraction, timeProvider)
        val hvMilk = HabitViewController(findViewById(R.id.milkButton),
                findViewById(R.id.milkLabel),
                Habit.Milk, dbAbstraction, timeProvider)
        val hvGrain = HabitViewController(findViewById(R.id.grainButton),
                findViewById(R.id.grainLabel),
                Habit.Grain, dbAbstraction, timeProvider)
        val hvFruits = HabitViewController(findViewById(R.id.fruitsButton),
                findViewById(R.id.fruitLabel),
                Habit.Fruits, dbAbstraction, timeProvider)
        val hvWorkout = HabitViewController(findViewById(R.id.workoutButton),
                findViewById(R.id.workoutLabel),
                Habit.Workout, dbAbstraction, timeProvider)

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




