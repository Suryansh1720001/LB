package com.example.legal_bridge.helper


object SpinnerConstants {
    val indianStates = arrayOf(
        "Select State","Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh",
        "Chhattisgarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana",
        "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra",
        "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim",
        "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    )


    val nationality = arrayOf(
        "Select Country", "India", "Afghan", "Albanian", "Algerian", "Andorran", "Angolan", "Antiguans", "Argentinean",
        "Armenian", "Australian", "Austrian", "Azerbaijani", "Bahamian", "Bahraini", "Bangladeshi", "Barbadian",
        "Belarusian", "Belgian", "Belizean", "Beninese", "Bhutanese", "Bolivian", "Bosnian", "Brazilian",
        "British", "Bruneian", "Bulgarian", "Burkinabe", "Burmese", "Burundian", "Cambodian", "Cameroonian",
        "Canadian", "Cape Verdean", "Central African", "Chadian", "Chilean", "Chinese", "Colombian", "Comoran",
        "Congolese", "Costa Rican", "Croatian", "Cuban", "Cypriot", "Czech", "Danish", "Djibouti", "Dominican",
        "Dutch", "East Timorese", "Ecuadorean", "Egyptian", "Emirian", "Equatorial Guinean", "Eritrean",
        "Estonian", "Ethiopian", "Fijian", "Filipino", "Finnish", "French", "Gabonese", "Gambian", "Georgian",
        "German", "Ghanaian", "Greek", "Grenadian", "Guatemalan", "Guinea-Bissauan", "Guinean", "Guyanese",
        "Haitian", "Herzegovinian", "Honduran", "Hungarian", "I-Kiribati", "Icelander", "Indonesian", "Iranian",
        "Iraqi", "Irish", "Israeli", "Italian", "Ivorian", "Jamaican", "Japanese", "Jordanian", "Kazakhstani",
        "Kenyan", "Kittian and Nevisian", "Kuwaiti", "Kyrgyz", "Laotian", "Latvian", "Lebanese", "Liberian",
        "Libyan", "Liechtensteiner", "Lithuanian", "Luxembourger", "Macedonian", "Malagasy", "Malawian",
        "Malaysian", "Maldivan", "Malian", "Maltese", "Marshallese", "Mauritanian", "Mauritian", "Mexican",
        "Micronesian", "Moldovan", "Monacan", "Mongolian", "Moroccan", "Mosotho", "Motswana", "Mozambican",
        "Namibian", "Nauruan", "Nepalese", "New Zealander", "Nicaraguan", "Nigerian", "Nigerien", "North Korean",
        "Northern Irish", "Norwegian", "Omani", "Pakistani", "Palauan", "Panamanian", "Papua New Guinean",
        "Paraguayan", "Peruvian", "Polish", "Portuguese", "Qatari", "Romanian", "Russian", "Rwandan",
        "Saint Lucian", "Salvadoran", "Samoan", "San Marinese", "Sao Tomean", "Saudi", "Scottish", "Senegalese",
        "Serbian", "Seychellois", "Sierra Leonean", "Singaporean", "Slovakian", "Slovenian", "Solomon Islander",
        "Somali", "South African", "South Korean", "Spanish", "Sri Lankan", "Sudanese", "Surinamer", "Swazi",
        "Swedish", "Swiss", "Syrian", "Taiwanese", "Tajik", "Tanzanian", "Thai", "Togolese", "Tongan",
        "Trinidadian or Tobagonian", "Tunisian", "Turkish", "Tuvaluan", "Ugandan", "Ukrainian", "Uruguayan",
        "Uzbekistani", "Venezuelan", "Vietnamese", "Welsh", "Yemenite", "Zambian", "Zimbabwean"
    )


    val gender = arrayListOf("Select gender","male", "female", "not specify")

    fun getGenderIndex(genderToFind: String): Int {
        return gender.indexOf(genderToFind)
    }

    val city = listOf(
        "Select City", "Agartala", "Agra", "Ahmedabad", "Ajmer", "Akola", "Aligarh", "Allahabad", "Alwar", "Ambattur",
        "Amravati", "Amritsar", "Asansol", "Aurangabad", "Bareilly", "Belgaum", "Bellary", "Bhavnagar", "Bhilai",
        "Bhilwara", "Bhiwandi", "Bhopal", "Bhubaneswar", "Bikaner", "Bokaro", "Chandigarh", "Chandrapur", "Chennai",
        "Coimbatore", "Cuttack", "Dehradun", "Delhi", "Dhanbad", "Durgapur", "Erode", "Faridabad", "Gaya", "Ghaziabad",
        "Gorakhpur", "Gurgaon", "Guwahati", "Gwalior", "Hisar", "Howrah", "Hubli-Dharwad", "Hyderabad", "Imphal",
        "Indore", "Jabalpur", "Jaipur", "Jalandhar", "Jammu", "Jamnagar", "Jamshedpur", "Jhansi", "Jodhpur", "Kakinada",
        "Kanpur", "Kochi", "Kolhapur", "Kolkata", "Kollam", "Kota", "Kozhikode", "Kurnool", "Lucknow", "Ludhiana",
        "Madurai", "Maheshtala", "Malegaon", "Mangalore", "Meerut", "Moradabad", "Mumbai","Muzaffarnagar", "Mysore", "Nagpur", "Nanded",
        "Nashik", "Nellore", "Noida", "Patna", "Pimpri-Chinchwad", "Pune", "Raipur", "Rajkot", "Rajpur Sonarpur",
        "Ranchi", "Rourkela", "Saharanpur", "Salem", "Sangli-Miraj & Kupwad", "Shahjahanpur", "Siliguri", "Solapur",
        "Srinagar", "Surat", "Thane", "Thiruvananthapuram", "Thrissur", "Tiruchirappalli", "Tirunelveli", "Tiruppur",
        "Udaipur", "Ujjain", "Ulhasnagar", "Vadodara", "Varanasi", "Vasai-Virar", "Vijayawada", "Visakhapatnam",
        "Warangal"
    )


}
