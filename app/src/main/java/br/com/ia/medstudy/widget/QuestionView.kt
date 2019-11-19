package br.com.ia.medstudy.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import br.com.ia.medstudy.R
import br.com.ia.medstudy.domain.model.Answer
import br.com.ia.medstudy.domain.model.LevelInfo
import br.com.ia.medstudy.domain.model.Question
import br.com.ia.medstudy.utils.ResourceUtils
import kotlinx.android.synthetic.main.view_question.view.*
import java.util.*

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

//Agent class to get and display answers
class QuestionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        LinearLayout(context, attrs, defStyle) {

    private lateinit var questions: Array<Question>
    private lateinit var levelInfos: Array<LevelInfo>
    private var totalNumberQuestionsToShow = 0

    private val totalQuestionsShownByLevel: MutableMap<Int, Int> = mutableMapOf()
    private val totalRightAnswerByLevel: MutableMap<Int, Int> = mutableMapOf()
    private val questionsShown: MutableList<Int> = mutableListOf()
    private val answers: MutableList<Answer> = mutableListOf()

    private var currentPosition = 0

    private lateinit var onQuestionAnswered: (Boolean) -> Unit
    private lateinit var onQuestionsFinish: (MutableMap<Int, Int>) -> Unit

    init {
        FrameLayout.inflate(context, R.layout.view_question, this)
    }

    private fun showNextQuestion() {
        currentPosition = getNextIndex()
        val question = questions[currentPosition]
        imgCheckAnswer.setOnClickListener { checkAnswer() }
        if (Question.Type.TEXT == question.type) {
            showTextQuestion(question)
        } else if (Question.Type.AUDIO == question.type) {
            showAudioQuestion(question)
        }
    }

    private fun showTextQuestion(question: Question) {
        txtQuestion.visibility = View.VISIBLE
        imgAudioQuestion.visibility = View.GONE
        txtQuestion.text = question.text
        setupAnswers(question)
        if (question.image != null && question.image.isNotBlank()) {
            imgQuestionComplement.visibility = View.VISIBLE
            imgQuestionComplement.setImageResource(ResourceUtils.getResourceDrawableIdByName(question.image))
        } else {
            imgQuestionComplement.visibility = View.GONE
        }
    }

    private fun showAudioQuestion(question: Question) {
        imgAudioQuestion.visibility = View.VISIBLE
        txtQuestion.visibility = View.GONE
        setupAnswers(question)
        if (question.image != null && question.image.isNotBlank()) {
            imgQuestionComplement.visibility = View.VISIBLE
            imgQuestionComplement.setImageResource(ResourceUtils.getResourceDrawableIdByName(question.image))
        } else {
            imgQuestionComplement.visibility = View.GONE
        }
    }

