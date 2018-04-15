package hackerspace.hawajskadlakazdego

import android.widget.Button
import android.widget.TextView
import java.util.*
import android.graphics.Color

class HabitViewController(
        val view: Button, val label: TextView, val habit: Habit, val db: DatabaseAbstraction,
        val timeProvider: ()->Calendar){
    val habitInfo = db.getHabitInfo(this.habit.ordinal)

    init {
        view.setOnClickListener({this.increment()})
    }

    fun increment(){
        this.db.addHabitAction(this.habit, this.timeProvider)
        this.redraw()
    }

    fun redraw(){
        this.label.setText(this.habitInfo.label + " " +
                this.db.countActionsForDay(this.habit, this.timeProvider()) + "/" +
                this.habitInfo.top.toString())
        val counter = this.db.countActionsForDay(this.habit, this.timeProvider()).toFloat() / this.habitInfo.top.toFloat()
        if (counter < 0.2) {
            this.view.setBackgroundResource(R.drawable.shape1_4)
        } else if (counter < 0.4) {
            this.view.setBackgroundResource(R.drawable.shape2_4)

        } else if (counter < 0.6) {
            this.view.setBackgroundResource(R.drawable.shape3_4)

        } else if (counter < 0.9) {
            this.view.setBackgroundResource(R.drawable.shape4_4)

        } else {
            this.view.setBackgroundResource(R.drawable.shape5_4)

        }


    }
}