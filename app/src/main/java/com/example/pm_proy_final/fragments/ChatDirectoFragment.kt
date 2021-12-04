package com.example.pm_proy_final.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // parte del recyclerview
        val listaMensajes = view.findViewById<RecyclerView>(R.id.listaMensajes)
        listaMensajes.adapter = MensajeListAdapter(mensajes, usuario)
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
            }
            // TODO: 4/12/2021 actualizar luego de enviar mensaje 
        }
    }
}