package com.example.pm_proy_final.models

data class Anuncio(
    var id: String,
    var titulo: String,
    var distrito: String,
    var descripcion: String,
    var imagenURL: String,
    var estado: Boolean,
    var userid: String,
    var imageName: String
)
