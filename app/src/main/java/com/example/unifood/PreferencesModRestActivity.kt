package com.example.unifood

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.example.unifood.R.layout
import com.example.unifood.R.id
import com.squareup.picasso.Picasso

class PreferencesModRestActivity(dRest: Map<String,String>) : AppCompatActivity() {
    private val dRest = dRest
    private var selected: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_preferences_mod_d_rest)

        val layout = findViewById<TableRow>(id.rest_layout)
        val acceptButton = findViewById<Button>(id.accept_button)

        for ((key, value) in dRest) {
            val imageButton = ImageButton(this)

            Picasso.get().load(value).into(imageButton)
            imageButton.setBackgroundResource(R.drawable.box_border_fourth_color)
            imageButton.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            imageButton.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            imageButton.setPadding(10,10,10,10)

            imageButton.setOnClickListener{
                if(selected.contains(key)){
                    selected.remove(key)
                    it.setBackgroundResource(R.drawable.box_border_main_color)

                }
                else{
                    selected.add(key)
                    it.setBackgroundResource(R.drawable.box_border_fourth_color)

                }
            }
            // Agregar el imageButton al TableRow
            layout.addView(imageButton)
        }

        acceptButton.setOnClickListener {

            val intent = Intent()
            val selL = selected.toList().toTypedArray()
            intent.putExtra("Restrictions selected", selL)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


}