package com.feut.server.db.facades;

import com.feut.server.db.DBConnection;
import com.google.common.base.CaseFormat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade {

    // TODO: Error afhandeling!
    public static List<Map<String, String>> Query(String query, String[] parameters) {
        List<Map<String, String>> result = new ArrayList<>();
        PreparedStatement stmt = null;
        try {
            stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                stmt.setString(i + 1, parameters[i]);
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
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }
        }

        return result;
    }

    public static Map<String, String> querySingle(String query, String[] parameters) {
        List<Map<String, String>> result = Query(query, parameters);
        assert(result.size() <= 1); // Er mag maar 1 row terug komen, anders moet je Query gebruiken

        return result.get(0);
    }
}
