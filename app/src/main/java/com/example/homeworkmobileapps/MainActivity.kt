package com.example.homeworkmobileapps

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplashScreen by remember { mutableStateOf(true) } // shows splash screen initially
            var showRegistration by remember { mutableStateOf(false) } // tracks if registration page should be shown
            var isLoggedIn by remember { mutableStateOf(false) } // tracks if user is logged in

            if (showSplashScreen) {
                SplashScreen {
                    showSplashScreen = false // after timeout, splash screen is hidden
                }
            } else {
                when {
                    showRegistration -> {
                        RegistrationPage(onBackToLogin = { showRegistration = false }) // show registration page when showRegistration is true
                    }
                    isLoggedIn -> {
                        MainPage(onBackToLogin = { isLoggedIn = false }) // show main page if user is logged in
                    }
                    else -> {
                        LoginPage(
                            onRegisterClick = { showRegistration = true }, // navigate to registration page when register is clicked
                            onLoginSuccess = { isLoggedIn = true } // login success leads to main page
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), // fills the entire screen
        contentAlignment = Alignment.Center // centers the content
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.cat), // displays an image from resources
                contentDescription = "Splash Screen Logo", // description for accessibility
                modifier = Modifier
                    .height(150.dp) // set height of the image
                    .width(150.dp) // set width of the image
            )
            Spacer(modifier = Modifier.height(16.dp)) // spacer to add space between the image and text
            Text(
                text = "Welcome to my app!", // splash screen message
                fontSize = 24.sp, // text size
                textAlign = TextAlign.Center // center the text horizontally
            )
        }
    }
    LaunchedEffect(Unit) {
        delay(3000) // splash screen shows for 3 seconds before timing out
        onTimeout() // after timeout, navigate to the next screen
    }
}

@Composable
fun LoginPage(onRegisterClick: () -> Unit, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") } // stores username input
    var password by remember { mutableStateOf("") } // stores password input
    val areFieldsValid = nameIsValid(username) && nameIsValid(password) // checks if fields are valid for enabling login button

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center) // centers content within the box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp) // adds padding around the column
        ) {
            TextField(
                value = username,
                onValueChange = { username = it }, // updates username as user types
                label = { Text("Username") }, // field label
                modifier = Modifier.fillMaxWidth(), // field fills the entire width
            )
            Spacer(modifier = Modifier.height(16.dp)) // adds space between the fields
            TextField(
                value = password,
                onValueChange = { password = it }, // updates password as user types
                label = { Text("Password") }, // field label
                visualTransformation = PasswordVisualTransformation(), // hides password input
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // sets keyboard type to password
                modifier = Modifier.fillMaxWidth(), // field fills the entire width
            )
            Spacer(modifier = Modifier.height(24.dp)) // adds space before buttons
            Button(
                onClick = { onLoginSuccess() }, // triggers successful login
                enabled = areFieldsValid, // button is enabled only if fields are valid
                modifier = Modifier.fillMaxWidth() // button fills the entire width
            ) {
                Text("Login") // button text
            }
            Spacer(modifier = Modifier.height(16.dp)) // adds space between buttons
            Button(
                onClick = onRegisterClick, // triggers registration page navigation
                modifier = Modifier.fillMaxWidth() // button fills the entire width
            ) {
                Text("Register") // button text
            }
        }
    }
}

fun nameIsValid(name: String): Boolean {
    if (name.length >= 3 && name.length <= 30) { // checks if name length is between 3 and 30
        return true
    }
    return false
}

fun isValidDob(input: String): Boolean {
    val regex = Regex("""^\d{2}/\d{2}/\d{4}$""") // regex for validating date of birth format
    return regex.matches(input) // returns true if input matches the format
}

@Composable
fun RegistrationPage(onBackToLogin: () -> Unit) {
    var firstName by remember { mutableStateOf("") } // stores first name input
    var lastName by remember { mutableStateOf("") } // stores last name input
    var dateOfBirth by remember { mutableStateOf("") } // stores date of birth input
    var email by remember { mutableStateOf("") } // stores email input
    var password by remember { mutableStateOf("") } // stores password input

    // checks if all fields follow requirements
    val isFormValid =
        isValidDob(dateOfBirth) && // checks if date of birth is valid
                nameIsValid(password) && // checks if password is valid
                Patterns.EMAIL_ADDRESS.matcher(email).matches() && // ensures email field is valid
                nameIsValid(firstName) && // checks if first name is valid
                nameIsValid(lastName) // checks if last name is valid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center) // centers content within the box
    ) {
        Column( // column that houses various fields
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp) // adds padding around the column
        ) {
            TextField( // first name field
                value = firstName,
                onValueChange = { firstName = it }, // updates first name as user types
                label = { Text("First Name") }, // field label
                modifier = Modifier.fillMaxWidth(), // field fills the entire width
            )
            Spacer(modifier = Modifier.height(16.dp)) // adds space between fields
            TextField( // last name field
                value = lastName,
                onValueChange = { lastName = it }, // updates last name as user types
                label = { Text("Last Name") }, // field label
                modifier = Modifier.fillMaxWidth(), // field fills the entire width
            )
            Spacer(modifier = Modifier.height(16.dp)) // adds space between fields
            TextField( // dob field
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it }, // updates date of birth as user types
                label = { Text("Date of Birth (mm/dd/yyyy)") }, // field label
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // sets keyboard type
                modifier = Modifier.fillMaxWidth(), // field fills the entire width
            )
            Spacer(modifier = Modifier.height(16.dp)) // adds space between fields
            TextField( // email field
                value = email,
                onValueChange = { email = it }, // updates email as user types
                label = { Text("Email") }, // field label
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // sets keyboard type to email
                modifier = Modifier.fillMaxWidth(), // field fills the entire width
            )
            Spacer(modifier = Modifier.height(16.dp)) // adds space between fields
            TextField( // password field
                value = password,
                onValueChange = { password = it }, // updates password as user types
                label = { Text("Password") }, // field label
                visualTransformation = PasswordVisualTransformation(), // hides password input
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // sets keyboard type to password
                modifier = Modifier.fillMaxWidth(), // field fills the entire width
            )
            Spacer(modifier = Modifier.height(24.dp)) // adds space before register button
            Button( // back button to login page
                onClick = onBackToLogin, // clicking the button brings you back
                enabled = isFormValid, // button is disabled if form is not valid
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
            Button(
                onClick = onBackToLogin, // triggers the callback function to go back to the login page
                modifier = Modifier.fillMaxWidth() // button fills the entire width of the screen
            ){
                Text("Back to login")
            }
        }
    }
}
@Composable
fun MainPage(onBackToLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize() // fills the entire screen
            .wrapContentSize(Alignment.Center) // centers the content within the box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // aligns all children horizontally to the center
            verticalArrangement = Arrangement.Center, // aligns all children vertically to the center
            modifier = Modifier.padding(16.dp) // adds padding around the column
        ) {
            Text(
                text = "This is the main page", // displays the text message
                fontSize = 24.sp, // sets the size of the text
                textAlign = TextAlign.Center // centers the text horizontally
            )
            Spacer(modifier = Modifier.height(16.dp)) // adds space between the text and the button
            Button(
                onClick = onBackToLogin, // triggers the callback function to go back to the login page
                modifier = Modifier.fillMaxWidth() // button fills the entire width of the screen
            ){
                Text("Back to login")
            }
        }
    }
}