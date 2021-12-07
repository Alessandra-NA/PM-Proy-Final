package com.example.pm_proy_final.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pm_proy_final.R
import com.example.pm_proy_final.adapters.AnuncioListAdapter
import com.example.pm_proy_final.managers.AnuncioManager
import com.example.pm_proy_final.models.Anuncio
import com.example.pm_proy_final.models.Usuario

class AnunciosFragment(var usuario: Usuario): Fragment(), OnItemSelectedListener, SearchView.OnQueryTextListener {

    interface OnAnuncioSelectedListener {
        fun onInfomacion(beta: Anuncio)
        fun onChat(alpha: Anuncio)
    }


    private var listener : OnAnuncioSelectedListener?=null
    private var ActualList = arrayListOf<Anuncio>()
    private var anuncios :  RecyclerView?= null;
    private var adapter : AnuncioListAdapter?=null;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnAnuncioSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_anuncios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val distritos = view.findViewById<Spinner>(R.id.distrito_filtro_anuncio)
        val buscador = view.findViewById<SearchView>(R.id.Buscador_anuncio)

        buscador.setOnQueryTextListener(this)

        var adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.distritosA,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        distritos.adapter=adapter;
        distritos.onItemSelectedListener=this
        distritos.setSelection(43)

        AnuncioManager().getAllAnuncios({
            this.ActualList.addAll(it)
            this.anuncios = view.findViewById<RecyclerView>(R.id.lista_anuncios)
            this.adapter= AnuncioListAdapter(usuario, it as ArrayList<Anuncio>, this, {
                    anuncio1: Anuncio ->
                listener?.onInfomacion(anuncio1)
            },{
                    anuncio2: Anuncio ->
                listener?.onChat(anuncio2)
            })
            this.anuncios!!.adapter =this.adapter

        },{
            Toast.makeText(context,"No se pudo obtener los anuncios", Toast.LENGTH_SHORT).show()
        })





    }



    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var choice = parent!!.getItemAtPosition(position).toString()
        if(view!=null){
            if(this.anuncios!=null){
                this.ActualList.removeAll(this.ActualList)

                if(choice == "Todos"){
                    this.anuncios!!.removeAllViews()
                    AnuncioManager().getAllAnuncios({
                        this.ActualList.addAll(it)
                        this.adapter  = AnuncioListAdapter(usuario, it, this,
                        { anuncio1: Anuncio ->

                            listener?.onInfomacion(anuncio1)
                        },{
                            anuncio2: Anuncio ->
                        listener?.onChat(anuncio2)
                    })
                        this.anuncios!!.adapter = this.adapter

                    },{
                        Toast.makeText(context,"No se pudo obtener los anuncios", Toast.LENGTH_SHORT).show()
                    })
                }

                if(choice!="Todos"){

                    this.anuncios!!.removeAllViews()
                    AnuncioManager().getByDistritoAnuncio(choice) {
                        this.ActualList.addAll(it)
                        this.adapter = AnuncioListAdapter(usuario, it, this,
                            { anuncio1: Anuncio ->
                                Toast.makeText(requireContext(), anuncio1.titulo, Toast.LENGTH_SHORT).show()
                                listener?.onInfomacion(anuncio1)
                            }, { anuncio2: Anuncio ->
                                listener?.onChat(anuncio2)
                            })
                        this.anuncios!!.adapter = this.adapter
                    }
                }
            }

        }

    }




    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        this.adapter!!.filtrado(p0!!)
        return false
    }

}