package com.example.myapplicationoh.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

// ---------- USER ----------
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val initials: String = "",
    val isAdmin: Boolean = false
)

// ---------- SPACE TYPE ----------
enum class SpaceType(val displayName: String, val emoji: String, val firestoreValue: String) {
    MEETING_ROOM("Meeting Room", "🏛️", "Meeting Rooms"),
    WORKSPACE("Workspace", "🖥️", "Workspaces");

    companion object {
        fun fromString(value: String?): SpaceType {
            return entries.find { it.firestoreValue == value || it.name == value } ?: MEETING_ROOM
        }
    }
}

// ---------- LOCATION STRUCTURE ----------
data class Tower(
    val id: String = "",
    val name: String = ""
)
data class Floor(
    val id: String = "",
    val towerId: String = "",
    val name: String = ""
)
data class Room(
    val id: String = "",
    val floorId: String = "",
    val name: String = "",
    @get:PropertyName("type") @set:PropertyName("type")
    var typeString: String = "Meeting Rooms",
    val seats: Int = 0
) {
    @get:Exclude
    val type: SpaceType get() = SpaceType.fromString(typeString)
}

// ---------- BOOKING ----------
data class TimeSlot(
    val id: String = "",
    val label: String = "",
    val isBooked: Boolean = false
)
data class Booking(
    val id: String = "",
    val bookingRef: String = "",
    val roomName: String = "",
    val tower: String = "",
    val floor: String = "",
    val date: String = "",
    val timeSlot: String = "",
    @get:PropertyName("category") @set:PropertyName("category")
    var categoryString: String = "Meeting Rooms",
    @get:PropertyName("status") @set:PropertyName("status")
    var statusString: String = "Upcoming"
) {
    @get:Exclude
    val category: SpaceType get() = SpaceType.fromString(categoryString)
    
    @get:Exclude
    val status: BookingStatus get() = BookingStatus.fromString(statusString)
}

enum class BookingStatus(val label: String) {
    TODAY("Today"),
    TOMORROW("Tomorrow"),
    UPCOMING("Upcoming"),
    COMPLETED("Completed");

    companion object {
        fun fromString(value: String?): BookingStatus {
            return entries.find { it.label == value || it.name == value } ?: UPCOMING
        }
    }
}

// ---------- ISSUE SYSTEM ----------
data class IssueCategory(
    val id: String = "",
    val name: String = ""
)
data class IssueType(
    val id: String = "",
    val categoryId: String = "",
    val name: String = ""
)
data class Issue(
    val id: String = "",
    val issueRef: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val location: String = "",
    val tower: String = "",
    val floor: String = "",
    val room: String = "",
    val reportedBy: String = "",
    val reportedByEmail: String = "",
    val reportedDate: String = "",
    @get:PropertyName("status") @set:PropertyName("status")
    var statusString: String = "Pending",
    val assignedTo: String = "",
    val photosCount: Int = 0
) {
    @get:Exclude
    val status: IssueStatus get() = IssueStatus.fromString(statusString)
}

enum class IssueStatus(
    val label: String,
    val colorKey: String
) {
    PENDING("Pending", "pending"),
    ASSIGNED("Assigned", "assigned"),
    IN_PROGRESS("In Progress", "inProgress"),
    RESOLVED("Resolved", "resolved");

    companion object {
        fun fromString(value: String?): IssueStatus {
            return entries.find { it.label == value || it.name == value } ?: PENDING
        }
    }
}