package com.example.gratefulnote.mainMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gratefulnote.R
import com.example.gratefulnote.common.presentation.ConfirmDialog
import com.example.gratefulnote.databinding.FragmentMainBinding
import com.example.gratefulnote.mainMenu.presentation.logic.DiaryPreviewPagingAdapter
import com.example.gratefulnote.mainMenu.presentation.component.DiaryPreviewViewHolder
import com.example.gratefulnote.mainMenu.presentation.logic.MainFragmentNavEvent
import com.example.gratefulnote.mainMenu.presentation.logic.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel : MainViewModel by hiltNavGraphViewModels(R.id.main_crud_graph)
    private lateinit var binding : FragmentMainBinding
    private lateinit var recyclerViewAdapter : MainRecyclerViewAdapter
    private lateinit var confirmDeleteDialog : ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        confirmDeleteDialog = ConfirmDialog.getInstance(
            message = getString(R.string.confirm_delete_message),
            requestKey = MainFragmentKey.confirmDeleteRequest,
            valueKey = MainFragmentKey.confirmDeleteValue,
            requireContext()
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_main , container , false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        setupRecyclerViewAdapter()
        observeNavigationState()
        setConfirmDeleteDialogResultListener()

        return binding.root
    }

    private fun setConfirmDeleteDialogResultListener(){
        childFragmentManager.setFragmentResultListener(
            MainFragmentKey.confirmDeleteRequest,
            this
        ){ _ , bundle ->
            if (bundle.getBoolean(
                MainFragmentKey.confirmDeleteValue
            ))
                viewModel.onDelete()
        }
    }

    private fun setupRecyclerViewAdapter(){
        val pagingAdapter = DiaryPreviewPagingAdapter(
            clickListener = DiaryPreviewViewHolder.ClickListener(
                onClickDelete = {
                    viewModel.setDeletedItemId(it.id)
                    if (!confirmDeleteDialog.isAdded)
                        confirmDeleteDialog.show(childFragmentManager , "TAG")
                },
                onClickEdit = {

                },
                onClickFavorite = {
                    viewModel.onToogleIsFavorite(it.id)
                }
            )
        ).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.recyclerView.adapter = pagingAdapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recycleViewPager.collectLatest {
                    if (pagingAdapter.itemCount.equals(0))
                        binding.emptyTextIndicator.visibility = View.GONE
                    else
                        binding.emptyTextIndicator.visibility = View.VISIBLE

                    pagingAdapter.submitData(it)
                }
            }
        }
    }

    private fun observeNavigationState(){
        val navController = findNavController()
        viewModel.navEvent.observe(viewLifecycleOwner){ navEvent ->
            if (navEvent == null)
                return@observe
            viewModel.doneNavigating()

            val nextDestination = when (navEvent) {
                MainFragmentNavEvent.MoveToAddGratitude ->
                    MainFragmentDirections.actionMainFragmentToAddGratetitudeFragment()
                MainFragmentNavEvent.OpenFilterDialog ->
                    MainFragmentDirections.actionMainFragmentToFilterDialogFragment()
            }
            if (navController.currentDestination?.id == R.id.mainFragment) {
                navController.navigate(nextDestination)
            }
        }
    }


//        viewModel.recyclerViewData.observe(viewLifecycleOwner){ recyclerViewState ->
//            val recyclerViewDataResponse = recyclerViewState.listDataResponse
//            when (recyclerViewDataResponse) {
//                is ResponseWrapper.Succeed -> {
//                    val listData = recyclerViewDataResponse.data!!
//
//                    binding.emptyTextIndicator.visibility =
//                        if (listData.isEmpty())
//                            View.VISIBLE
//                        else
//                            View.GONE
//                    binding.loadingIndicator.visibility = View.GONE
//
//                    recyclerViewAdapter.submitList(listData){
//                        // callback setelah submit berhasil
//                        if (recyclerViewState.scrollToPositionZero){
//                            binding.recyclerView.scrollToPosition(0)
//                            viewModel.doneScrollToPositionZero()
//                        }
//                    }
//                }
//                is ResponseWrapper.Loading -> {
//                    binding.loadingIndicator.visibility = View.VISIBLE
//                }
//                else -> throw Exception("Ada kesalahan dalam program")
//            }
//        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main_options_menu , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.filter_list_positive_emotion_action_icon -> {
                viewModel.onNavEvent(MainFragmentNavEvent.OpenFilterDialog)
                true
            }
            R.id.add_new_diary_action_icon -> {
                viewModel.onNavEvent(MainFragmentNavEvent.MoveToAddGratitude)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}