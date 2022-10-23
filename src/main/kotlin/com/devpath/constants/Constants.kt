package com.devpath.constants

class Constants {
    companion object {
        const val WELCOME = "Welcome to DevPath"

        // User
        const val USER_ALREADY_EXISTS = "There is already an user with the given email: "
        const val USER_NOT_FOUND_EMAIL = "No user was found with the given email: "
        const val USER_NOT_FOUND_ID = "No user was found with the given id: "
        const val USER_DELETED = "User deleted successfully"

        const val TRAIL_ALREADY_EXISTS = "This trail already exists: "
        const val TOPIC_ALREADY_EXISTS = "This topic already exists: "
        const val SUB_TOPIC_ALREADY_EXISTS = "This sub topic already exists: "

        const val TRAIL_NOT_FOUND = "This trail was not found: "
        const val TOPIC_NOT_FOUND = "This topic was not found: "
        const val SUB_TOPIC_NOT_FOUND = "This sub topic was not found: "

        const val DELETE_MESSAGE = "Trail deleted successfully"
        const val EMPTY_LIST = "No trails have been created yet"
    }
}
