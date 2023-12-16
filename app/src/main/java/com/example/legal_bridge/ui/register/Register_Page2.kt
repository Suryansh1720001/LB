package com.example.legal_bridge.ui.register

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.legal_bridge.R
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.databinding.FragmentRegisterPage2Binding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.model.ErrorResponse.ErrorResponse
import com.example.legal_bridge.model.PhoneVerification.OTPRequest.otpRequest
import com.example.legal_bridge.model.PhoneVerification.OTPRequest.otpResponse
import com.example.legal_bridge.model.PhoneVerification.OTPVerification.OTPverifyRequest
import com.example.legal_bridge.model.PhoneVerification.OTPVerification.otpVerifyResponse
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register_Page2 : Fragment() {

    private lateinit var sharedViewModel: SharedPreference
    private var _binding: FragmentRegisterPage2Binding? = null
    private val binding get() = _binding!!
    private var timerJob: Job? = null


    private val resendDelayMillis = 60_000 // 60 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRegisterPage2Binding.inflate(inflater, container, false)
        sharedViewModel = SharedPreference(requireContext())

        binding?.phoneContainer?.helperText = null
        binding?.resendTimer?.visibility= View.GONE
        binding?.llOtp?.visibility= View.GONE
        binding?.tvEnterOTP?.visibility= View.GONE


        initial()
        setupOtpInputs()
        handleClipboard()

        binding?.btnNext2?.setOnClickListener {
            if(binding?.btnNext2?.text == "NEXT" || binding?.btnNext2?.text == "Next"){
                binding?.progressBar?.visibility = View.VISIBLE
                verifyOTP()
            }else{
                sendOTP()
            }
        }

        binding?.resendTimer?.setOnClickListener {
            if(binding?.resendTimer?.text == "Resend OTP"){
                sendOTP()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun verifyOTP() {
        val enteredOTP = OTPverifyRequest(
            phoneNumber = "+91"+ binding?.phoneEditText?.text?.toString()!!,
            userOTP = binding?.etDigit1?.text?.toString() + binding?.etDigit2?.text?.toString() + binding?.etDigit3?.text?.toString() + binding?.etDigit4?.text?.toString() + binding?.etDigit5?.text?.toString() + binding?.etDigit6?.text?.toString()
        )
        val apiService = RetrofitClient.getApiService()
        val call = apiService.OTPVerify(enteredOTP)

        call.enqueue(object : Callback<otpVerifyResponse> {
            override fun onResponse(call: Call<otpVerifyResponse>, response: Response<otpVerifyResponse>) {
                if (response.isSuccessful) {
                    Log.d("USER", "Message: ${response.body()?.message}")
                    sharedViewModel.phone = binding?.phoneEditText?.text.toString()
                    navigateToNextFragment() // Call the function to navigate to the next fragment
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

            override fun onFailure(call: Call<otpVerifyResponse>, t: Throwable) {
                // Handle network failures
                Log.e("USER", "Failure: ${t.message}")
                showErrorCard("Failure: ${t.message}")
//                showErrorInDialog("Network error occurred")
                binding?.progressBar?.visibility = View.GONE
            }
        })
    }

    private fun navigateToNextFragment() {
        // Before navigating to the next fragment, check and cancel the timer job if it's running
        timerJob?.cancel()
        // Navigate to the next fragment
        findNavController().navigate(R.id.action_register_Page2_to_register_Page3)
    }


    private fun sendOTP() {
        binding?.phoneContainer?.helperText = validPhone()

        val isValidEmail = binding?.phoneContainer?.helperText == null

        if (!isValidEmail) {
            Snackbar.make(binding?.root!!, "Please enter valid phone Number", Snackbar.LENGTH_LONG)
                .show()
        } else {
            binding?.btnNext2?.isEnabled = false
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.phoneContainer?.helperText = null

            val apiService = RetrofitClient.getApiService()
//            val call = apiService.checkEmailExists(CheckEmailRequest(email = email.toString()))
            val call = apiService.sendOTP(otpRequest(phoneNumber ="+91" + binding?.phoneEditText?.text.toString()))

            call.enqueue(object : Callback<otpResponse> {
                override fun onResponse(call: Call<otpResponse>, response: Response<otpResponse>) {
                    if (response.isSuccessful) {
                        Log.d("USER", "Message: ${response.body()?.message}")
                        binding?.btnNext2?.text = "Next"
                        binding?.tvEnterOTP?.visibility = View.VISIBLE
                        binding?.llOtp?.visibility = View.VISIBLE
                        binding?.resendTimer?.visibility = View.VISIBLE

                        binding?.btnNext2?.isEnabled = true

//                        val bool : Boolean? = binding?.etDigit1?.text?.isNotEmpty()!! && binding?.etDigit2?.text?.isNotEmpty()!! && binding?.etDigit3?.text?.isNotEmpty()!! && binding?.etDigit4?.text?.isNotEmpty()!!
//                                && binding?.etDigit5?.text?.isNotEmpty()!! && binding?.etDigit6?.text?.isNotEmpty()!!

                        timerJob = lifecycleScope.launch {
                            var remainingTimeMillis = resendDelayMillis

                            while (remainingTimeMillis > 0) {
                                // Update the TextView with the remaining time
                                val seconds = remainingTimeMillis / 1000
                                binding?.resendTimer?.text = "Resend in: $seconds seconds"

                                // Delay for 1 second
                                delay(1_000)

                                // Decrement the remaining time by 1 second
                                remainingTimeMillis -= 1_000
                            }
                            binding?.resendTimer?.text = "Resend OTP"
                        }


                            // Enable the resend OTP button after the delay

                    } else {
                        // Registration failed, handle accordingly
                        Log.d("USER", "${response.code()}")
                        Log.d("USER", "${response.body()}")
                        Log.d("USER", "${response.errorBody()?.string()}")

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

                override fun onFailure(call: Call<otpResponse>, t: Throwable) {
                    // Handle network failures
                    Log.e("USER", "Failure: ${t.message}")
//                    showErrorInDialog("Network error occurred")
                    binding?.progressBar?.visibility = View.GONE
                }
            })


        }
    }


    private fun setupOtpInputs() {
        val editTextList = listOf(
            binding.etDigit1, binding.etDigit2, binding.etDigit3,
            binding.etDigit4, binding.etDigit5, binding.etDigit6
        )

        for ((index, editText) in editTextList.withIndex()) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        if (index < editTextList.size - 1) {
                            editTextList[index + 1].requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (index > 0) {
                        editTextList[index - 1].requestFocus()
                        editTextList[index - 1].setText(editText.text.toString()) // Update existing value
                    }
                }
                false
            }
        }
    }

    private fun handleClipboard() {
        binding.root.setOnClickListener {
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            if (clipboard?.hasPrimaryClip() == true) {
                val clipData = clipboard.primaryClip
                if (clipData?.itemCount == 1) {
                    val pastedText = clipData.getItemAt(0).text.toString().trim()
                    if (pastedText.length == 6) {
                        for (i in pastedText.indices) {
                            binding.root.postDelayed({
                                binding.etDigit1.setText(pastedText[0].toString())
                                binding.etDigit2.setText(pastedText[1].toString())
                                binding.etDigit3.setText(pastedText[2].toString())
                                binding.etDigit4.setText(pastedText[3].toString())
                                binding.etDigit5.setText(pastedText[4].toString())
                                binding.etDigit6.setText(pastedText[5].toString())
                            }, i * 100L)
                        }
                    }
                }
            }
        }
    }

    private fun initial() {
        binding?.phoneEditText?.setText(sharedViewModel.phone)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun phoneFocusListener()
    {
        binding?.phoneEditText?.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding?.phoneContainer?.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String?
    {
        val phoneText = binding?.phoneEditText?.text.toString()
        if(!phoneText.matches(".*[0-9].*".toRegex()))
        {
            return "Must be all Digits"
        }
        if(phoneText.length != 10)
        {
            return "Must be 10 Digits"
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
