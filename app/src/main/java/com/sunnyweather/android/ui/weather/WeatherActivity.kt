package com.sunnyweather.android.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastBinding
import com.sunnyweather.android.databinding.LifeIndexBinding
import com.sunnyweather.android.databinding.NowBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    lateinit var weatherbinding: ActivityWeatherBinding
    lateinit var nowBinding: NowBinding
    lateinit var forecastBinding: ForecastBinding
    lateinit var lifeIndexBinding: LifeIndexBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.statusBarColor = Color.TRANSPARENT
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        window.statusBarColor = Color.TRANSPARENT
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) //设置activity扩展到虚拟导航栏,但此时状态栏和导航栏都是半透明的
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //实际上不写这行，activity也扩展到了状态栏。
//        //使用下面代码可以达到与上面两行代码相同作用，并且配合其他FLAG可以使状态栏全透明
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY  or  View.SYSTEM_UI_FLAG_FULLSCREEN
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        //使用下面代码可以上面设置起到相同作用
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_weather)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
//        val placeName = findViewById<TextView>(R.id.placeName)
//        val currentTemp = findViewById<TextView>(R.id.currentTemp)
//        val currentSky = findViewById<TextView>(R.id.currentSky)
//        val currentAQI = findViewById<TextView>(R.id.currentAQI)
//        val nowLayout = findViewById<RelativeLayout>(R.id.nowLayout)
//        val forecastLayout = findViewById<LinearLayout>(R.id.forecastLayout)
//        val coldRiskText = findViewById<TextView>(R.id.coldRiskText)
//        val dressingText = findViewById<TextView>(R.id.dressingText)
//        val ultravioletText = findViewById<TextView>(R.id.ultravioletText)
//        val carWashingText = findViewById<TextView>(R.id.carWashingText)
//        val weatherLayout = findViewById<ScrollView>(R.id.weatherLayout)

        weatherbinding = ActivityWeatherBinding.inflate(layoutInflater)
//        nowBinding = NowBinding.inflate(layoutInflater)
//        forecastBinding = ForecastBinding.inflate(layoutInflater)
//        lifeIndexBinding = LifeIndexBinding.inflate(layoutInflater)
        setContentView(weatherbinding.root)

        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now.xml布局中的数据
        weatherbinding.includeNow.placeName.text = viewModel.placeName
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        weatherbinding.includeNow.currentTemp.text = currentTempText
        weatherbinding.includeNow.currentSky.text = getSky(realtime.skycon).info

        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        weatherbinding.includeNow.currentAQI.text = currentPM25Text
        weatherbinding.includeNow.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充forecast.xml布局中的数据
        weatherbinding.includeForecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                weatherbinding.includeForecast.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            weatherbinding.includeForecast.forecastLayout.addView(view)
        }

        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        weatherbinding.includeLife.coldRiskText.text = lifeIndex.coldRisk[0].desc
        weatherbinding.includeLife.dressingText.text = lifeIndex.dressing[0].desc
        weatherbinding.includeLife.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        weatherbinding.includeLife.carWashingText.text = lifeIndex.carWashing[0].desc
        weatherbinding.weatherLayout.visibility = View.VISIBLE
    }
}