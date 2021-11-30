package com.example.pm_proy_final.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pm_proy_final.R
import com.example.pm_proy_final.managers.MessagePreviewManager


class MessagePreviewListAdapter(
    private val fragment : Fragment,
    private val messagesPreviews: List<Int>,
    private val listener : (Int)->Unit
): RecyclerView.Adapter<MessagePreviewListAdapter.ViewHolder>() {

    class ViewHolder(view: View, val listener : (Int) -> Unit, val favoritosIds: List<Int>):
        RecyclerView.ViewHolder(view), View.OnClickListener
    {

        val txtNombre: TextView
        val txtTipo: TextView
        val txtMensaje: TextView
        init{
            txtNombre = view.findViewById(R.id.txtContactName)
            txtTipo = view.findViewById(R.id.txtMessageType)
            txtMensaje = view.findViewById(R.id.txtMessagePreview)
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener(adapterPosition)
            Log.e("MENSAJE", adapterPosition.toString())
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_previewmessage,parent,false)
        val viewHolder = MessagePreviewListAdapter.ViewHolder(view,listener,messagesPreviews)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: 30/11/2021  
    }
    override fun getItemCount(): Int {
        // TODO: 30/11/2021
        return 0
    }
}