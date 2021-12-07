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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class NuevoAnuncioFragment(usercodigo: String): Fragment(), AdapterView.OnItemSelectedListener {

    private var imagen : ImageView? = null
    private var titulo_post : EditText?=null
    private var descripcion_post : EditText?=null
    private var distritos : Spinner?=null
    private var estado: Spinner?= null
    private var codigouser= usercodigo

    private var storagered: StorageReference?= null;
//    private var imgref: DatabaseReference?=null;
//    private var thumb_bitmap: Bitmap? =null;



    private var actual_img: Uri? = null;
    private var image_URL: String? = null;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nuevoanuncio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        distritos = view.findViewById(R.id.distrito_post)
        estado = view.findViewById(R.id.estado_post)
        descripcion_post = view.findViewById(R.id.descripcion_post)
        titulo_post = view.findViewById(R.id.titulo_post)
        var galeria_icon = view.findViewById<ImageView>(R.id.galeria_post)
        var button_post = view.findViewById<Button>(R.id.POST_BUTTON)
        imagen = view.findViewById(R.id.imagen_post)

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

        button_post.setOnClickListener {
            FileUploader()
        }

    }

    fun requestPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
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
            Toast.makeText(requireContext(),"Se necesitan habilitar los permisos",Toast.LENGTH_SHORT).show()
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
//        var a = this.actual_img!!.lastPathSegment as String
        var nombre = (Date().time.toString())
        var filePath = this.storagered!!.child("fotos").child(nombre)

        filePath.putFile(this.actual_img!!).addOnSuccessListener { it->
            filePath.downloadUrl.addOnSuccessListener { a->
                this.image_URL = a.toString()

                AnuncioManager().addAnuncio(titulo_post!!.text.toString(),
                    this.distritos!!.selectedItem.toString(),
                    this.descripcion_post!!.text.toString(),
                    this.image_URL!!,
                    this.estado!!.selectedItem.toString()=="ENCONTRADO"
                    ,
                    this.codigouser,
                    nombre
                )
//                Glide.with(requireContext())
//                    .load(a.toString())
//                    .into(imagen!!)
                Toast.makeText(requireContext(),"Anuncio ingresado con exito",Toast.LENGTH_SHORT).show()
                CleanItems()
            }
        }



    }


    fun CleanItems(){
        this.titulo_post!!.setText("")
        this.descripcion_post!!.setText("")
        Glide.with(requireContext())
            .load("")
            .into(imagen!!)
    }



//imagen
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var choice = p0!!.getItemAtPosition(p2).toString()

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}