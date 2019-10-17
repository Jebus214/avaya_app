package com.example.avaya_app.screens

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.avaya_app.R

class ViewModel: ViewModel(){


    var src = MutableLiveData<Uri?>()

    var mail =""
    var numeroCuentaCredito = ""
    var numeroCuentaAhorros = ""
    var numeroCuentaPrestamo = ""
    var numeroTelefono = ""
    var param1= ""
    var param2= ""

    init {

        src.value = Uri.parse("android.resource://com.example.avaya_app/" + R.drawable.ic_image_black_24dp);
    }


    fun setSrc( source:Uri?) {
        src.value = source

    }

}