package com.example.myapplicationoh.data
import com.example.myapplicationoh.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    // TOWERS
    fun getTowers(onResult: (List<Tower>) -> Unit) {
        db.collection("towers")
            .addSnapshotListener { snapshot, _ ->
                val towers = snapshot?.documents?.mapNotNull {
                    it.toObject(Tower::class.java)
                } ?: emptyList()
                onResult(towers)
            }
    }
    // FLOORS
    fun getFloors(onResult: (List<Floor>) -> Unit) {
        db.collection("floors")
            .addSnapshotListener { snapshot, _ ->
                val floors = snapshot?.documents?.mapNotNull {
                    it.toObject(Floor::class.java)
                } ?: emptyList()
                onResult(floors)
            }
    }
    // ROOMS
    fun getRooms(onResult: (List<Room>) -> Unit) {
        db.collection("rooms")
            .addSnapshotListener { snapshot, _ ->
                val rooms = snapshot?.documents?.mapNotNull {
                    it.toObject(Room::class.java)
                } ?: emptyList()
                onResult(rooms)
            }
    }
    // ISSUE CATEGORIES
    fun getIssueCategories(onResult: (List<IssueCategory>) -> Unit) {
        db.collection("issueCategories")
            .addSnapshotListener { snapshot, _ ->
                val categories = snapshot?.documents?.mapNotNull {
                    it.toObject(IssueCategory::class.java)
                } ?: emptyList()
                onResult(categories)
            }
    }
    // ISSUE TYPES
    fun getIssueTypes(onResult: (List<IssueType>) -> Unit) {
        db.collection("issueTypes")
            .addSnapshotListener { snapshot, _ ->
                val types = snapshot?.documents?.mapNotNull {
                    it.toObject(IssueType::class.java)
                } ?: emptyList()
                onResult(types)
            }
    }
    // CREATE BOOKING
    suspend fun createBooking(booking: Booking) {
        db.collection("bookings")
            .document(booking.id)
            .set(booking)
            .await()
    }
    // CREATE ISSUE
    suspend fun createIssue(issue: Issue) {
        db.collection("issues")
            .document(issue.id)
            .set(issue)
            .await()
    }
}