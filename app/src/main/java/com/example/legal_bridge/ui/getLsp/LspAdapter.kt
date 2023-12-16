package com.example.legal_bridge.ui.getLsp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.legal_bridge.R
import com.example.legal_bridge.model.lsp.lspDataListResponse
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class lspAdapter(private val lspDataList: List<lspDataListResponse>, private val typeOfLsp : String) :
    RecyclerView.Adapter<lspAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lsp_cv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lspData = lspDataList[position]
        holder.bind(lspData,typeOfLsp)
    }

    override fun getItemCount(): Int {
        return lspDataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_lspName)
        private val lspImage: CircleImageView = itemView.findViewById(R.id.iv_lspimage)
        private val bio: TextView = itemView.findViewById(R.id.tv_bio)
        private val expertiseField: TextView = itemView.findViewById(R.id.tv_expertiseField)
        private val langauge: TextView = itemView.findViewById(R.id.tv_Language)
        private val experienceYear: TextView = itemView.findViewById(R.id.tv_expertiseYear)
        private val isAvailable: TextView = itemView.findViewById(R.id.tv_lspAvailability)
        private val rating: TextView = itemView.findViewById(R.id.tv_rating)
        private val btn_phone: FrameLayout = itemView.findViewById(R.id.fl_lsp_phone)


        fun bind(lspData: lspDataListResponse, typeOfLsp: String) {
            if (typeOfLsp== "advocate") {

                nameTextView.text = lspData.name
                bio.text = lspData.bio
                expertiseField.text =
                    "( " + lspData.expertiseField[0] + ", " + lspData.expertiseField[1] + ", " + lspData.expertiseField[2] + " +" + (lspData.expertiseField.size - 3).toString() + " more)"
                experienceYear.text = lspData.experience.toString() + " years of Experience"
                langauge.text = lspData.languages.toString()
                rating.text = lspData.rating.toString() + " ★"

                if (lspData.isActive) {
                    isAvailable.text = "• Available"
                } else {
                    isAvailable.text = "• Not Available"
                }


                btn_phone.setOnClickListener {
                    val phoneNumber =
                        lspData.phone // Replace this with the phone number you want to call
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                    itemView.context.startActivity(intent)
                }


                // Load image using Picasso or Glide (Assuming you have one of them added in your dependencies)
                Picasso.get().load(lspData.pic).into(lspImage)
            }
        }
    }
}
