package com.example.legal_bridge.ui.account_details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.legal_bridge.databinding.FragmentAccountDetailsBinding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.ui.account_details.accountSections.EditUserDetails
import com.example.legal_bridge.ui.account_details.accountSections.HelpCenterActivity
import com.example.legal_bridge.ui.account_details.accountSections.SettingActivity
import com.example.legal_bridge.ui.account_details.accountSections.UserAccountDetails
import com.example.legal_bridge.ui.home.AccountDetailsViewModel
import com.example.legal_bridge.ui.login.LoginActivity
import com.squareup.picasso.Picasso

class AccountDetailsFragment : Fragment() {

    private var _binding: FragmentAccountDetailsBinding? = null
    private lateinit var sharedViewModel: SharedPreference
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountDetailsViewModel =
            ViewModelProvider(this).get(AccountDetailsViewModel::class.java)

        _binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedViewModel = SharedPreference(requireContext())

        binding.flAppointment.setOnClickListener{
            Toast.makeText(context, "THIS", Toast.LENGTH_LONG).show()
        }

        binding?.userName?.setText(sharedViewModel.fullName)
        binding?.userEmail?.setText(sharedViewModel.email)


        binding?.flLogOut?.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            sharedViewModel.setDefaultValues()
            sharedViewModel.email = null
            startActivity(intent)
            requireActivity().finishAffinity();
        }
        Toast.makeText(requireContext(),"${sharedViewModel.image}",Toast.LENGTH_LONG).show()

        try {
            val imageUrl = sharedViewModel.image?.replace("http://", "https://")

            Picasso.get()
                .load(imageUrl )
                .into(binding?.userProfileImage)
        } catch (e: Exception) {
            // Handle exceptions related to image loading
            Log.d("USER","${e.printStackTrace()}")
            e.printStackTrace()
            // You can display a placeholder image or show an error message here
        }

        binding?.flUserProfile?.setOnClickListener {
            startActivity(Intent(requireContext(), UserAccountDetails::class.java))

        }




        binding?.flSetting?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }

        binding?.flHelpCenter?.setOnClickListener {
            startActivity(Intent(requireContext(), HelpCenterActivity::class.java))
        }



        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}