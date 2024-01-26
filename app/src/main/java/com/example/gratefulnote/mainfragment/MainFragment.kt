package com.example.gratefulnote.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gratefulnote.R
import com.example.gratefulnote.common.presentation.ConfirmDialog
import com.example.gratefulnote.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var adapter : MainRecyclerViewAdapter

    private lateinit var confirmDeleteDialog : ConfirmDialog
    private val filterDialog = FilterDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = MainViewModel.getInstance(
            requireActivity().application,
            this
        )

        confirmDeleteDialog = ConfirmDialog.getInstance(
            message = getString(R.string.confirm_delete_message),
            requestKey = getString(R.string.confirm_delete_request_key),
            valueKey = getString(R.string.confirm_delete_value_key),
            requireContext()
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_main , container , false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // RecyclerView Adapter
        adapter = MainRecyclerViewAdapter(PositiveAdapterClickListener(
            delete = {itemId ->
                viewModel.setDeletedItemId(itemId)
                if (!confirmDeleteDialog.isAdded)
                    confirmDeleteDialog.show(childFragmentManager , "TAG")
            },
            edit = {currentPositiveEmotion ->
                viewModel.clickEdit = true
                val action = MainFragmentDirections
                    .actionMainFragmentToEditPositiveEmotion(currentPositiveEmotion)
                findNavController().navigate(action)
            },
            favoriteUpdate = {
                viewModel.normalUpdate(it)
            }
        ))
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        setNavigateToAddGratitudeFragment()
        onChangeRecyclerViewData()
        onChangeFilterData()
        setConfirmDeleteDialogResultListener()

        return binding.root
    }

    private fun setConfirmDeleteDialogResultListener(){
        childFragmentManager.setFragmentResultListener(
            getString(R.string.confirm_delete_request_key),
            this
        ){ _ , bundle ->
            if (bundle.getBoolean(getString(R.string.confirm_delete_value_key)))
                viewModel.delete()
        }
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
        viewModel.recyclerViewData.observe(viewLifecycleOwner){ positiveEmotionList ->
            adapter.submitList(positiveEmotionList) {
                if (!viewModel.clickEdit) {
                    binding.recyclerView.scrollToPosition(0)
                }
                viewModel.clickEdit = false
            }

            binding.emptyTextIndicator.visibility =
                if (positiveEmotionList?.isEmpty() == true)
                    View.VISIBLE
                else
                    View.GONE

            binding.loadingIndicator.visibility =
                if (positiveEmotionList == null)
                    View.VISIBLE
                else
                    View.GONE
        }
    }

    private fun onChangeFilterData(){
        viewModel.filterState.observe(viewLifecycleOwner){
            viewModel.updateRecyclerViewData(it!!)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main_options_menu , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.recycler_view_filter) {
            if (!filterDialog.isAdded) {
                filterDialog.show(childFragmentManager, "TAG")
            }
        }
        else if (item.itemId == R.id.add_new_gratitude)
            viewModel.onClickAddNewGratitude()
        return super.onOptionsItemSelected(item)
    }



}