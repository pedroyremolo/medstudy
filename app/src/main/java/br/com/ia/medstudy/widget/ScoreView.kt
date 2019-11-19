package br.com.ia.medstudy.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import br.com.ia.medstudy.R
import kotlinx.android.synthetic.main.view_score.view.*
import org.jetbrains.anko.dimen

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

class ScoreView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        FrameLayout(context, attrs, defStyle) {

    enum class Status {
        OK, NOK
    }

    private var nextPosition = 0

    init {
        inflate(context, R.layout.view_score, this)
        initScore()
    }

    private fun initScore() {
        containerStatus.removeAllViews()
        for (i in 1..10) {
            var height = context.dimen(R.dimen.score_height_status)
            var layout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
            layout.bottomMargin = context.dimen(R.dimen.margin_micro)
            var status = View(context)
            status.layoutParams = layout
            status.setBackgroundResource(R.color.score_status_default)
            containerStatus.addView(status)
        }
    }

    fun changeNextStatusScore(status: Status) {
        if (status.equals(Status.OK)) {
            containerStatus.getChildAt(nextPosition++).setBackgroundResource(R.color.score_status_right)
        } else {
            containerStatus.getChildAt(nextPosition++).setBackgroundResource(R.color.score_status_wrong)
        }
    }

}