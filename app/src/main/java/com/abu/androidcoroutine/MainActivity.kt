package com.abu.androidcoroutine

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.TextureView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.abu.androidcoroutine.view.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ab_clear) {
            findViewById<TextView>(R.id.tv_display).text = ""
        }
        return super.onOptionsItemSelected(item)
    }
}
