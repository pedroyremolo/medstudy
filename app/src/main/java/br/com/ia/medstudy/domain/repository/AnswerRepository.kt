package br.com.ia.medstudy.domain.repository

import br.com.ia.medstudy.domain.model.Answer
import io.paperdb.Paper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

class AnswerRepository {

    companion object {
        private val PAPER_BOOK = "ANSWER_BOOK"
        private val PAPER_KEY_ANSWERS = "KEY_ANSWERS_%d"
    }

    fun save(level: Int, answers: List<Answer>) {
        doAsync {
            val book = Paper.book(PAPER_BOOK);
            val key = PAPER_KEY_ANSWERS.format(level)
            if (book.contains(key)) {
                book.delete(key)
            }
            book.write(key, answers)
        }
    }

    fun retrieve(level: Int, onResponseReady: (List<Answer>) -> Unit) {
        doAsync {
            val book = Paper.book(PAPER_BOOK);
            val key = PAPER_KEY_ANSWERS.format(level)
            val answers = book.read<List<Answer>>(key, mutableListOf())
            uiThread {
                onResponseReady(answers)
            }
        }
    }

}