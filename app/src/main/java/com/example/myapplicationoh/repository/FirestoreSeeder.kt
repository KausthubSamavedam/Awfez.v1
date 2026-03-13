package com.example.myapplicationoh.repository
import com.google.firebase.firestore.FirebaseFirestore
object FirestoreSeeder {
    fun seedIfEmpty() {
        val db = FirebaseFirestore.getInstance()
        db.collection("towers").get().addOnSuccessListener { snapshot ->
            if (!snapshot.isEmpty) return@addOnSuccessListener
            seedDatabase(db)
        }
    }
    private fun seedDatabase(db: FirebaseFirestore) {
        // ---------------- TOWERS ----------------
        val towers = listOf(
            mapOf("id" to "tower1","name" to "Tower 1"),
            mapOf("id" to "tower2","name" to "Tower 2")
        )
        towers.forEach {
            db.collection("towers").document(it["id"] as String).set(it)
        }

        // ---------------- FLOORS ----------------
        val floors = listOf(
            mapOf("id" to "t1_f1","name" to "Floor 1","towerId" to "tower1"),
            mapOf("id" to "t1_f2","name" to "Floor 2","towerId" to "tower1"),
            mapOf("id" to "t1_f3","name" to "Floor 3","towerId" to "tower1"),
            mapOf("id" to "t1_f4","name" to "Floor 4","towerId" to "tower1"),
            mapOf("id" to "t1_f5","name" to "Floor 5","towerId" to "tower1"),
            mapOf("id" to "t1_f6","name" to "Floor 6","towerId" to "tower1"),
            mapOf("id" to "t2_f1","name" to "Floor 1","towerId" to "tower2"),
            mapOf("id" to "t2_f2","name" to "Floor 2","towerId" to "tower2"),
            mapOf("id" to "t2_f3","name" to "Floor 3","towerId" to "tower2"),
            mapOf("id" to "t2_f4","name" to "Floor 4","towerId" to "tower2"),
            mapOf("id" to "t2_f5","name" to "Floor 5","towerId" to "tower2"),
            mapOf("id" to "t2_f6","name" to "Floor 6","towerId" to "tower2")
        )
        floors.forEach {
            db.collection("floors").document(it["id"] as String).set(it)
        }

        // ---------------- ROOMS ----------------
        val rooms = listOf(
            // TRAINING ROOMS TOWER 1
            mapOf("id" to "chitwan","name" to "Chitwan","towerId" to "tower1","floorId" to "t1_f1","type" to "TRAINING_ROOM"),
            mapOf("id" to "yala","name" to "Yala","towerId" to "tower1","floorId" to "t1_f1","type" to "TRAINING_ROOM"),
            mapOf("id" to "oulanka","name" to "Oulanka","towerId" to "tower1","floorId" to "t1_f1","type" to "TRAINING_ROOM"),
            mapOf("id" to "tulum","name" to "Tulum","towerId" to "tower1","floorId" to "t1_f2","type" to "TRAINING_ROOM"),
            mapOf("id" to "denali","name" to "Denali","towerId" to "tower1","floorId" to "t1_f2","type" to "TRAINING_ROOM"),
            mapOf("id" to "ranthambore","name" to "Ranthambore","towerId" to "tower1","floorId" to "t1_f2","type" to "TRAINING_ROOM"),
            mapOf("id" to "etosha","name" to "Etosha","towerId" to "tower1","floorId" to "t1_f3","type" to "TRAINING_ROOM"),
            mapOf("id" to "kruger","name" to "Kruger","towerId" to "tower1","floorId" to "t1_f3","type" to "TRAINING_ROOM"),
            mapOf("id" to "yellowstone","name" to "Yellowstone","towerId" to "tower1","floorId" to "t1_f3","type" to "TRAINING_ROOM"),

            // TRAINING ROOMS TOWER 2
            mapOf("id" to "corcovado","name" to "Corcovado","towerId" to "tower2","floorId" to "t2_f1","type" to "TRAINING_ROOM"),
            mapOf("id" to "rondane","name" to "Rondane","towerId" to "tower2","floorId" to "t2_f1","type" to "TRAINING_ROOM"),
            mapOf("id" to "sagarmatha","name" to "Sagarmatha","towerId" to "tower2","floorId" to "t2_f1","type" to "TRAINING_ROOM"),
            mapOf("id" to "komodo","name" to "Komodo","towerId" to "tower2","floorId" to "t2_f2","type" to "TRAINING_ROOM"),
            mapOf("id" to "kakadu","name" to "Kakadu","towerId" to "tower2","floorId" to "t2_f2","type" to "TRAINING_ROOM"),
            mapOf("id" to "kilimanjaro","name" to "Kilimanjaro","towerId" to "tower2","floorId" to "t2_f2","type" to "TRAINING_ROOM"),
            mapOf("id" to "masada","name" to "Masada","towerId" to "tower2","floorId" to "t2_f3","type" to "TRAINING_ROOM"),
            mapOf("id" to "hemis","name" to "Hemis","towerId" to "tower2","floorId" to "t2_f3","type" to "TRAINING_ROOM"),
            mapOf("id" to "tikal","name" to "Tikal","towerId" to "tower2","floorId" to "t2_f3","type" to "TRAINING_ROOM"),

            // MEETING ROOMS
            mapOf("id" to "borealis","name" to "Borealis Room","towerId" to "tower1","floorId" to "t1_f1","type" to "MEETING_ROOM"),
            mapOf("id" to "l8starry","name" to "L8 Starry Room","towerId" to "tower1","floorId" to "t1_f2","type" to "MEETING_ROOM"),
            mapOf("id" to "kalalau","name" to "Kalalau Room","towerId" to "tower1","floorId" to "t1_f4","type" to "MEETING_ROOM"),
            mapOf("id" to "kalalau2","name" to "Kalalau Room","towerId" to "tower2","floorId" to "t2_f1","type" to "MEETING_ROOM"),
            mapOf("id" to "carnation","name" to "Carnation Room","towerId" to "tower2","floorId" to "t2_f2","type" to "MEETING_ROOM"),
            mapOf("id" to "himachal","name" to "Himachal Room","towerId" to "tower2","floorId" to "t2_f3","type" to "MEETING_ROOM"),

            // WORKSTATIONS
            mapOf("id" to "daas","name" to "Daas Room","towerId" to "tower1","floorId" to "t1_f4","type" to "WORKSTATION"),
            mapOf("id" to "caas","name" to "CaaS Room","towerId" to "tower1","floorId" to "t1_f5","type" to "WORKSTATION"),
            mapOf("id" to "cloudcrucible","name" to "Cloud Crucible Room","towerId" to "tower1","floorId" to "t1_f6","type" to "WORKSTATION"),
            mapOf("id" to "flowmet","name" to "FlowMet Room","towerId" to "tower2","floorId" to "t2_f4","type" to "WORKSTATION"),
            mapOf("id" to "canvascloud","name" to "Canvas Cloud Experience Room","towerId" to "tower2","floorId" to "t2_f5","type" to "WORKSTATION"),
            mapOf("id" to "mosaic","name" to "Mosaic Room","towerId" to "tower2","floorId" to "t2_f6","type" to "WORKSTATION"),

            // INTERVIEW ROOMS
            mapOf("id" to "horizon","name" to "Horizon Room","towerId" to "tower1","floorId" to "t1_f4","type" to "INTERVIEW_ROOM"),
            mapOf("id" to "orbit","name" to "Orbit Room","towerId" to "tower1","floorId" to "t1_f5","type" to "INTERVIEW_ROOM"),
            mapOf("id" to "zenith","name" to "Zenith Room","towerId" to "tower1","floorId" to "t1_f6","type" to "INTERVIEW_ROOM"),
            mapOf("id" to "summit","name" to "Summit Room","towerId" to "tower2","floorId" to "t2_f4","type" to "INTERVIEW_ROOM"),
            mapOf("id" to "pinnacle","name" to "Pinnacle Room","towerId" to "tower2","floorId" to "t2_f5","type" to "INTERVIEW_ROOM"),
            mapOf("id" to "apex","name" to "Apex Room","towerId" to "tower2","floorId" to "t2_f6","type" to "INTERVIEW_ROOM")
        )
        rooms.forEach {
            db.collection("rooms").document(it["id"] as String).set(it)
        }

        // ---------------- ISSUE CATEGORIES ----------------
        val categories = listOf(
            mapOf("id" to "electricity","name" to "Electricity"),
            mapOf("id" to "plumbing","name" to "Plumbing"),
            mapOf("id" to "furniture","name" to "Furniture"),
            mapOf("id" to "av","name" to "AV"),
            mapOf("id" to "hvac","name" to "HVAC"),
            mapOf("id" to "network","name" to "Network")
        )
        categories.forEach {
            db.collection("issueCategories").document(it["id"] as String).set(it)
        }

        // ---------------- ISSUE TYPES ----------------
        val issueTypes = listOf(
            mapOf("name" to "Flickering Lights","categoryId" to "electricity"),
            mapOf("name" to "Power Outage","categoryId" to "electricity"),
            mapOf("name" to "Faulty Socket","categoryId" to "electricity"),
            mapOf("name" to "Water Leakage","categoryId" to "plumbing"),
            mapOf("name" to "Flush Not Working","categoryId" to "plumbing"),
            mapOf("name" to "Tap Issue","categoryId" to "plumbing"),
            mapOf("name" to "Broken Chair","categoryId" to "furniture"),
            mapOf("name" to "Damaged Desk","categoryId" to "furniture"),
            mapOf("name" to "TV Not Working","categoryId" to "av"),
            mapOf("name" to "Projector Issue","categoryId" to "av"),
            mapOf("name" to "AC Noise","categoryId" to "hvac"),
            mapOf("name" to "Temperature Issue","categoryId" to "hvac"),
            mapOf("name" to "WiFi Connectivity","categoryId" to "network"),
            mapOf("name" to "Network Signal Lost","categoryId" to "network")
        )
        issueTypes.forEachIndexed { index, map ->
            db.collection("issueTypes").document("type$index").set(map)
        }
    }
}