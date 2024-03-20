package org.pinsoft.friendapp.utils.constants;

public final class ResponseMessageConstants {

    private ResponseMessageConstants(){}

    // Error Messages
    public static final String SERVER_ERROR_MESSAGE = "Server Error";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found";
    public static final String UNAUTHORIZED_SERVER_ERROR_MESSAGE = "Unauthorized!";
    public static final String VALIDATION_ERROR_MESSAGE = "Validation error.";

    // User Error Messages
    public static final String MESSAGE_SAVE_FAILURE_MESSAGE = "MEssage cannot be saved in the database!";
    public static final String INVALID_MESSAGE_FORMAT = "Failure validating message!";
    public static final String MESSAGE_FROM_USER_INVALID = "Invalid from user!";
    public static final String USER_SERVICE_MODEL_INVALID = "Invalid userServiceModel!";
    public static final String USER_ENTITY_MODEL_INVALID = "Invalid user Entity!";
    public static final String MESSAGE_TO_USER_INVALID = "Invalid recipient!";
    public static final String RELATIONSHIP_INVALID_MESSAGE = "You are not friend, can not send message!";
    public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User can't found";
    public static final String USER_CAN_NOT_SAVED = "User can't saved";

    // User Successful Response Messages
    public static final String SUCCESSFUL_REGISTER_MESSAGE = "You have been successfully registered.";
    public static final String SUCCESSFUL_USER_PROFILE_EDIT_MESSAGE = "User Profile have been successfully edited.";
    public static final String SUCCESSFUL_USER_DELETE_MESSAGE = "User have been successfully deleted.";


    // Relationship Messages
    public static final String SUCCESSFUL_FRIEND_REQUEST_SUBMISSION_MESSAGE = "Your friend request have been successfully submitted!";
    public static final String SUCCESSFUL_FRIEND_REMOVE_MESSAGE = "User was removed from your friends list!";
    public static final String SUCCESSFUL_ADDED_FRIEND_MESSAGE = "User was added successfully to your friends list!";
    public static final String SUCCESSFUL_REJECT_FRIEND_REQUEST_MESSAGE = "Request was successfully rejected!";

    // Log Messages
    public static final String SUCCESSFUL_LOGS_DELETING_MESSAGE = "Logs have been successfully deleted.";
    public static final String SUCCESSFUL_USER_LOGS_DELETING_MESSAGE = "User logs have been successfully deleted.";

    public static final String FAILURE_LOGS_SAVING_MESSAGE = "Failure log saving!";
    public static final String FAILURE_LOGS_NOT_FOUND_MESSAGE = "No logs available for selected username.";
    public static final String FAILURE_LOGS_CLEARING_ERROR_MESSAGE = "Logs clearing error.";

    // Message Messages
    public static final String SUCCESSFUL_CREATE_MESSAGE_MESSAGE = "Message created successfully.";

    // Password Messages
    public static final String PASSWORDS_MISMATCH_ERROR_MESSAGE = "Passwords do not match.";

}
