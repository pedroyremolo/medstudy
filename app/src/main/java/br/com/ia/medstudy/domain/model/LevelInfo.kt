package br.com.ia.medstudy.domain.model

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

data class LevelInfo(val level: Int, val numberOfQuestionsToShow: Int, val minimumCorrectAnswers: Int,
                     val startPosition: Int, val endPosition: Int, val equipment: String)