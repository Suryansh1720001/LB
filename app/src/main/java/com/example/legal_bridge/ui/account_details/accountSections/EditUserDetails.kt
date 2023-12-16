package com.example.legal_bridge.ui.account_details.accountSections

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.legal_bridge.api.RetrofitClient
import com.example.legal_bridge.databinding.ActivityEditUserDetailsBinding
import com.example.legal_bridge.helper.CloudinaryInitializer
import com.example.legal_bridge.helper.LocationHelper
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.helper.SpinnerConstants
import com.example.legal_bridge.helper.utils
import com.example.legal_bridge.model.user.Address
import com.example.legal_bridge.model.user.EditUserDetails
import com.example.legal_bridge.model.user.Location
import com.example.legal_bridge.model.user.UserResponse
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditUserDetails : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserDetailsBinding
    private lateinit var sharedViewModel: SharedPreference
    private var Image_URL: String? = null
    private var Latitude: Double? = null
    private var Longitude: Double? = null
    private lateinit var selectedImageUri: Uri
    private var URL: String? = null
    private var selectedImagePath: String? = null
//    private lateinit var previousImageURL :String
    private lateinit var mDate :String
    private lateinit var mUploadedImageURL : String

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



                }
            }
        }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CloudinaryInitializer.init(this@EditUserDetails)
//        CloudinaryManager.init(this@EditUserDetails)
        sharedViewModel = SharedPreference(this)
