package com.example.pm_proy_final.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pm_proy_final.R
import com.example.pm_proy_final.models.Usuario

class PerfilFragment(val usuario: Usuario): Fragment() {
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
    }
}