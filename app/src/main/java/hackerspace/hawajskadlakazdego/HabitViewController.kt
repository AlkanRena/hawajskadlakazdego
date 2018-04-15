package hackerspace.hawajskadlakazdego

import android.widget.Button
import java.util.*

class HabitViewController(
        val view: Button, val habit: Habit, val db: DatabaseAbstraction){
    val habitInfo = db.getHabitInfo(this.habit.ordinal)

    init {
        view.setOnClickListener({this.increment()})
    }

    fun increment(){
        this.db.addHabitAction(this.habit)
        this.redraw()
    }

    fun redraw(){
        this.view.setText(this.habitInfo.label + " " +
                this.db.countActionsForDay(this.habit, Calendar.getInstance()) + "/" +
                this.habitInfo.top.toString())
    }
}