package com.devpath.constants

class Constants {
    companion object {
        // User
        const val USER_ALREADY_EXISTS = "There is already an user with the given email: "
        const val USER_WRONG_PASSWORD = "The given password is wrong"
        const val USER_NOT_FOUND_EMAIL = "No user was found with the given email: "
        const val USER_NOT_FOUND_ID = "No user was found with the given id: "
        const val USER_DELETED = "User deleted successfully"
        const val USER_TRAIL_DELETED = "User trail deleted successfully"

        // Mentor
        const val MENTOR_DEFAULT_ROLE = "I am a new Dev Path mentor!"
        const val MENTOR_DEFAULT_YEARS_OF_EXPERIENCE = 0
        const val MENTOR_DEFAULT_HOUR_COST = 0
        const val USER_IS_NOT_A_MENTOR = "This user is not a mentor"
        const val MENTOR_LIST_IS_EMPTY = "There are no mentors yet"
        const val MENTOR_DELETED = "This user is no longer a mentor"

        // Trail
        const val TRAIL_ALREADY_EXISTS = "There is already a trail with the given name: "
        const val TRAIL_NOT_FOUND = "No trail was found with the given id: "
        const val TRAIL_LIST_IS_EMPTY = "No trails have been created yet"
        const val TRAIL_DELETED = "Trail deleted successfully"

        // Topic
        const val TOPIC_ALREADY_EXISTS = "There is already a topic with the given name: "
        const val TOPIC_NOT_FOUND_ID = "No topic was found with the given id: "
        const val TOPIC_NOT_FOUND_NAME = "No topic was found with the given name: "
        const val TOPIC_LIST_IS_EMPTY = "No topics have been created yet"
        const val TOPIC_DELETED = "Topic deleted successfully"

        // Sub Topic
        const val SUB_TOPIC_ALREADY_EXISTS = "There is already a sub topic with the given name: "
        const val SUB_TOPIC_NOT_FOUND = "No sub topic was found with the given id: "
        const val SUB_TOPIC_LIST_IS_EMPTY = "No sub topics have been created yet"
        const val SUB_TOPIC_DELETED = "Sub Topic deleted successfully"

        // Job
        const val JOB_ALREADY_EXISTS = "There is already a job with the given title: "
        const val JOB_NOT_FOUND = "No job was found with the given id: "
        const val JOB_LIST_IS_EMPTY = "No jobs have been created yet"
        const val JOB_DELETED = "Job deleted successfully"
    }
}
