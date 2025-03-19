package com.example.shoppy_onlineshop.helpers

import android.graphics.Canvas
import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

fun setupSwipeToDelete(recyclerView: RecyclerView, onDelete: (position: Int) -> Unit) {

    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            onDelete(position)
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
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            val itemView = viewHolder.itemView
            val cardViewDelete = itemView.findViewById<CardView>(R.id.cardView_delete)
            val constraintLayoutMain = itemView.findViewById<ConstraintLayout>(R.id.constraintLayout_main)
            val cardViewMain = itemView.findViewById<CardView>(R.id.cardView_main)

            // Ensure that only the top swiped card is moved
            if (dX < 0) { // Swiping left
                cardViewDelete.visibility = View.VISIBLE
                constraintLayoutMain.translationX = dX
                cardViewMain.translationX = dX
            } else { // Reset swipe position if not swiping or swiping right
                cardViewDelete.visibility = View.GONE
                constraintLayoutMain.translationX = 0f
                cardViewMain.translationX = 0f
            }

            // Prevent the other cards from being affected by the swipe
            for (i in 0 until recyclerView.childCount) {
                val childView = recyclerView.getChildAt(i)
                if (childView != itemView) {
                    childView.translationX = 0f // Ensures no translation happens to non-swiped cards
                }
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            // Reset the views after swipe action
            val itemView = viewHolder.itemView
            val cardViewDelete = itemView.findViewById<CardView>(R.id.cardView_delete)
            val constraintLayoutMain = itemView.findViewById<ConstraintLayout>(R.id.constraintLayout_main)
            val cardViewMain = itemView.findViewById<CardView>(R.id.cardView_main)

            // Reset the swiped card translation
            constraintLayoutMain.translationX = 0f
            cardViewDelete.visibility = View.GONE
            cardViewMain.translationX = 0f
        }
    })
    itemTouchHelper.attachToRecyclerView(recyclerView)
}