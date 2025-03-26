package com.example.shoppy_onlineshop.helpers

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible

fun toggleSectionVisibility(
    sectionView: View,
    headerView: TextView,
    expandedText: String,
    collapsedText: String
) {
    if (sectionView.isVisible) {
        sectionView.visibility = View.GONE
        headerView.text = collapsedText
    } else {
        sectionView.visibility = View.VISIBLE
        headerView.text = expandedText
    }
}