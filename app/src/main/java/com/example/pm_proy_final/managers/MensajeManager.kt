package com.example.pm_proy_final.managers

import com.example.pm_proy_final.models.Mensaje
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class MensajeManager {
    private val dbFirebase = Firebase.firestore
    fun enviarMensaje(idSender: String, idReceiver: String, nameSender: String, nameReceiver: String, message: String, time: Date, img: String){
        val msg = hashMapOf(
            "idSender" to idSender,
            "idReceiver" to idReceiver,
            "nameSender" to nameSender,
            "nameReceiver" to nameReceiver,
            "message" to message,
            "time" to Timestamp(time),
            "img" to img
        )
        dbFirebase.collection("mensajes").document(Date().time.toString()).set(msg)
    }
    fun getMensajes1(id: String, callbackOK: (ArrayList<Mensaje>) -> Unit){
        dbFirebase.collection("mensajes").get().addOnSuccessListener {
            var messagesList = ArrayList<Mensaje>()
            for(document in it){
                var idSender = document.data.get("idSender").toString()
                var idReceiver = document.data.get("idReceiver").toString()
                if(idSender == id || idReceiver == id){
                    println(document)
                    val date = document.data.get("time") as com.google.firebase.Timestamp
                    val msg = Mensaje(
                        document.id,
                        idSender,
                        idReceiver,
                        document.data.get("nameSender").toString(),
                        document.data.get("nameReceiver").toString(),
                        document.data.get("message").toString(),
                        date.toDate(),
                        document.data.get("img").toString()
                    )
                    messagesList.add(msg)
                }
            }
            // ordenando lista
            messagesList.sortWith(compareByDescending { it.time })
            callbackOK(messagesList)
        }

    }
    fun getMensajes2(id1: String, id2: String, callbackOK: (ArrayList<Mensaje>) -> Unit){
        dbFirebase.collection("mensajes").get().addOnSuccessListener {
            var messagesList = ArrayList<Mensaje>()
            for(document in it){
                var idSender = document.data.get("idSender").toString()
                var idReceiver = document.data.get("idReceiver").toString()
                if((idSender == id1 && idReceiver == id2) || (idSender == id2 && idReceiver == id1)){
                    println(document)
                    val date = document.data.get("time") as com.google.firebase.Timestamp
                    val msg = Mensaje(
                        document.id,
                        idSender,
                        idReceiver,
                        document.data.get("nameSender").toString(),
                        document.data.get("nameReceiver").toString(),
                        document.data.get("message").toString(),
                        date.toDate(),
                        document.data.get("img").toString()
                    )
                    messagesList.add(msg)
                }
            }
            // ordenando lista
            messagesList.sortWith(compareByDescending { it.time })
            callbackOK(messagesList)
        }
    }
    fun getPreviews(id: String, callbackOK: (ArrayList<Mensaje>) -> Unit){
        getMensajes1(id){ mensajes ->
            var previews = ArrayList<Mensaje>()
            var idUsuarios = ArrayList<String>()
            for(mensaje in mensajes){
                if(mensaje.idSender != id && mensaje.idReceiver == id){
                    if(!idUsuarios.contains(mensaje.idSender)){
                        idUsuarios.add(mensaje.idSender)
                        previews.add(mensaje)
                    }
                } else {
                    if(!idUsuarios.contains(mensaje.idReceiver)){
                        idUsuarios.add(mensaje.idReceiver)
                        previews.add(mensaje)
                    }
                }
            }
            callbackOK(previews)
        }
    }
}