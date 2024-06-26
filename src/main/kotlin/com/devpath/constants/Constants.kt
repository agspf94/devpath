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
        const val USER_DOES_NOT_HAVE_TRAIL = "This user doesn't have any trails with the given id: "

        // Mentor
        const val MENTOR_DEFAULT_ROLE = "I am a new Dev Path mentor!"
        const val MENTOR_DEFAULT_YEARS_OF_EXPERIENCE = 0
        const val MENTOR_DEFAULT_HOUR_COST = 0
        const val USER_IS_NOT_A_MENTOR = "This user is not a mentor"
        const val MENTOR_LIST_IS_EMPTY = "There are no mentors yet"
        const val MENTOR_DELETED = "This user is no longer a mentor"

        const val MENTOR_STATUS_ACTIVE = "ACTIVE"
        const val MENTOR_STATUS_PENDING = "PENDING"
        const val MENTOR_STATUS_INACTIVE = "INACTIVE"

        const val USER_DIDNT_REQUEST_TO_BECOME_A_MENTOR = "The given user didn't request to become a mentor"
        const val MENTOR_AND_USER_ARE_THE_SAME = "A mentor cannot schedule a date with himself"
        const val NO_MENTORS_WERE_FOUND = "No mentors were found"

        // Schedule
        const val SCHEDULE_AVAILABLE = "AVAILABLE"
        const val SCHEDULE_PENDING = "PENDING"
        const val SCHEDULE_RESERVED = "RESERVED"
        const val SCHEDULE_CANCELLED = "CANCELLED"

        const val SCHEDULE_NOT_FOUND = "No schedule was found with the given id: "
        const val SCHEDULE_NOT_AVAILABLE = "This schedule is not available to be reserved"
        const val SCHEDULE_NOT_PENDING = "This schedule is cannot be approved because it is not pending"
        const val CAN_ONLY_CANCEL_SCHEDULE_AVAILABLE = "Only available schedules can be cancelled"

        // Trail
        const val TRAIL_ALREADY_EXISTS = "There is already a trail with the given name: "
        const val TRAIL_NOT_FOUND = "No trail was found with the given id: "
        const val TRAIL_LIST_IS_EMPTY = "No trails have been created yet"
        const val TRAIL_DELETED = "Trail deleted successfully"
        const val EMPTY_WORDS_LIST = "The given list of words is empty"
        const val NO_TRAILS_WERE_FOUND = "No trails were found"

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
