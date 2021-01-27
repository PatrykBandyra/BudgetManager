package sample;

import sample.customExceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

public class ReceiptLoader {

    /**
     * Loads receipt from txt file and validates it
     * @param filepath String
     * @return ArrayList of DataRows
     */
    public static ArrayList<DataRow> loadReceipt(String filepath) throws FileNotFoundException, IncorrectReceiptException,
            InvalidValueException, InvalidAmountException, InvalidUnitException,
            AmountNotMatchingUnitException, InvalidDateException {
        File file = new File(filepath);
        Scanner sc = new Scanner(file); // FileNotFoundException
        ArrayList<DataRow> data = new ArrayList<>();
        ArrayList<String> units = new ArrayList<>(
                Arrays.asList("pc",
                              "kg",
                              "l"));
        int line = 0;
        while (sc.hasNextLine()){
            ++line;
            String[] row = sc.nextLine().split("\\s+"); // split by whitespaces
            if (row.length != 6) {
                throw new IncorrectReceiptException("Invalid row in receipt. Row number: "+line);
            }
            // get value; no need for currency (always PLN)
            String[] valueAndCurrency = row[0].split("[a-zA-Z]", 2);
            try {
                double value = getValueFromString(valueAndCurrency[0]);
                double amount = getAmountFromString(row[1]);
                String unit = row[2];
                if (!units.contains(unit)) {
                    throw new InvalidUnitException("Invalid unit in receipt. Row number: "+line);
                }
                // check if amount matches the unit
                if (!ifAmountMatchesUnit(amount, unit)){
                    throw new AmountNotMatchingUnitException("Amount does not match the unit. Row number: "+line);
                }
                String category = row[3];
                String name = row[4];
                int[] date = dateFromString(row[5]);
                data.add(new DataRow(value, amount, unit, date[2], date[1], date[0], name, category));
            } catch (InvalidValueException exception) {
                throw new InvalidValueException(exception.getMessage()+line);
            } catch (InvalidAmountException exception) {
                throw new InvalidAmountException(exception.getMessage()+line);
            } catch (InvalidDateException exception) {
                throw new InvalidDateException(exception.getMessage()+line);
            }
        }
        return data;
    }

    /**
     * Casts string amount to float and validates it
     * @param amountString String
     * @return float amount
     * @throws InvalidAmountException
     */
    private static double getAmountFromString(String amountString) throws InvalidAmountException {
        try {
            double amount = Double.parseDouble(amountString);
            if (amount <= 0){
                throw new InvalidAmountException("Invalid amount in receipt. Row number: ");
            }
            return amount;
        } catch (NumberFormatException exception) {
            throw new InvalidAmountException("Invalid amount in receipt. Row number: ", exception);
        }
    }

