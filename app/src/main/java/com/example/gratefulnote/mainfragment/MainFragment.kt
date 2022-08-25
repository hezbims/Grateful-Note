package com.example.gratefulnote.mainfragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var adapter : PositiveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = MainViewModel.getInstance(
            requireActivity().application,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_main , container , false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // RecyclerView Adapter
        adapter = PositiveAdapter(PositiveAdapterClickListener(
            {
                viewModel.delete(it)
            },
            {
                val action = MainFragmentDirections.actionMainFragmentToEditPositiveEmotion(it)
                Navigation.findNavController(binding.root).navigate(action)
            }
        ))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        setNavigateToAddGratitudeFragment()
        onChangeRecyclerViewData()
        onChangeFilterData()

        return binding.root
    }

    private fun setNavigateToAddGratitudeFragment(){
        viewModel.eventMoveToAddGratitude.observe(viewLifecycleOwner){
            if (it == true){
                findNavController().navigate(R.id.action_mainFragment_to_addGratetitudeFragment)
                viewModel.doneNavigating()
            }
        }
    }

    private fun onChangeRecyclerViewData(){
        viewModel.recyclerViewData.observe(viewLifecycleOwner){
            adapter.submitList(viewModel.recyclerViewData.value!!){
                binding.recyclerView.scrollToPosition(0)
            }
            binding.emptyDataIndicator.visibility =
                if (viewModel.isDataEmpty)
                    View.VISIBLE
                else
                    View.GONE
        }
    }

    private fun onChangeFilterData(){
        viewModel.filterState.observe(viewLifecycleOwner){
            viewModel.updateRecyclerViewData()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.recycler_view_filter) {
            if (viewModel.canShowFilterDialog) {
                viewModel.cannotShowFilterDialog()
                FilterDialogFragment().show(childFragmentManager, "TAG")
            }
        }
        else if (item.itemId == R.id.add_new_gratitude)
            viewModel.onClickAddNewGratitude()
        return super.onOptionsItemSelected(item)
    }

}