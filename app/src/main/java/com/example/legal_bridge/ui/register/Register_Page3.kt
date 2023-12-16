package com.example.legal_bridge.ui.register

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.legal_bridge.MainActivity
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.databinding.FragmentRegisterPage3Binding
import com.example.legal_bridge.helper.CloudinaryInitializer
import com.example.legal_bridge.helper.SpinnerConstants
import com.example.legal_bridge.helper.LocationHelper
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.model.ErrorResponse.ErrorResponse
import com.example.legal_bridge.model.user.Address
import com.example.legal_bridge.model.user.Location
import com.example.legal_bridge.model.user.UserResponse
import com.example.legal_bridge.model.user.RegisterUserResquest
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Register_Page3 : Fragment() {

    private var _binding: FragmentRegisterPage3Binding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedPreference
    private var Image_URL: String? = null
    private var Latitude: Double? = null
    private var Longitude: Double? = null
    private lateinit var selectedImageUri: Uri
    private var URL: String? = null
    private lateinit var selectedImagePath: String
    private var overlay: View? = null // Declare the overlay as a class variable


//    object CloudinaryManager {
//        private var isInitialized = false
//
//        fun init(context: Context) {
//            if (!isInitialized) {
//                val config = hashMapOf(
//                    "cloud_name" to "dmeer8vir",
//                    "api_key" to "334712144964473",
//                    "api_secret" to "WkSCOB5LcMBtY73a0t-AAznlve8"
//                )
//                MediaManager.init(context, config)
//                isInitialized = true
//            }
//        }
//    }



    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    // Get the selected image URI
                    selectedImageUri = data.data!!
//                    sharedViewModel.image = selectedImageUri.toString()

                    // Set the image in ImageView
                    binding?.ivUserProfile?.setImageURI(selectedImageUri)

//                    binding?.tvPickImage?.text = "Image Selected"
//                    binding?.tvPickImage?.setBackgroundResource(R.drawable.bg_rect_selected)

                    // Get the file path from the URI
                    selectedImagePath = getImagePath(selectedImageUri)
                    sharedViewModel.image = selectedImagePath


                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        _binding = FragmentRegisterPage3Binding.inflate(inflater, container, false)
        sharedViewModel = SharedPreference(requireContext())
//        CloudinaryManager.init(requireContext())
        CloudinaryInitializer.init(requireContext())


        sharedViewModel.image = null


        // null the helpertext
        binding?.FullNameContainer?.helperText = null
        binding?.autoCompleteTextViewCityContainer?.helperText = null
        binding?.autoCompleteTextViewGenderContainer?.helperText = null
        binding?.autoCompleteTextViewStateContainer?.helperText = null
        binding?.passwordContainer?.helperText = null
        binding?.confirmPasswordContainer?.helperText = null
        binding?.pinCodeContainer?.helperText = null



        binding?.fldob?.setOnClickListener {
            showDatePickerDialog()
        }


        setUpCity()
        setUpState()
        setUpGender()

        focusAfterhelperTextshows()







        initial()


        binding?.ivUserProfile?.setOnClickListener {
            PickImage()
        }



        binding?.btnNext3?.setOnClickListener {
            validateFields()
        }


        return binding.root
    }

    private fun PickImage() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(galleryIntent)
    }

    private fun focusAfterhelperTextshows() {
        binding.FullNameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.FullNameEditText.text.toString().trim()
                if (fullName.isNotEmpty()) {
                    sharedViewModel.fullName = binding?.FullNameEditText?.text.toString()
                    binding.FullNameContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }

        binding.autoCompleteTextViewCityText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.autoCompleteTextViewCityText.text.toString().trim()
                if (fullName.isNotEmpty()) {
                    sharedViewModel.city= binding?.autoCompleteTextViewCityText?.text.toString()
                    binding.autoCompleteTextViewCityContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }


        binding.autoCompleteTextViewStateText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.autoCompleteTextViewStateText.text.toString().trim()
                if (fullName.isNotEmpty()) {
                    sharedViewModel.state= binding?.autoCompleteTextViewStateText?.text.toString()
                    binding.autoCompleteTextViewStateContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }


        binding.autoCompleteTextViewGenderText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.autoCompleteTextViewGenderText.text.toString().trim()
                if (fullName.isNotEmpty()) {
                    sharedViewModel.gender= binding?.autoCompleteTextViewGenderText?.text.toString()

                    binding.autoCompleteTextViewGenderContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }

        binding.pinCodeEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.pinCodeEditText.text.toString().trim()
                if (fullName.isNotEmpty()) {
                    sharedViewModel.pincode= binding?.pinCodeEditText?.text.toString()
                    binding.pinCodeContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }

    }

    private fun validateFields() {
        val fullName = binding.FullNameEditText.text.toString().trim()
        val dob = binding.tvDob.text.toString().trim()
        val city = binding.autoCompleteTextViewCityText.text.toString().trim()
        val state = binding.autoCompleteTextViewStateText.text.toString().trim()
        val pinCode = binding.pinCodeEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (fullName.isEmpty()) {
            binding.FullNameContainer.helperText = "Full Name is required"
            return
        } else {
            binding.FullNameContainer.helperText = null
        }

        if (dob == "DD/MM/YYYY") {
            showSnackbar( "Date of Birth is required")
            return
        }

        if (city.isEmpty()) {
            binding.autoCompleteTextViewCityContainer.helperText = "City is required"
            return
        } else {
            binding.autoCompleteTextViewCityContainer.helperText = null
        }

        if (state.isEmpty()) {
            binding.autoCompleteTextViewStateContainer.helperText = "State is required"
            return
        } else {
            binding.autoCompleteTextViewStateContainer.helperText = null
        }


        if (pinCode.isEmpty()) {
            binding.pinCodeContainer.helperText = "Pin Code is required"
            return
        } else {
            binding.pinCodeContainer.helperText = null
        }

        if (password.isBlank()) {
            binding?.passwordContainer?.helperText =
                "Enter Password"
        } else if (!isPasswordValid(password)) {
            binding?.passwordContainer?.helperText =
                "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
        } else if (confirmPassword.isBlank()) {
            binding?.confirmPasswordContainer?.helperText = "Enter Confirm Password"
        } else {
            // Reset the helper text for the password field
            binding?.passwordContainer?.helperText = null

            if (password != confirmPassword) {
                // Set helper text for confirm password field
                binding?.confirmPasswordContainer?.helperText = "Passwords do not match"
            } else {
                // Reset the helper text for the confirm password field
                binding?.confirmPasswordContainer?.helperText = null

                if (!(binding?.checkboxTandT?.isChecked!!) || !(binding?.checkboxPrivacypolicy?.isChecked!!)) {
                    showSnackbar("Please agree to Terms and Conditions and Privacy Policy")
                } else {
                    binding?.progressBar?.isVisible = true
                    (activity as? ViewGroup)?.addView(overlay)

                    // Store passwords in ViewModel or wherever needed
                    sharedViewModel.pass = password
                    sharedViewModel.confirmpass = confirmPassword

//                        val dateString = "Thu Oct 26 2023 00:00:00 GMT+0530 (India Standard Time)"
//                        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)")
//                        dateFormat.timeZone = TimeZone.getTimeZone("GMT") // Setting the timezone
//                        val date: Date = dateFormat.parse(dateString)



                    coordinatesFetch()


                    if (!(sharedViewModel.image == null)) {
                        uploadToCloudinary(sharedViewModel.image!!)
                    } else {
                        register("https://res.cloudinary.com/dmeer8vir/image/upload/v1700830965/roqfusjibr7xrzu4ygve.png")
                    }
                }
            }
        }
    }

    private fun coordinatesFetch() {
        val locationHelper = LocationHelper(requireContext())
        val address =  binding?.autoCompleteTextViewCityText?.text.toString() + "" + binding?.autoCompleteTextViewStateText?.text?.toString() +" Pincode: " +binding?.pinCodeEditText?.text.toString()
Toast.makeText(requireContext(),"${address}",Toast.LENGTH_LONG).show()
        val coordinates = locationHelper.getLatLngFromAddress(address)
//        val coordinates = locationHelper.getLatLngFromAddress("Room-605, Tower-11, Panchsheel Primrose, Opp. Govindpuram Anaaj Mandi, New, Hapur Rd, Ghaziabad, Uttar Pradesh 201013")
        if (coordinates != null) {
            val (latitude, longitude) = coordinates
            // Use latitude and longitude values here
            Latitude = latitude
            Longitude = longitude
            Log.d("LONG","$Latitude")
            Log.d("LONG","$Longitude")

        } else {
            Latitude = 0.0
            Latitude = 0.0
        }
    }

    private fun setUpCity() {
        val cities = SpinnerConstants.city // Replace with your city list
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.autoCompleteTextViewCityText.setAdapter(adapter)

        binding.autoCompleteTextViewCityText.threshold = 1 // Set minimum number of characters before suggestions start
        binding.autoCompleteTextViewCityText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.autoCompleteTextViewCityText.showDropDown()
            }
        }

        binding.autoCompleteTextViewCityText.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedCity = cities[position] // Get the selected city from the list
                sharedViewModel.city = selectedCity // Set selected city in the shared view model
            }
    }


    private fun setUpState() {
        val states = SpinnerConstants.indianStates // Replace with your city list
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, states)
        binding.autoCompleteTextViewStateText.setAdapter(adapter)

        binding.autoCompleteTextViewStateText.threshold = 1 // Set minimum number of characters before suggestions start
        binding.autoCompleteTextViewStateText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.autoCompleteTextViewStateText.showDropDown()
            }
        }

        binding.autoCompleteTextViewStateText.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedState = states[position] // Get the selected city from the list
                sharedViewModel.state = selectedState // Set selected city in the shared view model
            }
    }


    private fun setUpGender() {
        val gender = SpinnerConstants.gender // Replace with your city list
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, gender)
        binding.autoCompleteTextViewGenderText.setAdapter(adapter)

        binding.autoCompleteTextViewGenderText.threshold = 1 // Set minimum number of characters before suggestions start
        binding.autoCompleteTextViewGenderText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.autoCompleteTextViewGenderText.showDropDown()
            }
        }

        binding.autoCompleteTextViewGenderText.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedGender= gender[position] // Get the selected city from the list
                sharedViewModel.gender   = selectedGender // Set selected city in the shared view model
            }
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



    private fun getImagePath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor?.getString(it) } ?: ""
        cursor?.close()
        return path
    }


    private fun register(pic: String?) {

        Toast.makeText(requireContext(),"${sharedViewModel.tvDob}",Toast.LENGTH_LONG).show()

        val Location = Location(
            lat = Latitude?:0.0,
            long =  Longitude?:0.0
        )

        val address = Address(
            city = binding?.autoCompleteTextViewCityText?.text?.toString()!!,
            state = binding?.autoCompleteTextViewStateText?.text?.toString()!!,
            pincode = binding?.pinCodeEditText?.text?.toString()!!,
            coordinates = Location
        )

        val userRequest = RegisterUserResquest(
            dob = sharedViewModel.tvDob!!,
            email = sharedViewModel.email!!,
            gender = sharedViewModel.gender!!,
            address = address ,
            name = sharedViewModel.fullName!!,
            password = sharedViewModel.confirmpass!!,
            phone = sharedViewModel.phone!!,
            pic = pic
                ?: "https://res.cloudinary.com/dmeer8vir/image/upload/v1700830965/roqfusjibr7xrzu4ygve.png",
            role = "user"
        )


        val apiService = RetrofitClient.getApiService()
        val call = apiService.registerUser(userRequest)

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>,
            ) {
                if (response.isSuccessful) {
                    // Registration successful, handle accordingly
                    Log.d("USER", "${response.body()}")
                    Log.d("USER", "${response.code()}")
                    Log.d("USER", "${response.errorBody().toString()}")
                    Log.d("USER", "${response.errorBody()?.string()}")
                    Log.d("TOKEN", "${response.body()?.token}")

                    sharedViewModel.image = pic
                    sharedViewModel.isAlreadyLoggedIn = true
                    sharedViewModel.tvDob = response.body()?.dob
                    sharedViewModel._id = response.body()?._id
                    sharedViewModel.token = response?.body()?.token


                    binding?.progressBar?.visibility =
                        View.GONE                    // Proceed to the next screen or perform other actions
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    // Finish the current fragment
                    requireActivity().finishAffinity();
                } else {
                                    // Registration failed, handle accordingly
                    Log.d("USER", "${response.body()}")
                    Log.d("USER", "${response.code()}")
//                    Log.d("USER", "${response.message()}")
//                    Log.d("USER", "${response.errorBody().toString()}")
//                    Log.d("USER", "${response.errorBody()?.string()}")

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
                Log.d("USER", "${t.message}")
                showErrorInDialog(t.message!!)

            }
        })
    }

    private fun initial() {
        binding?.passwordEditText?.setText(sharedViewModel.pass)
        binding?.confirmPasswordEditText?.setText(sharedViewModel.confirmpass)
        binding?.FullNameEditText?.setText(sharedViewModel.fullName)
        binding?.autoCompleteTextViewStateText?.setText(sharedViewModel.state)
        binding?.autoCompleteTextViewCityText?.setText(sharedViewModel.city)
        binding?.pinCodeEditText?.setText(sharedViewModel.pincode)
        binding?.autoCompleteTextViewGenderText?.setText(sharedViewModel.gender)

        if (sharedViewModel.tvDob != null) {
            val dob = convertDateFormattoOriginal(sharedViewModel.tvDob!!)
            if (dob != null) {
                binding?.tvDob?.text = dob
            }
        }



//        val address =
//            "Ajay Kumar Garg Engineering College, 27th KM Milestone, Delhi - Meerut Expy, Ghaziabad, Uttar Pradesh 201015"
//        val locationHelper = LocationHelper(requireContext()) // Pass context here

//        val coordinates = locationHelper.getLatLngFromAddress(address)
//        if (coordinates != null) {
//            val (latitude, longitude) = coordinates
//            // Use latitude and longitude values here
//            Latitude = latitude
//            Longitude = longitude
//
//        } else {
//            // Handle the case where coordinates couldn't be retrieved
//            Latitude = 0.0
//            Latitude = 0.0
//        }

    }


    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"
        return password.matches(passwordRegex.toRegex())
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    fun getLatLngFromAddress(addressStr: String): Pair<Double, Double>? {
//        val geocoder = Geocoder(requireContext())
//        try {
//            val addresses: List<Address> = geocoder.getFromLocationName(addressStr, 1)!!
//            if (addresses.isNotEmpty()) {
//                val latitude = addresses[0].latitude
//                val longitude = addresses[0].longitude
//                Log.d("LONG","$longitude")
//                Log.d("LONG","$latitude")
//                return Pair(latitude, longitude)
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return null
//    }

    @SuppressLint("SetTextI18n")
    private fun uploadToCloudinary(filePath: String?) {
        Log.d("A", "sign up uploadToCloudinary- ")
        MediaManager.get().upload(filePath).callback(object : UploadCallback {
            override fun onStart(requestId: String) {
//                tv_status?.text = "start"
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
//                tv_status?.text = "Uploading... "
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
//                tv_status?.text = "image URL: " + resultData["url"].toString()
                Image_URL = resultData["url"].toString()
                Log.d("URL image", "${Image_URL}")

                val url = resultData["url"].toString()
                sharedViewModel.image = Image_URL
                register(Image_URL.toString())
            }

            override fun onError(requestId: String, error: ErrorInfo) {
//                tv_status?.text = "error " + error.description
                Log.d("USER", "${error.description}")
                binding?.progressBar?.visibility = View.GONE
                showErrorInDialogforUploadPic("Error occurred while uploading the image. Register instead of using an image.")
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
//                tv_status?.text = "Reshedule " + error.description
                Log.d("USER", "${error.description}")
                binding?.progressBar?.visibility = View.GONE
                showErrorInDialogforUploadPic("Error occurred while uploading the image. Register instead of using an image.")

            }
        }).dispatch()
    }



    fun showErrorInDialogforUploadPic(mess: String) {
        val builder = AlertDialog.Builder(requireContext())
        val message =mess
        builder.apply {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
               register(null)
                binding?.progressBar?.visibility = View.VISIBLE

            }
        }.create().show()
    }



    fun showErrorInDialog(mess: String) {
        binding?.progressBar?.visibility =
            View.GONE
        val builder = AlertDialog.Builder(requireContext())
        val message =mess
        builder.apply {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()


            }
        }.create().show()
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



    private fun createTransparentOverlay() {
        // Check if overlay already exists to avoid duplicates
        if (overlay == null) {
            overlay = View(context)
            overlay?.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.TRANSPARENT)
                isClickable = true // Intercept touch events
            }

            // Add the overlay to the root layout of your activity/fragment
            (activity as? ViewGroup)?.addView(overlay)
        }
    }

    private fun removeTransparentOverlay() {
        overlay?.let {
            // Remove the overlay from the root layout
            (activity as? ViewGroup)?.removeView(it)
            overlay = null // Reset overlay reference
        }
    }


}

