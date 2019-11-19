package br.com.ia.medstudy.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import br.com.ia.medstudy.R
import br.com.ia.medstudy.domain.model.QuestionOptions
import br.com.ia.medstudy.domain.model.User
import kotlinx.android.synthetic.main.view_main_options.view.*

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

class MainOptionsView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        FrameLayout(context, attrs, defStyle) {

    private lateinit var onClick: (QuestionOptions) -> Unit

    init {
        inflate(context, R.layout.view_main_options, this)
        initOnClick()
    }

    private fun initOnClick() {
        btnLeveling.setOnClickListener { onClick(QuestionOptions.LEVELING) }
        btnReinforcement.setOnClickListener { onClick(QuestionOptions.REINFORCEMENT) }
        btnLevelOne.setOnClickListener { onClick(QuestionOptions.ONE) }
        btnLevelTwo.setOnClickListener { onClick(QuestionOptions.TWO) }
        btnLevelThree.setOnClickListener { onClick(QuestionOptions.THREE) }
        btnLevelFour.setOnClickListener { onClick(QuestionOptions.FOUR) }
        btnLevelFive.setOnClickListener { onClick(QuestionOptions.FIVE) }
    }

    fun setupLevel(currentLevel: User.Level, isReinforcement: Boolean) {
        when (currentLevel) {
            User.Level.LEVELING -> {
                enableLeveling()
            }
            User.Level.ONE -> {
                enableLevelOne(isReinforcement)
            }
            User.Level.TWO -> {
                enableLevelTwo(isReinforcement)
            }
            User.Level.THREE -> {
                enableLevelThree(isReinforcement)
            }
            User.Level.FOUR -> {
                enableLevelFour(isReinforcement)
            }
            User.Level.FIVE -> {
                enableLevelFive(isReinforcement)
            }
            User.Level.FINISH -> {
                enableFinishState()
            }
        }
    }

    private fun enableLeveling() {
        disableAllOptions()
        btnLeveling.isEnabled = true
    }

    private fun enableLevelOne(isReinforcement: Boolean) {
        disableAllOptions()
        if (isReinforcement) {
            btnLevelOne.isEnabled = false
            btnReinforcement.isEnabled = true
            btnLevelOne.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_reinforcement))
        } else {
            btnLevelOne.isEnabled = false
            btnReinforcement.isEnabled = false
            btnLevelOne.setBackgroundResource(R.drawable.bg_btn_default)
        }
    }

    private fun enableLevelTwo(isReinforcement: Boolean) {
        disableAllOptions()
        if (isReinforcement) {
            btnLevelTwo.isEnabled = false
            btnReinforcement.isEnabled = true
            btnLevelTwo.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_reinforcement))
        } else {
            btnLevelTwo.isEnabled = false
            btnReinforcement.isEnabled = false
            btnLevelTwo.setBackgroundResource(R.drawable.bg_btn_default)
        }
        btnLevelOne.isEnabled = false
        btnLevelOne.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
    }

    private fun enableLevelThree(isReinforcement: Boolean) {
        disableAllOptions()
        if (isReinforcement) {
            btnLevelThree.isEnabled = false
            btnReinforcement.isEnabled = true
            btnLevelThree.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_reinforcement))
        } else {
            btnLevelThree.isEnabled = false
            btnReinforcement.isEnabled = false
            btnLevelThree.setBackgroundResource(R.drawable.bg_btn_default)
        }
        btnLevelOne.isEnabled = false
        btnLevelOne.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelTwo.isEnabled = false
        btnLevelTwo.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
    }

    private fun enableLevelFour(isReinforcement: Boolean) {
        disableAllOptions()
        if (isReinforcement) {
            btnLevelFour.isEnabled = false
            btnReinforcement.isEnabled = true
            btnLevelFour.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_reinforcement))
        } else {
            btnLevelFour.isEnabled = false
            btnReinforcement.isEnabled = false
            btnLevelFour.setBackgroundResource(R.drawable.bg_btn_default)
        }
        btnLevelOne.isEnabled = false
        btnLevelOne.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelTwo.isEnabled = false
        btnLevelTwo.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelThree.isEnabled = false
        btnLevelThree.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
    }

    private fun enableLevelFive(isReinforcement: Boolean) {
        disableAllOptions()
        if (isReinforcement) {
            btnLevelFive.isEnabled = false
            btnReinforcement.isEnabled = true
            btnLevelFive.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_reinforcement))
        } else {
            btnLevelFive.isEnabled = false
            btnReinforcement.isEnabled = false
            btnLevelFive.setBackgroundResource(R.drawable.bg_btn_default)
        }
        btnLevelOne.isEnabled = false
        btnLevelOne.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelTwo.isEnabled = false
        btnLevelTwo.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelThree.isEnabled = false
        btnLevelThree.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelFour.isEnabled = false
        btnLevelFour.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
    }

    private fun enableFinishState() {
        disableAllOptions()
        btnLevelOne.isEnabled = true
        btnLevelOne.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelTwo.isEnabled = true
        btnLevelTwo.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelThree.isEnabled = true
        btnLevelThree.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelFour.isEnabled = true
        btnLevelFour.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
        btnLevelFive.isEnabled = true
        btnLevelFive.setBackgroundColor(ContextCompat.getColor(context, R.color.main_options_btn_background_ok))
    }

    private fun disableAllOptions() {
        btnLeveling.isEnabled = false
        btnLevelTwo.isEnabled = false
        btnLevelThree.isEnabled = false
        btnLevelFour.isEnabled = false
        btnLevelFive.isEnabled = false
    }

    fun setOnClickOptionListener(onClick: (QuestionOptions) -> Unit) {
        this.onClick = onClick
    }

}