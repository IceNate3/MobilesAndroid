package com.example.howsthesky.weatherdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.howsthesky.*
import com.example.howsthesky.databinding.FragmentWeatherDetailBinding

class WeatherDetailFragment : Fragment() {

    lateinit var binding: FragmentWeatherDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var args = WeatherDetailFragmentArgs.fromBundle(requireArguments())

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_weather_detail,
            container,
            false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = WeatherDetailViewModelFactory(application)

        val weatherDetailViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(WeatherDetailViewModel::class.java)

        binding.weatherDetailViewModel = weatherDetailViewModel

        binding.setLifecycleOwner(this)

        weatherDetailViewModel.navigateToCurrentWeather.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(
                    WeatherDetailFragmentDirections
                        .actionWeatherDetailFragmentToCurrentWeatherFragment()
                )
                weatherDetailViewModel.doneNavigating()
            }
        })

        args.cityName.let {
            binding.cityNameHeading.text = formatTextToCapitalize(it)
            weatherDetailViewModel.getCompleteWeatherDetail(it)
        }

        weatherDetailViewModel.weatherData.observe(viewLifecycleOwner, {
            it?.let {
                binding.apply {
                    cityNameHeading.text = formatTextToCapitalize(it.cityName)
                    tempValueDetail.text = formatTemperatureString(it.mainWeatherData.temp)
                    weatherValueDetail.text =
                        formatTextToCapitalize(it.weatherDescList[0].description)
                    feelsLikeValue.text = formatTemperatureString(it.mainWeatherData.feelsLike)
                    minTempValue.text = formatTemperatureString(it.mainWeatherData.tempMin)
                    maxTempValue.text = formatTemperatureString(it.mainWeatherData.tempMax)
                    humidityValue.text = formatNumberToPercent(it.mainWeatherData.humidity)
                    windSpeedValue.text = formatWindSpeed(it.wind.speed)
                    cloudinessValue.text = formatNumberToPercent(it.cloudInfo.cloudiness)
                    visibilityValue.text = formatVisibilityValue(it.visibility)
                }
            }
        })

        val noInternetText = "Please Check Your Internet Connection and Try Again!!"

        weatherDetailViewModel.noInternetStatus.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(context, noInternetText, Toast.LENGTH_LONG).show()
                weatherDetailViewModel.doneShowingNoInternetToast()
            }
        })

        return binding.root
    }
}