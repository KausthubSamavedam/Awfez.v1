package com.example.myapplicationoh.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplicationoh.screens.AdminDashboardScreen
import com.example.myapplicationoh.screens.AdminIssueDetailScreen
import com.example.myapplicationoh.screens.AdminLoginScreen
import com.example.myapplicationoh.screens.BookSpaceScreen
import com.example.myapplicationoh.screens.BookingConfirmedScreen
import com.example.myapplicationoh.screens.BookingFailedScreen
import com.example.myapplicationoh.screens.HomeScreen
import com.example.myapplicationoh.screens.IssueReportedScreen
import com.example.myapplicationoh.screens.LoginScreen
import com.example.myapplicationoh.screens.MyBookingsScreen
import com.example.myapplicationoh.screens.MyIssuesScreen
import com.example.myapplicationoh.screens.ReportIssueScreen
import com.example.myapplicationoh.viewmodel.AdminViewModel
import com.example.myapplicationoh.viewmodel.AuthViewModel
import com.example.myapplicationoh.viewmodel.BookingViewModel
import com.example.myapplicationoh.viewmodel.IssueViewModel


@Composable
fun OfficeHubNavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val bookingViewModel: BookingViewModel = viewModel()
    val issueViewModel: IssueViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onAdminPortal = { navController.navigate("admin_login") }
            )
        }

        composable("admin_login") {
            AdminLoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("admin_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginAsUser = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },

            )
        }

        composable("home") {
            HomeScreen(
                viewModel = authViewModel,
                bookingViewModel = bookingViewModel,
                onBookSpace = { navController.navigate("book_space") },
                onReportIssue = { navController.navigate("report_issue") },
                onMyBookings = { navController.navigate("my_bookings") },
                onMyIssues = { navController.navigate("my_issues") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }

            )
        }

        composable("book_space") {
            BookSpaceScreen(
                viewModel = bookingViewModel,
                onBack = { navController.popBackStack() },
                onBookingConfirmed = { ref -> navController.navigate("booking_confirmed/$ref") },
                onBookingFailed = { navController.navigate("booking_failed") }
            )
        }

        composable(
            route = "booking_confirmed/{bookingRef}",
            arguments = listOf(navArgument("bookingRef") { type = NavType.StringType })
        ) { backStackEntry ->
            val ref = backStackEntry.arguments?.getString("bookingRef") ?: ""
            BookingConfirmedScreen(
                bookingRef = ref,
                viewModel = bookingViewModel,
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onViewBookings = { navController.navigate("my_bookings") }
            )
        }

        composable("booking_failed") {
            BookingFailedScreen(
                onTryAnotherSlot = { navController.popBackStack("book_space", false) },
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("my_bookings") {
            MyBookingsScreen(
                viewModel = bookingViewModel,
                onHome = { navController.navigate("home") },
                onMyIssues = { navController.navigate("my_issues") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("my_issues") {
            MyIssuesScreen(
                viewModel = issueViewModel,
                onBack = { navController.popBackStack() },
                onHome = { navController.navigate("home") },
                onMyBookings = { navController.navigate("my_bookings") },
                onAddIssue = { navController.navigate("report_issue") }
            )
        }

        composable("report_issue") {
            ReportIssueScreen(
                viewModel = issueViewModel,
                onBack = { navController.popBackStack() },
                onIssueSubmitted = { ref -> navController.navigate("issue_reported/$ref") }
            )
        }

        composable(
            route = "issue_reported/{issueRef}",
            arguments = listOf(navArgument("issueRef") { type = NavType.StringType })
        ) { backStackEntry ->
            val ref = backStackEntry.arguments?.getString("issueRef") ?: ""
            IssueReportedScreen(
                issueRef = ref,
                viewModel = issueViewModel,
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("admin_dashboard") {
            AdminDashboardScreen(
                viewModel = adminViewModel,
                onIssueClick = { issueId -> navController.navigate("admin_issue_detail/$issueId") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(
            route = "admin_issue_detail/{issueId}",
            arguments = listOf(navArgument("issueId") { type = NavType.StringType })
        ) { backStackEntry ->
            val issueId = backStackEntry.arguments?.getString("issueId") ?: ""
            AdminIssueDetailScreen(
                issueId = issueId,
                viewModel = adminViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
