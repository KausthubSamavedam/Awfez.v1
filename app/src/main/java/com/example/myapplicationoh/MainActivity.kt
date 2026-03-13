package com.example.myapplicationoh

import android.os.Bundle
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
//alex.johnson@company.com
//admin@company.com
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirestoreSeeder.seedIfEmpty()
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