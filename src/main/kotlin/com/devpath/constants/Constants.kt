package com.devpath.constants

class Constants {
    companion object {
        const val WELCOME = "Welcome to DevPath"

        // User
        const val USER_ALREADY_EXISTS = "There is already an user with the given email: "
        const val USER_NOT_FOUND_EMAIL = "No user was found with the given email: "
        const val USER_NOT_FOUND_ID = "No user was found with the given id: "
        const val USER_DELETED = "User deleted successfully"

        // Trail
        const val TRAIL_ALREADY_EXISTS = "There is already a trail with the given name: "
        const val TRAIL_NOT_FOUND = "No trail was found with the given id: "
        const val TRAIL_LIST_IS_EMPTY = "No trails have been created yet"
        const val TRAIL_DELETED = "Trail deleted successfully"
    }
}
