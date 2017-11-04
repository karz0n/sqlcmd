package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

class DataSetTest {
    private DataSet dataSet;

    @BeforeEach
    void setup() {
        dataSet = new DataSet(
            new DataSet.Data("column1", "value1"),
            new DataSet.Data("column2", "value2"),
            new DataSet.Data("column3", "value3")
        );
    }

    @Test
    void testSizeOfEmpty() {
        DataSet emptyDataSet = new DataSet();
        assertEquals(0, emptyDataSet.size());
    }

    @Test
    void testSize() {
        assertEquals(3, dataSet.size());
    }

    @Test
    void testGet() {
        assertAll("Index 0",
            () -> assertEquals("column1", dataSet.get(0).getName()),
            () -> assertEquals("value1", dataSet.get(0).getValue())
        );
        assertAll("Index 1",
            () -> assertEquals("column2", dataSet.get(1).getName()),
            () -> assertEquals("value2", dataSet.get(1).getValue())
        );
        assertAll("Index 2",
            () -> assertEquals("column3", dataSet.get(2).getName()),
            () -> assertEquals("value3", dataSet.get(2).getValue())
        );
    }

    @Test
    void testSet() {
        dataSet.set(0, new DataSet.Data("column4", "value4"));
        dataSet.set(1, new DataSet.Data("column5", "value5"));
        dataSet.set(2, new DataSet.Data("column6", "value6"));

        assertAll("Index 0",
            () -> assertEquals("column4", dataSet.get(0).getName()),
            () -> assertEquals("value4", dataSet.get(0).getValue())
        );
        assertAll("Index 1",
            () -> assertEquals("column5", dataSet.get(1).getName()),
            () -> assertEquals("value5", dataSet.get(1).getValue())
        );
        assertAll("Index 2",
            () -> assertEquals("column6", dataSet.get(2).getName()),
            () -> assertEquals("value6", dataSet.get(2).getValue())
        );
    }

    @Test
    void testGetString() {
        assertEquals("value1", dataSet.getString(0));
        assertEquals("value2", dataSet.getString(1));
        assertEquals("value3", dataSet.getString(2));
    }

    @Test
    void testGetNames() {
        assertArrayEquals(new String[]{"column1", "column2", "column3"}, dataSet.getNames());
    }

    @Test
    void testGetValues() {
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, dataSet.getValues());
    }

    @Test
    void testIncorrectIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> dataSet.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataSet.set(-1, null));
        assertThrows(IndexOutOfBoundsException.class, () -> dataSet.getString(-1));
    }
}
