package hackerspace.hawajskadlakazdego

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.util.Log
import java.util.Calendar

fun d(msg: String){
    Log.d("hawajska", msg)
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


    }

}
