package com.example.pm_proy_final

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pm_proy_final.fragments.*
import com.example.pm_proy_final.managers.AnuncioManager
import com.example.pm_proy_final.managers.MensajeManager
import com.example.pm_proy_final.models.Usuario
import com.example.pm_proy_final.managers.UsuarioManager
import com.example.pm_proy_final.models.Anuncio
import com.example.pm_proy_final.models.Mensaje
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), ChatPrincipalFragment.OnChatSelectedListener,
AnunciosFragment.OnAnuncioSelectedListener,PerfilFragment.OnAnuncioPerfilSelectedListener,
        AnuncioDetalleFragment.OnAnuncioDetalleIcons, PerfilFragment.OnPerfilAnuncioSelectedListener
{
    lateinit var usuario: Usuario
    var countMensajes = 0
    var countAnuncios = 0
    var currentFragment = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UsuarioManager().getUsuarioByUsername(intent.getBundleExtra("data")?.getString("username").toString()){
            usuario = it // obtiene user
            // loop para buscar nuevos mensajes
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post(object : Runnable {
                override fun run() {
                    checkMessageUpdates()
                    checkAnunciosUpdates()
                    mainHandler.postDelayed(this, 3000)
                }
            })
            changeAnunciosFragment()
        }
        createNotificationChannel()
        val bottomnav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomnav.setOnNavigationItemSelectedListener {
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
        currentFragment = "anuncios"
        setTitle("Anuncios")
        var fragment = AnunciosFragment(usuario)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
    fun changeNuevoAnuncioFragment(){
        currentFragment = "nuevo"
        setTitle("Nuevo anuncio")
        var fragment = NuevoAnuncioFragment(this.usuario.id)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
    fun changeChatPrincipalFragment(){
        setTitle("Mis mensajes")
        currentFragment = "chatsPreview"
        MensajeManager().getPreviews(usuario.id){
            var fragment = ChatPrincipalFragment(it, usuario)
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragmentMain, fragment)
            ft.commit()
        }
    }
    fun changeChatDirectoFragment(mensaje: Mensaje){
        currentFragment = "chat"
        var nameUsuario2: String
        var idUsuario2: String

        if(mensaje.idSender == usuario.id) {
            nameUsuario2 = mensaje.nameReceiver
            idUsuario2 = mensaje.idReceiver
        }
        else{
            nameUsuario2 = mensaje.nameSender
            idUsuario2 = mensaje.idSender
        }
        MensajeManager().getMensajes2(mensaje.idSender, mensaje.idReceiver){
            setTitle(nameUsuario2)
            var fragment = ChatDirectoFragment(it, usuario, nameUsuario2, idUsuario2)
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragmentMain, fragment)
            ft.commit()
        }
    }
    fun changePerfilFragment(){
        currentFragment = "perfil"
        setTitle("Perfil")
        var fragment = PerfilFragment(usuario, usuario)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }

    fun changeDetalleAnuncio(anuncio1: Anuncio){
        currentFragment = "detalle"
        setTitle("Detalle producto")
        var fragment = AnuncioDetalleFragment(anuncio1, usuario)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
    fun changeEditarAnuncio(anuncio1:Anuncio){
        currentFragment = "editar"
        setTitle("Editando anuncio")
        var fragment = EditarAnuncioFragment(anuncio1)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentMain, fragment)
        ft.commit()
    }
    fun checkAnunciosUpdates(){
        AnuncioManager().getCountAnuncios(){
            if(it>countAnuncios){
                if(countAnuncios!=0) {
                    sendNotification()
                    if (currentFragment == "anuncios") changeAnunciosFragment()
                    //if (currentFragment == "chat")  ChatDirectoFragment("","","","").
                }
                countAnuncios = it
            }
        }
    }

    fun checkMessageUpdates(){
        MensajeManager().getCountMensajes(usuario.id){
            if(it>countMensajes){
                if(countMensajes!=0) {
                    sendNotification()
                    if (currentFragment == "chatsPreview") changeChatPrincipalFragment()
                    //if (currentFragment == "chat")  ChatDirectoFragment("","","","").
                }
                countMensajes = it
            }
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "FinderApp"
            val description = "Mensaje"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                setDescription(description)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("FinderApp!")
            .setContentText("Tienes un nuevo mensaje.")
            .setPriority(NotificationCompat.PRIORITY_MAX)
        with(NotificationManagerCompat.from(this)){
            notify(101, builder.build())
        }
    }

    override fun onSelect(mensaje: Mensaje) {
        changeChatDirectoFragment(mensaje)
    }

    override fun onInfomacion(beta: Anuncio) {
        changeDetalleAnuncio(beta)
//        Toast.makeText(this,beta.titulo, Toast.LENGTH_SHORT).show()
    }

    override fun onChat(anuncio: Anuncio) {
        MensajeManager().getMensajes2(usuario.id, anuncio.userid){
            UsuarioManager().getUsuarioById(anuncio.userid){ usr ->
                setTitle((usr.nombres + " " + usr.apellidos).capitalize())
                var fragment = ChatDirectoFragment(it, usuario, usr.nombres + " " + usr.apellidos, usr.id)
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fragmentMain, fragment)
                ft.commit()
            }
        }
    }

    override fun onEditing(anuncio1: Anuncio) {
        changeEditarAnuncio(anuncio1)

    }

    override fun onDelete(anuncio2: Anuncio) {
        showAlertDialog(anuncio2)
    }

    private fun showAlertDialog(anuncio: Anuncio) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("Alerta")
        alertDialog.setMessage("¿Quieres eliminar este anuncio?")
        alertDialog.setPositiveButton(
            "Eliminar"
        ) { _, _ ->
            AnuncioManager().eliminarAnuncio(anuncio){
                Toast.makeText(this, "Anuncio eliminado", Toast.LENGTH_SHORT).show()
                changePerfilFragment()
            }
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    override fun ChatDetalle(anuncio: Anuncio) {
        MensajeManager().getMensajes2(usuario.id, anuncio.userid){
            UsuarioManager().getUsuarioById(anuncio.userid){ usr ->
                setTitle((usr.nombres + " " + usr.apellidos).capitalize())
                var fragment = ChatDirectoFragment(it, usuario, usr.nombres + " " + usr.apellidos, usr.id)
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fragmentMain, fragment)
                ft.commit()
            }
        }
    }

    override fun PerfilDetalle(anuncio: Anuncio) {
        UsuarioManager().getUsuarioById(anuncio.userid){ usr ->
            setTitle((usr.nombres + " " + usr.apellidos).capitalize())
            var fragment = PerfilFragment(usr, usuario)
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragmentMain, fragment)
            ft.commit()
        }
    }

    override fun onInfomacion2(anuncio: Anuncio) {
        println("oninformacion2")
        changeDetalleAnuncio(anuncio)
    }

    override fun onChat2(anuncio: Anuncio) {
        MensajeManager().getMensajes2(usuario.id, anuncio.userid){
            UsuarioManager().getUsuarioById(anuncio.userid){ usr ->
                setTitle((usr.nombres + " " + usr.apellidos).capitalize())
                var fragment = ChatDirectoFragment(it, usuario, usr.nombres + " " + usr.apellidos, usr.id)
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fragmentMain, fragment)
                ft.commit()
            }
        }
    }
}