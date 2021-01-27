import org.junit.Test;
import sample.InputValidation;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class InputValidationTest {

    @Test
    public void testDoName() {
        assertEquals(InputValidation.doName("Jack"), "\"Jack\"");
    }

    @Test
    public void testDoCategory() {
        assertEquals(InputValidation.doName("food"), "\"food\"");
    }

    @Test
    public void testValidateValueSuccess() throws Exception {
        assertEquals(12, InputValidation.validateValue("12"), 0.001);
    }

    @Test(expected = Exception.class)
    public void testValidateValueFailure1() throws Exception {
        assertEquals(12, InputValidation.validateValue("1a"), 0.001);
    }

    @Test(expected = Exception.class)
    public void testValidateValueFailure2() throws Exception {
        assertEquals(-20.2, InputValidation.validateValue("-20.2"), 0.001);
    }

    @Test
    public void testValidateAmountSuccess() throws Exception {
        assertEquals(12.4, InputValidation.validateAmount("12.4"), 0.001);
    }

    @Test(expected = Exception.class)
    public void testValidateAmountFailure1() throws Exception {
        assertEquals(12.4, InputValidation.validateAmount("12.a"), 0.001);
    }

    @Test(expected = Exception.class)
    public void testValidateAmountFailure2() throws Exception {
        assertEquals(-12, InputValidation.validateAmount("-12"), 0.001);
    }

    @Test
    public void testIfAmountMatchesUnitSuccess() {
        assertTrue(InputValidation.ifAmountMatchesUnit(12, "pc"));
        assertTrue(InputValidation.ifAmountMatchesUnit(12.5, "kg"));
    }

    @Test
    public void testIfAmountMatchesUnitFailure() {
        assertFalse(InputValidation.ifAmountMatchesUnit(12.5, "pc"));
    }

    @Test
    public void testValidateDateSuccess() throws Exception {
        assertArrayEquals(InputValidation.validateDate("1", "2", "2020"), new int[]{1, 2, 2020});
    }

    @Test(expected = Exception.class)
    public void testValidateDateFailure() throws Exception {
        assertArrayEquals(InputValidation.validateDate("1", "2", "2021"), new int[]{1, 12, 2021});
    }

}
