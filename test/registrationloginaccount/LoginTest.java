/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package registrationloginaccount;

import static org.junit.Assert.*;
/**
 *
 * @author Palesa
 */
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoginTest {

    public LoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCheckUserName_Valid() {
        assertTrue(Login.checkUserName("kyl_1"));
    }

    @Test
    public void testCheckUserName_NoUnderscore() {
        assertFalse(Login.checkUserName("kyle1"));
    }

    @Test
    public void testCheckUserName_TooLong() {
        assertFalse(Login.checkUserName("kyle_12345"));
    }

    @Test
    public void testCheckUserName_Empty() {
        assertFalse(Login.checkUserName(""));
    }

    @Test
    public void testCheckPasswordComplexity_Valid() {
        assertTrue(Login.checkPasswordComplexity("Ch@t8App1"));
    }

    @Test
    public void testCheckPasswordComplexity_TooShort() {
        assertFalse(Login.checkPasswordComplexity("C@1a"));
    }

    @Test
    public void testCheckPasswordComplexity_NoCapitalLetter() {
        assertFalse(Login.checkPasswordComplexity("ch@t8app1"));
    }

    @Test
    public void testCheckPasswordComplexity_NoNumber() {
        assertFalse(Login.checkPasswordComplexity("Ch@tApppp"));
    }

    @Test
    public void testCheckPasswordComplexity_NoSpecialCharacter() {
        assertFalse(Login.checkPasswordComplexity("Chat8App1"));
    }

    @Test
    public void testCheckPasswordComplexity_Empty() {
        assertFalse(Login.checkPasswordComplexity(""));
    }

    @Test
    public void testCheckCellPhoneNumber_Valid() {
        assertTrue(Login.checkCellPhoneNumber("+27831234567"));
    }

    @Test
    public void testCheckCellPhoneNumber_MissingInternationalCode() {
        assertFalse(Login.checkCellPhoneNumber("0831234567"));
    }

    @Test
    public void testCheckCellPhoneNumber_TooLong() {
        assertFalse(Login.checkCellPhoneNumber("+2783123456789"));
    }

    @Test
    public void testCheckCellPhoneNumber_Empty() {
        assertFalse(Login.checkCellPhoneNumber(""));
    }

    @Test
    public void testRegisterUser_Success() {
        Login instance = new Login("kyl_1", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        String expResult = "User registered successfully.";
        String result = instance.registerUser();
        assertEquals(expResult, result);
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        String expResult = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        String result = instance.registerUser();
        assertEquals(expResult, result);
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        Login instance = new Login("kyl_1", "password", "+27831234567", "Kyle", "Smith");
        String expResult = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        String result = instance.registerUser();
        assertEquals(expResult, result);
    }

    @Test
    public void testRegisterUser_InvalidCellPhone() {
        Login instance = new Login("kyl_1", "Ch@t8App1", "0831234567", "Kyle", "Smith");
        String expResult = "Cell phone number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        String result = instance.registerUser();
        assertEquals(expResult, result);
    }

    @Test
    public void testLoginUser_Success() {
        Login instance = new Login("kyl_1", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        instance.registerUser();
        assertTrue(instance.loginUser("kyl_1", "Ch@t8App1"));
    }

    @Test
    public void testLoginUser_WrongPassword() {
        Login instance = new Login("kyl_1", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        instance.registerUser();
        assertFalse(instance.loginUser("kyl_1", "WrongPass1!"));
    }

    @Test
    public void testLoginUser_WrongUsername() {
        Login instance = new Login("kyl_1", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        instance.registerUser();
        assertFalse(instance.loginUser("wrong_1", "Ch@t8App1"));
    }

    @Test
    public void testReturnLoginStatus_Success() {
        Login instance = new Login("kyl_1", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        instance.registerUser();
        instance.loginUser("kyl_1", "Ch@t8App1");
        String expResult = "Welcome Kyle Smith, it is great to see you again.";
        String result = instance.returnLoginStatus();
        assertEquals(expResult, result);
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        Login instance = new Login("kyl_1", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        String expResult = "Username or password incorrect, please try again.";
        String result = instance.returnLoginStatus(false);
        assertEquals(expResult, result);
    }
}
