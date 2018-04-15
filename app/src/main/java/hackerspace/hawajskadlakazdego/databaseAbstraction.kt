package hackerspace.hawajskadlakazdego

import java.util.*

class DatabaseAbstraction(val db: AppDatabase){
    val dao = db.habitAccess()

    fun getHabitInfo(habitId: Int): HabitInfo{
        return this.dao.getHabitInfo(habitId)
    }

    fun countActionsForDay(habit: Habit, day: Calendar): Int{
        val from: Calendar = day.clone() as Calendar
        from.set(Calendar.HOUR, 0)
        from.set(Calendar.MINUTE, 0)
        val to = day.clone() as Calendar
        to.set(Calendar.HOUR, 23)
        to.set(Calendar.MINUTE, 59)
        return this.dao.countHabitActions(habit.ordinal,
                from.timeInMillis, to.timeInMillis)
    }

    fun addHabitAction(habit: Habit, timeProvider: ()->Calendar){
        this.dao.insertAll(arrayOf(HabitRecord(
           timeProvider().timeInMillis, habit.ordinal
        )))
    }

}