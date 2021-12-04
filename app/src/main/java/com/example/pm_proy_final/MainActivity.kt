package com.example.pm_proy_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pm_proy_final.fragments.AnunciosFragment
import com.example.pm_proy_final.fragments.ChatPrincipalFragment
import com.example.pm_proy_final.fragments.NuevoAnuncioFragment
import com.example.pm_proy_final.fragments.PerfilFragment
import com.example.pm_proy_final.managers.MensajeManager
import com.example.pm_proy_final.models.Usuario
import com.example.pm_proy_final.managers.UsuarioManager
import com.example.pm_proy_final.models.Mensaje
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), ChatPrincipalFragment.OnChatSelectedListener {
    lateinit var usuario: Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UsuarioManager().getUsuarioByUsername(intent.getBundleExtra("data")?.getString("username").toString()){
            usuario = it // obtiene user
        }
        changeAnunciosFragment()
        val bottomnav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomnav.setOnNavigationItemSelectedListener {
            println('a')
            when(it.itemId){
                R.id.home -> {
                    changeAnunciosFragment()
                    true
                }
                R.id.nuevoAnuncio -> {
                    changeNuevoAnuncioFragment()
                    true
                }
                R.id.chat -> {
                    changeChatPrincipalFragment()
                    true
                }
                R.id.perfil -> {
                    changePerfilFragment()
                    true
                }
                else -> false
            }
            true
        }
    }

    fun changeAnunciosFragment(){
        setTitle("Anuncios")
        var fragment = AnunciosFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
    fun changeNuevoAnuncioFragment(){
        setTitle("Nuevo anuncio")
        var fragment = NuevoAnuncioFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
    fun changeChatPrincipalFragment(){
        setTitle("Mis mensajes")
        MensajeManager().getPreviews(usuario.id){
            println(it[0].time)
            var fragment = ChatPrincipalFragment(it, usuario)
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragmentMain, fragment)
            ft.commit()
        }
    }
    fun changePerfilFragment(){
        setTitle("Perfil")
        var fragment = PerfilFragment(usuario)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }

    override fun onSelect(mensaje: Mensaje) {
        TODO("Not yet implemented")
    }
}