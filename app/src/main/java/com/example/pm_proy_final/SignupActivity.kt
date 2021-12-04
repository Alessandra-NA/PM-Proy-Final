package com.example.pm_proy_final

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pm_proy_final.managers.UsuarioManager

class SignupActivity: AppCompatActivity() {
    private lateinit var txtUsername : EditText
    private lateinit var txtPassword : EditText
    private lateinit var txtNombres : EditText
    private lateinit var txtApellidos : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        // declarando variables
        txtUsername = findViewById<EditText>(R.id.edTxtUsername2)
        txtPassword = findViewById<EditText>(R.id.edtxtPwd2)
        txtNombres = findViewById<EditText>(R.id.edTxtNombres)
        txtApellidos = findViewById<EditText>(R.id.edTxtApellidos)

        findViewById<Button>(R.id.btnRegistro2).setOnClickListener{
            UsuarioManager().verificarExisteUsuario(txtUsername.text.toString()){ yaExiste ->
                if(txtUsername.text.toString() == "" || txtPassword.text.toString() == "" || txtNombres.text.toString() == "" || txtApellidos.text.toString() == "") Toast.makeText(this,"Debe completar todos los campos", Toast.LENGTH_SHORT).show()
                else if (yaExiste) Toast.makeText(this,"El usuario ya existe",Toast.LENGTH_SHORT).show()
                else {
                    val intent : Intent = Intent()
                    intent.setClass(this, LoginActivity::class.java)
                    UsuarioManager().addUsuario(txtUsername.text.toString(),txtPassword.text.toString(),txtNombres.text.toString(),txtApellidos.text.toString())

                    startActivity(intent)
                }
            }
        }
    }
}