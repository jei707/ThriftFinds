package com.example.thrifters.service;

import com.example.thrifters.model.User;

public class UserSession {
    private static String loggedInUsername;  // Store the logged-in username
    
    // Method to set the logged-in username
    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }
    
    // Method to get the logged-in username
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getUserId();
        } else {
            throw new IllegalStateException("No user is logged in.");
        }
    }

    public static void clearSession() {
        currentUser = null;
    }
}


