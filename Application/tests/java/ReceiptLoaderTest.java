import org.junit.Test;
import static org.junit.Assert.assertTrue;

import sample.ReceiptLoader;
import sample.customExceptions.*;

import java.io.FileNotFoundException;

public class ReceiptLoaderTest {

    /**
     * Tests of private methods of ReceiptLoader in class ReceiptLoader.
     *
     * Here is tested receipt loading.
     */

    @Test
    public void goodReceipt1() {
        boolean okay = true;
        try {
            ReceiptLoader.loadReceipt("./Application/tests/receipts/a.txt");
        } catch (FileNotFoundException | IncorrectReceiptException | InvalidValueException | InvalidAmountException | InvalidUnitException | AmountNotMatchingUnitException | InvalidDateException exception) {
            System.err.println(exception.getMessage());
            okay = false;
        }
        assertTrue(okay);
    }

    @Test
    public void goodReceipt2() {
        boolean okay = true;
        try {
            ReceiptLoader.loadReceipt("./Application/tests/receipts/b.txt");
        } catch (FileNotFoundException | IncorrectReceiptException | InvalidValueException | InvalidAmountException | InvalidUnitException | AmountNotMatchingUnitException | InvalidDateException exception) {
            System.err.println(exception.getMessage());
            okay = false;
        }
        assertTrue(okay);
    }

    @Test
    public void goodReceipt3() {
        boolean okay = true;
        try {
            ReceiptLoader.loadReceipt("./Application/tests/receipts/c.txt");
        } catch (FileNotFoundException | IncorrectReceiptException | InvalidValueException | InvalidAmountException | InvalidUnitException | AmountNotMatchingUnitException | InvalidDateException exception) {
            System.err.println(exception.getMessage());
            okay = false;
        }
        assertTrue(okay);
    }

    @Test(expected = Exception.class)
    public void badReceipt1() throws Exception {
        ReceiptLoader.loadReceipt("./Application/tests/receipts/d.txt");
    }

    @Test(expected = Exception.class)
    public void badReceipt2() throws Exception {
        ReceiptLoader.loadReceipt("./Application/tests/receipts/d.txt");
    }

    @Test(expected = Exception.class)
    public void badReceipt3() throws Exception {
        ReceiptLoader.loadReceipt("./Application/tests/receipts/d.txt");
    }
}
