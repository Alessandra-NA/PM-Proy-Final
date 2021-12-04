package com.example.pm_proy_final.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pm_proy_final.R
import com.example.pm_proy_final.adapters.MessagePreviewListAdapter
import com.example.pm_proy_final.models.Mensaje
import com.example.pm_proy_final.models.Usuario

class ChatPrincipalFragment(val mensajes: ArrayList<Mensaje>, val usuario: Usuario): Fragment() {
    interface OnChatSelectedListener{
        fun onSelect(mensaje: Mensaje)
    }
    private var listener: OnChatSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnChatSelectedListener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatprincipal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listaMsj = view.findViewById<RecyclerView>(R.id.listaPreviewMensajes)
        listaMsj.adapter = MessagePreviewListAdapter(mensajes, usuario){
            listener?.onSelect(it)
        }
    }
}