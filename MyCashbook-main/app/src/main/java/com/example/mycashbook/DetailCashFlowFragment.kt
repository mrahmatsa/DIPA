package com.example.mycashbook

import android.graphics.Movie
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycashbook.database.DatabaseHandler
import com.example.mycashbook.databinding.FragmentDetailCashFlowBinding
import java.util.*


class DetailCashFlowFragment : Fragment() {
    private var _binding: FragmentDetailCashFlowBinding? = null
    private lateinit var dbHandler: DatabaseHandler
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentDetailCashFlowBinding.inflate(inflater, container, false)
        setupRvDetail()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kembaliDetailButton.setOnClickListener {
            findNavController().navigate(R.id.action_detailCashFlowFragment_to_SecondFragment)
        }
    }

    private fun setupRvDetail() {
        val recyclerView: RecyclerView = binding.recyclerView
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        dbHandler = DatabaseHandler(context)
        var cashFlowList = dbHandler.getAllRecord()
        val adapter = CashflowAdapter(cashFlowList)
        recyclerView.adapter = adapter
    }
}