package com.sunnyweather.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.ui.place.PlaceAdapter
import com.sunnyweather.android.ui.place.PlaceViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val viewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)
//
//        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//        val adapter = PlaceAdapter(viewModel.placeList)
//        recyclerView.adapter = adapter
//
//        viewModel.searchPlaces("北京")
//        viewModel.placeLiveData.observe(this, Observer { result ->
//            val places = result.getOrNull()
//            if (places != null) {
//                viewModel.placeList.clear()
//                viewModel.placeList.addAll(places)
//                adapter.notifyDataSetChanged()
//            } else {
//                Toast.makeText(this, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
//                result.exceptionOrNull()?.printStackTrace()
//            }
//        })
    }
}