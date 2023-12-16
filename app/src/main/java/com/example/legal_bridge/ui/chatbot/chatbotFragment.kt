    package com.example.legal_bridge.ui.chatbot

    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.EditText
    import android.widget.ImageButton
    import android.widget.ImageView
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.legal_bridge.databinding.FragmentChatbotBinding
    import com.google.android.material.snackbar.Snackbar
    import okhttp3.Callback
    import okhttp3.FormBody
    import okhttp3.MediaType.Companion.toMediaType
    import okhttp3.OkHttpClient
    import okhttp3.Request
    import okhttp3.RequestBody
    import okhttp3.Response
    import org.json.JSONException
    import org.json.JSONObject
    import java.io.IOException

    class chatbotFragment : Fragment() {

        private var _binding: FragmentChatbotBinding? = null

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        private val openAIUrl = "https://api.openai.com/v1/engines/davinci/completions"
        private val apiKey = "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"

        private lateinit var recyclerView: RecyclerView
        private lateinit var welcomeImageView: ImageView
        private lateinit var messageEditText: EditText
        private lateinit var sendButton: ImageButton
        private lateinit var messageList: MutableList<Message>
        private lateinit var messageAdapter: MessageAdapter

        //    private val JSON = MediaType.get("application/json; charset=utf-8")
        private val JSON = "application/json; charset=utf-8".toMediaType()
        private val client = OkHttpClient()

        private val handler = Handler(Looper.getMainLooper())


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val notificationsViewModel =
                ViewModelProvider(this).get(chatbotViewModel::class.java)

            _binding = FragmentChatbotBinding.inflate(inflater, container, false)
            val root: View = binding.root

            //        val textView: TextView = binding.textNotifications
            //        notificationsViewModel.text.observe(viewLifecycleOwner) {
            ////            textView.text = it
            //        }


            Handler().postDelayed({
                messageList.add(
                    Message(
                        "Hey! How may I assist you with Legal Service today? ",
                        Message.SENT_BY_BOT
                    )
                )
                // Your code to display the first message of your chatBot here
            }, 500) // 2000 milliseconds = 2 seconds delay


            messageList = mutableListOf()



            recyclerView = binding?.recyclerView!!
            welcomeImageView = binding?.welcomeChatbotAi!!
            messageEditText = binding?.messageEditText!!
            sendButton = binding?.sendBtn!!


            messageEditText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    // Your code to perform when the EditText gains focus
                    welcomeImageView.visibility = View.GONE
                    //                welcomeImageView.alpha = 0.8f
                }
            }
            // setup recycler view
            messageAdapter = MessageAdapter(messageList)
            recyclerView.adapter = messageAdapter
            recyclerView.layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }




            sendButton.setOnClickListener {


                val question = messageEditText.text.toString().trim()
                if(question.isBlank()){
                    Snackbar.make(requireView(),"Type Something...", Snackbar.LENGTH_LONG).show()
                }else {
                    addToChat(question, Message.SENT_BY_ME)
                    messageEditText.text.clear()
                    callAPI(question)
                    welcomeImageView.visibility = View.GONE
                    //            welcomeImageView.alpha = 0.5f
                }
            }



            return root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }



        private fun addToChat(message: String, sentBy: String) {
           activity?.runOnUiThread {
                messageList.add(Message(message, sentBy))
                messageAdapter.notifyDataSetChanged()
                recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
            }
        }


        private fun addResponse(response: String) {
            messageList.removeAt(messageList.size - 1)
            addToChat(response, Message.SENT_BY_BOT)
        }

        private fun callAPI(question: String) {

            // okhttp
            messageList.add(Message("Typing... ", Message.SENT_BY_BOT))

//            val jsonBody = JSONObject().apply {
//
//                put("model", "text-davinci-003")
//
//                put("prompt", question)
//                put("max_tokens", 7)
//                put("temperature", 0.7)
//            }

            val jsonBody = JSONObject().apply {
                put("model", "gpt-3.5-turbo")
                put("prompt", question)
                put("max_tokens", 70)
                put("temperature", 0.7)
            }

            //        val body = RequestBody.create(jsonBody.toString(), JSON)
            val body = RequestBody.create(JSON, jsonBody.toString())

//            val requestBody = FormBody.Builder()
//                .add("prompt", question)
//                .add("max_tokens", "150")
//                .build()

            val request = Request.Builder()
//                .url("https://api.openai.com/v1/completions")
                .url(openAIUrl)
                .addHeader("Authorization", "Bearer $apiKey")
//                .header("Authorization", "Bearer "+APIConstantValue.CHAT_GPT_API)
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    addResponse("Failed to load response due to ${e.message}")
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            Log.d("BODY","${response.body?.string()}")
                            val jsonObject = JSONObject(response.body?.string())
                            val jsonArray = jsonObject.getJSONArray("choices")
                            val result = jsonArray.getJSONObject(0).getString("text")
                            addResponse(result.trim())
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        Log.d("BODY","RESPONSE UNSUCCESS")
                        Log.d("BODY","${response.body?.string()}")
                        addResponse("Failed to load response due to ${response.body?.toString()}")
                    }
                }
            })
        }


        //    override fun onBackPressed() {
        //        Log.d("MyActivity", "Back button pressed")
        //        chatBack()
        //
        //    }

        //    private fun chatBack() {
        //        val chatBack = Dialog(this)
        //        chatBack.setCancelable(false)
        //        val binding = DialogBackHelpBinding.inflate(layoutInflater)
        //        chatBack.setContentView(binding.root)
        //
        //        binding?.btnDeleteNo?.setOnClickListener {
        //            chatBack.dismiss()
        //        }
        //        binding?.btnDeleteYes?.setOnClickListener {
        //            super.onBackPressed()
        //        }
        //
        //
        //
        //        chatBack.show()
        //
        //    }
    }
