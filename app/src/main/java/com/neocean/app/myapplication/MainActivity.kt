package com.neocean.app.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import com.neocean.app.library.MultiStateView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn1.setOnClickListener {
            ms_view.viewState = MultiStateView.VIEW_STATE_CONTENT
        }
        btn2.setOnClickListener {
            ms_view.viewState = MultiStateView.VIEW_STATE_ERROR
        }
        btn3.setOnClickListener {
            ms_view.viewState = MultiStateView.VIEW_STATE_EMPTY
        }
        btn4.setOnClickListener {
            ms_view.viewState = MultiStateView.VIEW_STATE_LOADING
        }
    }
}
