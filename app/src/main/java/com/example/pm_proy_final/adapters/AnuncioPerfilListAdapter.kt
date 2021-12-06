package com.example.pm_proy_final.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pm_proy_final.R
import com.example.pm_proy_final.models.Anuncio

class AnuncioPerfilListAdapter (
    private val anuncioList : List<Anuncio>,
    private val fragment: Fragment,
    private val listener: (Anuncio)->Unit,
    private val listener2: (Anuncio)->Unit
) : RecyclerView.Adapter<AnuncioPerfilListAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        val listener: (Anuncio)->Unit,
        val anuncioList : List<Anuncio>
    ):
        RecyclerView.ViewHolder(view), View.OnClickListener
    {
        val txtitulo: TextView
        val txtEstadp: TextView
        val Editing: ImageView
        val trash : ImageView
        val imagen: ImageView
        init{
            txtitulo = view.findViewById(R.id.Titulo_anuncio_perfil)
            txtEstadp = view.findViewById(R.id.Estado_anunci_perfil)
            Editing = view.findViewById(R.id.editar_anuncio_perfil)
            trash = view.findViewById(R.id.trash_anuncio_perfil)
            imagen = view.findViewById(R.id.imagen_anuncio_perfil)

            Editing.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener(this.anuncioList[adapterPosition])
        }
    }



    override fun getItemCount(): Int {
        return this.anuncioList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anuncio_perfil,parent,false)
        val viewHolder = AnuncioPerfilListAdapter.ViewHolder(view,listener,this.anuncioList)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtitulo.text = anuncioList[position].titulo
        Glide.with(fragment)
            .load(anuncioList[position].imagenURL)
            .fitCenter()
            .into(holder.imagen)


        Glide.with(fragment)
            .load(R.drawable.ic_baseline_delete_24)
            .fitCenter()
            .into(holder.trash)


        Glide.with(fragment)
            .load(R.drawable.ic_baseline_edit_24)
            .fitCenter()
            .into(holder.Editing)


        holder.trash.setOnClickListener { listener2(anuncioList[position]) }

        if(anuncioList[position].estado) holder.txtEstadp.text = "ENCONTRADO"
        else holder.txtEstadp.text = "PERDIDO"
    }




}