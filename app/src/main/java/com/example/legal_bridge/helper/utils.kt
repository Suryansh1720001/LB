package com.example.legal_bridge.helper
//import javax.mail.*
//import javax.mail.internet.InternetAddress
//import javax.mail.internet.MimeMessage
//import java.util.Properties

class utils {

//    fun sendEmail(userMessage: String) {
//        val properties = Properties()
//        properties["mail.smtp.host"] = "suryansh7202@gmail.com" // Your SMTP server
//        properties["mail.smtp.port"] = "587" // SMTP port (587 is common for TLS)
//        properties["mail.smtp.auth"] = "true" // Enable authentication
//        properties["mail.smtp.starttls.enable"] = "true" // Enable TLS
//
//        val session = Session.getInstance(properties, object : Authenticator() {
//            override fun getPasswordAuthentication(): PasswordAuthentication {
//                return PasswordAuthentication("your_email@example.com", "your_password")
//                // Replace with your email credentials
//            }
//        })
//
//        val message = MimeMessage(session)
//        message.setFrom(InternetAddress("your_email@example.com"))
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient@example.com"))
//        message.subject = "Help Center Issue"
//        message.setText(userMessage)
//
//        try {
//            Transport.send(message)
//            // Email sent successfully
//        } catch (e: MessagingException) {
//            e.printStackTrace()
//            // Handle exception (e.g., log or display an error message)
//        }
//    }


}