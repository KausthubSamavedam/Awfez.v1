package com.example.myapplicationoh.repository

import com.example.myapplicationoh.model.Issue
import com.google.firebase.firestore.FirebaseFirestore
object FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    fun addIssue(issue: Issue) {
        db.collection("issues")
            .add(issue)
    }
    fun getIssues(onResult: (List<Issue>) -> Unit) {
        db.collection("issues")
            .addSnapshotListener { snapshot, _ ->
                val issues = snapshot?.documents?.mapNotNull {
                    it.toObject(Issue::class.java)
                } ?: emptyList()
                onResult(issues)
            }
    }
}