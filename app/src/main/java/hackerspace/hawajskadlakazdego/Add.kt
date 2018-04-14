package hackerspace.hawajskadlakazdego

import android.arch.persistence.room.*
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import java.sql.Timestamp


@Entity(tableName = "habitrecord")
data class HabitRecord(val habitId: Int){
    @PrimaryKey
    var time: Long = System.currentTimeMillis().toLong()
}

@Dao
interface HabitAccess{
    @Query("SELECT * FROM habitrecord")
    fun getAll(): List<HabitRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(records: Array<HabitRecord>)
}

@Database(entities = arrayOf(HabitRecord::class) ,exportSchema = false, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun habitAccess(): HabitAccess

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase::class.java, "habit.db").allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

class Add : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        var db = AppDatabase.getInstance(this)
        val text = db?.habitAccess()?.getAll()?.size?.toString()
        (findViewById(R.id.fatButton) as Button).setText(text ?: "dupa")
        db?.habitAccess()?.insertAll(arrayOf(HabitRecord(1)))


    }

}
