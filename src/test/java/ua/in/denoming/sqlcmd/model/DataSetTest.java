package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DataSetTest {
    private final int DATA_SET_CAPACITY = 3;
    private DataSet dataSet;

    @BeforeEach
    void setup() {
        dataSet = new DataSet(DATA_SET_CAPACITY);
        dataSet.set("column1", "value1");
        dataSet.set("column2", "value2");
        dataSet.set("column3", "value3");
    }

    @Test
    void testSizeOfEmpty() {
        DataSet emptyDataSet = new DataSet();
        assertEquals(0, emptyDataSet.size());
    }

    @Test
    void testSize() {
        assertEquals(DATA_SET_CAPACITY, dataSet.size());
    }

    @Test
    void testGet() {
        assertAll("Get values",
            () -> assertEquals("value1", dataSet.get("column1")),
            () -> assertEquals("value2", dataSet.get("column2")),
            () -> assertEquals("value3", dataSet.get("column3"))
        );
    }

    @Test
    void testSet() {
        dataSet.set("column1", "value4");
        dataSet.set("column2", "value5");
        dataSet.set("column3", "value6");

        assertAll("Set values",
            () -> assertEquals("value4", dataSet.get("column1")),
            () -> assertEquals("value5", dataSet.get("column2")),
            () -> assertEquals("value6", dataSet.get("column3"))
        );
    }

    @Test
    void testNames() {
        assertEquals(
            Arrays.asList("column1", "column2", "column3").toString(),
            dataSet.names().toString()
        );
    }

    @Test
    void testValues() {
        assertEquals(
            Arrays.asList("value1", "value2", "value3").toString(),
            dataSet.values().toString()
        );
    }

    @Test
    void testStringConversion() {
        assertEquals(
            "['column1' = 'value1', 'column2' = 'value2', 'column3' = 'value3']",
            dataSet.toString()
        );
    }

    @Test
    void testEqualComparison() {
        DataSet otherDataSet = new DataSet(DATA_SET_CAPACITY);
        otherDataSet.set("column1", "value1");
        otherDataSet.set("column2", "value2");
        otherDataSet.set("column3", "value3");
        assertEquals(otherDataSet, dataSet);

        otherDataSet.set("column1", "some new value");
        assertNotEquals(otherDataSet, dataSet);
    }
}
