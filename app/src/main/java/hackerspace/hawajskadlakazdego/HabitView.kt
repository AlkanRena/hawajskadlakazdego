package hackerspace.hawajskadlakazdego

import android.widget.Button

class HabitView(val view: Button, val counter: Int, val top: Int, val label: String, val dbid: Int){

    fun redraw(){
        this.view.setText(this.label + " " + this.counter.toString() + "/" + this.top.toString())
    }
}