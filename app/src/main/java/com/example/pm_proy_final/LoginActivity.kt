package com.example.pm_proy_final

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    private lateinit var txtCorreo : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtCorreo = findViewById(R.id.edtxtUsername)
        findViewById<Button>(R.id.btnIniciarSesion).setOnClickListener{
            // SOLO ESTAMOS PASANDO EL USUARIO AL MAIN POR AHORA para poder trabajar con lo demás
            // TODO: 30/11/2021 Verificar usuario con la BD 
            // TODO: 30/11/2021 Verificar contraseña y usuario
            // TODO: 30/11/2021 Verificar campos vacíos 
            val intent : Intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            val bundle : Bundle = Bundle()
            bundle.putString("username", txtCorreo.text.toString())
            intent.putExtra("data",bundle)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btnRegistro1).setOnClickListener{
            val intent : Intent = Intent(this, SignupActivity::class.java)
            startActivityForResult(intent, 10)
        }
    }
}