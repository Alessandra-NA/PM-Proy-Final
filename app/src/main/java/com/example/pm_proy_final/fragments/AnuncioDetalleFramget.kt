package com.example.pm_proy_final.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pm_proy_final.R
import com.example.pm_proy_final.models.Anuncio

class AnuncioDetalleFramget(anuncio:Anuncio): Fragment() {

    interface OnAnuncioDetalleIcons{
        fun ChatDetalle(beta: Anuncio)
        fun PerfilDetalle(alpha: Anuncio)
    }

    private var listener: OnAnuncioDetalleIcons?=null
    private var anuncio : Anuncio = anuncio

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnAnuncioDetalleIcons
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle_anuncio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //texto
        val descripcion = view.findViewById<TextView>(R.id.descripcion_detalle)
        val titulo = view.findViewById<TextView>(R.id.titulo_detalle)
        val distrito = view.findViewById<TextView>(R.id.distrito_detalle)
        val estado = view.findViewById<TextView>(R.id.Estado_detalle)
        val imagen = view.findViewById<ImageView>(R.id.imagen_detalle)

        //iconos
        val chat = view.findViewById<ImageView>(R.id.chat_detalle_anuncio)
        val perfil = view.findViewById<ImageView>(R.id.perfil_detalle_anuncio)

        descripcion.text= this.anuncio.descripcion
        titulo.text = this.anuncio.titulo
        distrito.text = this.anuncio.distrito
        if(this.anuncio.estado) estado.text = "ENCONTRADO"
        else estado.text = "PERDIDO"
        Glide.with(this)
            .load(anuncio.imagenURL)
            .fitCenter()
            .into(imagen)

        chat.setOnClickListener {
            listener?.ChatDetalle(anuncio)
        }

        perfil.setOnClickListener {
            listener?.PerfilDetalle(anuncio)
        }

    }

}