    private fun setupAnswers(question: Question) {
        rdgAnswers.clearCheck()
        for (i in 0..2) {
            val radioButton = rdgAnswers.getChildAt(i) as RadioButton
            radioButton.text = question.options[i]
            radioButton.isEnabled = true
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.question_answer_default))
        }
    }

    private fun checkAnswer() {
        val selectedId = rdgAnswers.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(context, "Selecione uma resposta.", Toast.LENGTH_LONG).show()
            return
        }
        disableAnswers()
        imgCheckAnswer.isEnabled = false
        val radioButtonAnswer: RadioButton = rdgAnswers.findViewById(selectedId)
        val selectedPosition = rdgAnswers.indexOfChild(radioButtonAnswer)
        val question = questions[currentPosition]
        val isRightAnswer = selectedPosition == question.rightOption
        if (isRightAnswer) {
            radioButtonAnswer.setTextColor(ContextCompat.getColor(context, R.color.question_answer_right))
            totalRightAnswerByLevel.put(question.level, totalRightAnswerByLevel[question.level]!! + 1)
        } else {
            val rightRadioButton = rdgAnswers.getChildAt(question.rightOption) as RadioButton
            rightRadioButton.setTextColor(ContextCompat.getColor(context, R.color.question_answer_right))
            radioButtonAnswer.setTextColor(ContextCompat.getColor(context, R.color.question_answer_wrong))
        }
        answers.add(Answer(selectedPosition, question))
        onQuestionAnswered(isRightAnswer)
        imgCheckAnswer.setOnClickListener {
            if (hasNextQuestion()) {
                showNextQuestion()
            } else {
                onQuestionsFinish(totalRightAnswerByLevel)
            }
        }
        imgCheckAnswer.isEnabled = true
    }

    private fun disableAnswers() {
        for (i in 0..2) {
            val radioButton = rdgAnswers.getChildAt(i) as RadioButton
            radioButton.isEnabled = false
        }
    }

    private fun getNextIndex(): Int {
        var nextIndex = 0

        for (index in 0..levelInfos.size - 1) {
            val totalQuestionsShownThisLevel = totalQuestionsShownByLevel[index]!!
            var levelRule = levelInfos[index]
            if (totalQuestionsShownThisLevel >= levelRule.numberOfQuestionsToShow) {
                continue
            }
            totalQuestionsShownByLevel.put(index, totalQuestionsShownThisLevel + 1)
            nextIndex = levelRule.startPosition + Random().nextInt(totalNumberQuestionsToShow)
            while (questionsShown.contains(nextIndex)) {
                nextIndex = levelRule.startPosition + Random().nextInt(questions.size)
            }
            break
        }

        questionsShown.add(currentPosition)

        return nextIndex
    }

    fun startQuestions(totalNumberQuestionsToShow: Int, levelInfos: Array<LevelInfo>, questions: Array<Question>) {
        invalidate()
        Log.d(TAG, "(startQuestions) Iniciando as questoes do quiz.")
        this.questions = questions;
        this.levelInfos = levelInfos
        this.totalNumberQuestionsToShow = totalNumberQuestionsToShow
        this.levelInfos.forEachIndexed { index, ruleLevel ->
            totalQuestionsShownByLevel.put(index, 0);
            totalRightAnswerByLevel.put(ruleLevel.level, 0)
        }
        showNextQuestion()
    }

    fun showAnswers(answers: List<Answer>) {
        invalidate()
        Log.d(TAG, "(showAnswers) Iniciando a leitura das respostas.")
        this.answers.addAll(answers)
        showAnswer(answers[currentPosition])
        imgCheckAnswer.setOnClickListener {
            showAnswer(answers[++currentPosition])
            if (hasNextAnswer()) {
                imgCheckAnswer.visibility = View.VISIBLE
            } else {
                imgCheckAnswer.visibility = View.GONE
            }
            if (hasPreviousAnswer()) {
                imgPrevious.visibility = View.VISIBLE
            }
        }
        imgPrevious.setOnClickListener {
            showAnswer(answers[--currentPosition])
            if (hasPreviousAnswer()) {
                imgPrevious.visibility = View.VISIBLE
            } else {
                imgPrevious.visibility = View.GONE
            }
            if (hasNextAnswer()) {
                imgCheckAnswer.visibility = View.VISIBLE
            }
        }
    }

    private fun showAnswer(answer: Answer) {
        if (Question.Type.TEXT == answer.question.type) {
            showTextQuestion(answer.question)
        } else if (Question.Type.AUDIO == answer.question.type) {
            showAudioQuestion(answer.question)
        }
        val radioButtonAnswer = rdgAnswers.getChildAt(answer.optionSelected) as RadioButton
        radioButtonAnswer.isChecked = true
        if (answer.isRight()) {
            radioButtonAnswer.setTextColor(ContextCompat.getColor(context, R.color.question_answer_right))
        } else {
            val rightRadioButton = rdgAnswers.getChildAt(answer.question.rightOption) as RadioButton
            rightRadioButton.setTextColor(ContextCompat.getColor(context, R.color.question_answer_right))
            radioButtonAnswer.setTextColor(ContextCompat.getColor(context, R.color.question_answer_wrong))
        }
        disableAnswers()
    }

    private fun hasNextAnswer(): Boolean {
        return currentPosition < answers.size - 1
    }

    private fun hasPreviousAnswer(): Boolean {
        return currentPosition > 0
    }

    private fun hasNextQuestion(): Boolean {
        return questionsShown.size < totalNumberQuestionsToShow
    }

    fun setOnQuestionAnswered(onQuestionAnswered: (Boolean) -> Unit) {
        this.onQuestionAnswered = onQuestionAnswered
    }

    fun setOnQuestionsFinish(onQuestionsFinish: (Map<Int, Int>) -> Unit) {
        this.onQuestionsFinish = onQuestionsFinish
    }

    fun getAnswers(): List<Answer> {
        return answers
    }

    companion object {
        val TAG = "QuestionView"
    }

}