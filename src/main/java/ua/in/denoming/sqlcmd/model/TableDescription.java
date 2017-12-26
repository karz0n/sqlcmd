package ua.in.denoming.sqlcmd.model;

import java.util.Objects;

public class TableDescription {
    private String catalog;
    private String schema;
    private String name;
    private String type;
    private String remarks;

    TableDescription(String catalog,
                     String schema,
                     String name,
                     String type,
                     String remarks) {
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
        this.type = type;
        this.remarks = remarks;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject)
            return true;
        if (otherObject == null || getClass() != otherObject.getClass())
            return false;

        TableDescription other = (TableDescription) otherObject;
        return Objects.equals(schema, other.schema) && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, name);
    }

    @Override
    public String toString() {
        return String.format(
            "'%s'{catalog='%s', schema='%s', type='%s', remarks='%s'}", name, catalog, schema, type, remarks
        );
    }
}
