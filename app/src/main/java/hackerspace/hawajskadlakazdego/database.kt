package hackerspace.hawajskadlakazdego

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.content.Context
import android.util.Log
import java.util.*

enum class Habit{
    Fat, Meat, Milk, Grain, Fruits, Workout
}

@Entity(tableName = "habits")
data class HabitInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var top: Int,
    var label: String
)

@Entity(tableName = "habitrecord", foreignKeys = arrayOf(ForeignKey(entity = HabitInfo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("habit_id"),
        onDelete = ForeignKey.CASCADE)))
data class HabitRecord(
        @PrimaryKey var time: Long,
        var habit_id: Int){}

@Dao
interface HabitAccess{
    @Query("SELECT * FROM habitrecord")
    fun getAll(): List<HabitRecord>

    @Query("SELECT * FROM habits WHERE id= :habitId LIMIT 1 ")
    fun getHabitInfo(habitId: Int): HabitInfo

    @Query("SELECT count(*) FROM habitrecord WHERE habit_id=:habitId AND time BETWEEN :from AND :to")
    fun countHabitActions(habitId: Int, from: Long, to: Long): Int

    @Query("SELECT * FROM habitrecord WHERE time BETWEEN :from AND :to")
    fun getHabitRecords(from: Long, to: Long): List<HabitRecord>

    /*fun getHabits( day: Calendar): List<HabitRecord> {
    }*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(records: Array<HabitRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabbits(records: Array<HabitRecord>)

}

@Database(entities = arrayOf(HabitRecord::class, HabitInfo::class) ,exportSchema = false, version = 2)
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
                db.execSQL("INSERT INTO `habits`(id, label, top) VALUES " +
                        "(" + Habit.Workout.ordinal + ", 'Ćwiczenia', 7 ), " +
                        "(" + Habit.Fruits.ordinal + ", 'Warzywa i owoce', 5 ), " +
                        "(" + Habit.Grain.ordinal + ", 'Produkty pełnoziarniste', 3 ), " +
                        "(" + Habit.Milk.ordinal + ", 'Nabiał', 2 ), " +
                        "(" + Habit.Meat.ordinal + ", 'Mięso', 1 ), " +
                        "(" + Habit.Fat.ordinal + ", 'Tłuszcze, orzechy', 1 );")
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