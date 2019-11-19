package br.com.ia.medstudy.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import br.com.ia.medstudy.R
import br.com.ia.medstudy.domain.model.User
import br.com.ia.medstudy.utils.Preferences
import kotlinx.android.synthetic.main.activity_wizard.*
import org.jetbrains.anko.startActivity

class WizardActivity : AppCompatActivity() {

    private var currentStepWizard: Int = 0
    private val maxStepWizard: Int by lazy { resources.getStringArray(R.array.wizard_introduction_messages).size }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_wizard)
        if (Preferences.getUser(this).level.valueLevel > User.Level.LEVELING.valueLevel) {
            startActivity<HomeActivity>()
            finish()
        } else {
            setupView()
        }
    }

    private fun setupView() {
        setupCurrentStep()
        btnNext.setOnClickListener {

            if(++currentStepWizard < maxStepWizard){
                setupCurrentStep()
            } else {
                startActivity<HomeActivity>()
                finish()
            }

        }
    }

    private fun setupCurrentStep() {
        txtMessage.text = resources.getStringArray(R.array.wizard_introduction_messages)[currentStepWizard]
        btnNext.text = resources.getStringArray(R.array.wizard_button_text)[currentStepWizard]
    }

}
