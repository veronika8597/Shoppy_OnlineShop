package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children

class AddressesFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        viewModel.loadAddresses() // Force LiveData to reload from SharedPreferences
    }

    private val viewModel: AddressesViewModel by viewModels()
    private lateinit var adapter: AddressesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_addresses, container, false)
        recyclerView = view.findViewById(R.id.userAddresses_recyclerView)
        val addButton: Button = view.findViewById(R.id.addAddress_button)

        adapter = AddressesAdapter(mutableListOf<AddressItem>()) { addressItem ->
            val bundle = Bundle().apply { putInt("addressId", addressItem.id) }
            findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment, bundle)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData and update adapter
        viewModel.addresses.observe(viewLifecycleOwner) { addresses ->
            adapter.updateData(addresses)
        }

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment)
        }

        setupSwipeToDelete()

        return view
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedItem = adapter.getItem(position)
                viewModel.deleteAddress(deletedItem)
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



}