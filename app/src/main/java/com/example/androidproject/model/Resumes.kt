package com.example.androidproject.model

data class ResumeResponse (
    val resumes : ArrayList<resumesItem>
)


data class resumesItem(
    val academic_background: AcademicBackground,
    val created_at: String,
    val email: String,
    val id: Int,
    val prefered_work_location: List<String>,
    val profile_pic: String,
    val specialties: List<String>,
    val tradesman_full_name: String,
    val updated_at: String,
    val user_id: Int,
    val work_fee: Int
)


data class AcademicBackground(
    val Decription: String,
    val Field_of_study: String,
    val School: String
)