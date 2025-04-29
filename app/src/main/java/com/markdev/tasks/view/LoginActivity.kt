package com.markdev.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.markdev.tasks.R
import com.markdev.tasks.databinding.ActivityLoginBinding
import com.markdev.tasks.viewmodel.LoginViewModel
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()

        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + binding.main.paddingStart, systemBars.top,
                systemBars.right + binding.main.paddingEnd, systemBars.bottom
            )
            insets
        }

        viewModel.verifyAuthentication()


        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)


        observe()


    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loggedUser.observe(this) {
            if (it) {
                showAuthentication()

            }
        }
    }

    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.login(email, password)
    }

    private fun showAuthentication() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }
            })

        val info: BiometricPrompt.PromptInfo =
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação Biométrica")
                .setSubtitle("")
                .setDescription("Para usar o aplicativo é necessário validar a sua impressão digital")
                .setNegativeButtonText("Cancelar")
                .build()

        biometricPrompt.authenticate(info)
    }
}