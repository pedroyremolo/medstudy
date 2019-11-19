package br.com.ia.medstudy.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import br.com.ia.medstudy.R
import br.com.ia.medstudy.utils.ResourceUtils
import br.com.ia.medstudy.utils.UnitUtils
import com.viewpagerindicator.CirclePageIndicator

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

class EquipmentAcquiredView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        FrameLayout(context, attrs, defStyle) {

    private lateinit var viewPager: ViewPager
    private lateinit var equipments: List<String>

    init {
        if (isInEditMode) {
            setupEquipments(arrayListOf("equipment_1", "equipment_2", "equipment_3"))
        }
    }

    fun setupEquipments(equipments: List<String>) {
        this.equipments = equipments
        viewPager = ViewPager(context)
        viewPager.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        viewPager.offscreenPageLimit = if (equipments.size <= 3) equipments.size else 3
        val adapter = EquipmentsPagerAdapter()
        viewPager.adapter = adapter
        addView(viewPager)
        if (equipments.size > 1) {
            val pageIndicator = getIndicator()
            pageIndicator.setViewPager(viewPager)
            addView(pageIndicator)
        }
    }

    private inner class EquipmentsPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return equipments.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val imageView = ImageView(context)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.layoutParams = layoutParams
            imageView.setImageResource(ResourceUtils.getResourceDrawableIdByName(equipments[position]))

            container.addView(imageView)

            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as ImageView)
        }

    }

    private fun getIndicator(): CirclePageIndicator {
        val pageIndicator = CirclePageIndicator(context)
        pageIndicator.radius = UnitUtils.convertDpToPx(context, 10).toFloat()
        pageIndicator.fillColor = ContextCompat.getColor(context, R.color.black)
        pageIndicator.strokeColor = ContextCompat.getColor(context, android.R.color.black)
        pageIndicator.pageColor = ContextCompat.getColor(context, android.R.color.transparent)
        pageIndicator.strokeWidth = UnitUtils.convertDpToPx(context, 1).toFloat()
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        val padding = UnitUtils.convertDpToPx(context, 10)
        pageIndicator.setPadding(padding, padding, padding, padding)
        pageIndicator.layoutParams = layoutParams
        return pageIndicator
    }

}