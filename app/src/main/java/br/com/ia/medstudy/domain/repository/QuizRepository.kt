package br.com.ia.medstudy.domain.repository

import android.content.res.Resources
import android.util.Log
import br.com.ia.medstudy.R
import br.com.ia.medstudy.domain.model.LevelInfo
import br.com.ia.medstudy.domain.model.Quiz
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

class QuizRepository {

    companion object {
        val TAG = "QuizRepository"
    }

    fun retrieveLevelingQuestions(resource: Resources, onResponseReady: (Quiz) -> Unit) = readQuestions(resource, R.raw.leveling, onResponseReady)

    fun retrieveLevelOneQuestions(resource: Resources, onResponseReady: (Quiz) -> Unit) = readQuestions(resource, R.raw.level_one, onResponseReady)

    fun retrieveLevelTwoQuestions(resource: Resources, onResponseReady: (Quiz) -> Unit) = readQuestions(resource, R.raw.level_two, onResponseReady)

    fun retrieveLevelThreeQuestions(resource: Resources, onResponseReady: (Quiz) -> Unit) = readQuestions(resource, R.raw.level_three, onResponseReady)

    fun retrieveLevelFourQuestions(resource: Resources, onResponseReady: (Quiz) -> Unit) = readQuestions(resource, R.raw.level_four, onResponseReady)

    fun retrieveLevelFiveQuestions(resource: Resources, onResponseReady: (Quiz) -> Unit) = readQuestions(resource, R.raw.level_five, onResponseReady)

    fun retrieveReinforcement(resource: Resources, level: Int, onResponseReady: (Quiz) -> Unit) {
        readQuestions(resource, R.raw.reinforcement) { quiz ->
            quiz.levelInfo.forEach { levelRule ->
                if (levelRule.level == level) {
                    val newLevelRule = LevelInfo(level, levelRule.numberOfQuestionsToShow, levelRule.minimumCorrectAnswers, 0,
                            levelRule.endPosition - levelRule.startPosition, levelRule.equipment)
                    onResponseReady(Quiz(quiz.totalNumberQuestionsToShow, arrayOf(newLevelRule),
                            quiz.questions.copyOfRange(levelRule.startPosition, levelRule.endPosition + 1)))
                }
            }
        }
    }

    private fun readQuestions(resource: Resources, resourceId: Int, onResponseReady: (Quiz) -> Unit) {
        doAsync {

            Log.d(TAG, "Lendo o arquivo de configuração.")

            val resourceRaw = resource.openRawResource(resourceId)

            val outputStream = ByteArrayOutputStream()

            val buf = ByteArray(1024)
            var len: Int
            try {
                len = resourceRaw.read(buf)
                while (len != -1) {
                    outputStream.write(buf, 0, len)
                    len = resourceRaw.read(buf)
                }
                outputStream.close()
                resourceRaw.close()
            } catch (e: IOException) {
                Log.e(TAG, "Erro ao ler o arquivo de configuração", e)
            }

            val quiz = Gson().fromJson(outputStream.toString(), Quiz::class.java)
            Log.d(TAG, "Arquivo lido com sucesso. ${outputStream.toString().substring(0, 50)}")

            uiThread {
                onResponseReady(quiz)
            }

        }
    }

}