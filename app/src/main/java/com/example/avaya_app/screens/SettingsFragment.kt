package com.example.avaya_app.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.avaya_app.databinding.FragmentSettingsBinding
import androidx.lifecycle.Observer
import com.example.avaya_app.R

class SettingsFragment : Fragment() {

    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings_screen_title)

        viewModel = activity?.run {
            ViewModelProviders.of(this)[ViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        binding= DataBindingUtil.inflate<FragmentSettingsBinding>(inflater,
            R.layout.fragment_settings, container, false)

        binding.mailTextBox.text= SpannableStringBuilder(viewModel.mail)
        binding.creditoTextBox.text= SpannableStringBuilder(viewModel.numeroCuentaCredito)
        binding.prestamoTextBox.text= SpannableStringBuilder(viewModel.numeroCuentaPrestamo)
        binding.ahorrosTextBox.text= SpannableStringBuilder(viewModel.numeroCuentaAhorros)
        binding.numeroTextBox.text= SpannableStringBuilder(viewModel.numeroTelefono)

        viewModel.src.observe(this, Observer {newUri ->
            binding.imagePreview.setImageURI(newUri)
        })




        binding.saveSettingsButton.setOnClickListener {

            viewModel.mail= binding.mailTextBox.text.toString()
            viewModel.numeroCuentaCredito= binding.creditoTextBox.text.toString()
            viewModel.numeroCuentaAhorros= binding.ahorrosTextBox.text.toString()
            viewModel.numeroCuentaPrestamo= binding.prestamoTextBox.text.toString()
            viewModel.numeroTelefono= binding.numeroTextBox.text.toString()

            Toast.makeText(getActivity(),"Ajustes guardados", Toast.LENGTH_SHORT).show()
        }




        binding.addImagePreviewButton.setOnClickListener {

            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (activity?.baseContext?.let { it1 ->
                        ContextCompat.checkSelfPermission(
                            it1,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    } ==PackageManager.PERMISSION_DENIED){
                    val permissions= arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                    requestPermissions(permissions,
                        PERMISSION_CODE
                    )

                }
                else{

                    pickImageFromGallery()
                }

            }else{

                pickImageFromGallery()
            } }


        return binding.root
    }

    companion object{

        private const val IMAGE_PICK_CODE=1000
        private const val PERMISSION_CODE=1001

    }


    private fun pickImageFromGallery(){

        val intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            PERMISSION_CODE ->{
                if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{

                    Toast.makeText(getActivity(),"permisos insuficientes", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== Activity.RESULT_OK && requestCode== IMAGE_PICK_CODE){

            viewModel.setSrc(data?.data)

        }
    }

}
