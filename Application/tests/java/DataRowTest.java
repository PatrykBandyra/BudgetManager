import org.junit.Test;
import sample.DataRow;

import static org.junit.Assert.*;

public class DataRowTest {

    @Test
    public void testGetId() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.getId(), 100);
    }

    @Test
    public void testGetSetValue() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(123.4, data.getValue(), 0.001);
        data.setValue(1.5);
        assertEquals(1.5, data.getValue(), 0.001);
    }

    @Test
    public void testGetSetAmount() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(123.4, data.getValue(), 0.001);
        data.setValue(1.5);
        assertEquals(1.5, data.getValue(), 0.001);
    }

    @Test
    public void testGetSetUnit() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.getUnit(), "kg");
        data.setUnit("l");
        assertEquals(data.getUnit(), "l");
    }

    @Test
    public void testGetSetYear() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.getYear(), 2020);
        data.setYear(2021);
        assertEquals(data.getYear(), 2021);
    }

    @Test
    public void testGetSetMonth() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.getMonth(), 12);
        data.setMonth(1);
        assertEquals(data.getMonth(), 1);
    }

    @Test
    public void testGetSetDay() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.getDay(), 12);
        data.setDay(1);
        assertEquals(data.getDay(), 1);
    }

    @Test
    public void testGetSetDate() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.getDate(), "12/12/2020");
        data.setDay(2);
        data.setDate();
        assertEquals(data.getDate(), "2/12/2020");
    }

    @Test
    public void testGetSetCategory() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.getCategory(), "food");
        data.setCategory("office");
        assertEquals(data.getCategory(), "office");
    }

    @Test
    public void testToString() {
        DataRow data = new DataRow(100, 123.4, 2.5, "kg", 2020, 12, 12, "bananas", "food");
        assertEquals(data.toString(), "Id: 100, Value: 123.4, Amount: 2.5, Unit: kg, Category: food, Name: bananas, Date: 12/12/2020");
    }
}
