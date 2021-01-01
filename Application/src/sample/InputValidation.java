package sample;

import sample.customExceptions.InvalidAmountException;
import sample.customExceptions.InvalidDateException;
import sample.customExceptions.InvalidValueException;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public class InputValidation {

    public static String doName(String name) {
        return "\""+name+"\"";
    }

    public static String doCategory(String category) {
        return "\""+category+"\"";
    }

    public static double validateValue(String valueString) throws InvalidValueException {
        try {
            double value = Double.parseDouble(valueString);
            if (value <= 0) {
                throw new InvalidValueException("Invalid value.");
            }
            return value;
        } catch (NumberFormatException | InvalidValueException exception) {
            throw new InvalidValueException("Invalid value.", exception);
        }
    }

    public static double validateAmount(String amountString) throws InvalidAmountException {
        try {
            double amount = Double.parseDouble(amountString);
            if (amount <= 0){
                throw new InvalidAmountException("Invalid amount.");
            }
            return amount;
        } catch (NumberFormatException exception) {
            throw new InvalidAmountException("Invalid amount.", exception);
        }
    }

    public static boolean ifAmountMatchesUnit(double amount, String unit) {
        return amount % 1 == 0 || !unit.equals("pc");
    }

    public static int[] validateDate(String dayString, String monthString, String yearString) throws InvalidDateException {
        try{
            int day = Integer.parseInt(dayString);
            int month = Integer.parseInt(monthString);
            int year = Integer.parseInt(yearString);
            // validate date
            Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
            Timestamp ourTimeStamp = Timestamp.valueOf(LocalDateTime.of(year, month, day, 0, 0)); // DateTimeException
            if (ourTimeStamp.getTime() > currentTimeStamp.getTime()){
                throw new InvalidDateException("Invalid date.");
            }
            return new int[] {day, month, year};
        } catch (NumberFormatException | DateTimeException exception){
            throw new InvalidDateException("Invalid date.", exception);
        }
    }
}
