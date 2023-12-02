package com.example.legal_bridge.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.legal_bridge.R
import com.example.legal_bridge.databinding.FragmentRegisterPage1Binding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar



class Register_Page1 : Fragment() {

    private lateinit var sharedViewModel: SharedPreference
    private var _binding: FragmentRegisterPage1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentRegisterPage1Binding.inflate(inflater, container, false)
//         sharedViewModel = ViewModelProvider(this).get(SharedPreference::class.java)

        sharedViewModel = SharedPreference(requireContext())


        intial()
       binding?.emailContainer?.helperText = null
        binding?.FullNameContainer?.helperText = null
        binding?.phoneContainer?.helperText = null

        binding?.btnNext1?.setOnClickListener { submitForm() }




        binding?.tvAlreadyLogin?.setOnClickListener {
//            val navController = findNavController()
            // Replace YourLoginDestination with the ID of your login destination
            startActivity(Intent(activity, LoginActivity::class.java))
//
//            // Clear back stack up to the register_Page1 fragment
//            navController.popBackStack(R.id.register_Page1, false)
            activity?.setResult(Activity.RESULT_CANCELED)
//            activity?.finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Handle the back button press
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.setResult(Activity.RESULT_CANCELED)
//            activity?.finish()
        }

        return binding.root
    }



    private fun intial() {
        emailFocusListener()
        phoneFocusListener()
        fullNameFocusListener()


        binding?.emailContainer?.helperText = validEmail()
        binding?.FullNameContainer?.helperText = validFullName()
        binding?.phoneContainer?.helperText = validPhone()

        val validEmail = binding?.emailContainer?.helperText == null
        val validFullName = binding?.FullNameContainer?.helperText == null
        val validPhone = binding?.phoneContainer?.helperText == null


        if (!(validEmail && validPhone && validFullName)) {

            binding?.emailEditText?.setText(sharedViewModel.email)
            binding?.FullNameEditText?.setText(sharedViewModel.fullName)
            binding?.phoneEditText?.setText(sharedViewModel.phone)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fullNameFocusListener() {
        binding?.FullNameEditText?.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding?.FullNameContainer?.helperText = validFullName()
            }
        }
    }

    private fun validFullName(): String? {

        if(TextUtils.isEmpty(binding?.FullNameEditText?.getText().toString())){
            return "Must be valid Name"
        }

        return null

    }

    private fun submitForm()
    {
        binding?.emailContainer?.helperText = validEmail()
        binding?.FullNameContainer?.helperText = validFullName()
        binding?.phoneContainer?.helperText = validPhone()

        val validEmail = binding?.emailContainer?.helperText == null
        val validFullName = binding?.FullNameContainer?.helperText == null
        val validPhone = binding?.phoneContainer?.helperText == null

        if (!(validEmail && validPhone && validFullName)) {
            Snackbar.make(binding?.root!!, "Please enter valid details", Snackbar.LENGTH_LONG)
                .show()
        }
        else {
           sharedViewModel.email = binding?.emailEditText?.text.toString()
            sharedViewModel.fullName = binding?.FullNameEditText?.text.toString()
            sharedViewModel.phone = binding?.phoneEditText?.text.toString()

//            startActivity(Intent(context, Register_Page2::class.java))
            findNavController().navigate(R.id.action_register_Page1_to_register_Page2)
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
}
