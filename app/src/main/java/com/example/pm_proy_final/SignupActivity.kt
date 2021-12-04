package com.example.pm_proy_final

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pm_proy_final.managers.UsuarioManager

class SignupActivity: AppCompatActivity() {
    private lateinit var txtUsername : String
    private lateinit var txtPassword : String
    private lateinit var txtNombres : String
    private lateinit var txtApellidos : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        // declarando variables
        txtUsername = findViewById<EditText>(R.id.edTxtUsername2).text.toString()
        txtPassword = findViewById<EditText>(R.id.edtxtPwd2).text.toString()
        txtNombres = findViewById<EditText>(R.id.edTxtNombres).text.toString()
        txtApellidos = findViewById<EditText>(R.id.edTxtApellidos).text.toString()
        findViewById<Button>(R.id.btnRegistro2).setOnClickListener{
            if(txtUsername == "" || txtPassword == "" || txtNombres == "" || txtApellidos == "") Toast.makeText(this,"Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            else {
                val intent : Intent = Intent()
                intent.setClass(this, LoginActivity::class.java)
                UsuarioManager().addUsuario(txtUsername,txtPassword,txtNombres,txtApellidos)
                Toast.makeText(this,"Registro exitoso",Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        }
        // TODO: 3/12/2021 Verificar usuario existente 
    }
}