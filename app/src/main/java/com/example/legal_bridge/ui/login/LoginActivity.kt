package com.example.legal_bridge.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.legal_bridge.MainActivity
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.databinding.ActivityLoginBinding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.helper.SpinnerConstants
import com.example.legal_bridge.model.ErrorResponse.ErrorResponse
import com.example.legal_bridge.model.user.LoginUserResquest
import com.example.legal_bridge.model.user.UserResponse
import com.example.legal_bridge.ui.ForgetPassword.ForgetPassword
import com.example.legal_bridge.ui.register.Register_Activity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_REGISTER = 123 // Or any unique request code
    }
    private lateinit var sharedViewModel: SharedPreference

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = SharedPreference(this)

        binding?.emailContainer?.helperText = null
        binding?.passwordContainer?.helperText = null


        if(sharedViewModel.isAlreadyLoggedIn){
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish()
        }


        binding?.btnLogin?.setOnClickListener {
            Login()
        }




        binding?.tvNewUser?.setOnClickListener {
//            startActivity(Intent(this@LoginActivity,Register_Activity::class.java))
//            finish()

            sharedViewModel.setDefaultValues()

            startActivityForResult(Intent(this, Register_Activity::class.java), REQUEST_REGISTER)


        }

        binding?.tvForgetPassword?.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPassword::class.java))

        }




    }

    override fun onBackPressed() {
        finishAffinity()
    }

    // Handle the result from RegisterPage1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REGISTER && resultCode == Activity.RESULT_CANCELED) {
            // If back pressed in RegisterPage1, finish the app
            finishAffinity()
        }
    }

    private fun Login() {
        val password = binding?.passwordEditText?.text.toString()

        binding?.emailContainer?.helperText = validEmail()
        val validEmail = binding?.emailContainer?.helperText == null


        if (!(validEmail)) {
            Snackbar.make(binding?.root!!, "Please enter valid details", Snackbar.LENGTH_LONG)
                .show()
        }else if(!isPasswordValid(password)){
            binding?.passwordContainer?.helperText =
                "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
        }else{
            binding?.progressBar?.visibility = View.VISIBLE

            binding?.passwordContainer?.helperText = null
            binding?.emailContainer?.helperText = null



//            val p = "$2b$10$b/vuS/QnS4xeNWpESfJyAeb6.e.m7S4ELAOmVeQPjGNUc/Lh382sO"

            val LoginUserResquest = LoginUserResquest(
               email= binding?.emailEditText?.text.toString(),
                password = password
            )

            val apiService = RetrofitClient.getApiService()
            val call = apiService.loginUser(LoginUserResquest)

            call.enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        binding?.progressBar?.visibility = View.GONE
                        // Registration successful, handle accordingly
                        Log.d("USER","${response.body()}")
                        Log.d("USER","${response.code()}")
                        Log.d("USER","${response.message()}")
                        Log.d("USER","${response.errorBody().toString()}")
                        Log.d("USER","${response.errorBody()?.string()}")
                        Log.d("USER","${response}")

                        Log.d("TOKEN","${response.body()?.token}")
//                        Toast.makeText(this@LoginActivity, "${response.body()?.token}",Toast.LENGTH_LONG ).show()
                        // Proceed to the next screen or perform other actions

                        // save the user value on the phone
                        sharedViewModel.email = response.body()?.email
                        sharedViewModel.fullName = response.body()?.name
                        sharedViewModel.image = response.body()?.pic
                        sharedViewModel.phone = response.body()?.phone
                        sharedViewModel.tvDob = response.body()?.dob
                        sharedViewModel.gender = SpinnerConstants.getGenderIndex(response.body()?.gender!!)


                        Toast.makeText(this@LoginActivity, "${response.body()?.gender!!}",Toast.LENGTH_LONG ).show()
                        Toast.makeText(this@LoginActivity, "${SpinnerConstants.getGenderIndex(response.body()?.gender!!)}",Toast.LENGTH_LONG ).show()




                        sharedViewModel.isAlreadyLoggedIn = true


                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()

                    } else {

                        binding?.progressBar?.visibility = View.GONE

                        // Registration failed, handle accordingly
                        Log.d("USER","${response.body()}")
                        Log.d("USER","${response.code()}")
//                        Log.d("USER","${response.message()}")
//                        Log.d("USER","${response.errorBody().toString()}")
//                        Log.d("USER","${response.errorBody()?.string()}")
//                        Log.d("USER","${response}")



                        try {
                            val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                            val errorMessage = errorResponse?.error?.message ?: "Unknown error occurred"
                            if (errorMessage == "Unknown error occurred") {
                                // Log the actual JSON response to debug further
                                Log.d("USER", "Original Error Response: ${response.errorBody()?.string()}")
                            }
                            Log.d("USER", errorMessage)
                            showErrorInDialog(errorMessage)

                        } catch (e: Exception) {
                            Log.e("USER", "Exception while parsing error response: ${e.message}")
                            showErrorInDialog("Error occurred while processing the request")
//                        showForgetPasswordDialog("Error occurred while processing the request",false)
                        }


                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("USER","${t.message}")
                    showErrorInDialog("Error occurred while processing the request")
                    binding?.progressBar?.visibility = View.GONE
                }
            })
        }





        }


    private fun validEmail(): String?
    {
        val emailText = binding?.emailEditText?.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }



    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"
        return password.matches(passwordRegex.toRegex())
    }


    fun showErrorInDialog(mess: String) {
        binding?.progressBar?.visibility =
            View.GONE
        val builder = AlertDialog.Builder(this@LoginActivity)
        val message =mess
        builder.apply {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

            }
        }.create().show()
    }

}