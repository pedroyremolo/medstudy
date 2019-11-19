package br.com.ia.medstudy.domain.model

import java.io.Serializable

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

data class Question(val type: Type, val level: Int, val text: String?, val image: String?, val audio: String?, val options: Array<String>,
                    val rightOption: Int, val textWrongOption: String?, val imageWrongOption: String?, val audioWrongOption: String?) : Serializable {

    enum class Type {
        TEXT, AUDIO
    }

}