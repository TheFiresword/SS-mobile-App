package insa.etudiant.sstou
import android.content.Intent
import android.widget.EditText
import android.content.Context

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView

class EnterLocationActivity : AppCompatActivity() {

    var defLocation : String = String()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_location)

        val locationEdit = findViewById<AutoCompleteTextView>(R.id.inputLocation)
        val confirmLoc = findViewById<Button>(R.id.confirmLocationButton)    //Bouton "confirmer"
        val retButton = findViewById<Button>(R.id.button_ret_0) //Bouton "retour"
        val listSalles = resources.getStringArray(R.array.listSalles)
        val arrayAdapter = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSalles)
        locationEdit.setAdapter(arrayAdapter)

        confirmLoc.setOnClickListener {              //Quand on clique sur le bouton confirmer
            val confirmedLoc = locationEdit.text.toString()               //On enregistre le texte
            if (confirmedLoc !in listSalles) {
                val warning_text = findViewById<TextView>(R.id.warning)
                warning_text.setVisibility(View.VISIBLE)
            }
            else {
                val intent = Intent(this, WaitingForHelpActivity::class.java)       //Se diriger vers l'activité "Suivante"
                intent.putExtra("location", confirmedLoc)
                defLocation = confirmedLoc
                startActivity(intent)
            }
        }

        retButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}