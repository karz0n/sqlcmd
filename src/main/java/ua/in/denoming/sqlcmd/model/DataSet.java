package ua.in.denoming.sqlcmd.model;

import java.util.ArrayList;
import java.util.Objects;

public class DataSet {
    private ArrayList<Data> storage;

    public static class Data {
        String name;
        Object value;

        @Override
        public String toString() {
            return String.format("name='%s', value='%s'", name, value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }

        @Override
        public boolean equals(Object otherObject) {
            if (this == otherObject)
                return true;
            if (otherObject == null)
                return false;
            if (getClass() != otherObject.getClass())
                return false;

            Data other = (Data) otherObject;
            return Objects.equals(name, other.name) && Objects.equals(value, other.value);
        }

        Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @SuppressWarnings("unused")
        public String getName() {
            return name;
        }

        @SuppressWarnings("WeakerAccess")
        public Object getValue() {
            return value;
        }
    }

    @SuppressWarnings("unused")
    public DataSet() {
        this(0);
    }

    public DataSet(int capacity) {
        this.storage = new ArrayList<>(capacity);
    }

    public void put(String name, Object value) {
        storage.add(new Data(name, value));
    }

    @SuppressWarnings("unused")
    public void set(int index, Data data) {
        storage.set(index, data);
    }

    @SuppressWarnings("unused")
    public Data get(int index) {
        return this.storage.get(index);
    }

    public String getString(int index) {
        return String.valueOf(
            this.storage.get(index).getValue()
        );
    }

    public String[] getNames() {
        if (storage.size() == 0) {
            return new String[0];
        }

        String[] names = new String[storage.size()];
        for (int i = 0; i < storage.size(); i++) {
            names[i] = storage.get(i).name;
        }
        return names;
    }

    @SuppressWarnings("WeakerAccess")
    public Object[] getValues() {
        if (storage.size() == 0) {
            return new Object[0];
        }

        Object[] values = new Object[storage.size()];
        for (int i = 0; i < storage.size(); i++) {
            values[i] = storage.get(i).value;
        }
        return values;
    }

    public int size() {
        return storage.size();
    }

    @Override
    public String toString() {
        if (storage.size() == 0) {
            return "DataSet {<empty>}";
        }

        StringBuilder builder = new StringBuilder("DataSet {").append(System.lineSeparator());
        for (Data data : storage) {
            builder.append('\t').append(data.toString()).append(System.lineSeparator());
        }
        return builder.append("}").toString();
    }
}
