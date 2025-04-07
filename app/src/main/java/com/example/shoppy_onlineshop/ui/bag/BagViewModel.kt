package com.example.shoppy_onlineshop.ui.bag

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.helpers.fetchProductDetails
import com.example.shoppy_onlineshop.helpers.removeFromFavorites
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BagViewModel : ViewModel() {

    private val databaseBagRef = FirebaseDatabase.getInstance().getReference("bag")

    private val _bagItems = MutableLiveData<List<BagItem>>()
    val bagItems: LiveData<List<BagItem>> get() = _bagItems

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadBagProducts(userId: String?) {
        if (userId != null) {
            fetchBagItems(
                userId,
                onSuccess = { items ->
                    _bagItems.value = items
                    _errorMessage.value = null
                },
                onFailure = { exception ->
                    _errorMessage.value = "Failed to load bag items: ${exception.message}"
                }
            )
        }
    }

    fun updateLocalBag(product: StoreProduct) {
        val currentList = _bagItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentList.add(BagItem(product = product, quantity = 1))
        }

        _bagItems.value = currentList
    }

    fun addToBag(userId: String, product: StoreProduct, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val bagItem = product.toBagItem(1)
        val productId = product.id.toString()

        databaseBagRef.child(userId).child(productId).setValue(bagItem)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun removeProductFromBag(userId: String, productId: Int) {
        val currentList = _bagItems.value.orEmpty().toMutableList()
        val updatedList = currentList.filter { it.product.id != productId }
        _bagItems.value = updatedList

        removeFromDBBag(userId, productId,
            onSuccess = {
                loadBagProducts(userId)
            },
            onFailure = {
                Log.e("BagViewModel", "Failed to remove product from bag", it)
            })
    }

    private fun removeFromDBBag(userId: String, productId: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        databaseBagRef.child(userId).child(productId.toString())
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                _errorMessage.value = "Failed to remove item: ${it.message}"
                onFailure(it)
            }
    }

    fun clearBag(userId: String) {
        databaseBagRef.child(userId).removeValue()
            .addOnSuccessListener {
                _bagItems.value = emptyList()
            }
            .addOnFailureListener {
                _errorMessage.value = "Failed to clear bag: ${it.message}"
            }
    }

    fun increaseQuantity(userId: String, productId: Int) {
        val currentList = _bagItems.value.orEmpty().toMutableList()
        val item = currentList.find { it.product.id == productId }
        if (item != null) {
            item.quantity += 1
            _bagItems.value = currentList
            saveQuantityToDB(userId, item)
        }
    }

    fun decreaseQuantity(userId: String, productId: Int):Boolean {
        val currentList = _bagItems.value.orEmpty().toMutableList()
        val item = currentList.find { it.product.id == productId }
        if (item != null && item.quantity > 1) {
            item.quantity -= 1
            _bagItems.value = currentList
            saveQuantityToDB(userId, item)
            return false
        } else{
            return true
        }
    }

    private fun saveQuantityToDB(userId: String, item: BagItem) {
        val productId = item.product.id.toString()
        databaseBagRef.child(userId).child(productId).setValue(item)
    }

    fun fetchBagItems(userId: String, onSuccess: (List<BagItem>) -> Unit, onFailure: (Exception) -> Unit) {
        databaseBagRef.child(userId).get().addOnSuccessListener { snapshot ->
            val bag = mutableListOf<BagItem>()
            for (child in snapshot.children) {
                child.getValue(BagItem::class.java)?.let { bag.add(it) }
            }
            onSuccess(bag)
        }.addOnFailureListener { onFailure(it) }
    }

    fun isProductInBag(userId: String, product: StoreProduct, callback: (Boolean) -> Unit) {
        databaseBagRef.child(userId).child(product.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.exists())
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false)
                }
            })
    }

    fun isProductInLocalBag(productId: Int): Boolean {
        return _bagItems.value?.any { it.product.id == productId } == true
    }

}