    /**
     * Casts string value to float and validates it
     * @param valueString String
     * @return float value
     * @throws InvalidValueException
     */
    private static double getValueFromString(String valueString) throws InvalidValueException {
        try {
            double value = Double.parseDouble(valueString);
            if (value <= 0) {
                throw new InvalidValueException("Invalid value in receipt. Row number: ");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new InvalidValueException("Invalid value in receipt. Row number: ", exception);
        }
    }

    /**
     * Checks if unit "pc" has corresponding amount that is an integer
     * @param amount float
     * @param unit String
     * @return boolean
     */
    private static boolean ifAmountMatchesUnit(double amount, String unit) {
        return amount % 1 == 0 || !unit.equals("pc");
    }

    /**
     * Casts string date to int date and validates it.
     * Returns array of int where arr[0] = day, arr[1] = month, arr[2] = year
     * @param dateString ("22.11.2020" or "22/11/2020")
     * @return int[3]
     * @throws InvalidDateException
     */
    private static int[] dateFromString(String dateString) throws InvalidDateException {     // "22.10.2020"
        try{
            String[] dateSplitString = dateString.split("[./]");    // {"22", "10", "2020"}
            if (dateSplitString.length != 3){
                throw new InvalidDateException("Invalid date in receipt. Row number: ");
            }
            int[] returnDate = new int[] {Integer.parseInt(dateSplitString[0]), Integer.parseInt(dateSplitString[1]),
                                          Integer.parseInt(dateSplitString[2])};    // NumberFormatException
            // validate date
            Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
            Timestamp ourTimeStamp = Timestamp.valueOf(LocalDateTime.of(returnDate[2], returnDate[1],
                                     returnDate[0], 0, 0)); // DateTimeException
            if (ourTimeStamp.getTime() > currentTimeStamp.getTime()){
                throw new InvalidDateException("Invalid date in receipt. Row number: ");
            }
            return returnDate;
        } catch (NumberFormatException | DateTimeException exception){
            throw new InvalidDateException("Invalid date in receipt. Row number: ", exception);
        }
    }
    public static int unit_tests(int test_nr){
        try{if(ReceiptLoader.getValueFromString("7534254")!=7534254) throw new AssertionError("\n\t\tReceiptLoader.getValueFromString() failed\n");
        }catch(InvalidValueException exception){throw new AssertionError("\n\t\tReceiptLoader.getValueFromString() failed\n");}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{if(ReceiptLoader.getValueFromString("1")!=1) throw new AssertionError("\n\t\tReceiptLoader.getValueFromString() failed\n");
        }catch(InvalidValueException exception){throw new AssertionError("\n\t\tReceiptLoader.getValueFromString() failed\n");}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{ReceiptLoader.getValueFromString("0"); throw new AssertionError("\n\t\tReceiptLoader.getValueFromString() failed\n");
        }catch(InvalidValueException ignored){}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{ReceiptLoader.getValueFromString("-1"); throw new AssertionError("\n\t\tReceiptLoader.getValueFromString() failed\n");
        }catch(InvalidValueException ignored){}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{if(ReceiptLoader.getAmountFromString("1")!=1) throw new AssertionError("\n\t\tReceiptLoader.getAmountFromString() failed\n");
        }catch(InvalidAmountException exception){throw new AssertionError("\n\t\tReceiptLoader.getAmountFromString() failed\n");}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{if(ReceiptLoader.getAmountFromString("3762526")!=3762526) throw new AssertionError("\n\t\tReceiptLoader.getAmountFromString() failed\n");
        }catch(InvalidAmountException exception){throw new AssertionError("\n\t\tReceiptLoader.getAmountFromString() failed\n");}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{ReceiptLoader.getAmountFromString("0"); throw new AssertionError("\n\t\tReceiptLoader.getAmountFromString() failed\n");
        }catch(InvalidAmountException ignored){}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{ReceiptLoader.getAmountFromString("-1"); throw new AssertionError("\n\t\tReceiptLoader.getAmountFromString() failed\n");
        }catch(InvalidAmountException ignored){}
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!ReceiptLoader.ifAmountMatchesUnit(53, "pc")) throw new AssertionError("\n\t\tReceiptLoader.ifAmountMatchesUnit() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!ReceiptLoader.ifAmountMatchesUnit(53.0, "pc")) throw new AssertionError("\n\t\tReceiptLoader.ifAmountMatchesUnit() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(ReceiptLoader.ifAmountMatchesUnit(53.1, "pc")) throw new AssertionError("\n\t\tReceiptLoader.ifAmountMatchesUnit() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!ReceiptLoader.ifAmountMatchesUnit(53.1, "pd")) throw new AssertionError("\n\t\tReceiptLoader.ifAmountMatchesUnit() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!ReceiptLoader.ifAmountMatchesUnit(53.0, "pd")) throw new AssertionError("\n\t\tReceiptLoader.ifAmountMatchesUnit() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        return test_nr;
    }

    public static void main(String[] args) {
        unit_tests(1);
    }

}
