package com.example.legal_bridge.ui.ForgetPassword

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.databinding.ActivityForgetPasswordBinding
import com.example.legal_bridge.model.ErrorResponse.ErrorResponse
import com.example.legal_bridge.model.forgetPassModel.forgetPasswordRequest
import com.example.legal_bridge.model.forgetPassModel.forgetPasswordResponse
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.emailContainer?.helperText = null


        binding?.btnSubmit?.setOnClickListener {
            binding?.emailContainer?.helperText = validEmail()
            val validEmail = binding?.emailContainer?.helperText == null




            if (!(validEmail)) {
                Snackbar.make(binding?.root!!, "Please enter valid details", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                binding?.progressBar?.visibility = View.VISIBLE
                binding?.emailContainer?.helperText = null


                val ForgetPasswordUserResquest = forgetPasswordRequest(
                    email = binding?.emailEditText?.text.toString(),
                )

                val apiService = RetrofitClient.getApiService()
                val call = apiService.forgotPassword(ForgetPasswordUserResquest)

                call.enqueue(object : Callback<forgetPasswordResponse> {
                    override fun onResponse(
                        call: Call<forgetPasswordResponse>,
                        response: Response<forgetPasswordResponse>
                    ) {
                        if (response.isSuccessful) {
                            // Registration successful, handle accordingly
                            Log.d("USER", "${response.body()?.message}")
                            Log.d("USER", "${response.code()}")
                            Log.d("USER", "${response.message()}")
                            Log.d("USER", "${response.errorBody().toString()}")
                            Log.d("USER", "${response.errorBody()?.string()}")
                            showForgetPasswordDialog(response.body()?.message, true)

                        } else {
                            // Registration failed, handle accordingly
                            Log.d("USER", "${response.body()}")
                            Log.d("USER", "${response.code()}")
                            Log.d("USER", "${response.message()}")

                            // Parse error response using Gson
                            try {
                                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                                val errorMessage = errorResponse?.error?.message ?: "Unknown error occurred"
                                if (errorMessage == "Unknown error occurred") {
                                    // Log the actual JSON response to debug further
                                    Log.d("USER", "Original Error Response: ${response.errorBody()?.string()}")
                                }
                                Log.d("USER", errorMessage)
                                showForgetPasswordDialog(errorMessage,false)
                            } catch (e: Exception) {
                                Log.e("USER", "Exception while parsing error response: ${e.message}")
                                showForgetPasswordDialog("Error occurred while processing the request",false)
                            }



                        }
                    }

                    override fun onFailure(call: Call<forgetPasswordResponse>, t: Throwable) {
                        Log.d("USER", "${t.message}")
                        binding?.progressBar?.visibility = View.GONE
                        showForgetPasswordDialog(t.message,false)

                    }
                })
            }


        }

    }


    private fun validEmail(): String? {
        val emailText = binding?.emailEditText?.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }


    fun showForgetPasswordDialog(mess : String?,value:Boolean?) {
        binding?.progressBar?.visibility = View.GONE
        val builder = AlertDialog.Builder(this@ForgetPassword)
        val message = mess



        builder.apply {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                if(value!!){
                    finish()
                }
            }
        }.create().show()
    }

}