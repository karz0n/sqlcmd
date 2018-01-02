package ua.in.denoming.sqlcmd.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableDescriptionTest {
    private static final String CATALOG_NAME = "someCatalog";
    private static final String SCHEMA_NAME = "someSchema";
    private static final String TABLE_NAME = "someTable";
    private static final String TABLE_TYPE = "someType";
    private static final String TABLE_REMARKS = "someRemarks";

    private static TableDescription tableDescription;

    @BeforeAll
    static void setup() {
        tableDescription = new TableDescription(
            CATALOG_NAME,
            SCHEMA_NAME,
            TABLE_NAME,
            TABLE_TYPE,
            TABLE_REMARKS
        );
    }

    @Test
    void testGetCatalog() {
        assertEquals(CATALOG_NAME, tableDescription.getCatalog());
    }

    @Test
    void testGetSchema() {
        assertEquals(SCHEMA_NAME, tableDescription.getSchema());
    }

    @Test
    void testGetName() {
        assertEquals(TABLE_NAME, tableDescription.getName());
    }

    @Test
    void testGetType() {
        assertEquals(TABLE_TYPE, tableDescription.getType());
    }

    @Test
    void testGetRemarks() {
        assertEquals(TABLE_REMARKS, tableDescription.getRemarks());
    }

    @Test
    void testStringConversion() {
        String expected = String.format(
            "'%s'{catalog='%s', schema='%s', type='%s', remarks='%s'}",
            TABLE_NAME, CATALOG_NAME, SCHEMA_NAME, TABLE_TYPE, TABLE_REMARKS
        );
        assertEquals(expected, tableDescription.toString());
    }
}
