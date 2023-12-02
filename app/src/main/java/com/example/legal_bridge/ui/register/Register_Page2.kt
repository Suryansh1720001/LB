package com.example.legal_bridge.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.legal_bridge.helper.SpinnerConstants
import com.example.legal_bridge.R
import com.example.legal_bridge.databinding.FragmentRegisterPage2Binding
import com.example.legal_bridge.helper.SharedPreference
import com.google.android.material.snackbar.Snackbar

import java.text.SimpleDateFormat
import java.util.*

class Register_Page2 : Fragment() {

//    companion object {
//        private val IMAGE_CHOOSE = 1000;
//        private val PERMISSION_CODE = 1001;
//        private val REQUEST_CODE = 1002  // Use any unique value
//    }


    private lateinit var sharedViewModel: SharedPreference


    private var _binding: FragmentRegisterPage2Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRegisterPage2Binding.inflate(inflater, container, false)
//        sharedViewModel = ViewModelProvider(this).get(SharedPreference::class.java)
        sharedViewModel = SharedPreference(requireContext())

        binding?.AddressLine1?.helperText = null
        binding?.AddressLine2?.helperText = null
        binding?.pinCodeContainer?.helperText = null

        initial()






        binding?.fldob?.setOnClickListener {
            showDatePickerDialog()
        }


        // for nation
        val Nationadapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            SpinnerConstants.nationality
        )
        Nationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerNationality?.adapter = Nationadapter
        binding?.spinnerNationality?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val selectedNation = SpinnerConstants.nationality[position]
                    sharedViewModel.nationality = position

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing here
                }
            }


        // for States ;
        val Stateadapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            SpinnerConstants.indianStates
        )
        Stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerState?.adapter = Stateadapter
        binding?.spinnerState?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle item selection if needed
                    val selectedState = SpinnerConstants.indianStates[position]
                    sharedViewModel.state = position

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing here
                }
            }


        // city
        val Cityadapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, SpinnerConstants.city)
        Cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerCity?.adapter = Cityadapter
        binding?.spinnerCity?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle item selection if needed
                val selectedCity = SpinnerConstants.city[position]
                sharedViewModel.city = position

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing here
            }
        }


        //language
//        not required
//        val Languageadapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Constants.language)
//        Languageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding?.spinnerLanguage?.adapter = Languageadapter
//        binding?.spinnerLanguage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                // Handle item selection if needed
//                val selectedLanguage = Constants.language[position]
//                sharedViewModel.language = position
//
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing here
//            }
//        }


        // gender
        val Genderadapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, SpinnerConstants.gender)
        Genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerGender?.adapter = Genderadapter
        binding?.spinnerGender?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle item selection if needed
                    val selectedGender = SpinnerConstants.gender[position]
                    sharedViewModel.gender = position

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing here
                }
            }


//        binding?.tvPickImage?.setOnClickListener {
//            ImagePicker.with(this)
//                .compress(1024)         //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
//                .createIntent { intent ->
//                    startForProfileImageResult.launch(intent)
//                }
//        }

// companion object


        binding?.btnNext2?.setOnClickListener {
            sharedViewModel.addressLine1 = binding?.addressLine1EditText?.text.toString()
            sharedViewModel.addressLine2 = binding?.addressLine2EditText?.text?.toString()
            sharedViewModel.pincode = binding?.pinCodeEditText?.text?.toString()


            if(binding?.tvDob?.text !=null && sharedViewModel.addressLine1 !=null && sharedViewModel.pincode !=null && sharedViewModel.gender!=0 && sharedViewModel.city !=0 &&
                sharedViewModel.state!=0 && sharedViewModel.nationality!=0 ){
                findNavController().navigate(R.id.action_register_Page2_to_register_Page3)
            }else{
                Snackbar.make(binding?.root!!, "Please enter valid details", Snackbar.LENGTH_LONG)
                    .show()
            }

        }

        // Inflate the layout for this fragment
        return binding.root
    }




//    private val startForProfileImageResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            val resultCode = result.resultCode
//            val data = result.data
//
//            if (resultCode == Activity.RESULT_OK) {
//                //Image Uri will not be null for RESULT_OK
//                val fileUri = data?.data!!
//
//               selectedImageUri = fileUri
//               binding?.ivUserProfile?.setImageURI(fileUri)
//                selectedImagePath = getImagePath(selectedImageUri)
//
//                Toast.makeText(context, "$selectedImagePath", Toast.LENGTH_LONG).show()
//                sharedViewModel.image = selectedImagePath
//            } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
//            }
//        }


    private fun initial() {
        Toast.makeText(context, "${sharedViewModel.image}", Toast.LENGTH_LONG).show()
//      val dob = convertDateFormattoOriginal(sharedViewModel.tvDob!!)
//        if(convertDateFormattoOriginal(sharedViewModel.tvDob!!)!=null){
//            binding?.tvDob?.text = dob
//        }

        if (sharedViewModel.tvDob != null) {
            val dob = convertDateFormattoOriginal(sharedViewModel.tvDob!!)
            if (dob != null) {
                binding?.tvDob?.text = dob
            }
        }

        binding?.pinCodeEditText?.setText(sharedViewModel.pincode)
        binding?.addressLine1EditText?.setText(sharedViewModel.addressLine1)
        binding?.addressLine2EditText?.setText(sharedViewModel.addressLine2)


// Inside the initial() function
        val savedGenderPosition = sharedViewModel.gender
        Toast.makeText(requireContext(),"$savedGenderPosition",Toast.LENGTH_LONG).show()
//        binding?.spinnerGender?.setSelection(savedGenderPosition!!)

//        val savedGenderPosition = sharedViewModel.gender
        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, SpinnerConstants.gender)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerGender?.adapter = genderAdapter

// Set selection after the adapter is set
        binding?.spinnerGender?.setSelection(2)
        binding?.spinnerCity?.setSelection(2)



//        if(sharedViewModel.image !=null){
//            binding?.tvPickImage?.text = "Image Selected"
//            binding?.tvPickImage?.setBackgroundResource(R.drawable.bg_rect_selected)
//        }

        // spinner reamainng



    }


    private fun convertDateFormattoOriginal(inputDate: String?): String? {
        if (inputDate.isNullOrEmpty()) {
            // Handle the case when the date string is null or empty
             return null

        }
        val inputFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Update the calendar with the selected date
                calendar.set(year, month, dayOfMonth)

                // Format the date as "dd/MM/yyyy" for the TextView
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = sdf.format(calendar.time)
                binding?.tvDob?.text = formattedDate

                // Convert the date to the desired format for sharedViewModel.tvDob
                val dateFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.getDefault())
                val formattedForViewModel = dateFormat.format(calendar.time)

                Toast.makeText(requireContext(), formattedForViewModel, Toast.LENGTH_LONG).show()

                // Save the date in sharedViewModel.tvDob
                sharedViewModel.tvDob = formattedForViewModel
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }




    fun onStateInputLayoutClick(view: View) {
        // Open the dropdown
        val stateSpinner = view.findViewById<Spinner>(R.id.spinner_state)
        stateSpinner.performClick()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
