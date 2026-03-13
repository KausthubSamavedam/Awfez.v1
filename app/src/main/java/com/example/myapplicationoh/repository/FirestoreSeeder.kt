package com.example.myapplicationoh.repository

import com.example.myapplicationoh.model.SpaceType
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

        // ---------- TOWERS ----------
        val towers = listOf(
            mapOf("id" to "tower1", "name" to "Tower 1"),
            mapOf("id" to "tower2", "name" to "Tower 2")
        )

        towers.forEach {
            db.collection("towers").document(it["id"] as String).set(it)
        }


        // ---------- FLOORS ----------
        val floors = mutableListOf<Map<String, Any>>()

        (1..6).forEach { floor ->
            floors.add(mapOf("id" to "t1_f$floor", "name" to "Floor $floor", "towerId" to "tower1"))
            floors.add(mapOf("id" to "t2_f$floor", "name" to "Floor $floor", "towerId" to "tower2"))
        }

        floors.forEach {
            db.collection("floors").document(it["id"] as String).set(it)
        }


        // ---------- ROOMS ----------
        val rooms = listOf(

            // TRAINING ROOMS TOWER 1
            room("chitwan","Chitwan","tower1","t1_f1",SpaceType.TRAINING_ROOM),
            room("yala","Yala","tower1","t1_f1",SpaceType.TRAINING_ROOM),
            room("oulanka","Oulanka","tower1","t1_f1",SpaceType.TRAINING_ROOM),

            room("tulum","Tulum","tower1","t1_f2",SpaceType.TRAINING_ROOM),
            room("denali","Denali","tower1","t1_f2",SpaceType.TRAINING_ROOM),
            room("ranthambore","Ranthambore","tower1","t1_f2",SpaceType.TRAINING_ROOM),

            room("etosha","Etosha","tower1","t1_f3",SpaceType.TRAINING_ROOM),
            room("kruger","Kruger","tower1","t1_f3",SpaceType.TRAINING_ROOM),
            room("yellowstone","Yellowstone","tower1","t1_f3",SpaceType.TRAINING_ROOM),

            // TRAINING ROOMS TOWER 2
            room("corcovado","Corcovado","tower2","t2_f1",SpaceType.TRAINING_ROOM),
            room("rondane","Rondane","tower2","t2_f1",SpaceType.TRAINING_ROOM),
            room("sagarmatha","Sagarmatha","tower2","t2_f1",SpaceType.TRAINING_ROOM),

            room("komodo","Komodo","tower2","t2_f2",SpaceType.TRAINING_ROOM),
            room("kakadu","Kakadu","tower2","t2_f2",SpaceType.TRAINING_ROOM),
            room("kilimanjaro","Kilimanjaro","tower2","t2_f2",SpaceType.TRAINING_ROOM),

            room("masada","Masada","tower2","t2_f3",SpaceType.TRAINING_ROOM),
            room("hemis","Hemis","tower2","t2_f3",SpaceType.TRAINING_ROOM),
            room("tikal","Tikal","tower2","t2_f3",SpaceType.TRAINING_ROOM),


            // MPH HALL
            room("mph1","MPH Hall 1","tower1","t1_f1",SpaceType.MPH_HALL),
            room("mph2","MPH Hall 2","tower2","t2_f1",SpaceType.MPH_HALL),


            // MEETING ROOMS
            room("borealis","Borealis Room","tower1","t1_f1",SpaceType.MEETING_ROOM),
            room("l8starry","L8 Starry Room","tower1","t1_f2",SpaceType.MEETING_ROOM),
            room("kalalau","Kalalau Room","tower1","t1_f4",SpaceType.MEETING_ROOM),

            room("kalalau2","Kalalua Room","tower2","t2_f1",SpaceType.MEETING_ROOM),
            room("carnation","Carnation Room","tower2","t2_f2",SpaceType.MEETING_ROOM),
            room("himachal","Himachal Room","tower2","t2_f3",SpaceType.MEETING_ROOM),


            // WORKSTATIONS
            room("daas","Daas Room","tower1","t1_f4",SpaceType.WORKSTATION),
            room("pcaas","PCaaS Room","tower1","t1_f5",SpaceType.WORKSTATION),
            room("cloudcrucible","Cloud Crucible Room","tower1","t1_f6",SpaceType.WORKSTATION),

            room("flowmet","FlowMet Room","tower2","t2_f4",SpaceType.WORKSTATION),
            room("canvascloud","Canvas Cloud Experience Room","tower2","t2_f5",SpaceType.WORKSTATION),
            room("mosaic","Mosaic Room","tower2","t2_f6",SpaceType.WORKSTATION),


            // INTERVIEW ROOMS
            room("horizon","Horizon Room","tower1","t1_f4",SpaceType.INTERVIEW_ROOM),
            room("orbit","Orbit Room","tower1","t1_f5",SpaceType.INTERVIEW_ROOM),
            room("zenith","Zenith Room","tower1","t1_f6",SpaceType.INTERVIEW_ROOM),

            room("summit","Summit Room","tower2","t2_f4",SpaceType.INTERVIEW_ROOM),
            room("pinnacle","Pinnacle Room","tower2","t2_f5",SpaceType.INTERVIEW_ROOM),
            room("apex","Apex Room","tower2","t2_f6",SpaceType.INTERVIEW_ROOM),

            //WASHROOMS
            room("washroom_t1_f2","Washroom Tower1 Floor2","tower1","t1_f2",SpaceType.WASHROOM)
        )

        rooms.forEach {
            db.collection("rooms").document(it["id"] as String).set(it)
        }


        // ---------- ISSUE CATEGORIES ----------
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


        // ---------- ISSUE TYPES ----------
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


    // Helper function
    private fun room(
        id:String,
        name:String,
        tower:String,
        floor:String,
        type:SpaceType
    ): Map<String,Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "towerId" to tower,
            "floorId" to floor,
            "type" to type.firestoreValue
        )
    }
}