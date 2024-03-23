package com.example.unifood.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.unifood.model.Preferences
import com.example.unifood.repository.PreferencesRepository;
import org.json.JSONObject

class PreferencesViewModel(){
    private val preferencesRepository = PreferencesRepository()



    suspend fun getPreferences(): Preferences {
        val pref = preferencesRepository.getPreferences()
        val dRes = pref["Restrictions"] as Map<String, String>
        val tastes = pref["tastes"] as Map<String, String>


        val mDRes = dRes.filterKeys {!it.endsWith("text")}
        val mTastes = tastes.filterKeys {!it.endsWith("text")}

        return Preferences(mDRes, mTastes)

    }
}
