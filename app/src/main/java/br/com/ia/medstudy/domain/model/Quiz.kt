package br.com.ia.medstudy.domain.model

import java.io.Serializable

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

data class Quiz(val totalNumberQuestionsToShow: Int, val levelInfo: Array<LevelInfo>, val questions: Array<Question>) : Serializable