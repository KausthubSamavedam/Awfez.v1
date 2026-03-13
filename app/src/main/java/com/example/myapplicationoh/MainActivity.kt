package com.example.myapplicationoh

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationoh.navigation.OfficeHubNavGraph
import com.example.myapplicationoh.repository.FirestoreSeeder
import com.example.myapplicationoh.ui.theme.OfficeHubTheme
import com.example.myapplicationoh.utils.NotificationHelper
import com.google.firebase.messaging.FirebaseMessaging

//alex.johnson@company.com
//admin@company.com
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful){
                val token = task.result
                Log.d("FCM Token",token)
            }

        }
        FirestoreSeeder.seedIfEmpty()
        NotificationHelper.createChannel(this)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
            )
        }
        setContent {
            OfficeHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    OfficeHubNavGraph(navController = navController)
                }
            }
        }
    }
}