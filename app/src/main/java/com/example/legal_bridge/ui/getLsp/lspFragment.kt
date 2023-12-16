package com.example.legal_bridge.ui.getLsp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.legal_bridge.R
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.model.lsp.lspDataListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class lspFragment : Fragment() {

    private var telephonyManager: TelephonyManager? = null
    private lateinit var phoneStateListener: PhoneStateListener
    private lateinit var sharedViewModel: SharedPreference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: lspAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_lsp, container, false)

        sharedViewModel = SharedPreference(requireContext())

        recyclerView = root.findViewById(R.id.rv_lsp)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val typeOfLsp = "advocate"

        setupRecyclerView(typeOfLsp)

        telephonyManager = requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    Toast.makeText(requireContext(), "Call end", Toast.LENGTH_LONG).show()
                }
            }
        }
        checkAndRequestPermission()

        return root
    }




    private fun setupRecyclerView(typeOfLsp: String) {
        val token = sharedViewModel.token

        val apiService = RetrofitClient.getApiService()
        val call = apiService.getLSPData("Bearer " + sharedViewModel.token!!)

        call.enqueue(object : Callback<List<lspDataListResponse>> {
            override fun onResponse(
                call: Call<List<lspDataListResponse>>,
                response: Response<List<lspDataListResponse>>
            ) {
                if (response.isSuccessful) {
                    val lspDataList = response.body() ?: emptyList()
                    adapter = lspAdapter(lspDataList, typeOfLsp)
                    recyclerView.adapter = adapter
                } else {
                    // Handle unsuccessful response
                    // ...
                }
            }

            override fun onFailure(call: Call<List<lspDataListResponse>>, t: Throwable) {
                // Handle failure
                // ...
            }
        })
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_CALL_LOG),
                READ_CALL_LOG_PERMISSION_CODE
            )
        } else {
            registerPhoneStateListener()
        }
    }

    private fun registerPhoneStateListener() {
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CALL_LOG_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerPhoneStateListener()
            } else {
                // Permission denied, handle accordingly
                // For example, show a message explaining why the permission is needed
            }
        }
    }

    companion object {
        private const val READ_CALL_LOG_PERMISSION_CODE = 1001
    }
}
