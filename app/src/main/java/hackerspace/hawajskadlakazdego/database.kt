package hackerspace.hawajskadlakazdego

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.content.Context
import android.util.Log
import java.util.*

enum class Habit{
    Fat, Meat, Milk, Grain, Vegetables, Exercise
}

@Entity(tableName = "habits")
data class HabitsTable(
        @PrimaryKey(autoGenerate = true)
        var id: Long
)

@Entity(tableName = "habitrecord", foreignKeys = arrayOf(ForeignKey(entity = HabitsTable::class,
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

    @Query("SELECT * FROM habitrecord WHERE time BETWEEN :from AND :to")
    fun _getHabits(from: Long, to: Long): List<HabitRecord>

    /*fun getHabits( day: Calendar): List<HabitRecord> {
    }*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(records: Array<HabitRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabbits(records: Array<HabitRecord>)

}

@Database(entities = arrayOf(HabitRecord::class, HabitsTable::class) ,exportSchema = false, version = 2)
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

        class databaseCallbacks : Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO `habits`(id) VALUES (1), (2), (3), (4), (5)")
                Log.d("hawajska", "Initialized new database")
                val now = Calendar.getInstance()
                now.set(Calendar.HOUR, 0)
                now.set(Calendar.MINUTE, 0)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (1, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 1)
                now.set(Calendar.MINUTE, 1)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (1, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 2)
                now.set(Calendar.MINUTE, 2)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (2, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 3)
                now.set(Calendar.MINUTE, 3)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (1, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 4)
                now.set(Calendar.MINUTE, 4)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (3, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 5)
                now.set(Calendar.MINUTE, 5)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (5, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 6)
                now.set(Calendar.MINUTE, 6)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (5, " + now.timeInMillis.toString() + ")")

                now.add(Calendar.DAY_OF_MONTH, -1)
                now.set(Calendar.HOUR, 0)
                now.set(Calendar.MINUTE, 0)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (1, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 1)
                now.set(Calendar.MINUTE, 1)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (1, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 2)
                now.set(Calendar.MINUTE, 2)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (1, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 3)
                now.set(Calendar.MINUTE, 3)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (1, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 4)
                now.set(Calendar.MINUTE, 4)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (3, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 5)
                now.set(Calendar.MINUTE, 5)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (3, " + now.timeInMillis.toString() + ")")
                now.set(Calendar.HOUR, 6)
                now.set(Calendar.MINUTE, 6)
                db.execSQL("INSERT INTO `habitrecord`(habit_id, time) VALUES (5, " + now.timeInMillis.toString() + ")")



            }
        }

    }

    fun destroyInstance() {
        INSTANCE = null
    }
}