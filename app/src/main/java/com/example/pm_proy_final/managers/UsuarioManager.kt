package com.example.pm_proy_final.managers

import com.example.pm_proy_final.models.Usuario
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class UsuarioManager() {
    private val dbFirebase = Firebase.firestore
    fun addUsuario(username: String, password: String, nombres: String, apellidos: String){
        val user = hashMapOf(
            "username" to username,
            "password" to password,
            "nombres" to nombres,
            "apellidos" to apellidos
        )
        dbFirebase.collection("usuarios").document(Date().time.toString()).set(user)
    }
    fun verificarContraseña(username: String, password: String, callbackOK: (Boolean) -> Unit){
        dbFirebase.collection("usuarios").whereEqualTo("username", username).get().addOnSuccessListener { res ->
            if(res.documents[0].data?.get("password").toString() == password) callbackOK(true)
            else callbackOK(false)
        }
    }
    fun getUsuarioByUsername(username: String, callbackOK: (Usuario) -> Unit){
        dbFirebase.collection("usuarios").whereEqualTo("username", username).get().addOnSuccessListener { res ->
            val usuario = Usuario(
                res.documents[0].id.toString(),
                res.documents[0].data?.get("username").toString(),
                res.documents[0].data?.get("password").toString(),
                res.documents[0].data?.get("nombres").toString(),
                res.documents[0].data?.get("apellidos").toString()
            )
            callbackOK(usuario)
        }
    }
    fun verificarExisteUsuario(username: String, callbackOK: (Boolean) -> Unit){
        dbFirebase.collection("usuarios").whereEqualTo("username", username).get()
            .addOnSuccessListener {
            if(it.isEmpty) callbackOK(false)
            else callbackOK(true)
        }.addOnFailureListener{
            callbackOK(false)
        }
    }
}