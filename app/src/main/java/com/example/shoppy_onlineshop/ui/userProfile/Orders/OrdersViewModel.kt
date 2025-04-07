import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.ui.userProfile.Orders.Order
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OrdersViewModel : ViewModel() {
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val database = FirebaseDatabase.getInstance()

    fun loadOrders(userId: String) {
        val ordersRef = database.getReference("orders") // Assuming your orders are in "orders"
        ordersRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<Order>()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.let { orderList.add(it) }
                }
                _orders.value = orderList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error appropriately, e.g., log it or show an error message
                println("Error loading orders: ${error.message}")
                _orders.value = emptyList() // Set to empty list in case of error
            }
        })
    }
    // Keep your save order function
    fun saveOrderToFirebase(userId: String, order: Order, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val orderRef = database.getReference("orders") // Use the same database instance
        orderRef.child(order.orderId).setValue(order) // Changed to order.orderId
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}