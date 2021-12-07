package com.example.pm_proy_final.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pm_proy_final.R
import com.example.pm_proy_final.managers.AnuncioManager
import com.example.pm_proy_final.models.Anuncio
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class EditarAnuncioFragment(anuncio: Anuncio):Fragment(), AdapterView.OnItemSelectedListener {
    private var imagen : ImageView? = null
    private var titulo_edit : EditText?=null
    private var descripcion_edit : EditText?=null
    private var distritos : Spinner?=null
    private var estado: Spinner?= null


    private var seleccion: Boolean=false
    private var Anuncio1: Anuncio = anuncio
    private var storagered: StorageReference?= null;
//    private var imgref: DatabaseReference?=null;
//    private var thumb_bitmap: Bitmap? =null;

    private var before_URL: String? = anuncio.imagenURL



    private var actual_img: Uri? = null;
    private var image_URL: String? = null;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editar_anuncio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        distritos = view.findViewById(R.id.distrito_editar)
        estado = view.findViewById(R.id.estado_editar)
        descripcion_edit = view.findViewById(R.id.descripcion_editar)
        titulo_edit = view.findViewById(R.id.titulo_editar)
        var camera_icon = view.findViewById<ImageView>(R.id.camera_editar)
        var galeria_icon = view.findViewById<ImageView>(R.id.galeria_editar)
        var button_edit = view.findViewById<Button>(R.id.EDIT_BUTTON)
        imagen = view.findViewById(R.id.imagen_editar)

        var a : ArrayList<String> = ArrayList<String>()

        a.add("Ancon")
        a.add("Ate vitarte")
        a.add("Barranco")
        a.add("breña")
        a.add("Carabayllo")
        a.add("Chaclacayo")
        a.add("Chorrillos")
        a.add("Cieneguilla")
        a.add("Comas")
        a.add("El agustino")
        a.add("Independencia")
        a.add("Jesus maria")
        a.add("La molina")
        a.add("La victoria")
        a.add("Lima")
        a.add("Lince")
        a.add("Los olivos")
        a.add("Lugirancho")
        a.add("Lurin")
        a.add("Magdalena del mar")
        a.add("Miraflores")
        a.add("Pachacamac")
        a.add("Pucusana")
        a.add("Pueblo libre")
        a.add("Punta hermosa")
        a.add("Punta negra")
        a.add("Rimac")
        a.add("San bartolo")
        a.add("San Borja")
        a.add("San isidro")
        a.add("San juan de lugirancho")
        a.add("San juan de miraflores")
        a.add("San Luis")
        a.add("San Martin de porres")
        a.add("San miguel")
        a.add("Santa anita")
        a.add("Santa maria del mar")
        a.add("Santa rosa")
        a.add("Santiago de surco")
        a.add("Surquillo")
        a.add("Villa del salvador")
        a.add("Villa maria del triunfo")
        a.add("Autobus")

        //llenado de datos
        for(i in 0..a.size-1){
            if(a.get(i)==this.Anuncio1.distrito) distritos!!.setSelection(i)
        }

        if(this.Anuncio1.estado) estado!!.setSelection(1)
        else estado!!.setSelection(0)

        titulo_edit!!.setText(Anuncio1.titulo)
        descripcion_edit!!.setText(Anuncio1.descripcion)
        Glide.with(requireContext())
                    .load(Anuncio1.imagenURL)
                   .into(imagen!!)


        storagered = FirebaseStorage.getInstance().getReference();

        var adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.distritos,
            android.R.layout.simple_spinner_item
        );

        var adapter2 = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.estado,
            android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        distritos!!.adapter=adapter;
        distritos!!.onItemSelectedListener=this
        estado!!.adapter=adapter2;
        estado!!.onItemSelectedListener=this

        galeria_icon.setOnClickListener{
            requestPermission()
        }

        button_edit.setOnClickListener {
            FileUploader()
        }

    }

    fun requestPermission(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickPhotoFromGallery()
                }else -> requestPermissionLaunche.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            pickPhotoFromGallery()
        }
    }

    private val requestPermissionLaunche = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted->
        if(isGranted){
            pickPhotoFromGallery()
        }else{
            Toast.makeText(requireContext(),"Se necesitan habilitar los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            //se ha seleccionado
                this.seleccion=true
            val data = result.data?.data
            //opcional, resize the image-> data equals uri
            this.actual_img=data
            imagen!!.setImageURI(data)
        }

    }

    private fun pickPhotoFromGallery() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun deleteImage(filename: String) {
        this.storagered!!.child("fotos/$filename").delete().addOnSuccessListener {
            Toast.makeText(requireContext(),"Imagen eliminada con exito", Toast.LENGTH_SHORT).show()
        }

    }


    private fun FileUploader(){

        //se debe eliminar los storages anteriores aqui
        //Primero se debe hacer una limpieza-pendiente
        if(this.seleccion){

            this.deleteImage(this.Anuncio1.imageName)

            var nombre = (Date().time.toString())
            var filePath = this.storagered!!.child("fotos").child(nombre)

            filePath.putFile(this.actual_img!!).addOnSuccessListener { it->
                filePath.downloadUrl.addOnSuccessListener { a->
                    this.image_URL = a.toString()

                    var nuevo = Anuncio(
                        "0",
                        titulo_edit!!.text.toString(),
                        this.distritos!!.selectedItem.toString(),
                        this.descripcion_edit!!.text.toString(),
                        this.image_URL!!,
                        this.estado!!.selectedItem.toString()=="ENCONTRADO"
                        ,
                        this.Anuncio1.userid,
                        nombre
                    )

                    AnuncioManager().EditarAnuncio(this.Anuncio1.id,nuevo)

                    Toast.makeText(requireContext(),"Anuncio editado con exito", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            var nuevo = Anuncio(
                "0",
                titulo_edit!!.text.toString(),
                this.distritos!!.selectedItem.toString(),
                this.descripcion_edit!!.text.toString(),
                this.Anuncio1.imagenURL,
                this.estado!!.selectedItem.toString()=="ENCONTRADO"
                ,
                this.Anuncio1.userid,
                this.Anuncio1.imageName
            )
            AnuncioManager().EditarAnuncio(this.Anuncio1.id,nuevo)
            Toast.makeText(requireContext(),"Anuncio editado con exito", Toast.LENGTH_SHORT).show()
        }




    }

    //imagen
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var choice = p0!!.getItemAtPosition(p2).toString()

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}