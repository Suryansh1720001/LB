package com.example.legal_bridge.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.legal_bridge.MainActivity
import com.example.legal_bridge.R
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.databinding.FragmentRegisterPage1Binding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.model.ErrorResponse.ErrorResponse
import com.example.legal_bridge.model.emailcheck.CheckEmailRequest
import com.example.legal_bridge.model.emailcheck.CheckEmailResponse
import com.example.legal_bridge.model.user.UserResponse
import com.example.legal_bridge.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register_Page1 : Fragment() {

    private lateinit var sharedViewModel: SharedPreference
    private var _binding: FragmentRegisterPage1Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentRegisterPage1Binding.inflate(inflater, container, false)

        sharedViewModel = SharedPreference(requireContext())


        intial()
        binding?.emailContainer?.helperText = null
        binding?.btnNext1?.setOnClickListener { submitForm() }




        binding?.tvAlreadyLogin?.setOnClickListener {
            // Replace YourLoginDestination with the ID of your login destination
            startActivity(Intent(activity, LoginActivity::class.java))
//            // Clear back stack up to the register_Page1 fragment
            activity?.setResult(Activity.RESULT_CANCELED)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Handle the back button press
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.setResult(Activity.RESULT_CANCELED)
        }


        return binding.root
    }



    private fun intial() {
        emailFocusListener()

        binding?.emailContainer?.helperText = validEmail()

        val validEmail = binding?.emailContainer?.helperText == null

        if (!(validEmail)) {
            binding?.emailEditText?.setText(sharedViewModel.email)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun submitForm() {

        binding?.emailContainer?.helperText = validEmail()

        val isValidEmail = binding?.emailContainer?.helperText == null

        if (!isValidEmail) {
            Snackbar.make(binding?.root!!, "Please enter valid email id", Snackbar.LENGTH_LONG)
                .show()
        } else {
            binding?.progressBar?.visibility = View.VISIBLE

            val email = binding?.emailEditText?.text?.toString()

            binding?.emailContainer?.helperText = null

            val apiService = RetrofitClient.getApiService()
//            val call = apiService.checkEmailExists(CheckEmailRequest(email = email.toString()))
            val call = apiService.checkEmailExists(CheckEmailRequest(email = email))

            call.enqueue(object : Callback<CheckEmailResponse> {
                override fun onResponse(call: Call<CheckEmailResponse>, response: Response<CheckEmailResponse>) {
                    if (response.isSuccessful) {

                            Log.d("USER", "Message: ${response.body()?.message}")

                        sharedViewModel.email = email

//                   findNavController().navigate(R.id.action_register_Page1_to_register_Page2)
                        findNavController().navigate(R.id.action_register_Page1_to_register_Page2)
                    } else {
                        // Registration failed, handle accordingly
                        Log.d("USER", "${response.code()}")
                        Log.d("USER", "${response.body()}")
//                        Log.d("USER", "${response.errorBody().toString()}")

                        try {
                            val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                            val errorMessage = errorResponse?.error?.message ?: "Unknown error occurred"
                            if (errorMessage == "Unknown error occurred") {
//                                Log the actual JSON response to debug further
                                Log.d("USER", "Original Error Response: ${response.errorBody()?.string()}")
                            }
                            Log.d("USER", errorMessage)
                            showErrorCard(errorMessage)


                        } catch (e: Exception) {
                            Log.e("USER", "Exception while parsing error response: ${e.message}")
                            showErrorCard("Error occurred while processing the request")
                        }
                    }
                    binding?.progressBar?.visibility = View.GONE
                }

                override fun onFailure(call: Call<CheckEmailResponse>, t: Throwable) {
                    // Handle network failures
                    Log.e("USER", "Failure: ${t.message}")
                    showErrorCard("Network error occurred")
                    binding?.progressBar?.visibility = View.GONE
                }
            })
        }
    }




    private fun emailFocusListener()
    {
        binding?.emailEditText?.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding?.emailContainer?.helperText = validEmail()
            }
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


    private fun showErrorCard(mess:String) {
        binding?.progressBar?.visibility = View.GONE
//         Inflate the layout containing the CardView
        val errorCardView = LayoutInflater.from(requireContext()).inflate(R.layout.error_dialog, null)


//        Create a dialog or use another view to display the error card
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(errorCardView)


        val dialog = builder.create()

        errorCardView.findViewById<TextView>(R.id.error_mess).text = mess
//        Show the dialog
        dialog.show()

//        Set actions for the 'OK' button
        val okButton = errorCardView.findViewById<Button>(R.id.btn_ok)
        okButton.setOnClickListener { dialog.dismiss() }
    }




}


