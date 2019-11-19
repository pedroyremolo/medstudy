package br.com.ia.medstudy.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import br.com.ia.medstudy.R
import br.com.ia.medstudy.domain.model.*
import br.com.ia.medstudy.domain.repository.AnswerRepository
import br.com.ia.medstudy.domain.repository.QuizRepository
import br.com.ia.medstudy.utils.DialogUtils
import br.com.ia.medstudy.utils.Preferences
import br.com.ia.medstudy.widget.ScoreView
import kotlinx.android.synthetic.main.activity_quiz.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class QuizActivity : AppCompatActivity() {

    private val option: QuestionOptions by lazy { QuestionOptions.valueOf(intent.getStringExtra(ARG_QUESTION_OPTION_SELECTED)) }
    private val user: User by lazy { Preferences.getUser(this) }
    private lateinit var quiz: Quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_quiz)
        setupView()
    }

    private fun setupView() {
        if (option != QuestionOptions.LEVELING &&
                option != QuestionOptions.REINFORCEMENT &&
                option.valueOption < user.level.valueLevel) {
            showAnswers()
        } else {
            showQuestions()
        }
    }

    private fun showAnswers() {
        AnswerRepository().retrieve(option.valueOption) {
            setupAnswers(it)
        }
    }

    private fun showQuestions() {
        when (option.name) {
            QuestionOptions.LEVELING.name -> QuizRepository().retrieveLevelingQuestions(resources) {
                setupQuestions(it)
            }
            QuestionOptions.REINFORCEMENT.name -> QuizRepository().retrieveReinforcement(resources, user.level.valueLevel) {
                setupQuestions(it)
            }
            QuestionOptions.ONE.name -> QuizRepository().retrieveLevelOneQuestions(resources) {
                setupQuestions(it)
            }
            QuestionOptions.TWO.name -> QuizRepository().retrieveLevelTwoQuestions(resources) {
                setupQuestions(it)
            }
            QuestionOptions.THREE.name -> QuizRepository().retrieveLevelThreeQuestions(resources) {
                setupQuestions(it)
            }
            QuestionOptions.FOUR.name -> QuizRepository().retrieveLevelFourQuestions(resources) {
                setupQuestions(it)
            }
            QuestionOptions.FIVE.name -> QuizRepository().retrieveLevelFiveQuestions(resources) {
                setupQuestions(it)
            }
        }
    }

    private fun setupAnswers(answers: List<Answer>) {
        Log.d(TAG, "(setupAnswers) Iniciando o modo leitura para a opcao ${option.name}")
        if (answers.isEmpty()) {
            Toast.makeText(this, "Você não passou por esse nível!", Toast.LENGTH_LONG).show()
            finish()
        } else {
            qtvQuestions.showAnswers(answers)
            answers.forEach { answer ->
                scvScore.changeNextStatusScore(if (answer.isRight()) ScoreView.Status.OK else ScoreView.Status.NOK)
            }
        }
    }

    private fun setupQuestions(quiz: Quiz) {
        Log.d(TAG, "(setupQuestions) Iniciando o quiz para a opcao ${option.name}")
        this.quiz = quiz
        qtvQuestions.startQuestions(quiz.totalNumberQuestionsToShow, quiz.levelInfo, quiz.questions)
        qtvQuestions.setOnQuestionAnswered { isRight ->
            scvScore.changeNextStatusScore(if (isRight) ScoreView.Status.OK else ScoreView.Status.NOK)
        }
        qtvQuestions.setOnQuestionsFinish { totalRightAnswerByLevel ->
            val lastLevelRule = quiz.levelInfo.size - 1
            for (index in 0..lastLevelRule) {
                val levelRule = quiz.levelInfo[index]
                if (totalRightAnswerByLevel[levelRule.level]!! < levelRule.minimumCorrectAnswers) {
                    if (option == QuestionOptions.LEVELING) {
                        user.level = User.Level.values()[levelRule.level]
                        showMessageLeveling(quiz.levelInfo.toList().subList(0, index), levelRule.level)
                    } else {
                        user.putInReinforcement()
                        showMessageReinforcement(levelRule.level);
                        AnswerRepository().save(user.level.valueLevel, qtvQuestions.getAnswers())
                    }
                    Preferences.setUser(this, user)
                    break
                } else if (totalRightAnswerByLevel[levelRule.level]!! >= levelRule.minimumCorrectAnswers && index < lastLevelRule) {
                    continue
                } else if (totalRightAnswerByLevel[levelRule.level]!! >= levelRule.minimumCorrectAnswers && index == lastLevelRule) {
                    if (option == QuestionOptions.LEVELING) {
                        user.level = User.Level.values()[levelRule.level]
                        showMessageLeveling(quiz.levelInfo.toList().subList(0, index), levelRule.level)
                    } else if (user.isReinforcement) {
                        user.isReinforcement = false
                        showMessageLeavingReinforcement(levelRule.level);
                    } else {
                        AnswerRepository().save(user.level.valueLevel, qtvQuestions.getAnswers())
                        user.goToNextLevel()
                        showMessageNextLevel(levelRule, user.level.valueLevel);
                    }
                    Preferences.setUser(this, user)
                }
            }
        }
    }

    private fun showMessageLeavingReinforcement(level: Int) {
        alert("Parabéns! Você saiu do reforço do nível $level :)", "Reforço") {
            yesButton { dialog ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }

    private fun showMessageNextLevel(levelInfo: LevelInfo, level: Int) {

        var title: String
        if (User.Level.FINISH.valueLevel == level) {
            title = "Parabéns! Você finalizou o jogo e ganhou:"
        } else {
            title = "Parabéns! Você foi para o nível $level e ganhou:"
        }

        val equipments = ArrayList<String>()
        equipments.add(levelInfo.equipment)
        DialogUtils.createEquipmentDialogs(this, title,
                equipments,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    finish()
                }).show()

    }

    private fun showMessageReinforcement(level: Int) {
        alert("Você precisará de reforço para o nível $level :(", "Reforço") {
            yesButton { dialog ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }

    private fun showMessageLeveling(levelInfoList: List<LevelInfo>, level: Int) {

        val equipments = ArrayList<String>(levelInfoList.size)
        levelInfoList.forEach { levelInfo ->
            equipments.add(levelInfo.equipment)
        }

        if (equipments.size == 0) {
            alert("Você vai para o nível $level", "Nivelamento") {
                yesButton { dialog ->
                    dialog.dismiss()
                    finish()
                }
            }.show()
        } else {
            val title = "Parabéns! Você vai para o nível $level e ganhou:"
            DialogUtils.createEquipmentDialogs(this, title,
                    equipments,
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        finish()
                    }).show()
        }


    }

    companion object {

        val TAG = "QuizActivity"
        val ARG_QUESTION_OPTION_SELECTED = "ARG_QUESTION_OPTION_SELECTED"

    }

}
