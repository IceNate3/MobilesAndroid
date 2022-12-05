package com.example.howsthesky.helper

import android.media.Image
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.howsthesky.R
import com.example.howsthesky.convertImgIdToUri
import com.example.howsthesky.formatTemperatureString
import com.example.howsthesky.formatWeatherDescription
import kotlinx.android.synthetic.main.list_item_recent_cities.view.*
import kotlin.math.absoluteValue

@BindingAdapter("cityName")
fun TextView.setCityName(item: Weather?) {
    item?.let {
        text = item.cityName
    }
}

@BindingAdapter("cityTemperature")
fun TextView.setCityTemperature(item: Weather?) {
    item?.let {
        text = formatTemperatureString(item.temperature)
    }
}

@BindingAdapter("weatherDescription")
fun TextView.setWeatherDescription(item: Weather?) {
    item?.let {
        text = formatWeatherDescription(item.weatherDescription)
    }
}

@BindingAdapter("imgId")
fun ImageView.setWeatherImage(item: Weather?) {
    item?.let {
        val imgUri = convertImgIdToUri(item.appIconId)
        val imgView = city_weather_image
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.mipmap.unknown_weather))
            .into(imgView)
    }
}

@BindingAdapter("imageId")
fun bindImage(imgView: ImageView, imgId: String?) {
    imgId?.let {
        val imgUri = convertImgIdToUri(imgId)
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.mipmap.unknown_weather))
            .into(imgView)
    }
}
