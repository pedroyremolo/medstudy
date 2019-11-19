package br.com.ia.medstudy.domain.model

import java.io.Serializable

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

data class Answer(val optionSelected: Int, val question: Question) : Serializable {

    fun isRight(): Boolean {
        return optionSelected == question.rightOption
    }

}