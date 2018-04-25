package com.feut.server.db.facades;

import com.feut.server.db.DBConnection;
import com.google.common.base.CaseFormat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade {

    private enum QueryType {
        SELECT,
        UPDATE,
    }

    // TODO: Error afhandeling!
    private static List<Map<String, String>> executeQuery(String query, String[] parameters, QueryType queryType) throws SQLException {
        List<Map<String, String>> result = new ArrayList<>();
        PreparedStatement stmt = null;
        try {
            stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                stmt.setString(i + 1, parameters[i]);
            }

            if (queryType == QueryType.UPDATE) {
                stmt.execute();
                return null;
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                Map<String, String> mappedRow = new HashMap<String, String>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i + 1);

                    mappedRow.put(columnName, rs.getString(i + 1));
                }
                result.add(mappedRow);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return result;
    }

    protected static List<Map<String, String>> Query(String query, String[] parameters) throws SQLException {
        return executeQuery(query, parameters, QueryType.SELECT);
    }

    protected static void Update(String query, String[] parameters) throws SQLException {
        List<Map<String, String>> result = executeQuery(query, parameters, QueryType.UPDATE);
    }

    protected static Map<String, String> querySingle(String query, String[] parameters) throws SQLException {
        List<Map<String, String>> result = executeQuery(query, parameters, QueryType.SELECT);
        assert (result.size() <= 1); // Er mag maar 1 row terug komen, anders moet je Query gebruiken

        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
