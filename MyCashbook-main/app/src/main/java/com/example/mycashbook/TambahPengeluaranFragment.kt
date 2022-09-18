package com.example.mycashbook

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycashbook.database.CashflowModel
import com.example.mycashbook.database.DatabaseHandler
import com.example.mycashbook.databinding.FragmentTambahPengeluaranBinding
import java.text.SimpleDateFormat
import java.util.*


class TambahPengeluaranFragment : Fragment() {
    private var _binding: FragmentTambahPengeluaranBinding? = null
    private lateinit var dbHandler: DatabaseHandler
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var datePickerDialog: DatePickerDialog? = null
    private var dateFormatter: SimpleDateFormat? = null
    private val tvDateResult: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbHandler = DatabaseHandler(context)
        // Inflate the layout for this fragment
        _binding = FragmentTambahPengeluaranBinding.inflate(inflater, container, false)
        dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var cashFlow = CashflowModel()
        binding.tanggalButton.setOnClickListener(View.OnClickListener { showDateDialog() })
        binding.simpanButton.setOnClickListener {
            cashFlow.masukKeluar = "keluar"
            cashFlow.tanggal = binding.tanggalEdtText.text.toString()
            cashFlow.nominal = binding.nominal.text.toString().toInt()
            cashFlow.keterangan = binding.keterangan.text.toString()
            dbHandler.addRecord(cashFlow)
            findNavController().navigate(R.id.action_tambahPengeluaranFragment_to_SecondFragment)
        }
        binding.kembaliButton.setOnClickListener {
            findNavController().navigate(R.id.action_tambahPengeluaranFragment_to_SecondFragment)
        }
    }

    private fun showDateDialog() {
        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        val newCalendar: Calendar = Calendar.getInstance()
        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = DatePickerDialog(
            requireContext(),
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val newDate: Calendar = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)

                binding.tanggalEdtText.setText(dateFormatter!!.format(newDate.getTime()))
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )
        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog!!.show()
    }
}