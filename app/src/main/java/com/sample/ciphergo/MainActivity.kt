package com.sample.ciphergo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dom.team.ciphergo.CipherGo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_ciluk.setOnClickListener {
            textView.text = CipherGo.ciluuuukk()
            Log.i("CILUK", CipherGo.ciluuuukk())
        }
    }
}
