package com.example.myapplicationoh.data

import com.example.myapplicationoh.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    // ---------------- TOWERS ----------------
    fun observeTowers(onResult: (List<Tower>) -> Unit) {
        db.collection("towers")
            .addSnapshotListener { snapshot, _ ->
                val towers = snapshot?.documents?.mapNotNull {
                    it.toObject(Tower::class.java)
                } ?: emptyList()

                onResult(towers)
            }
    }

    // ---------------- FLOORS ----------------
    fun observeFloors(onResult: (List<Floor>) -> Unit) {
        db.collection("floors")
            .addSnapshotListener { snapshot, _ ->
                val floors = snapshot?.documents?.mapNotNull {
                    it.toObject(Floor::class.java)
                } ?: emptyList()

                onResult(floors)
            }
    }

    // ---------------- ROOMS ----------------
    fun observeRooms(onResult: (List<Room>) -> Unit) {
        db.collection("rooms")
            .addSnapshotListener { snapshot, _ ->
                val rooms = snapshot?.documents?.mapNotNull {
                    it.toObject(Room::class.java)
                } ?: emptyList()

                onResult(rooms)
            }
    }

    // ---------------- BOOKINGS ----------------
    fun observeBookings(onResult: (List<Booking>) -> Unit) {

        val auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener { firebaseAuth ->

            val userId = firebaseAuth.currentUser?.uid ?: return@addAuthStateListener

            db.collection("bookings")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, _ ->

                    val bookings = snapshot?.documents?.mapNotNull {
                        it.toObject(Booking::class.java)
                    } ?: emptyList()
                    onResult(bookings)
                }
        }
    }

    suspend fun createBooking(booking: Booking) {
        db.collection("bookings")
            .document(booking.id)
            .set(booking)
            .await()
    }

    // ---------------- ISSUE DATA ----------------

    fun observeIssueCategories(onResult: (List<IssueCategory>) -> Unit) {
        db.collection("issueCategories")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.mapNotNull {
                    it.toObject(IssueCategory::class.java)
                } ?: emptyList()

                onResult(list)
            }
    }

    fun observeIssueTypes(onResult: (List<IssueType>) -> Unit) {
        db.collection("issueTypes")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.mapNotNull {
                    it.toObject(IssueType::class.java)
                } ?: emptyList()

                onResult(list)
            }
    }

    fun observeIssues(onResult: (List<Issue>) -> Unit) {

        val auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener { firebaseAuth ->

            val userId = firebaseAuth.currentUser?.uid ?: return@addAuthStateListener

            db.collection("issues")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, _ ->

                    val issues = snapshot?.documents?.mapNotNull {
                        it.toObject(Issue::class.java)
                    } ?: emptyList()
                    onResult(issues)
                }
        }
    }
    //------ ADMIN ISSUES (ALL) --------
    fun observeAllIssues(onResult: (List<Issue>) -> Unit) {

        db.collection("issues")
            .addSnapshotListener { snapshot, _ ->

                val issues = snapshot?.documents?.mapNotNull {
                    it.toObject(Issue::class.java)
                } ?: emptyList()

                onResult(issues)
            }
    }
    //----------- ALL BOOKINGS --------
    fun observeAllBookings(onResult: (List<Booking>) -> Unit) {

        db.collection("bookings")
            .addSnapshotListener { snapshot, _ ->

                val bookings = snapshot?.documents?.mapNotNull {
                    it.toObject(Booking::class.java)
                } ?: emptyList()

                onResult(bookings)
            }
    }

    suspend fun createIssue(issue: Issue) {
        db.collection("issues")
            .document(issue.id)
            .set(issue)
            .await()
    }

    suspend fun updateIssueStatus(issueId: String, status: String) {
        db.collection("issues")
            .document(issueId)
            .update("status", status)
            .await()
    }

    suspend fun updateIssueAssignment(issueId: String, department: String) {
        db.collection("issues")
            .document(issueId)
            .update(
                mapOf(
                    "assignedTo" to department,
                    "status" to "Assigned"
                )
            ).await()
    }
}