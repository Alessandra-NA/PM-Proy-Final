package com.example.pm_proy_final.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pm_proy_final.R
import com.example.pm_proy_final.models.Mensaje
import com.example.pm_proy_final.models.Usuario
import java.text.SimpleDateFormat

class MensajeListAdapter(val mensajes: ArrayList<Mensaje>, val usuario: Usuario): RecyclerView.Adapter<MensajeListAdapter.ViewHolder>() {

    class ViewHolder(view: View, val mensajes: ArrayList<Mensaje>, val usuario: Usuario):
        RecyclerView.ViewHolder(view)
    {
        val txtContactNameChat: TextView
        val txtMessageChat: TextView
        val txtDateChat: TextView
        init{
            txtContactNameChat = view.findViewById(R.id.txtContactNameChat)
            txtMessageChat = view.findViewById(R.id.txtMessageChat)
            txtDateChat = view.findViewById(R.id.txtDateChat)
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
        if(mensajes[position].idSender == usuario.id) holder.txtContactNameChat.text = mensajes[position].nameReceiver
        else holder.txtContactNameChat.text = mensajes[position].nameSender
        holder.txtMessageChat.text = mensajes[position].message
        holder.txtDateChat.text = SimpleDateFormat("dd/M/yyyy hh:mm").format(mensajes[position].time)
    }

    override fun getItemCount(): Int {
        return mensajes.size
    }
}