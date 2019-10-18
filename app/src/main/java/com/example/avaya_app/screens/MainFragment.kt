package com.example.avaya_app.screens


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.avaya_app.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MultipartBody
import androidx.lifecycle.Observer
import com.example.avaya_app.*
import com.example.avaya_app.services.APIService
import com.example.avaya_app.services.ApiUtils

import okhttp3.ResponseBody

class MainFragment : Fragment() {
    private lateinit var viewModel: ViewModel

    private lateinit var binding: FragmentMainBinding

    var mAPIService: APIService? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.main_screen_title)


        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)


        viewModel = activity?.run {
            ViewModelProviders.of(this)[ViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.src.observe(this, Observer {newUri ->
            binding.imageView.setImageURI(newUri)
        })




     //Buttons code
        binding.creditoButton.setOnClickListener {

            viewModel.param1=getString(R.string.transaction_credito)
            viewModel.param2=viewModel.numeroCuentaCredito
            postData(viewModel)

        }


        binding.ahorroButton.setOnClickListener {

            viewModel.param1=getString(R.string.transaction_ahorro)
            viewModel.param2=viewModel.numeroCuentaAhorros
            postData(viewModel)
        }

        binding.prestamoButton.setOnClickListener {

            viewModel.param1=getString(R.string.transaction_prestamo)
            viewModel.param2=viewModel.numeroCuentaPrestamo
            postData(viewModel)
        }

        binding.agenteButton.setOnClickListener {

            viewModel.param1=getString(R.string.transaction_agente)
            viewModel.param2=viewModel.numeroTelefono
            postData(viewModel)
        }



        setHasOptionsMenu(true)

        return binding.root

    }



    //Request Code


    private  fun postData(viewModel: ViewModel){


        val family = MultipartBody.Part.createFormData("family","AAADEVRFID")
        val type = MultipartBody.Part.createFormData("type","AAADEVRFIDLOCALIZATION")
        val version = MultipartBody.Part.createFormData("version","1.0")
        val eventBody = MultipartBody.Part.createFormData("eventBody","{'correoElectronico':'"+viewModel.mail+"','Param1':'"+viewModel.param1+"','Param2':'"+viewModel.param2+"'}")

        mAPIService = ApiUtils.apiService

        mAPIService!!.request(family, type, version, eventBody).enqueue(object :Callback<ResponseBody> {


            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {



                if (response.code()==200) {

                    Toast.makeText(activity,getString(R.string.succes_request_message), Toast.LENGTH_SHORT).show()
                    Log.i("BODY", "Succes resquest:" + response.body()!!.toString())

                }

                else{

                    Toast.makeText(activity,"Server Error response code:"+  response.code().toString(), Toast.LENGTH_SHORT).show()
                    Log.i("response code: ",   response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(activity,getString(R.string.fail_request_message), Toast.LENGTH_SHORT).show()

                Log.i("error-2",  t.localizedMessage)

            }


        })


    }





    //Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

}
