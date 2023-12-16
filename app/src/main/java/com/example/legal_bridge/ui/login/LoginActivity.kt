package com.example.legal_bridge.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.legal_bridge.MainActivity
import com.example.legal_bridge.R
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.databinding.ActivityLoginBinding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.helper.SpinnerConstants
import com.example.legal_bridge.helper.utils
import com.example.legal_bridge.model.ErrorResponse.ErrorResponse
import com.example.legal_bridge.model.user.LoginUserResquest
import com.example.legal_bridge.model.user.UserResponse
import com.example.legal_bridge.ui.ForgetPassword.ForgetPassword
import com.example.legal_bridge.ui.register.Register_Activity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_REGISTER = 123
    }
    private lateinit var sharedViewModel: SharedPreference
    private lateinit var binding: ActivityLoginBinding
    private var overlay: View? = null // Declare the overlay as a class variable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = SharedPreference(this)

//        checkInternetConnection(this)

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
            utils().createTransparentOverlay(this@LoginActivity) // Call to create the transparent overlay


            binding?.passwordContainer?.helperText = null
            binding?.emailContainer?.helperText = null
//          val p = "$2b$10$b/vuS/QnS4xeNWpESfJyAeb6.e.m7S4ELAOmVeQPjGNUc/Lh382sO"

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
                        utils().removeTransparentOverlay(this@LoginActivity) // Call to remove the transparent overlay

                        // Registration successful, handle accordingly
                        Log.d("USER","${response.body()}")
                        Log.d("USER","${response.code()}")
                        Log.d("USER","${response.message()}")
                        Log.d("USER","${response.errorBody().toString()}")
                        Log.d("USER","${response.errorBody()?.string()}")
                        Log.d("USER","${response}")

                        Log.d("TOKEN","${response.body()?.token}")
//                        Toast.makeText(this@LoginActivity, "${response.body()?.token}",Toast.LENGTH_LONG ).show()
//                        Proceed to the next screen or perform other actions

//                         save the user value on the phone
                        sharedViewModel.email = response.body()?.email
                        sharedViewModel.fullName = response.body()?.name
                        sharedViewModel.image = response.body()?.pic
                        sharedViewModel.phone = response.body()?.phone
                        sharedViewModel.tvDob = response.body()?.dob
                        sharedViewModel.gender = response.body()?.gender
                        sharedViewModel.city = response.body()?.address?.city
                        sharedViewModel.state = response.body()?.address?.state
                        sharedViewModel.pincode = response.body()?.address?.pincode
                        sharedViewModel._id = response?.body()?._id
                        sharedViewModel.token = response?.body()?.token



                        Toast.makeText(this@LoginActivity, "${response.body()?.gender!!}",Toast.LENGTH_LONG ).show()
                        Toast.makeText(this@LoginActivity, "${SpinnerConstants.getGenderIndex(response.body()?.gender!!)}",Toast.LENGTH_LONG ).show()




                        sharedViewModel.isAlreadyLoggedIn = true


                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()

                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        utils().removeTransparentOverlay(this@LoginActivity) // Call to remove the transparent overlay


                        //      Registration failed, handle accordingly
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
//                                Log the actual JSON response to debug further
                                Log.d("USER", "Original Error Response: ${response.errorBody()?.string()}")
                            }
                            Log.d("USER", errorMessage)
//                            showErrorInDialog(errorMessage)
                            showErrorCard(errorMessage)


                        } catch (e: Exception) {
                            Log.e("USER", "Exception while parsing error response: ${e.message}")
//                            showErrorInDialog("Error occurred while processing the request")
                            showErrorCard("Error occurred while processing the request")
//                        showForgetPasswordDialog("Error occurred while processing the request",false)
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                    Log.d("USER","${t.message}")
//                    showErrorInDialog()
                    showErrorCard("Error occurred while processing the request")
                    binding?.progressBar?.visibility = View.GONE
                    utils().removeTransparentOverlay(this@LoginActivity) // Call to remove the transparent overlay

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


//    fun showErrorInDialog(mess: String) {
//        binding?.progressBar?.visibility =
//            View.GONE
//        val builder = AlertDialog.Builder(this@LoginActivity)
//        val message =mess
//        builder.apply {
//            setMessage(message)
//            setPositiveButton("OK") { dialog, _ ->
//                dialog.dismiss()
//
//            }
//        }.create().show()
//    }


    @SuppressLint("MissingInflatedId")
    private fun showErrorCard(mess:String) {
        binding?.progressBar?.visibility = View.GONE
        utils().removeTransparentOverlay(this@LoginActivity) // Call to remove the transparent overlay

//         Inflate the layout containing the CardView
        val errorCardView = LayoutInflater.from(this).inflate(R.layout.error_dialog, null)




//        Create a dialog or use another view to display the error card
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(errorCardView)


        val dialog = builder.create()

        errorCardView.findViewById<TextView>(R.id.error_mess).text = mess
//        Show the dialog
        dialog.show()

//        Set actions for the 'OK' button
        val okButton = errorCardView.findViewById<Button>(R.id.btn_ok)
        okButton.setOnClickListener { dialog.dismiss()
            utils().removeTransparentOverlay(this@LoginActivity) // Call to remove the transparent overlay
        }
    }




    // Check Internet Connectivity
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    // Show Dialog Box
    fun showDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("No internet connection!")
        dialogBuilder.setPositiveButton("Retry") { dialog, _ ->
            dialog.dismiss()
            checkInternetConnection(context)
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }



    // Check Internet Connection

    // Check Internet Connection
    fun checkInternetConnection(context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Checking internet...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            // Simulating a delay for checking internet
            delay(3000)

            // Check connectivity
            val isConnected = isInternetAvailable(context)

            withContext(Dispatchers.Main) {
                progressDialog.dismiss()

                if (!isConnected) {
                    showDialog(context) // Show dialog again if not connected
                }
            }
        }
    }





}