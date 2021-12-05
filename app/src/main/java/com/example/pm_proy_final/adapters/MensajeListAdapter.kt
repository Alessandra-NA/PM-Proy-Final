package com.example.pm_proy_final.adapters

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.pm_proy_final.R
import com.example.pm_proy_final.models.Mensaje
import com.example.pm_proy_final.models.Usuario
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat

class MensajeListAdapter(val mensajes: ArrayList<Mensaje>, val usuario: Usuario): RecyclerView.Adapter<MensajeListAdapter.ViewHolder>() {

    class ViewHolder(view: View, val mensajes: ArrayList<Mensaje>, val usuario: Usuario):
        RecyclerView.ViewHolder(view)
    {
        val txtContactNameChat: TextView
        val txtMessageChat: TextView
        val txtDateChat: TextView
        val imgPhoto: ImageView
        init{
            txtContactNameChat = view.findViewById(R.id.txtContactNameChat)
            txtMessageChat = view.findViewById(R.id.txtMessageChat)
            txtDateChat = view.findViewById(R.id.txtDateChat)
            imgPhoto = view.findViewById(R.id.imgPhotoChat)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message,parent,false)
        val viewHolder = MensajeListAdapter.ViewHolder(view, mensajes, usuario)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtContactNameChat.text = mensajes[position].nameSender
        holder.txtMessageChat.text = mensajes[position].message
        holder.txtDateChat.text = SimpleDateFormat("dd/M/yyyy hh:mm").format(mensajes[position].time)
        if (mensajes[position].img == "") {
            holder.imgPhoto.layoutParams.height = 0
        } else {
            val imgName = mensajes[position].img
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$imgName")
            val localfile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                var bitmapDrawable = BitmapDrawable(bitmap)
                holder.imgPhoto.setImageDrawable(bitmapDrawable)
            }
        }
    }

    override fun getItemCount(): Int {
        return mensajes.size
    }
}