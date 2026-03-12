package com.example.myapplicationoh.repository

import com.example.myapplicationoh.model.Booking
import com.example.myapplicationoh.model.BookingStatus
import com.example.myapplicationoh.model.Floor
import com.example.myapplicationoh.model.Issue
import com.example.myapplicationoh.model.IssueCategory
import com.example.myapplicationoh.model.IssueStatus
import com.example.myapplicationoh.model.IssueType
import com.example.myapplicationoh.model.Room
import com.example.myapplicationoh.model.SpaceType
import com.example.myapplicationoh.model.TimeSlot
import com.example.myapplicationoh.model.Tower
import com.example.myapplicationoh.model.User


object MockDataRepository {

    val regularUser = User("u1", "Alex Johnson", "alex.johnson@company.com", "AJ")
    val adminUser = User("a1", "Facilities Admin", "admin@company.com", "FA", isAdmin = true)

    fun authenticateUser(email: String, password: String): User? =
        if (email == "alex.johnson@company.com" && password.isNotEmpty()) regularUser else null

    fun authenticateAdmin(email: String, password: String): User? =
        if (email == "admin@company.com" && password.isNotEmpty()) adminUser else null

    val towers = listOf(
        Tower("t1", "Tower A"),
        Tower("t2", "Tower B"),
        Tower("t3", "Tower C")
    )

    val floors = listOf(
        Floor("f1", "t1", "Floor 1"),
        Floor("f2", "t1", "Floor 2"),
        Floor("f3", "t1", "Floor 3"),
        Floor("f4", "t1", "Floor 4"),
        Floor("f5", "t1", "Floor 5"),
        Floor("f6", "t2", "Floor 1"),
        Floor("f7", "t2", "Floor 2"),
        Floor("f8", "t2", "Floor 3"),
        Floor("f9", "t2", "Floor 8"),
        Floor("f10", "t2", "Floor 12"),
        Floor("f11", "t3", "Floor 1"),
        Floor("f12", "t3", "Floor 2"),
        Floor("f13", "t3", "Floor 5")
    )

    fun getFloorsForTower(towerId: String) = floors.filter { it.towerId == towerId }

    val rooms = listOf(
        Room("r1", "f5", "Desk W-14 — Open Workspace", SpaceType.WORKSPACE),
        Room("r2", "f5", "Desk W-15 — Open Workspace", SpaceType.WORKSPACE),
        Room("r3", "f4", "Room 401 — Small Meeting Room (4 seats)", SpaceType.MEETING_ROOM, 4),
        Room("r4", "f9", "Room 803 — Boardroom (10 seats)", SpaceType.MEETING_ROOM, 10),
        Room("r5", "f9", "Room 804 — Focus Room (4 seats)", SpaceType.MEETING_ROOM, 4),
        Room("r6", "f9", "Desk B-10 — Workspace", SpaceType.WORKSPACE),
        Room("r7", "f10", "Room 1201 — Conference Room (20 seats)", SpaceType.MEETING_ROOM, 20),
        Room("r8", "f13", "Room 502 — Focus Room (4 seats)", SpaceType.MEETING_ROOM, 4),
        Room("r9", "f12", "Collab Zone — Open Workspace", SpaceType.WORKSPACE)
    )

    fun getRoomsForFloor(floorId: String) = rooms.filter { it.floorId == floorId }

    fun getTimeSlotsForRoom(roomId: String): List<TimeSlot> = listOf(
        TimeSlot("ts1", "9:00–10:00", isBooked = (roomId == "r4")),
        TimeSlot("ts2", "10:00–11:00"),
        TimeSlot("ts3", "11:00–12:00"),
        TimeSlot("ts4", "12:00–13:00"),
        TimeSlot("ts5", "13:00–14:00", isBooked = true),
        TimeSlot("ts6", "14:00–15:00"),
        TimeSlot("ts7", "15:00–16:00"),
        TimeSlot("ts8", "16:00–17:00")
    )

