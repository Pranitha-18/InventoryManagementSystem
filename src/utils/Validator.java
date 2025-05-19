package utils;

public class Validator {

    // Check if a string is empty or only spaces
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Check if a string is a valid integer
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }
}
