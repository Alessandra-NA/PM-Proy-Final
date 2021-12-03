package com.example.pm_proy_final

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pm_proy_final.models.UsuarioManager

class LoginActivity: AppCompatActivity() {
    private lateinit var txtUsername : String
    private lateinit var txtPassword : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.btnIniciarSesion).setOnClickListener{
            txtUsername = findViewById<EditText>(R.id.edtxtUsername).text.toString()
            txtPassword = findViewById<EditText>(R.id.edtxtPwd).text.toString()
            // TODO: 30/11/2021 Verificar campos vacíos
            if (txtUsername == "" || txtPassword == ""){
                Toast.makeText(this,"Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: 30/11/2021 Verificar usuario con la BD
                UsuarioManager().verificarExisteUsuario(txtUsername){ existe ->
                    if(existe){
                        // TODO: 30/11/2021 Verificar contraseña y usuario
                        UsuarioManager().verificarContraseña(txtUsername, txtPassword){
                            if (!it){
                                Toast.makeText(this,"Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this,"Ingreso exitoso", Toast.LENGTH_SHORT).show()
                                val intent : Intent = Intent()
                                intent.setClass(this, MainActivity::class.java)
                                val bundle : Bundle = Bundle()
                                bundle.putString("username", txtUsername)
                                intent.putExtra("data",bundle)
                                startActivity(intent)
                            }
                        }
                    }
                    else Toast.makeText(this,"El usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }

        }
        findViewById<Button>(R.id.btnRegistro1).setOnClickListener{
            val intent : Intent = Intent()
            intent.setClass(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }
}