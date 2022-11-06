package com.devpath.constants

class Constants {
    companion object {
        const val WELCOME = "Welcome to DevPath"

        // User
        const val USER_ALREADY_EXISTS = "There is already an user with the given email: "
        const val USER_NOT_FOUND_EMAIL = "No user was found with the given email: "
        const val USER_NOT_FOUND_ID = "No user was found with the given id: "
        const val USER_DELETED = "User deleted successfully"
        const val USER_IS_NOT_A_MENTOR = "This user is not a mentor"

        // Trail
        const val TRAIL_ALREADY_EXISTS = "There is already a trail with the given name: "
        const val TRAIL_NOT_FOUND = "No trail was found with the given id: "
        const val TRAIL_LIST_IS_EMPTY = "No trails have been created yet"
        const val TRAIL_DELETED = "Trail deleted successfully"

        // Topic
        const val TOPIC_NOT_FOUND = "No topic was found with the given id: "

        // Sub Topic
        const val SUB_TOPIC_NOT_FOUND = "No sub topic was found with the given id: "

        // Mentor
        const val MENTOR_DEFAULT_DESCRIPTION = "I am a new Dev Path mentor!"
        const val MENTOR_DEFAULT_HOUR_COST = 0
        const val MENTOR_DELETED = "This user is no longer a mentor"
        const val MENTOR_LIST_IS_EMPTY = "There are no mentors yet"
    }
}
