package co.ruizhang.weatherapp.views.citylist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.ruizhang.weatherapp.R

class CityListDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        with(outRect) {
            val itemCount = parent.adapter?.itemCount ?: 1
            if (position == itemCount - 1) {
                this.bottom = 0
            } else {
                this.bottom = view.resources.getDimensionPixelSize(R.dimen.default_space_x0_5)
            }
        }
    }
}