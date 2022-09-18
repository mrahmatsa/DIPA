package com.example.mycashbook

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mycashbook.database.DatabaseHandler
import com.example.mycashbook.database.UserModel
import com.example.mycashbook.databinding.FragmentPengaturanBinding
import com.example.mycashbook.databinding.FragmentTambahPemasukanBinding
import java.text.SimpleDateFormat
import java.util.*

class PengaturanFragment : Fragment() {
    private var _binding: FragmentPengaturanBinding? = null
    private lateinit var dbHandler: DatabaseHandler
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        dbHandler = DatabaseHandler(context)
        // Inflate the layout for this fragment
        _binding = FragmentPengaturanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = sharedPreferences.getInt("id", 0)
        var oldUserData = dbHandler.getUser(id)
        binding.SimpanButton.setOnClickListener {
            var curPassword = binding.oldPassword.text.toString()
            if (curPassword == oldUserData.password){
                dbHandler.updatePassword(id, binding.newPassword.text.toString())
                var editor : SharedPreferences.Editor  = sharedPreferences.edit()
                editor.clear()
                editor.commit()
                findNavController().navigate(R.id.action_pengaturanFragment_to_FirstFragment)
            }
        }
        binding.KembaliButton.setOnClickListener {
            findNavController().navigate(R.id.action_pengaturanFragment_to_SecondFragment)
        }
    }
}