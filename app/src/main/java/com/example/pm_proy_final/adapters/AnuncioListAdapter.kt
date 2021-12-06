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

class AnuncioListAdapter(
    private val anuncioList : List<Anuncio>,
    private val fragment: Fragment,
    private val listener: (Anuncio)->Unit,
    private val listener2: (Anuncio)->Unit
) :RecyclerView.Adapter<AnuncioListAdapter.ViewHolder>() {

        class ViewHolder(
            view: View ,
            val listener: (Anuncio)->Unit,
            val anuncioList : List<Anuncio>
                         ):
            RecyclerView.ViewHolder(view), View.OnClickListener
        {
            val txtitulo: TextView
            val txtEstadp: TextView
            val InformacionI: ImageView
            val coment : ImageView
            val imagen: ImageView
            init{
                txtitulo = view.findViewById(R.id.Tituloanuncio)
                txtEstadp = view.findViewById(R.id.Estadoanuncio)
                InformacionI = view.findViewById(R.id.informacionanuncio)
                coment = view.findViewById(R.id.comentanuncio)
                imagen = view.findViewById(R.id.imagenanuncio)

                InformacionI.setOnClickListener(this)
//
            }



            override fun onClick(p0: View?) {
                listener(this.anuncioList[adapterPosition])
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anuncio,parent,false)
        val viewHolder = AnuncioListAdapter.ViewHolder(view,listener,this.anuncioList)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtitulo.text = anuncioList[position].titulo
        Glide.with(fragment)
            .load(anuncioList[position].imagenURL)
            .fitCenter()
            .into(holder.imagen)

        Glide.with(fragment)
            .load(R.drawable.ic_launcher_information)
            .fitCenter()
            .into(holder.InformacionI)

        Glide.with(fragment)
            .load(R.drawable.ic_baseline_chat_24)
            .fitCenter()
            .into(holder.coment)

        holder.coment.setOnClickListener { listener2(anuncioList[position]) }

        if(anuncioList[position].estado) holder.txtEstadp.text = "ENCONTRADO"
        else holder.txtEstadp.text = "PERDIDO"

//

    }


    override fun getItemCount(): Int {
        return this.anuncioList.size
    }

}