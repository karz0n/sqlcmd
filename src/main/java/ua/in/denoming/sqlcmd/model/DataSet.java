package ua.in.denoming.sqlcmd.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class DataSet {
    private Map<String, Object> storage;

    public DataSet() {
        this(0);
    }

    public DataSet(int capacity) {
        this.storage = new LinkedHashMap<>(capacity);
    }

    public void set(String name, Object value) {
        storage.put(name, value);
    }

    public Object get(String name) {
        return this.storage.get(name);
    }

    public Set<String> names() {
        return Collections.unmodifiableSet(storage.keySet());
    }

    public Collection<Object> values() {
        return Collections.unmodifiableCollection(storage.values());
    }

    public int size() {
        return storage.size();
    }

    public Iterator<Map.Entry<String, Object>> iterator() {
        return storage.entrySet().iterator();
    }

    @Override
    public String toString() {
        if (storage.size() == 0) {
            return "";
        }

        int counter = 0;
        StringBuilder builder = new StringBuilder("[");
        for (Map.Entry<String, Object> entries : storage.entrySet()) {
            if (counter > 0) {
                builder.append(", ");
            }
            builder
                .append("'")
                .append(entries.getKey())
                .append("' = '")
                .append(entries.getValue())
                .append("'");
            counter++;
        }
        return builder.append("]").toString();
    }

    @Override
    public int hashCode() {
        return storage.hashCode();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject)
            return true;
        if (otherObject == null || getClass() != otherObject.getClass())
            return false;

        DataSet other = (DataSet) otherObject;
        return storage.equals(other.storage);
    }
}
