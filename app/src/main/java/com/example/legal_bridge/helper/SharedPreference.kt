package com.example.legal_bridge.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreference(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

// first fragment
    var email: String?
        get() = prefs.getString("email", null)
        set(value) = prefs.edit { putString("email", value) }

    var fullName: String?
        get() = prefs.getString("fullName", null)
        set(value) = prefs.edit { putString("fullName", value) }

    var phone: String?
        get() = prefs.getString("phone", null)
        set(value) = prefs.edit { putString("phone", value) }


    // second fragment

    var tvDob: String?
        get() = prefs.getString("tvDob", null)
        set(value) = prefs.edit { putString("tvDob", value) }

    var nationality: Int?
        get() = prefs.getInt("Nationality", 0)
        set(value) = prefs.edit { putInt("Nationality", value ?: 0) }

    var state: Int?
        get() = prefs.getInt("State", 0) // Provide a default value suitable for your use case
        set(value) = prefs.edit { putInt("State", value ?: 0) } // Use the Elvis operator to handle null values

    var city: Int?
        get() = prefs.getInt("City", 0) // Provide a default value suitable for your use case
        set(value) = prefs.edit { putInt("City", value ?: 0) } // Use the Elvis operator to handle null values

    var language: Int?
        get() = prefs.getInt("Language", 0) // Provide a default value suitable for your use case
        set(value) = prefs.edit { putInt("Language", value ?: 0) } // Use the Elvis operator to handle null values

    var gender: Int?
        get() = prefs.getInt("Gender", 0) // Provide a default value suitable for your use case
        set(value) = prefs.edit { putInt("Gender", value ?: 0) } // Use the Elvis operator to handle null values

    var image: String?
        get() = prefs.getString("image", null)
        set(value) = prefs.edit { putString("image", value) }

    var pincode: String?
        get() = prefs.getString("pincode", null)
        set(value) = prefs.edit { putString("pincode", value) }

    var addressLine1: String?
        get() = prefs.getString("addressLine1", null)
        set(value) = prefs.edit { putString("addressLine1", value) }

    var addressLine2: String?
        get() = prefs.getString("addressLine2", null)
        set(value) = prefs.edit { putString("addressLine2", value) }


    var isAlreadyLoggedIn: Boolean
        get() = prefs.getBoolean("isAlreadyLoggedIn", false)
        set(value) = prefs.edit { putBoolean("isAlreadyLoggedIn", value) }



    // third fragment

    var pass: String?
        get() = prefs.getString("pass", null)
        set(value) = prefs.edit { putString("pass", value) }

    var confirmpass: String?
        get() = prefs.getString("confirmpass", null)
        set(value) = prefs.edit { putString("confirmpass", value) }






    fun setDefaultValues() {
        // Set default values for each preference
        clearPreference("email")
        clearPreference("fullName")
        clearPreference("phone")

        // Set default values for preferences in the second fragment
        clearPreference("tvDob")
        clearPreference("Nationality")
        clearPreference("State")
        clearPreference("City")
        clearPreference("Language")
        clearPreference("Gender")
        clearPreference("image")
        clearPreference("pincode")
        clearPreference("addressLine1")
        clearPreference("addressLine2")
        clearPreference("isAlreadyLoggedIn")

        // Set default values for preferences in the third fragment
        clearPreference("pass")
        clearPreference("confirmpass")

        // Add more preferences as needed
    }

    private fun clearPreference(key: String) {
        prefs.edit().remove(key).apply()
    }


//    // Function to set default value for a specific preference
//    private inline fun <reified T : Any> setValue(key: String, defaultValue: T) {
//        when (defaultValue) {
//            is String -> prefs.edit { putString(key, defaultValue) }
//            is Int -> prefs.edit { putInt(key, defaultValue) }
//            // Add more cases for other data types if needed
//        }
//    }
//
//    // Function to set default values for all preferences
//    fun setDefaultValues() {
//        // Set default values for each preference
//        setValue("email", "")
//        setValue("fullName", "")
//        setValue("phone", "")
//
//        // Set default values for preferences in the second fragment
//        setValue("tvDob", "")
//        setValue("Nationality", 0)
//        setValue("State", 0)
//        setValue("City", 0)
//        setValue("Language", 0)
//        setValue("Gender", 0)
//        setValue("image", "")
//        setValue("pincode", "")
//        setValue("addressLine1", "")
//        setValue("addressLine2", "")
//
//        // Set default values for preferences in the third fragment
//        setValue("pass", "")
//        setValue("confirmpass", "")
//
//        // Add more preferences as needed
//    }


}

