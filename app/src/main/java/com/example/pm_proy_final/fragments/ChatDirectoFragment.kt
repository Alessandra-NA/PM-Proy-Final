package com.example.pm_proy_final.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.pm_proy_final.R
import com.example.pm_proy_final.adapters.MensajeListAdapter
import com.example.pm_proy_final.adapters.MessagePreviewListAdapter
import com.example.pm_proy_final.managers.MensajeManager
import com.example.pm_proy_final.models.Mensaje
import com.example.pm_proy_final.models.Usuario
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ChatDirectoFragment(var mensajes: ArrayList<Mensaje>,
                          var usuario: Usuario, var nameUsuario2: String, var idUsuario2: String): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatdirecto, container, false)
    }
    lateinit var imgUri: Uri
    val mainHandler = Handler(Looper.getMainLooper())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // parte del recyclerview
        val listaMensajes = view.findViewById<RecyclerView>(R.id.listaMensajes)
        var countMensajes = 0
        listaMensajes.adapter = MensajeListAdapter(mensajes, usuario)

        mainHandler.post(object : Runnable {
            override fun run() {
                MensajeManager().getCountMensajes2(usuario.id, idUsuario2){
                    if(it>countMensajes){
                        if(countMensajes!=0) {
                            MensajeManager().getMensajes2(usuario.id, idUsuario2){
                                listaMensajes.adapter = MensajeListAdapter(it, usuario)
                            }
                        }
                        countMensajes = it
                    }
                }
                mainHandler.postDelayed(this, 3000)
            }
        })

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                // doSomeOperations()
            }
        }

        // adjuntar imagen
        view.findViewById<ImageButton>(R.id.imgButtonFotoMensaje).setOnClickListener(){
            openGallery()
        }
        // parte de enviar mensaje
        view.findViewById<ImageButton>(R.id.imgButtonEnviarMensaje).setOnClickListener {
            if(view.findViewById<EditText>(R.id.edTxtEnviarMensaje).text.toString() == "") Toast.makeText(view.context,"Debe escribir algo", Toast.LENGTH_SHORT).show()
            else {
                MensajeManager().enviarMensaje(
                    usuario.id,
                    idUsuario2,
                    usuario.nombres + " " + usuario.apellidos,
                    nameUsuario2,
                    view.findViewById<EditText>(R.id.edTxtEnviarMensaje).text.toString(),
                    Date(),
                    ""
                )
                view.findViewById<EditText>(R.id.edTxtEnviarMensaje).setText("")
                MensajeManager().getMensajes2(usuario.id, idUsuario2){
                    listaMensajes.adapter = MensajeListAdapter(it, usuario)
                }
            }
        }
        listaMensajes.scrollToPosition(mensajes.size - 1)

    }
    fun openGallery(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(i, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.data != null) {
            imgUri = data.data!!
            val filename = UUID.randomUUID().toString()
            var fragment = EnviarImagenFragment(
                view?.findViewById<EditText>(R.id.edTxtEnviarMensaje)?.text.toString(),
                usuario, nameUsuario2, idUsuario2, imgUri, filename
            )
            val ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.fragmentMain, fragment)
            ft?.commit()
        }
    }

    override fun onStop() {
        super.onStop()
        mainHandler.removeCallbacksAndMessages(null);
    }
}