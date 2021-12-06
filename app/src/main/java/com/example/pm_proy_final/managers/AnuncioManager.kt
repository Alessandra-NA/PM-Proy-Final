package com.example.pm_proy_final.managers


import android.util.Log
import android.widget.Toast
import com.example.pm_proy_final.models.Anuncio
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.coroutineContext


class AnuncioManager {
    private val dbFirebase = Firebase.firestore

    fun addAnuncio(titulo: String, distrito: String, descripcion: String, imagenURL: String, estado: Boolean, userid: String){
        val anuncio= hashMapOf(
          "titulo"  to titulo,
            "distrito" to distrito,
            "descripcion" to descripcion,
            "imagenURL" to imagenURL,
            "estado" to estado,
            "userid" to userid
        )
        dbFirebase.collection("anuncios").document(Date().time.toString()).set(anuncio)
    }

    fun eliminarAnuncio(anuncio: Anuncio, callbackOK: () -> Unit){
        dbFirebase.collection("anuncios").document(anuncio.id).get().addOnSuccessListener{
            it.reference.delete()
            callbackOK()
        }
    }
    fun getAllAnuncios(callbackOK : (List<Anuncio>) -> Unit, callbackError: (String) -> Unit){
       dbFirebase.collection("anuncios").get()
           .addOnSuccessListener {
               res->
               val anuncios = arrayListOf<Anuncio>()
               for(document in res){
                   val p = Anuncio(
                       document.id,
                        document.data["titulo"]!! as String,
                       document.data["distrito"]!! as String,
                       document.data["descripcion"]!! as String,
                       document.data["imagenURL"]!! as String,
                       document.data["estado"] as Boolean,
                       document.data["userid"] as String
                   )
                   anuncios.add(p)
               }
               callbackOK(anuncios)
           }
    }

    fun getByIdAnuncio(id:String,callbackOK : (Anuncio) -> Unit, callbackError: (String) -> Unit){
        dbFirebase.collection("anuncios")
            .get()
            .addOnSuccessListener {
                res->
                var p : Anuncio? =null
                for(document in res){
                    if(document.id==id){
                        Log.e("DETECCION","SE DETECTO EL ID")
                        p = Anuncio(
                            document.id,
                            document.data["titulo"]!! as String,
                            document.data["distrito"]!! as String,
                            document.data["descripcion"]!! as String,
                            document.data["imagenURL"]!! as String,
                            document.data["estado"] as Boolean,
                            document.data["userid"] as String)
                    }
                }
                callbackOK(p!!)
            }
    }

    fun getByUserIdAnuncio(id:String,callbackOK : (List<Anuncio>) -> Unit, callbackError: (String) -> Unit){
        dbFirebase.collection("anuncios")
            .whereEqualTo("userid",id)
            .get()
            .addOnSuccessListener {
                    res->
                val anuncios = arrayListOf<Anuncio>()
                for(document in res){
                    val p = Anuncio(
                        document.id,
                        document.data["titulo"]!! as String,
                        document.data["distrito"]!! as String,
                        document.data["descripcion"]!! as String,
                        document.data["imagenURL"]!! as String,
                        document.data["estado"] as Boolean,
                        document.data["userid"] as String
                    )
                    anuncios.add(p)
                }
                callbackOK(anuncios)
            }
    }

    fun EditarAnuncio(id:String, Anuncio:Anuncio){

        val anuncio1= hashMapOf(
            "titulo"  to Anuncio.titulo,
            "distrito" to Anuncio.distrito,
            "descripcion" to Anuncio.descripcion,
            "imagenURL" to Anuncio.imagenURL,
            "estado" to Anuncio.estado,
            "userid" to Anuncio.userid
        )
        dbFirebase.collection("anuncios")
            .document(id)
            .set(anuncio1)
    }






}