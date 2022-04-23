package com.example.gratefulnote.addgratitudefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gratefulnote.R

class AddGratetitudeFragment : Fragment() {

    private lateinit var viewModel: AddGratetitudeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_gratitude, container, false)
    }


}