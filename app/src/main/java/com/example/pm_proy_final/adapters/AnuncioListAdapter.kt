package com.example.pm_proy_final.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pm_proy_final.R
import com.example.pm_proy_final.models.Anuncio
import com.example.pm_proy_final.models.Usuario
import java.util.stream.Collectors

class AnuncioListAdapter(
    private val usuario: Usuario,
    private var anuncioList : ArrayList<Anuncio>,
    private val fragment: Fragment,
    private val listener: (Anuncio)->Unit,
    private val listener2: (Anuncio)->Unit
) :RecyclerView.Adapter<AnuncioListAdapter.ViewHolder>() {

    private var listaOriginal = ArrayList<Anuncio>()
        init{
            listaOriginal.addAll(anuncioList)
        }

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
                listener(this.anuncioList.get(adapterPosition))
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anuncio,parent,false)
        val viewHolder = AnuncioListAdapter.ViewHolder(view,listener,this.anuncioList)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println(anuncioList.get(position).userid == usuario.id)
        if(anuncioList.get(position).userid == usuario.id) {
            holder.coment.visibility = View.INVISIBLE
            holder.coment.marginStart
        }
        holder.txtitulo.text = anuncioList.get(position).titulo
        Glide.with(fragment)
            .load(anuncioList.get(position).imagenURL)
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


        holder.coment.setOnClickListener { listener2(anuncioList.get(position)) }

        if(anuncioList.get(position).estado) holder.txtEstadp.text = "ENCONTRADO"
        else holder.txtEstadp.text = "PERDIDO"
//
    }

    fun filtrado(txtBuscar:String?){
        if(txtBuscar!=null){
            var longitud = txtBuscar.length
            if(longitud==0){
                this.anuncioList.clear()
                this.anuncioList.addAll(this.listaOriginal)

            } else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    var coleccion =
                        this.anuncioList.stream()
                            .filter{
                                it.titulo.toLowerCase().contains(txtBuscar.toLowerCase())
                            }.collect(Collectors.toList())
                    this.anuncioList.clear()
                    this.anuncioList.addAll(coleccion)
                } else {
                    this.anuncioList.clear()
                    for(c in listaOriginal){
                        if(c.titulo.toLowerCase().contains(txtBuscar.toLowerCase())){
                            this.anuncioList.add(c)
                        }

                    }
                };
            }
            notifyDataSetChanged()
        }

    }


    override fun getItemCount(): Int {
        return this.anuncioList.size
    }

}