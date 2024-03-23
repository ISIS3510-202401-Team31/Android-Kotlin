package com.example.unifood

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.example.unifood.R
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.example.unifood.model.Preferences
import com.example.unifood.viewmodel.PreferencesViewModel
import com.squareup.picasso.Picasso
import java.util.regex.Pattern

class PreferencesActivity: AppCompatActivity() {

    private lateinit var viewModel: PreferencesViewModel
    private lateinit var pref: Preferences
    private lateinit var d_restrictions: TableRow
    private lateinit var tastes: TableRow
    private val REQUEST_SELECT_DIET = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        viewModel = PreferencesViewModel(this.application)

        d_restrictions = findViewById(R.id.res_layout)
        tastes = findViewById(R.id.tastes_layout)
        val mod_res: Button = findViewById(R.id.modify_res_button)
        val mod_tastes: Button = findViewById(R.id.modify_tastes_button)

        mod_res.setOnClickListener{
            val intent = Intent(this,PreferencesModRestActivity(pref.dietRestrictions)::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_DIET && resultCode == Activity.RESULT_OK) {
            val dietSelected = data?.getStringArrayListExtra("Restrictions selected")

            if (dietSelected != null) {
                for (dRes in dietSelected){
                    val imageView = ImageView(this)

                    Picasso.get().load(pref.dietRestrictions[dRes]).into(imageView)
                    imageView.setBackgroundColor(R.color.fifth)
                    imageView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    imageView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    imageView.setPadding(10,10,10,10)

                    d_restrictions.addView(imageView)
                }

            }
        }
    }

    //val pref = viewModel.getPreferences()
}