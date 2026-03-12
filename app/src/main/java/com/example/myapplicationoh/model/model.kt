package com.example.myapplicationoh.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val initials: String,
    val isAdmin: Boolean = false
)

enum class SpaceType(val displayName: String, val emoji: String) {
    MEETING_ROOM("Meeting Room", "🏛️"),
    WORKSPACE("Workspace", "🖥️")
}

data class Tower(
    val id: String,
    val name: String)

data class Floor(
    val id: String,
    val towerId: String,
    val name: String)

data class Room(
    val id: String,
    val floorId: String,
    val name: String,
    val type: SpaceType,
    val seats: Int? = null
)
data class TimeSlot(
    val id: String,
    val label: String,
    val isBooked: Boolean = false)

data class Booking(
    val id: String,
    val bookingRef: String,
    val roomName: String,
    val tower: String,
    val floor: String,
    val date: String,
    val timeSlot: String,
    val category: SpaceType,
    val status: BookingStatus
)

enum class BookingStatus(val label: String) {
    TODAY("Today"),
    TOMORROW("Tomorrow"),
    UPCOMING("Upcoming"),
    COMPLETED("Completed")
}

data class IssueCategory(
    val id: String,
    val name: String)
data class IssueType(
    val id: String,
    val categoryId: String,
    val name: String)

data class Issue(
    val id: String,
    val issueRef: String,
    val title: String,
    val description: String,
    val category: String,
    val location: String,
    val tower: String,
    val floor: String,
    val room: String,
    val reportedBy: String,
    val reportedByEmail: String,
    val reportedDate: String,
    val status: IssueStatus,
    val assignedTo: String = "",
    val photosCount: Int = 0
)

enum class IssueStatus(
    val label: String,
    val colorKey: String) {
    PENDING("Pending", "pending"),
    ASSIGNED("Assigned", "assigned"),
    IN_PROGRESS("In Progress", "inProgress"),
    RESOLVED("Resolved", "resolved")
}