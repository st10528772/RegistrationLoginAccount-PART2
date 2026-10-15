/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registrationloginaccount;

import java.util.regex.Pattern;

public class Login {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String cellPhoneNumber;
    private boolean loginSuccessful;

    public Login(String userName, String password, String cellPhoneNumber, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginSuccessful = false;
    }

    // Static validation helpers
    public static boolean checkUserName(String userName) {
        if (userName == null) {
            return false;
        }
        return userName.contains("_") && userName.length() <= 5;
    }

    public static boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapitalLetter = true;
            }
            if (Character.isDigit(character)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapitalLetter && hasNumber && hasSpecialCharacter;
    }

    public static boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "\\+27[0-9]{9}$";
        return Pattern.matches(regex, cellPhoneNumber);
    }

    // Instance validation
    public String registerUser() {
        if (!checkUserName(this.userName)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(this.password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(this.cellPhoneNumber)) {
            return "Cell phone number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        }

        return "User registered successfully.";
    }

    // Overloaded registerUser for standard parameter testing
    public String registerUser(String userName, String password, String cellPhoneNumber, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        return registerUser();
    }

    public boolean loginUser(String enteredUserName, String enteredPassword) {
        if (enteredUserName == null || enteredPassword == null) {
            this.loginSuccessful = false;
            return this.loginSuccessful;
        }

        this.loginSuccessful = this.userName != null && this.password != null
                && this.userName.equals(enteredUserName)
                && this.password.equals(enteredPassword);

        return this.loginSuccessful;
    }

    public String returnLoginStatus() {
        if (this.loginSuccessful) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public String returnLoginStatus(boolean status) {
        this.loginSuccessful = status;
        return returnLoginStatus();
    }
}