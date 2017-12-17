package ua.in.denoming.sqlcmd.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TableGenerator {
    private static final int PADDING_SIZE = 2;
    private static final String TABLE_JOINT_SYMBOL = "+";
    private static final String TABLE_V_SPLIT_SYMBOL = "|";
    private static final String TABLE_H_SPLIT_SYMBOL = "-";

    public String generate(ArrayList<DataSet> dataSets) {
        if (dataSets.size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        List<String> headers = generateHeaders(dataSets);
        List<List<String>> rows = generateRows(dataSets);

        Map<Integer, Integer> columnMaxWidthMapping = getMaximumWidthOfTable(headers, rows);

        createRowLine(stringBuilder, headers.size(), columnMaxWidthMapping);
        stringBuilder.append(System.lineSeparator());

        for (int headerIndex = 0; headerIndex < headers.size(); headerIndex++) {
            fillCell(stringBuilder, headers.get(headerIndex), headerIndex, columnMaxWidthMapping);
        }

        stringBuilder.append(System.lineSeparator());
        createRowLine(stringBuilder, headers.size(), columnMaxWidthMapping);

        for (List<String> row : rows) {
            stringBuilder.append(System.lineSeparator());
            for (int cellIndex = 0; cellIndex < row.size(); cellIndex++) {
                fillCell(stringBuilder, row.get(cellIndex), cellIndex, columnMaxWidthMapping);
            }
        }

        stringBuilder.append(System.lineSeparator());
        createRowLine(stringBuilder, headers.size(), columnMaxWidthMapping);

        return stringBuilder.toString();
    }

    private List<String> generateHeaders(ArrayList<DataSet> dataSets) {
        if (dataSets.size() == 0) {
            return new LinkedList<>();
        }

        return Arrays.asList(
            dataSets.get(0).getNames()
        );
    }

    private List<List<String>> generateRows(ArrayList<DataSet> dataSets) {
        if (dataSets.size() == 0) {
            return new LinkedList<>();
        }

        List<List<String>> rows = new LinkedList<>();
        for (DataSet dataSet : dataSets) {
            List<String> row = new LinkedList<>();
            for (int i = 0; i < dataSet.size(); i++) {
                row.add(dataSet.getString(i).trim());
            }
            rows.add(row);
        }

        return rows;
    }

    private void fillSpace(StringBuilder stringBuilder, int length) {
        for (int i = 0; i < length; i++) {
            stringBuilder.append(" ");
        }
    }

    private void createRowLine(StringBuilder stringBuilder, int headersListSize, Map<Integer, Integer> columnMaxWidthMapping) {
        for (int i = 0; i < headersListSize; i++) {
            if (i == 0) {
                stringBuilder.append(TABLE_JOINT_SYMBOL);
            }

            for (int j = 0; j < columnMaxWidthMapping.get(i) + PADDING_SIZE * 2; j++) {
                stringBuilder.append(TABLE_H_SPLIT_SYMBOL);
            }
            stringBuilder.append(TABLE_JOINT_SYMBOL);
        }
    }

    private Map<Integer, Integer> getMaximumWidthOfTable(List<String> headersList, List<List<String>> rowsList) {
        Map<Integer, Integer> columnMaxWidthMapping = new HashMap<>();

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
            columnMaxWidthMapping.put(columnIndex, 0);
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
            if (headersList.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex)) {
                columnMaxWidthMapping.put(columnIndex, headersList.get(columnIndex).length());
            }
        }

        for (List<String> row : rowsList) {
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                if (row.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex)) {
                    columnMaxWidthMapping.put(columnIndex, row.get(columnIndex).length());
                }
            }
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
            if (columnMaxWidthMapping.get(columnIndex) % 2 != 0) {
                columnMaxWidthMapping.put(columnIndex, columnMaxWidthMapping.get(columnIndex) + 1);
            }
        }

        return columnMaxWidthMapping;
    }

    private int getOptimumCellPadding(int cellIndex, int dataLength, Map<Integer, Integer> columnMaxWidthMapping, int cellPaddingSize) {
        if (dataLength % 2 != 0) {
            dataLength++;
        }

        if (dataLength < columnMaxWidthMapping.get(cellIndex)) {
            cellPaddingSize = cellPaddingSize + (columnMaxWidthMapping.get(cellIndex) - dataLength) / 2;
        }

        return cellPaddingSize;
    }

    private void fillCell(StringBuilder stringBuilder, String cell, int cellIndex, Map<Integer, Integer> columnMaxWidthMapping) {
        int cellPaddingSize = getOptimumCellPadding(cellIndex, cell.length(), columnMaxWidthMapping, PADDING_SIZE);

        if (cellIndex == 0) {
            stringBuilder.append(TABLE_V_SPLIT_SYMBOL);
        }

        fillSpace(stringBuilder, cellPaddingSize);
        stringBuilder.append(cell);
        if (cell.length() % 2 != 0) {
            stringBuilder.append(" ");
        }

        fillSpace(stringBuilder, cellPaddingSize);

        stringBuilder.append(TABLE_V_SPLIT_SYMBOL);
    }
}
