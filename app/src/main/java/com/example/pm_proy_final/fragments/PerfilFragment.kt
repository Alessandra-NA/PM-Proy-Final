package com.example.pm_proy_final.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pm_proy_final.R
import com.example.pm_proy_final.adapters.AnuncioListAdapter
import com.example.pm_proy_final.adapters.AnuncioPerfilListAdapter
import com.example.pm_proy_final.managers.AnuncioManager
import com.example.pm_proy_final.models.Anuncio
import com.example.pm_proy_final.models.Usuario

class PerfilFragment(val usuario: Usuario, val usuarioLogin: Usuario): Fragment() {

    interface OnPerfilAnuncioSelectedListener {
        fun onInfomacion2(anuncio: Anuncio)
        fun onChat2(anuncio: Anuncio)
    }

    interface OnAnuncioPerfilSelectedListener{
        fun onEditing(anuncio1: Anuncio)
        fun onDelete(anuncio2: Anuncio)
    }

    private var user: Usuario ?= usuario
    private var listener: OnAnuncioPerfilSelectedListener?=null
    private var listener2: OnPerfilAnuncioSelectedListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnAnuncioPerfilSelectedListener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.txtUsernamePerfil).text = usuario.username
        view.findViewById<TextView>(R.id.txtNombresPerfil).text = usuario.nombres
        view.findViewById<TextView>(R.id.txtApellidosPerfil).text = usuario.apellidos



       if(usuario.id == usuarioLogin.id) {
           AnuncioManager().getByUserIdAnuncio(this.usuario.id,{
               val recycListadoAnuncioPerfil = view.findViewById<RecyclerView>(R.id.lista_anuncios_perfil)
               recycListadoAnuncioPerfil.adapter=AnuncioPerfilListAdapter(it,this,{
                       anuncio1: Anuncio ->
                   listener?.onEditing(anuncio1)
               },{ anuncio2: Anuncio ->
                   listener?.onDelete(anuncio2)
               })
           },{
               Toast.makeText(context,"Couldnt get the annouces", Toast.LENGTH_SHORT).show()
           })
       }
       else {
           AnuncioManager().getByUserIdAnuncio(this.usuario.id,{
               val recycListadoAnuncio = view.findViewById<RecyclerView>(R.id.lista_anuncios_perfil)
               recycListadoAnuncio.adapter = AnuncioListAdapter(usuarioLogin, it, this, {
                       anuncio1: Anuncio ->
                   println("pre oninfo2")
                   listener2?.onInfomacion2(anuncio1)
               },{
                       anuncio2: Anuncio ->
                   listener2?.onChat2(anuncio2)
               })
           },{
               Toast.makeText(context,"Couldnt get the annouces", Toast.LENGTH_SHORT).show()
           })
       }

    }
}