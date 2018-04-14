package hackerspace.hawajskadlakazdego

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import java.sql.Timestamp
import android.util.Log


@Entity(tableName = "habits")
data class Habit(
        @PrimaryKey(autoGenerate = true)
        var id: Long
)

@Entity(tableName = "habitrecord", foreignKeys = arrayOf(ForeignKey(entity = Habit::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("habit_id"),
        onDelete = ForeignKey.CASCADE)))
data class HabitRecord(@PrimaryKey var time: Long){
    var habit_id: Long = 0
}

@Dao
interface HabitAccess{
    @Query("SELECT * FROM habitrecord")
    fun getAll(): List<HabitRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(records: Array<HabitRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabbits(records: Array<HabitRecord>)
}

@Database(entities = arrayOf(HabitRecord::class, Habit::class) ,exportSchema = false, version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun habitAccess(): HabitAccess

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase::class.java, "habit.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration().addCallback(databaseCallbacks())
                            .build()
                }
            }
            return INSTANCE
        }

        class databaseCallbacks :RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO `habits`(id) VALUES (1), (2), (3), (4), (5)")
                Log.d("database", "Initialized new database")
            }
        }

    }

    fun destroyInstance() {
        INSTANCE = null
    }
}

class Add : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        var db = AppDatabase.getInstance(this)

        val text = db?.habitAccess()?.getAll()?.size?.toString()
        (findViewById(R.id.fatButton) as Button).setText(text ?: "dupa")
        //db?.habitAccess()?.insertAll(arrayOf(HabitRecord(1)))


    }

}
