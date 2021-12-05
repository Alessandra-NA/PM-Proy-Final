package com.example.pm_proy_final.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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




class ChatDirectoFragment(var mensajes: ArrayList<Mensaje>,
                          var usuario: Usuario, var nameUsuario2: String, var idUsuario2: String): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatdirecto, container, false)
    }
    val mainHandler = Handler(Looper.getMainLooper())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // parte del recyclerview
        val listaMensajes = view.findViewById<RecyclerView>(R.id.listaMensajes)
        var countMensajes = 0
        listaMensajes.adapter = MensajeListAdapter(mensajes, usuario)
        mainHandler.post(object : Runnable {
            override fun run() {
                println("cargando2")
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
    }

    override fun onStop() {
        super.onStop()
        mainHandler.removeCallbacksAndMessages(null);
    }
}