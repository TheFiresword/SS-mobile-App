package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


//data class Profile(val id: String, val firstName: String, val lastName: String, val email: String, telephone, dispo)
fun createRescuer(requestQueue : RequestQueue, firstname: String, lastname:String, email:String, password:String, phoneNumber: String, success_callback: () -> Unit, error_callback: () -> Unit) {
    val url = "https://backend-service-3kjf.onrender.com/rescuers"
    val requestBody = JSONObject().apply {
        put("firstname", firstname)
        put("lastname", lastname)
        put("email", email)
        put("password", password)
        put("telephone", phoneNumber)
        put("disponibility", 0)
    }

    val jsonRequest = JsonObjectRequest(
        Request.Method.POST,
        url,
        requestBody,
        { response ->
            success_callback()
        },
        { error ->
            error_callback()
        }
    )
    requestQueue.add(jsonRequest)
}

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val title = findViewById<TextView>(R.id.Title)
        val firstName = findViewById<EditText>(R.id.firstNameInput)
        val lastName = findViewById<EditText>(R.id.lastNameInput)
        val phone = findViewById<EditText>(R.id.phoneInput)
        val email = findViewById<EditText>(R.id.emailInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val confPassword = findViewById<EditText>(R.id.confPasswordInput)
        val confirmation_button = findViewById<Button>(R.id.button_confirmation)
        val warningText = findViewById<TextView>(R.id.warningText)

        title.setText("Création d'un nouveau compte SST")

        confirmation_button.setOnClickListener {
            val firstNameC = firstName.text.toString()
            val lastNameC = lastName.text.toString()
            val phoneC = phone.text.toString()
            val emailC = email.text.toString()
            val passwordC = password.text.toString()
            val confPasswordC = confPassword.text.toString()

            //Erreurs dans le remplissage des champs :
            if (firstNameC.isEmpty() or lastNameC.isEmpty() or emailC.isEmpty() or phoneC.isEmpty()) {
                warningText.setVisibility(View.VISIBLE)
                warningText.setText("Veuillez remplir tous les champs !")
            }
            else if (passwordC != confPasswordC) {
                warningText.setVisibility(View.VISIBLE)
                warningText.setText("Les mots de passe ne sont pas les mêmes !")
            }
            else {
                requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
                createRescuer(requestQueue, firstNameC,lastNameC,emailC, passwordC, phoneC,
                    success_callback = {
                        val message = "Utilisateur ajouté avec succès"
                        Toast.makeText(applicationContext, "Success: $message", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, RescuerListActivity::class.java)
                        startActivity(intent)
                },
                    error_callback = {
                        val errorMessage = "Erreur dans la création de l'utilisateur"
                        Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    })
            }

        }
    }
}