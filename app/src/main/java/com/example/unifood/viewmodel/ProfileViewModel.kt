package com.example.unifood.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.example.unifood.repository.ProfileRepository


class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val profileRepository = ProfileRepository(application.applicationContext)

    fun getLastKnownLocation(): Location? {
        return profileRepository.getLastKnownLocation()
    }
}

