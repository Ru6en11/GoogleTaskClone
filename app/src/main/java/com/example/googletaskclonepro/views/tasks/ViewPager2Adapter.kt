package com.example.googletaskclonepro.views.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.googletaskclonepro.databinding.FragmentCategoryBinding
import kotlin.properties.Delegates

const val ARG_LISTENER = "com.example.googletaskclonepro.views.tasks.arg_listener"
const val ARG_POSITION = "com.example.googletaskclonepro.views.tasks.arg_position"
private const val TAB_COUNT = 3
val adapters = mutableListOf<TasksAdapter>()

class ViewPager2Adapter(private val f: TasksFragment) : FragmentStateAdapter(f), java.io.Serializable {

    override fun getItemCount(): Int = TAB_COUNT

    override fun createFragment(position: Int): Fragment {

        return CategoryFragment.newInstance(this.f as TasksListener, position)
    }

}

class CategoryFragment : Fragment() {

    lateinit var adapter: TasksAdapter
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var listener: TasksListener
    private var position by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = requireArguments().getSerializable(ARG_LISTENER) as TasksListener
        position = requireArguments().getInt(ARG_POSITION)
        adapter = TasksAdapter(listener, position)
        adapters.add(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        val touchHelper = TasksItemTouchHelper()
        touchHelper.itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        listener.observeData(viewLifecycleOwner, adapter, position)

        return binding.root
    }

    companion object {

        fun newInstance(listener: TasksListener, position: Int): CategoryFragment =
            CategoryFragment().apply {
                arguments = bundleOf(
                    ARG_LISTENER to listener,
                    ARG_POSITION to position
                )
            }
    }

}