/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package registrationloginaccount;

import java.util.Scanner;

public class RegistrationLoginAccount {

    public static void main(String[] args) {
        //declaration
        Scanner inputDevice = new Scanner(System.in);
        String firstName;
        String lastName;
        String userName;
        String password;
        String cellPhoneNumber;

        //Prompting the user to eneter their details
        System.out.println("Enter your first name:");
        firstName = inputDevice.nextLine();

        System.out.println("Enter your last name:");
        lastName = inputDevice.nextLine();
        //Loop  to validate username
        while (true) {
            System.out.println("Enter your username:");
            userName = inputDevice.nextLine();

            if (Login.checkUserName(userName)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted; please ensure your username contains an underscore and is no more than five characters in length.");
            }
        }
        //Loop  to validate password
        while (true) {
            System.out.println("Enter password");
            password = inputDevice.nextLine();

            if (Login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted; please ensure password contains at least eight characters, a capital letter, a number and a special character.");
            }
        }
        //Loop  to validate cellphone number
        while (true) {
            System.out.println("Enter Cellphone number");
            cellPhoneNumber = inputDevice.nextLine();

            if (Login.checkCellPhoneNumber(cellPhoneNumber)) {
                System.out.println("Cellphone number successfully added.");
                break;
            } else {
                System.out.println("Cellphone number incorrectly formatted or does not contain an international code.");
            }
        }

        // Creating the Login object
        Login user = new Login(userName, password, cellPhoneNumber, firstName, lastName);
        // Register using registerUser()
        System.out.println();
        String registrationMessage = user.registerUser();
        System.out.println(registrationMessage);

        // Allowing login in case registration was successful
        if (registrationMessage.equals("User registered successfully.")) {
            System.out.println();
            System.out.println("LOGIN");

            System.out.println("Enter your username:");
            String enteredUserName = inputDevice.nextLine();

            System.out.println("Enter your password:");
            String enteredPassword = inputDevice.nextLine();

            boolean loggedIn = user.loginUser(enteredUserName, enteredPassword);

            System.out.println(user.returnLoginStatus());

            if (loggedIn) {
                runQuickChat(inputDevice);
            }
        } else {
            System.out.println();
            System.out.println("Registration failed. Please try again");
        }

        inputDevice.close();
    }

    // Asks for a whole number and keeps asking until the user types one
    private static int readInt(Scanner inputDevice, String prompt) {
        while (true) {
            System.out.println(prompt);
            String line = inputDevice.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    // QuickChat menu, only reached after a successful login
    private static void runQuickChat(Scanner inputDevice) {
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        int choice = 0;
        while (choice != 3) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            choice = readInt(inputDevice, "Choose an option:");

            if (choice == 1) {
                System.out.println("Sending messages is coming next.");
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                System.out.println("Goodbye.");
            } else {
                System.out.println("Invalid option. Please choose 1, 2 or 3.");
            }
        }
    }
}
