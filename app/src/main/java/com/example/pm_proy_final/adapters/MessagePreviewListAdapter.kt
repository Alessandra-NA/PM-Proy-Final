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


class MessagePreviewListAdapter(
    private val mensajes: ArrayList<Mensaje>,
    private val usuario: Usuario,
    // TODO: 3/12/2021 listener al hacer click
    private val listener : (Mensaje)->Unit
): RecyclerView.Adapter<MessagePreviewListAdapter.ViewHolder>() {

    class ViewHolder(view: View, val listener : (Mensaje) -> Unit, val mensajes: ArrayList<Mensaje>):
        RecyclerView.ViewHolder(view), View.OnClickListener
    {

        val txtContactName: TextView
        val txtTipo: TextView
        val txtMensaje: TextView
        init{
            txtContactName = view.findViewById(R.id.txtContactNameChat)
            txtTipo = view.findViewById(R.id.txtMessageType)
            txtMensaje = view.findViewById(R.id.txtMessageChat)
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener(mensajes[adapterPosition])
            Log.e("MENSAJE", adapterPosition.toString())
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_previewmessage,parent,false)
        val viewHolder = ViewHolder(view, listener, mensajes)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mensajes[position].idReceiver == usuario.id){
            holder.txtContactName.text = mensajes[position].nameSender
            holder.txtTipo.text = "R:"
            holder.txtMensaje.text = mensajes[position].message
        } else {
            holder.txtContactName.text = mensajes[position].nameReceiver
            holder.txtTipo.text = "Tú:"
            holder.txtMensaje.text = mensajes[position].message
        }
    }
    override fun getItemCount(): Int {
        return mensajes.size
    }
}