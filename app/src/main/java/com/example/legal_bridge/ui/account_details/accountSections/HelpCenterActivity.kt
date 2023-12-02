package com.example.legal_bridge.ui.account_details.accountSections

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.legal_bridge.R
import com.example.legal_bridge.databinding.ActivityHelpCenterBinding
import com.example.legal_bridge.databinding.ActivityMainBinding
import com.example.legal_bridge.helper.utils

class HelpCenterActivity : AppCompatActivity() {

    private lateinit var faqListView: ExpandableListView
    private lateinit var adapter: FAQAdapter
    private val listData: HashMap<String, List<String>> = HashMap()
    private lateinit var binding: ActivityHelpCenterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Assuming you've created your group and child layouts (group_layout.xml and child_layout.xml)

        // Populate your FAQ data (questions and answers)
        populateListData()

        faqListView = findViewById(R.id.faqListView)
        adapter = FAQAdapter(this, listData)
        faqListView.setAdapter(adapter)

        // Set a child click listener to expand/collapse items
//        faqListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//            val answer = parent.adapter.getChild(groupPosition, childPosition) as String
//            // Toggle visibility of the answer
//            toggleAnswer(v, answer)
//            true
//        }
        faqListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val childData = adapter.getChild(groupPosition, childPosition)
            val answer = if (childData is String) {
                childData
            } else {
                childData?.toString() ?: ""
            }

            // Now you can work with the 'answer' variable
            toggleAnswer(v, answer)
            true
        }

        binding?.btnSupport?.setOnClickListener {
//            val userMessage = "User's message goes here"
//            val emailUtils = utils()
//            emailUtils.sendEmail(userMessage)
        }


    }

    private fun populateListData() {

        val question1 = "How do I sign up on LegalBridge?"
        val answer1 = "To sign up, download the LegalBridge app from the Play Store, open the app, and follow the simple registration process to create your account."

        val question2 = "What types of legal professionals are available on LegalBridge?"
        val answer2 = "LegalBridge offers a wide range of legal professionals including Advocates, Arbitrators, Mediators, Notaries, and Document Writers."

        val question3 = "Can I select specific criteria when searching for a legal service provider?"
        val answer3 = "Yes, you can filter providers based on criteria such as service fees, years of experience, proximity, and more to find the most suitable one for your needs."

        val question4 = "How are legal service providers verified on LegalBridge?"
        val answer4 = "All providers undergo a rigorous verification process to ensure credibility and trustworthiness before being listed on the platform."

        val question5 = "Is there a consultation fee?"
        val answer5 = "Many providers offer free consultations. However, some may charge a fee. The app allows you to filter providers offering free consultations."

        val question6 = "Can I book appointments through the app?"
        val answer6 = "Absolutely, once you find a suitable legal service provider, you can seamlessly book an appointment through the app."

        val question7 = "How secure are my documents on LegalBridge?"
        val answer7 = "We employ encrypted, end-to-end document transfers to ensure the utmost security and confidentiality of your legal documents."

        val question8 = "What if I face an issue with a legal service provider?"
        val answer8 = "You can reach out to our support team through the app. We'll assist you in resolving any issues or concerns."

        val question9 = "How are legal fees handled?"
        val answer9 = "Legal fees are discussed and agreed upon between you and the service provider. The app provides transparency in showcasing service fees."

        val question10 = "Is LegalBridge available in multiple languages?"
        val answer10 = "Yes, our app supports multiple languages to cater to users from diverse linguistic backgrounds."

        val question11 = "How can I leave feedback about a legal service provider?"
        val answer11 = "After your interaction, you can rate and leave feedback for the provider. Your input helps maintain quality standards on the platform."

        val question12 = "Are there provisions for handling sensitive cases like domestic violence?"
        val answer12 = "Yes, LegalBridge aims to facilitate the reporting and handling of sensitive cases like domestic violence or sexual assault."

        val question13 = "Can I switch between online and in-person consultations?"
        val answer13 = "Yes, the app allows you to choose between online or in-person consultations based on your preferences."

        val question14 = "How can I ensure the authenticity of a legal service provider?"
        val answer14 = "You can review provider profiles, user ratings, and feedback to gauge their authenticity and reliability."

        val question15 = "What happens if I miss an appointment?"
        val answer15 = "It's advisable to inform the provider in advance. Some providers may have their policies regarding missed appointments."

        val question16 = "How do I pay for legal services on LegalBridge?"
        val answer16 = "Secure payment gateways within the app enable convenient and secure payment for the services."

        val question17 = "Is there a guarantee for the services provided?"
        val answer17 = "While we ensure verification and quality checks, service outcomes may vary. We encourage feedback to maintain quality standards."

        val question18 = "Are there any additional charges apart from the service fees?"
        val answer18 = "Unless specified, there are no hidden charges. Any additional charges will be transparently communicated by the service provider."

        val question19 = "Can I edit my posted legal case?"
        val answer19 = "Yes, you can edit or update your posted case details as needed through the app."

        val question20 = "How do I delete my LegalBridge account?"
        val answer20 = "You can request account deletion by contacting our support team. Ensure all ongoing engagements are resolved before deletion."

        // Populate the HashMap with questions and answers
        listData[question1] = listOf(answer1)
        listData[question2] = listOf(answer2)
        listData[question3] = listOf(answer3)
        listData[question4] = listOf(answer4)
        listData[question5] = listOf(answer5)
        listData[question6] = listOf(answer6)
        listData[question7] = listOf(answer7)
        listData[question8] = listOf(answer8)
        listData[question9] = listOf(answer9)
        listData[question10] = listOf(answer10)
        listData[question11] = listOf(answer11)
        listData[question12] = listOf(answer12)
        listData[question13] = listOf(answer13)
        listData[question14] = listOf(answer14)
        listData[question15] = listOf(answer15)
        listData[question16] = listOf(answer16)
        listData[question17] = listOf(answer17)
        listData[question18] = listOf(answer18)
        listData[question19] = listOf(answer19)
        listData[question20] = listOf(answer20)


    }





    private fun toggleAnswer(view: View, answer: String) {
        val answerTextView = view.findViewById<TextView>(R.id.answerContentTextView)
        val answerText = view.findViewById<TextView>(R.id.answerTitleTextView)
//        if (answerTextView.visibility == View.GONE) {
//            // Expand animation
//            answerTextView.text = answer
//            answerTextView.visibility = View.VISIBLE
//
//
//        } else {
//            // Collapse animation
//            answerTextView.visibility = View.GONE
//            answerText.visibility = View.GONE
//            view.visibility = View.GONE
//
//        }
    }
}
