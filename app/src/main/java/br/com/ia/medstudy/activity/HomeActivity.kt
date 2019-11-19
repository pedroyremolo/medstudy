package br.com.ia.medstudy.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import br.com.ia.medstudy.R
import br.com.ia.medstudy.domain.model.User
import br.com.ia.medstudy.utils.DialogUtils
import br.com.ia.medstudy.utils.Preferences
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_home)
        setupView()
    }

    private fun setupView() {
        movOptions.setOnClickOptionListener {
            startActivity<QuizActivity>(QuizActivity.ARG_QUESTION_OPTION_SELECTED to it.name)
        }
        btnShowEquipments.setOnClickListener {
            DialogUtils.createEquipmentDialogs(this, "Equipamentos ganhos!",
                    getEquipmentList(),
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    }).show()
        }
        btnNewGame.setOnClickListener {
            Preferences.setUser(this, User())
            startActivity<WizardActivity>()
            finish()
        }
    }

    private fun updateOptions() {
        val user = Preferences.getUser(baseContext)
        if (user.level == User.Level.FINISH) {
            btnNewGame.visibility = View.VISIBLE
        } else {
            btnNewGame.visibility = View.GONE
        }
        /*if (user.level.valueLevel > User.Level.ONE.valueLevel) {
            btnShowEquipments.visibility = View.VISIBLE
        } else {
            btnShowEquipments.visibility = View.GONE
        }*/
        movOptions.setupLevel(user.level, user.isReinforcement)
    }

    private fun getEquipmentList(): MutableList<String> {
        val user = Preferences.getUser(baseContext)
        var currentLevel = user.level.valueLevel
        val equipments = mutableListOf<String>()
        for (index in 1..currentLevel - 1) {
            equipments.add("equipment_$index")
        }
        return equipments
    }

    override fun onResume() {
        super.onResume()
        updateOptions()
    }

}
