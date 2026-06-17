package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

public class DAL {
    
    public static void addTerm(String newTerm) {
        try (Connection conn = JDBC.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO dailyTerms (term, created_date, renewed_date) VALUES (?, NOW(), NOW());");

            ps.setString(1, newTerm);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayDeque<DailyTerm> showTerms() {
        ArrayDeque<DailyTerm> currentTerms = new ArrayDeque<>();

        try (Connection conn = JDBC.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM dailyTerms ORDER BY id;");
            
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
