package com.example.gratefulnote.topehelpfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentTopeHelpBinding

class TopeHelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentTopeHelpBinding
                = DataBindingUtil.inflate(layoutInflater , R.layout.fragment_tope_help , container , false)

        binding.companionObject = Companion

        return binding.root
    }

    companion object{
        val positiveEmotionDescription = """
            1.) Joy :
                Perasaan menyenangkan dan semangat dari hal yang telah dilakukan
                
                
            2.) Gratitude :
                Bersyukur terhadap apa yang telah terjadi. Bisa juga dengan mencari sisi baik dari hal yang sebelumnya kita anggap buruk
                
                
            3.) Serenity :
                Perasaan tenang dan damai (Contoh : perasaan ketika berada di alam yang indah dan sunyi)
                
                
            4.) Interest :
                Ketertarikan terhadap suatu hal
                
                
            5.) Hope :
                Harapan untuk sesuatu yang lebih baik
                
                
            6.) Pride :
                Kebanggaan terhadap apa yang dicapai
                
                
            7.) Amusement :
                Kondisi ketika tubuh menerima peristiwa yang humoris atau 'entertaining'. Biasanya ditandai dengan tertawa atau senyum
                
                
            8.) Inspiration :
                Terinspirasi dengan suatu hal.
                
                
            9.) Awe :
                Takjub dengan suatu keagungan. (Contoh : Melihat pegunungan atau gedung-gedung yang menjulang tinggi, melihat padang rumput yang luas, dsb)
                
                
            10.) Love :
                Berbagi positive emotion dengan orang lain.
        """.trimIndent()
    }
}