package com.example.pm_proy_final.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pm_proy_final.R
import com.example.pm_proy_final.adapters.AnuncioListAdapter
import com.example.pm_proy_final.managers.AnuncioManager
import com.example.pm_proy_final.models.Anuncio
import com.example.pm_proy_final.models.Usuario

class AnunciosFragment(var usuario: Usuario): Fragment(), OnItemSelectedListener {

    interface OnAnuncioSelectedListener {
        fun onInfomacion(beta: Anuncio)
        fun onChat(alpha: Anuncio)
    }


    private var listener : OnAnuncioSelectedListener?=null

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

        var adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.distritos,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        distritos.adapter=adapter;
        distritos.onItemSelectedListener=this


        AnuncioManager().getAllAnuncios({
            val recycListadoAnuncio = view.findViewById<RecyclerView>(R.id.lista_anuncios)
            recycListadoAnuncio.adapter = AnuncioListAdapter(usuario, it, this, {
                        anuncio1: Anuncio ->
                    listener?.onInfomacion(anuncio1)
                },{
                    anuncio2: Anuncio ->
                listener?.onChat(anuncio2)
            })

        },{
            Toast.makeText(context,"No se pudo obtener los anuncios", Toast.LENGTH_SHORT).show()
        })


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var choice = parent!!.getItemAtPosition(position).toString()
//        Toast.makeText(requireContext(), choice, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}