package com.example.pm_proy_final.models

import java.util.Date

data class Mensaje(
    var id: String,
    var idSender: String,
    var idReceiver: String,
    var nameSender: String,
    var nameReceiver: String,
    var message: String,
    var time: Date,
    var img: String
)