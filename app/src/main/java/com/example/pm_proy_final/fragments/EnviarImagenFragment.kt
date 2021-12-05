package com.example.pm_proy_final.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.pm_proy_final.R
import com.example.pm_proy_final.managers.MensajeManager
import com.example.pm_proy_final.models.Usuario
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class EnviarImagenFragment(var mensaje: String,
                           var usuario: Usuario, var nameUsuario2: String,
                           var idUsuario2: String, var img: Uri, var imgName: String): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_uploadimage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bitmap = MediaStore.Images.Media.getBitmap(this.activity?.contentResolver, img)
        var bitmapDrawable = BitmapDrawable(bitmap)
        view.findViewById<ImageView>(R.id.imgPhotoMensaje).setImageDrawable(bitmapDrawable)
        view.findViewById<EditText>(R.id.edTxtEnviarFoto).setText(mensaje)
        view.findViewById<ImageButton>(R.id.imgButtonEnviarFoto).setOnClickListener(){
            val ref = FirebaseStorage.getInstance().getReference("/images/$imgName")
            ref.putFile(img)
            MensajeManager().enviarMensaje(usuario.id, idUsuario2, usuario.nombres + " " + usuario.apellidos,
                nameUsuario2, view.findViewById<EditText>(R.id.edTxtEnviarFoto).text.toString(), Date(), imgName
            )
            MensajeManager().getMensajes2(usuario.id, idUsuario2){
                var fragment = ChatDirectoFragment(it, usuario, nameUsuario2, idUsuario2)
                val ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.fragmentMain, fragment)
                ft?.commit()
            }
        }
    }
}