package com.example.mycashbook

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycashbook.database.DatabaseHandler
import com.example.mycashbook.database.UserModel
import com.example.mycashbook.databinding.FragmentLoginBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var dbHandler: DatabaseHandler
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        dbHandler = DatabaseHandler(context)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userModel : UserModel = UserModel()
        binding.buttonLogin.setOnClickListener {
            userModel.username = binding.username.text.toString()
            userModel.password = binding.password.text.toString()
            if (dbHandler.getUser(userModel.username, userModel.password) == null){
                binding.warning.text = "Username atau password salah"
            }
            else{
                var editor : SharedPreferences.Editor  = sharedPreferences.edit()
                editor.putInt("id", dbHandler.getUser(userModel.username, userModel.password)!!.getIdUser())
                editor.commit()
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

