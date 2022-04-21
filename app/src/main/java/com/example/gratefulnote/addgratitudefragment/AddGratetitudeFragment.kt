package com.example.gratefulnote.addgratitudefragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gratefulnote.R

class AddGratetitudeFragment : Fragment() {

    companion object {
        fun newInstance() = AddGratetitudeFragment()
    }

    private lateinit var viewModel: AddGratetitudeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_gratitude, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddGratetitudeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}