    val bookings = mutableListOf(
        Booking(
            "b1", "BK-2026-4821", "Room 803 — Boardroom", "Tower B", "Floor 8",
            "Thu, Mar 13", "11:00–12:00", SpaceType.MEETING_ROOM, BookingStatus.TODAY
        ),
        Booking("b2", "BK-2026-4732", "Desk W-14", "Tower A", "Floor 5 — Open Workspace",
            "Fri, Mar 14", "9:00–17:00", SpaceType.WORKSPACE, BookingStatus.TOMORROW),
        Booking("b3", "BK-2026-4100", "Room 502 — Focus Room", "Tower C", "Floor 5",
            "Tue, Mar 10", "14:00–16:00", SpaceType.MEETING_ROOM, BookingStatus.COMPLETED)
    )

    val issueCategories = listOf(
        IssueCategory("c1", "Electrical / Lighting"),
        IssueCategory("c2", "HVAC / Air Conditioning"),
        IssueCategory("c3", "Plumbing"),
        IssueCategory("c4", "IT / Network"),
        IssueCategory("c5", "Furniture / Equipment"),
        IssueCategory("c6", "Cleaning / Housekeeping"),
        IssueCategory("c7", "Security"),
        IssueCategory("c8", "Other")
    )

    val issueTypes = listOf(
        IssueType("it1", "c1", "Lights not Working"),
        IssueType("it2", "c1", "Lights Flickering"),
        IssueType("it3", "c1", "Power Outlet Issue"),
        IssueType("it4", "c2", "AC not Cooling"),
        IssueType("it5", "c2", "AC not Working"),
        IssueType("it6", "c2", "Temperature Too High"),
        IssueType("it7", "c3", "Tap Leaking"),
        IssueType("it8", "c3", "Toilet Issue"),
        IssueType("it9", "c4", "WiFi Not Working"),
        IssueType("it10", "c4", "WiFi Slow"),
        IssueType("it11", "c4", "No Internet"),
        IssueType("it12", "c5", "Chair Broken"),
        IssueType("it13", "c5", "Desk Damaged"),
        IssueType("it14", "c6", "Cleaning Required"),
        IssueType("it15", "c8", "Other Issue")
    )

    fun getIssueTypesForCategory(categoryId: String) =
        issueTypes.filter { it.categoryId == categoryId }

    val issues = mutableListOf(
        Issue(
            "i1", "#ISS-2026-0889", "Lights flickering in office",
            "The overhead lights in Room 803 have been flickering for two days. It's causing eye strain during meetings and affecting productivity.",
            "Electrical / Lighting", "Tower B, Floor 8, Room 803",
            "Tower B", "Floor 8", "Room 803",
            "Alex Johnson", "alex.johnson@company.com", "Mar 10, 2026",
            IssueStatus.IN_PROGRESS, "Electrical", 2
        ),
        Issue("i2", "#ISS-2026-0875", "AC not working properly",
            "The air conditioning in the open space area is not cooling properly.",
            "HVAC / Air Conditioning", "Tower A, Floor 5, Open Space",
            "Tower A", "Floor 5", "Open Space",
            "Priya Nair", "priya.nair@company.com", "Mar 8, 2026",
            IssueStatus.PENDING),
        Issue("i3", "#ISS-2026-0856", "Washroom tap leaking",
            "The washroom tap on floor 3 has been leaking for a week.",
            "Plumbing", "Tower B, Floor 3, Restroom",
            "Tower B", "Floor 3", "Restroom",
            "Alex Johnson", "alex.johnson@company.com", "Mar 5, 2026",
            IssueStatus.RESOLVED),
        Issue("i4", "#ISS-2026-0892", "WiFi connectivity issues",
            "WiFi is dropping intermittently in the collab zone.",
            "IT / Network", "Tower C, Floor 2, Collab Zone",
            "Tower C", "Floor 2", "Collab Zone",
            "Ravi Kumar", "ravi.kumar@company.com", "Mar 11, 2026",
            IssueStatus.ASSIGNED)
    )

    fun getMyIssues(userEmail: String) = issues.filter { it.reportedByEmail == userEmail }
    fun getAllIssues() = issues.toList()
}