package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;

public class DAL {
    

    public static ArrayDeque<DailyTerm> showTerms() {
        ArrayDeque<DailyTerm> currentTerms = new ArrayDeque<>();

        try (Connection conn = JDBC.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM dailyTerms ORDER BY id;");
            //ps.setInt(1, 5);
            ResultSet rs = ps.executeQuery();
            DailyTerm term;
            while (rs.next()){
                term = new DailyTerm(rs.getString("term"), rs.getTimestamp("created_date").toLocalDateTime(), rs.getTimestamp("renewed_date").toLocalDateTime());
                currentTerms.add(term);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentTerms;
    }

    
}
