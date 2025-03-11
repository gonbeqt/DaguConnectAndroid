package com.example.androidproject.view.tradesman

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidproject.view.theme.myGradient3
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.viewmodel.Resumes.SubmitResumeViewModel


@Composable
fun ProfileVerification(
    modifier: Modifier = Modifier,
    navController: NavController,
    submitResumeViewModel: SubmitResumeViewModel,
    statusofapproval: String
) {
    val submitResumeState by submitResumeViewModel.submitResumeState.collectAsState()

    // Initialize currentStep and progressPercentage based on statusofapproval
    val initialStep = if (statusofapproval == "Pending") 5 else 1
    val initialProgress = if (statusofapproval == "Pending") 100 else 0

    var currentStep by remember { mutableStateOf(initialStep) }
    var progressPercentage by remember { mutableStateOf(initialProgress) }

    val context = LocalContext.current

    // State variables for fields
    var estimatedRate by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var aboutMe by remember { mutableStateOf("") }
    var selectedJob by remember { mutableStateOf("Select job category") }
    var selectedLocation by remember { mutableStateOf("Select location") }
    var frontIdUri by remember { mutableStateOf<Uri?>(null) }
    var backIdUri by remember { mutableStateOf<Uri?>(null) }
    var tradeCredentialUri by remember { mutableStateOf<Uri?>(null) }

    val isTyping = estimatedRate.isNotEmpty()

    // Validation function
    fun validateStep(step: Int): Boolean {
        return when (step) {
            1 -> {
                selectedJob != "Select job category" &&
                        selectedLocation != "Select location" &&
                        estimatedRate.isNotEmpty() &&
                        phoneNumber.isNotEmpty()
            }
            2 -> aboutMe.isNotEmpty()
            3 -> frontIdUri != null && backIdUri != null && tradeCredentialUri != null
            4 -> true // Preview step, no additional validation needed
            else -> true
        }
    }

    // Function to show error message
    fun showErrorMessage() {
        val message = when (currentStep) {
            1 -> "Please fill in Job Title, Preferred Location, Estimated Rate, and Phone Number."
            2 -> "Please provide details in the About Me section."
            3 -> "Please upload Front ID, Back ID, and Trade Credential."
            else -> "Please complete all required fields."
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradient3)
            .padding(WindowInsets.systemBars.asPaddingValues())

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        if (currentStep > 1) {
                            if (currentStep == 5) {
                                navController.navigate("main_screen")
                                currentStep++
                                progressPercentage += 25
                            }
                            currentStep--
                            progressPercentage -= 25
                        } else if (currentStep == 1) {
                            navController.navigate("main_screen")
                        }
                    },
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow Back",
                tint = Color.White
            )
            Text(text = "Verify Profile", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "$progressPercentage%",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Complete",
                    color = Color(0xFFC1C1C1),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = when (currentStep) {
                        1 -> "Job Details"
                        2 -> "Personal Details"
                        3 -> "Credential and Verification"
                        4 -> "Preview and Submit"
                        else -> "Profile Verification"
                    },
                    color = Color(0xFFC1C1C1),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            StepProgressIndicator(currentStep = currentStep, totalSteps = 5)

            Card(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    if (currentStep == 1) { // First page
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 14.dp),
                            text = "Please provide your job details to continue.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "Job Title",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Box(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            JobSelectionDropdown(
                                label = "",
                                options = listOf(
                                    "Plumber", "Carpenter", "Mason", "Cleaner", "Electrician"
                                ),
                                selectedOption = selectedJob,
                                onOptionSelected = { selectedJob = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "Preferred Location",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Box(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            JobSelectionDropdown(
                                label = "",
                                options = listOf(
                                    "Agno", "Aguilar", "Alcala", "Anda", "Asingan", "Balungao", "Bani", "Basista", "Bautista",
                                    "Bayambang", "Binalonan", "Binmaley", "Bolinao", "Bugallon", "Burgos", "Calasiao",
                                    "Dagupan City", "Dasol", "Infanta", "Labrador", "Laoac", "Lingayen", "Mabini", "Malasiqui",
                                    "Manaoag", "Mangaldan", "Mangatarem", "Mapandan", "Natividad", "Pozorrubio", "Rosales",
                                    "San Fabian", "San Jacinto", "San Manuel", "San Nicolas", "San Quintin", "Santa Barbara",
                                    "Santa Maria", "Santo Tomas", "Sison", "Sual", "Tayug", "Umingan", "Urbiztondo",
                                    "Urdaneta City", "Villasis"
                                ),
                                selectedOption = selectedLocation,
                                onOptionSelected = { selectedLocation = "$it, Pangasinan" }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "Estimated rate:",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 14.dp, vertical = 18.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₱",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        style = TextStyle(fontSize = 14.sp),
                                        color = if (isTyping) Color.Black else Color.Gray
                                    )

                                    BasicTextField(
                                        value = estimatedRate,
                                        onValueChange = { newText ->
                                            estimatedRate = if (newText.startsWith("₱")) {
                                                newText.substring(1) // remove extra peso signs
                                            } else newText
                                        },
                                        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number),
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .fillMaxWidth(),
                                        decorationBox = { innerTextField ->
                                            if (estimatedRate.isEmpty()) {
                                                Text(text = "Enter Amount",
                                                    color = Color.Gray,
                                                    style = TextStyle(fontSize = 14.sp),
                                                    fontSize = 14.sp,
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "Phone Number:",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 14.dp, vertical = 18.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "+63",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        style = TextStyle(fontSize = 14.sp),
                                        color = if (isTyping) Color.Black else Color.Gray
                                    )

                                    BasicTextField(
                                        value = phoneNumber,
                                        onValueChange = { newText ->
                                            phoneNumber = if (newText.startsWith("+63")) {
                                                newText.substring(1) // remove extra +63
                                            } else newText
                                        },
                                        textStyle = TextStyle(
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .fillMaxWidth(),
                                        decorationBox = { innerTextField ->
                                            if (phoneNumber.isEmpty()) {
                                                Text(
                                                    text = "Enter Your Number",
                                                    color = Color.Gray,
                                                    style = TextStyle(fontSize = 14.sp),
                                                    fontSize = 14.sp,
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }
                            }
                        }
                    } else if (currentStep == 2) { // Second page
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 14.dp),
                            text = "Tell us about yourself.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "About Me",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 14.dp, end = 14.dp)
                                .fillMaxWidth()
                                .height(300.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            BasicTextField(
                                value = aboutMe,
                                onValueChange = { newText ->
                                    if (newText.length <= 1000) {
                                        aboutMe = newText
                                    } else {
                                        aboutMe = newText.substring(0, 1000)
                                        Toast.makeText(context, "Character count exceeds", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                textStyle = TextStyle(fontSize = 14.sp),
                                modifier = Modifier.fillMaxSize(),
                                decorationBox = { innerTextField ->
                                    if (aboutMe.isEmpty()) {
                                        Text(text = "Enter response here...",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "${aboutMe.length}/1000",
                                fontSize = 14.sp,
                                color = if (aboutMe.length >= 910) Color.Red else Color.Black
                            )
                        }
                    } else if (currentStep == 3) {
                        UploadDocumentsScreen(
                            context = context,
                            frontIdUri = frontIdUri,
                            backIdUri = backIdUri,
                            tradeCredentialUri = tradeCredentialUri,
                            onFrontIdSelected = { frontIdUri = it },
                            onBackIdSelected = { backIdUri = it },
                            onTradeCredentialSelected = { tradeCredentialUri = it }
                        )
                    } else if (currentStep == 4) { // Fourth page
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 14.dp),
                            text = "You're almost done! Kindly review your details and submit when ready.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            Text("Job: $selectedJob")
                            Text("Preferred Location: $selectedLocation")
                            Text("Estimated Rate: $estimatedRate")
                            Text("Phone Number: $phoneNumber")
                            Text("About Me: $aboutMe")

                            frontIdUri?.let {
                                Text(
                                    text = "Front ID: ${it.lastPathSegment}",
                                    color = Color.Blue,
                                    modifier = Modifier.clickable { openFile(context, it) }
                                )
                            } ?: Text("Front ID: No file uploaded")

                            backIdUri?.let {
                                Text(
                                    text = "Back ID: ${it.lastPathSegment}",
                                    color = Color.Blue,
                                    modifier = Modifier.clickable { openFile(context, it) }
                                )
                            } ?: Text("Back ID: No file uploaded")

                            tradeCredentialUri?.let {
                                Text(
                                    text = "Trade Credential: ${it.lastPathSegment}",
                                    color = Color.Blue,
                                    modifier = Modifier.clickable { openFile(context, it) }
                                )
                            } ?: Text("Trade Credential: No file uploaded")
                        }
                    } else if (currentStep == 5) { // Fifth page
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pendingapproval_ic),
                                contentDescription = "Pending Approval",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Pending Approval",
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                Text(
                                    text = "Profile submitted! Please wait for confirmation to complete verification. This may take a few minutes.",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Column(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(start = 14.dp, end = 14.dp, bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF42C2AE), shape = RoundedCornerShape(6.dp))
                                .clickable {
                                    if (validateStep(currentStep)) {
                                        when (currentStep) {
                                            1 -> {
                                                currentStep = 2
                                                progressPercentage = 25
                                            }
                                            2 -> {
                                                currentStep = 3
                                                progressPercentage = 50
                                            }
                                            3 -> {
                                                currentStep = 4
                                                progressPercentage = 75
                                            }
                                            4 -> {
                                                val workFee = estimatedRate.toIntOrNull() ?: 0 // Safe conversion
                                                submitResumeViewModel.submitResume(
                                                    specialty = selectedJob,
                                                    aboutme = aboutMe,
                                                    workfee = workFee,
                                                    preferedworklocation = selectedLocation,
                                                    valididfront = frontIdUri!!,
                                                    valididback = backIdUri!!,
                                                    documents = tradeCredentialUri!!, // Ensure these are not null due to validation
                                                    context = context
                                                )
                                                currentStep = 5
                                                progressPercentage = 100
                                            }
                                        }
                                    } else {
                                        showErrorMessage()
                                    }
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (progressPercentage) {
                                    75 -> "Submit"
                                    100 -> "Done"
                                    else -> "Next"
                                },
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        LaunchedEffect(submitResumeState) {
                            when(val submitresume = submitResumeState){
                                is SubmitResumeViewModel.SubmitResumeState.Loading -> {
                                    // nothing
                                }
                                is SubmitResumeViewModel.SubmitResumeState.Success -> {
                                    Toast.makeText(context,"Resume submitted successfully", Toast.LENGTH_SHORT).show()
                                    submitResumeViewModel.resetState()
                                }
                                is SubmitResumeViewModel.SubmitResumeState.Error -> {
                                    val error = submitresume.message
                                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
                                    Log.d("testerrerro", error)
                                    submitResumeViewModel.resetState()
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun StepProgressIndicator(currentStep: Int, totalSteps: Int = 5) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp), // horizontal padding for better spacing
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f) // ensures equal distribution of width
                    .height(8.dp)
                    .background(
                        color = if (i <= currentStep) Color(0xFF6FCF97) else Color.White,
                        shape = RoundedCornerShape(50)
                    )
                    .border(
                        width = 1.dp,
                        color = if (i <= currentStep) Color(0xFF6FCF97) else Color.White,
                        shape = RoundedCornerShape(50)
                    )
            )
            if (i != totalSteps) {
                Spacer(modifier = Modifier.width(4.dp)) //spacing between steps
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSelectionDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()

) {
    var expanded by remember { mutableStateOf(false) }

    val isPlaceholder = selectedOption == "Select job category" || selectedOption == "Select location" || selectedOption == "Select type of valid ID" || selectedOption == "Select type of document"
    val textColor = if (isPlaceholder) Color.Gray else Color.Black  // text gray when unselected

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },  // keep label gray
            readOnly = true,
            textStyle = TextStyle(color = textColor),  // apply correct text color
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .clickable { expanded = true }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.Gray,
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(310.dp).background(Color.White)//width and bg color of the dropdown
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun UploadDocumentsScreen(
    context: Context,
    frontIdUri: Uri?,
    backIdUri: Uri?,
    tradeCredentialUri: Uri?,
    onFrontIdSelected: (Uri) -> Unit,
    onBackIdSelected: (Uri) -> Unit,
    onTradeCredentialSelected: (Uri) -> Unit
) {
    var selectedID by remember { mutableStateOf("") }
    var selectedDocument by remember { mutableStateOf("") }

    val frontIdPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onFrontIdSelected(it) } }

    val backIdPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onBackIdSelected(it) } }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onTradeCredentialSelected(it) } }

    Column(modifier = Modifier.padding(14.dp)) {
        Text(
            text = "Please upload a valid ID and certifications to verify your legitimacy as a tradesman.",
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Text(text = "Valid ID", fontSize = 14.sp, color = Color.DarkGray)
        JobSelectionDropdown(
            label = "",
            options = listOf(
                "Passport", "Driver’s License", "National ID (PhilSys ID)",
                "SSS ID", "GSIS ID", "PRC ID", "UMID", "Postal ID",
                "Voter’s ID", "TIN ID", "Senior Citizen ID", "PWD ID",
                "Barangay Clearance with photo", "IP ID", "Other Valid ID with photo"
            ),
            selectedOption = selectedID,
            onOptionSelected = { selectedID = it }
        )

        UploadField(
            label = "Front ID",
            uri = frontIdUri,
            onUploadClick = {
                if (selectedID.isEmpty()) {
                    Toast.makeText(context, "Please choose a type of ID first", Toast.LENGTH_SHORT).show()
                } else {
                    frontIdPickerLauncher.launch("image/*")
                }
            }
        ) {
            frontIdUri?.let { openFile(context, it) }
        }

        UploadField(
            label = "Back ID",
            uri = backIdUri,
            onUploadClick = {
                if (selectedID.isEmpty()) {
                    Toast.makeText(context, "Please choose a type of ID first", Toast.LENGTH_SHORT).show()
                } else {
                    backIdPickerLauncher.launch("image/*")
                }
            }
        ) {
            backIdUri?.let { openFile(context, it) }
        }

        Text(text = "Trade Credential", fontSize = 14.sp, color = Color.DarkGray)
        JobSelectionDropdown(
            label = "",
            options = listOf(
                "Trade Certification", "License/Registration from a Trade Authority",
                "Apprenticeship Completion Certificate", "Union Membership Card",
                "Training Course Certificate", "Contractor/Business License",
                "Work Experience Letter from Employers", "Professional Association Membership",
                "Compliance Certifications", "Government-Issued Skill Assessment",
                "Other Valid Document with Photo"
            ),
            selectedOption = selectedDocument,
            onOptionSelected = { selectedDocument = it }
        )

        UploadField(
            label = "Trade Credential",
            uri = tradeCredentialUri,
            fileType = "pdf",
            onUploadClick = {
                if (selectedDocument.isEmpty()) {
                    Toast.makeText(context, "Please choose a type of credential first", Toast.LENGTH_SHORT).show()
                } else {
                    pdfPickerLauncher.launch("application/pdf")
                }
            }
        ) {
            tradeCredentialUri?.let { openFile(context, it) }
        }
    }
}


@Composable
fun UploadField(label: String, uri: Uri?, fileType: String = "image", onUploadClick: () -> Unit, onViewClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 5.dp).padding(top = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier.clickable { if (uri != null) onViewClick() }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ${uri?.lastPathSegment ?: "No file uploaded"}",
                fontSize = 14.sp,
                color = if (uri != null) Color.Blue else Color.Gray
            )
            if (uri != null) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "View File",
                    tint = Color.Blue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Box(
            modifier = Modifier.clickable { onUploadClick() }
                .background(Color(0xFF3E5CE1), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.upload_ic),
                    contentDescription = "Add File",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add File", fontSize = 12.sp, color = Color.White)
            }
        }
    }
    Text(
        text = "Accepted file types: ${if (fileType == "image") ".jpg, .jpeg, .png" else ".pdf"}",
        fontStyle = FontStyle.Italic,
        fontSize = 10.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 18.dp)
    )
}

fun openFile(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, context.contentResolver.getType(uri))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(intent)
        Log.d("openFile", "Attempting to open URI: $uri")

    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No app available to open this file", Toast.LENGTH_SHORT).show()
    }
}