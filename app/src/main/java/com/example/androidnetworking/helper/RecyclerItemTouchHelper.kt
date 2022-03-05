package com.example.androidnetworking.helper

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidnetworking.adapter.PosterAdapter

class RecyclerItemTouchHelper(

    dragDirs: Int,
    swipeDirs: Int,
    private val listener: RecyclerItemTouchHelperListener

): ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null && viewHolder is PosterAdapter.PosterViewHolder){
            val foreGroundView : View = viewHolder.llPoster
            getDefaultUIUtil().onSelected(foreGroundView)
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder is PosterAdapter.PosterViewHolder) {
            val foreGround: View = viewHolder.llPoster
            getDefaultUIUtil().onDrawOver(c, recyclerView, foreGround, dX/3, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is PosterAdapter.PosterViewHolder){
            val foreGround: View = viewHolder.llPoster
            getDefaultUIUtil().clearView(foreGround)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder is PosterAdapter.PosterViewHolder){
            val foreGround = viewHolder.llPoster
            getDefaultUIUtil().onDraw(c, recyclerView, foreGround,dX/3, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }

}