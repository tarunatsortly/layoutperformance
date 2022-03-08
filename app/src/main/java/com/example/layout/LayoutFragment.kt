package com.example.layout

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.layout.databinding.FragmentLayoutBinding
import com.example.layout.databinding.SimpleContainerBinding

val Int.dp: Int
    get() {
        return android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

val Float.sp: Float
    get() {
        return android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )
    }

fun TextView.applyStyle(font: FontType?, color: Int) {
    font?.let {
        this.typeface = font.typeface
        this.textSize = font.size
    }?.apply {
        setTextColor(color)
    }
}

class LayoutFragment : Fragment() {

    private var binding: FragmentLayoutBinding? = null

    private val images = arrayListOf(
        R.drawable.icon_18_px_calendar,
        R.drawable.attachment_black,
        R.drawable.link_black,
        R.drawable.mail_black,
        R.drawable.menu_icon_account,
        R.drawable.phone_black,
        R.drawable.scan_to_search_icon
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLayoutBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fieldsContainer = binding?.fieldsContainer
        val inflater = LayoutInflater.from(this.context)
        for (i in 1..100) {
            val simple = SimpleContainerBinding.inflate(inflater)
            simple.secondImageView.setImageResource(images.random())
            fieldsContainer?.addView(simple.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}