package com.example.mycashbook

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycashbook.database.CashflowModel
import com.example.mycashbook.database.DatabaseHandler
import com.example.mycashbook.databinding.FragmentHomeBinding
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        var dbHanler = DatabaseHandler(context)
        var nominal = dbHanler.getAllRecord()

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            dbHanler.getAllMasukForGraph().toTypedArray()
        )
        val series2: LineGraphSeries<DataPoint> = LineGraphSeries(
            dbHanler.getAllKeluarForGraph().toTypedArray()
        )
        binding.textviewSecond.text = "Rangkuman Bulan Ini"
        binding.textviewSecond2.text = "Pengeluaran : Rp. "+dbHanler.getAllKeluar()
        binding.textviewSecond3.text = "Pemasukan : Rp. "+dbHanler.getAllMasuk()
        binding.idGraphView.title = "Cashflow Graph"
        binding.idGraphView.titleColor = R.color.purple_200
        binding.idGraphView.titleTextSize = 8F
        series.color = Color.GREEN
        series2.color = Color.RED
        binding.idGraphView.addSeries(series)
        binding.idGraphView.addSeries(series2)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tambahPemasukan.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_tambahPemasukanFragment)
        }
        binding.tambahPenegeluaran.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_tambahPengeluaranFragment)
        }
        binding.detailCashFlow.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_detailCashFlowFragment)
        }
        binding.pengaturan.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_pengaturanFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}