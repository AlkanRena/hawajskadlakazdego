package hackerspace.hawajskadlakazdego

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add.*


class Habbit(max: Int, current: Int =0){
    public val max = max
    public var current = current
}

class HabbitController(max: Int, current: Int = 0, view: Button){
    val data = Habbit(max, current)
    val view = view

    init{
        view.setOnClickListener({
            this.data.current ++
            val ratio = this.data.current.toFloat() / this.data.max.toFloat()
                if(ratio<0.33)
                    this.view.setBackgroundColor( Color.rgb(255, 0, 32))
                else if(ratio < 0.66)
                    this.view.setBackgroundColor( Color.rgb(128, 128, 32))
                else
                    this.view.setBackgroundColor( Color.rgb(0, 255, 32))
            this.view.setText(this.view.text.toString() + this.data.current.toString())
        })
    }
}

class Add : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)


        val c1 = HabbitController(1, 0, findViewById(R.id.fatButton))
        val c2 = HabbitController(1, 0, findViewById(R.id.meatButton))
        val c3 = HabbitController(2, 0, findViewById(R.id.milkButton))
        val c4 = HabbitController(3, 0, findViewById(R.id.grainButton))
        val c5 = HabbitController(5, 0, findViewById(R.id.fruitsButton))
    }

}