//        previousImageURL = sharedViewModel.image?:"https://res.cloudinary.com/dmeer8vir/image/upload/v1700830965/roqfusjibr7xrzu4ygve.png"
//        sharedViewModel.image = null

        sharedViewModel.isUserDetailsUpdated = false
        // null helper text
        binding?.FullNameContainer?.helperText = null
        binding?.autoCompleteTextViewCityContainer?.helperText = null
        binding?.autoCompleteTextViewGenderContainer?.helperText = null
        binding?.autoCompleteTextViewStateContainer?.helperText = null
        binding?.pinCodeContainer?.helperText = null


        binding?.FullNameEditText?.setText(sharedViewModel.fullName!!)
        binding?.tvDob?.setText(utils().convertTimestamp(sharedViewModel.tvDob!!))
        binding?.autoCompleteTextViewCityText?.setText(sharedViewModel.city)
        binding?.autoCompleteTextViewStateText?.setText(sharedViewModel.state)
        binding?.autoCompleteTextViewGenderText?.setText(sharedViewModel.gender)
        binding?.pinCodeEditText?.setText(sharedViewModel.pincode)

        try {
            val imageUrl = sharedViewModel.image?.replace("http://", "https://")
            Picasso.get()
                .load(imageUrl )
                .into(binding?.ivUserProfile)
        } catch (e: Exception) {
            // Handle exceptions related to image loading
            Log.d("USER","${e.printStackTrace()}")
            e.printStackTrace()
            // You can display a placeholder image or show an error message here
        }


        setUpCity()
        setUpState()
        setUpGender()
        focusAfterhelperTextshows()

        binding?.btnSaveChanges?.setOnClickListener {
            validateFields()
        }

        binding?.fldob?.setOnClickListener {
            showDatePickerDialog()
        }

        binding?.ivUserProfile?.setOnClickListener {
           PickImage()
        }

        mDate =  sharedViewModel.tvDob!!



    }


    private fun PickImage() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(galleryIntent)
    }

    private fun getImagePath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor?.getString(it) } ?: ""
        cursor?.close()
        return path
    }

    private fun validateFields() {
        val fullName = binding.FullNameEditText.text.toString().trim()
        val dob = binding.tvDob.text.toString().trim()
        val city = binding.autoCompleteTextViewCityText.text.toString().trim()
        val state = binding.autoCompleteTextViewStateText.text.toString().trim()
        val pinCode = binding.pinCodeEditText.text.toString().trim()


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


                    coordinatesFetch()


                    if (!(selectedImagePath == null)) {
                        uploadToCloudinary(selectedImagePath)
                    } else {
                       SaveChanges(sharedViewModel.image)
                    }
                }

        private fun coordinatesFetch() {


    val locationHelper = LocationHelper(this@EditUserDetails)
            val address =  binding?.autoCompleteTextViewCityText?.text.toString() + "" + binding?.autoCompleteTextViewStateText?.text?.toString() +" Pincode: " +binding?.pinCodeEditText?.text.toString()

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


    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }


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
//                mUploadedImageURL = Image_URL.toString()
              SaveChanges(Image_URL.toString())
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



    fun showErrorInDialogforUploadPic( mess: String) {
        val builder = AlertDialog.Builder(this@EditUserDetails)
        val message =mess
        builder.apply {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                SaveChanges(sharedViewModel.image)
                binding?.progressBar?.visibility = View.VISIBLE

            }
        }.create().show()
    }



    private fun focusAfterhelperTextshows() {
        binding.FullNameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.FullNameEditText.text.toString().trim()
                if (fullName.isNotEmpty()) {
//                    sharedViewModel.fullName = binding?.FullNameEditText?.text.toString()
                    binding.FullNameContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }

        binding.autoCompleteTextViewCityText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.autoCompleteTextViewCityText.text.toString().trim()
                if (fullName.isNotEmpty()) {
//                    sharedViewModel.city= binding?.autoCompleteTextViewCityText?.text.toString()
                    binding.autoCompleteTextViewCityContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }


        binding.autoCompleteTextViewStateText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.autoCompleteTextViewStateText.text.toString().trim()
                if (fullName.isNotEmpty()) {
//                    sharedViewModel.state= binding?.autoCompleteTextViewStateText?.text.toString()
                    binding.autoCompleteTextViewStateContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }


        binding.autoCompleteTextViewGenderText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.autoCompleteTextViewGenderText.text.toString().trim()
                if (fullName.isNotEmpty()) {
//                    sharedViewModel.gender= binding?.autoCompleteTextViewGenderText?.text.toString()

                    binding.autoCompleteTextViewGenderContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }

        binding.pinCodeEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val fullName = binding.pinCodeEditText.text.toString().trim()
                if (fullName.isNotEmpty()) {
//                    sharedViewModel.pincode= binding?.pinCodeEditText?.text.toString()
                    binding.pinCodeContainer.helperText = null // Reset helper text when field loses focus and is filled
                }
            }
        }

    }



    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this@EditUserDetails,
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
                // Save the date in sharedViewModel.tvDob
                mDate = formattedForViewModel
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }




    private fun SaveChanges(pic: String?) {

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

        val editUserDetails = EditUserDetails(
            dob = mDate,
            gender = binding?.autoCompleteTextViewGenderText?.text.toString(),
            address = address ,
            name = binding?.FullNameEditText?.text.toString(),
            pic = pic
                ?: "https://res.cloudinary.com/dmeer8vir/image/upload/v1700830965/roqfusjibr7xrzu4ygve.png",
            role = "user"
        )

        val apiService = RetrofitClient.getApiService()

// Make the API call
        val userId = sharedViewModel._id!!  // Replace with the actual user ID
        val token = "Bearer " + sharedViewModel.token!!
        val call = apiService.updateUserDetails(token, userId, editUserDetails)

// Execute the call asynchronously
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                   sharedViewModel.fullName = response.body()?.name
                    sharedViewModel.tvDob = response.body()?.dob
                    sharedViewModel.gender = response.body()?.gender
                    sharedViewModel.image = response.body()?.pic
                    sharedViewModel.city = response.body()?.address?.city
                    sharedViewModel.pincode = response.body()?.address?.pincode
                    sharedViewModel.state = response.body()?.address?.state
                    sharedViewModel.isUserDetailsUpdated = true


                    Log.d("USER", "${response.body()}")
                    Log.d("USER", "${response.code()}")
                    Log.d("USER", "${response.message()}")
                    Log.d("USER", "${response.errorBody().toString()}")
                    Log.d("USER", "${response.errorBody()?.string()}")
                    Toast.makeText(this@EditUserDetails,"${response.body()?.message}",Toast.LENGTH_LONG).show()
                    finish()
//                     Process the response here
                } else {
                                        Log.d("USER", "${response.body()}")
                    Log.d("USER", "${response.code()}")
                    Log.d("USER", "${response.message()}")
                    Log.d("USER", "${response.errorBody().toString()}")
                    Log.d("USER", "${response.errorBody()?.string()}")
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Handle failure
                Log.d("USER","{${t.message}}")
            }
        })
    }


    private fun setUpCity() {
        val cities = SpinnerConstants.city // Replace with your city list
        val adapter = ArrayAdapter(this@EditUserDetails, android.R.layout.simple_dropdown_item_1line, cities)
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
        val adapter = ArrayAdapter(this@EditUserDetails, android.R.layout.simple_dropdown_item_1line, states)
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




//
//    private fun setUpState() {
//        val states = citiesByState.keys.toList()
//        val adapter = ArrayAdapter(this@EditUserDetails, android.R.layout.simple_dropdown_item_1line, states)
//        binding.autoCompleteTextViewStateText.setAdapter(adapter)
//
//        binding.autoCompleteTextViewStateText.threshold = 1 // Set minimum number of characters before suggestions start
//        binding.autoCompleteTextViewStateText.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                binding.autoCompleteTextViewStateText.showDropDown()
//            }
//        }
//
//        binding.autoCompleteTextViewStateText.onItemClickListener =
//            AdapterView.OnItemClickListener { _, _, position, _ ->
//                val selectedState = states[position] // Get the selected state from the list
//                sharedViewModel.state = selectedState // Set selected state in the shared view model
//
//                // Update cities based on selected state
//                val citiesForSelectedState = citiesByState[selectedState] ?: emptyList()
//                updateCitiesAdapter(citiesForSelectedState)
//            }
//    }
//
//    private fun updateCitiesAdapter(cities: List<String>) {
//        val adapter = ArrayAdapter(this@EditUserDetails, android.R.layout.simple_dropdown_item_1line, cities)
//        binding.autoCompleteTextViewCityText.setAdapter(adapter)
//    }
//
//    private fun setUpCity() {
//        // Initially, set cities for the first state in the list
//        val initialCities = citiesByState.values.firstOrNull() ?: emptyList()
//        updateCitiesAdapter(initialCities)
//
//        binding.autoCompleteTextViewCityText.threshold = 1 // Set minimum number of characters before suggestions start
//        binding.autoCompleteTextViewCityText.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                binding.autoCompleteTextViewCityText.showDropDown()
//            }
//        }
//
//        binding.autoCompleteTextViewCityText.onItemClickListener =
//            AdapterView.OnItemClickListener { _, _, position, _ ->
//                val selectedCity = citiesByState[sharedViewModel.state]?.get(position) // Get the selected city from the list
//                sharedViewModel.city = selectedCity // Set selected city in the shared view model
//            }
//    }
//
//
//
//
//


    private fun setUpGender() {
        val gender = SpinnerConstants.gender // Replace with your city list
        val adapter = ArrayAdapter(this@EditUserDetails, android.R.layout.simple_dropdown_item_1line, gender)
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



}