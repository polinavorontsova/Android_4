package com.ab.labs.planner.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ab.labs.planner.App
import com.ab.labs.R
import com.ab.labs.planner.core.BaseFragment
import com.ab.labs.planner.core.extension.getViewModelExt
import com.ab.labs.planner.core.extension.navigateExt
import com.ab.labs.databinding.FragmentLoginBinding
import com.ab.labs.planner.ui.activity.MainActivity
import com.ab.labs.planner.ui.activity.USER_ID_PREF

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        loginViewModel = getViewModelExt { LoginViewModel(App.instance.userRepository) }

        initObservers()
        initListeners()

        return binding.root
    }

    private fun initObservers() {
        loginViewModel.loginError.observe(viewLifecycleOwner) {
            if (it) {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = getString(R.string.login_error)
            } else {
                binding.textError.visibility = View.GONE
            }
        }
        loginViewModel.userId.observe(viewLifecycleOwner) {
            it?.let {
                val sharedPreferences = requireActivity().getSharedPreferences(USER_ID_PREF, 0)
                sharedPreferences.edit().putLong(USER_ID_PREF, it).apply()

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun initListeners() {
        binding.btnRegistratin.setOnClickListener {
            navigateExt(LoginFragmentDirections.actionNavLoginToNavRegistration())
        }
        binding.btnLogin.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            when {
                name.isEmpty() -> {
                    binding.editTextName.requestFocus()
                    binding.editTextName.error = getString(R.string.name_field_error)
                }
                password.isEmpty() -> {
                    binding.editTextPassword.requestFocus()
                    binding.editTextPassword.error = getString(R.string.password_field_error)
                }
                else -> {
                    loginViewModel.login(name, password)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}