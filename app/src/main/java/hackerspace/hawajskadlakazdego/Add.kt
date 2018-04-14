package hackerspace.hawajskadlakazdego

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add.*



class Add : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val button = findViewById(R.id.button) as Button;
        button.setOnClickListener({button.setText("dupa")
        })
    }

}
