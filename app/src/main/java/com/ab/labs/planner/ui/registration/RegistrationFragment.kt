package com.ab.labs.planner.ui.registration

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ab.labs.planner.App
import com.ab.labs.R
import com.ab.labs.planner.core.BaseFragment
import com.ab.labs.planner.core.bitmapToString
import com.ab.labs.planner.core.decodeUri
import com.ab.labs.planner.core.extension.getViewModelExt
import com.ab.labs.planner.data.entity.User
import com.ab.labs.databinding.FragmentRegistrationBinding
import com.ab.labs.planner.ui.activity.ImageChooserInterface
import com.ab.labs.planner.ui.activity.MainActivity
import com.ab.labs.planner.ui.activity.USER_ID_PREF

class RegistrationFragment : BaseFragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var registrationViewModel: RegistrationViewModel
    private var icon = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        registrationViewModel =
            getViewModelExt { RegistrationViewModel(App.instance.userRepository) }
        initObservers()
        initListeners()
        return binding.root
    }

    private fun initObservers() {
        registrationViewModel.existUser.observe(viewLifecycleOwner) {
            if (it) {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = getString(R.string.registr_error_exists)
            } else {
                binding.textError.visibility = View.GONE
            }
        }
        registrationViewModel.userId.observe(viewLifecycleOwner) {
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
        binding.btnRegistr.setOnClickListener { save() }

        binding.profileImage.setOnClickListener {
            val imageChooser = activity as ImageChooserInterface
            imageChooser.showImageChooser {
                it?.let {
                    binding.profileImage.setImageURI(it)
                    val btm = decodeUri(requireContext(), it, 500)
                    icon = bitmapToString(btm!!)
                    binding.deleteImage.isVisible = true
                }
            }
        }
        binding.deleteImage.setOnClickListener {
            icon = ""
            binding.profileImage.setImageResource(R.drawable.ic_account_outline_24)
            binding.deleteImage.isVisible = false
        }
    }

    private fun save() {
        val name = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        when {
            name.isEmpty() -> {
                binding.editTextName.requestFocus()
                binding.editTextName.error = getString(R.string.name_field_error)
            }
            email.isEmpty() -> {
                binding.editTextEmail.requestFocus()
                binding.editTextEmail.error = getString(R.string.email_field_error)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.editTextEmail.requestFocus()
                binding.editTextEmail.error = getString(R.string.email_incorrect_error)
            }
            password.isEmpty() -> {
                binding.editTextPassword.requestFocus()
                binding.editTextPassword.error = getString(R.string.password_field_error)
            }
            else -> {
                val user = User(name = name, password = password, email = email)
                user.icon = icon
                registrationViewModel.save(user)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}