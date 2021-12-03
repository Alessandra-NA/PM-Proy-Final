package com.example.pm_proy_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pm_proy_final.fragments.AnunciosFragment
import com.example.pm_proy_final.fragments.ChatPrincipalFragment
import com.example.pm_proy_final.fragments.NuevoAnuncioFragment
import com.example.pm_proy_final.fragments.PerfilFragment
import com.example.pm_proy_final.models.UsuarioManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        var fragment = ChatPrincipalFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
    fun changePerfilFragment(){
        setTitle("Perfil")
        var fragment = PerfilFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
}