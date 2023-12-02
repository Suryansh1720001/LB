package com.example.legal_bridge.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.legal_bridge.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var testimoninalImageList = mutableListOf<Int>()
    private var testimonialNameList = mutableListOf<String>()
    private var testimonialsList = mutableListOf<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(AccountDetailsViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root




//        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }


//        positionListTestimonial()
//        var viewPager = binding.viewPager
//        viewPager.adapter = viewPageAdapter(testimoninalImageList,testimonialNameList, testimonialsList)
//        viewPager.orientation  = ViewPager2.ORIENTATION_HORIZONTAL
//
//        val indicator = binding.circularIndicator
//        indicator.setViewPager(viewPager)
//
//        val before_button = binding.btnBefore
//        before_button.setOnClickListener {
//
//            viewPager.apply {
//                beginFakeDrag()
//                fakeDragBy(-10f)
//                endFakeDrag()
//
//            }
//        }
//
//        val next_button = binding.btnNext
//        next_button.setOnClickListener {
//
//            viewPager.apply {
//                beginFakeDrag()
//                fakeDragBy(+10f)
//                endFakeDrag()
//
//            }
//        }





        return root
    }
//
//    private fun addToListTestimonial(testimonialImage : Int, testimonialName : String, testimonial : String){
//        testimoninalImageList.add(testimonialImage)
//        testimonialNameList.add(testimonialName)
//        testimonialsList.add(testimonial)
//
//    }

//
//    private fun positionListTestimonial(){
//       addToListTestimonial(R.drawable.men_profile, "Vikas Sharma", "“ This Application helps\u2028 me a lot. “" )
//       addToListTestimonial(R.drawable.woment_profile, "Shruti Singh", "“ This helps me in a very Critical Situation....Thanks. “" )
//       addToListTestimonial(R.drawable.men_profile, "Vishu Kumar", "“ Chat-bot is very good. “" )
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}