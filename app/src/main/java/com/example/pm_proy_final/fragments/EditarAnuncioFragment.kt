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

class EditarAnuncioFragment(anuncio: Anuncio):Fragment(), AdapterView.OnItemSelectedListener {
    private var imagen : ImageView? = null
    private var titulo_edit : EditText?=null
    private var descripcion_edit : EditText?=null
    private var distritos : Spinner?=null
    private var estado: Spinner?= null

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
        a.add("San Luis")
        a.add("La molina")
        a.add("Brazil")
        a.add("Ecuador")

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


    private fun FileUploader(){
        var a = this.actual_img!!.lastPathSegment as String

        var filePath = this.storagered!!.child("fotos").child(a)

        //Primero se debe hacer una limpieza-pendiente

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
                    this.Anuncio1.userid
                )

                AnuncioManager().EditarAnuncio(this.Anuncio1.id,nuevo)

                Toast.makeText(requireContext(),"Anuncio editado con exito", Toast.LENGTH_SHORT).show()
            }
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