package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Scanner;

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

    public static void renewTerm(String renewedTerm, Scanner scanner) {
        try (Connection conn = JDBC.getConnection()) {
            ArrayDeque<DailyTerm> expiredTerms = showTerms();

            for (DailyTerm term : expiredTerms) {
                if (renewedTerm.equalsIgnoreCase(term.term)) {
                    System.out.print("type 'renew' to confirm: ");
                    String input = scanner.nextLine();
                    if (input.equals("renew")) {
                        PreparedStatement ps = conn.prepareStatement("UPDATE dailyTerms SET renewed_date = NOW() WHERE term = ?;");
                        ps.setString(1, renewedTerm);
                        ps.executeUpdate();
                        System.out.println("It has been renewed");
                        return;
                    } else {
                        System.out.println("seems you've changed your mind");
                    }
                    break;
                }
            }
            System.out.println("term does not exist in database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editTerm(String editedTerm, Scanner scanner) {
        try (Connection conn = JDBC.getConnection()) {
            ArrayDeque<DailyTerm> allTerms = showTerms();

            for (DailyTerm term : allTerms) {
                if (editedTerm.equalsIgnoreCase(term.term)) {
                    String sql = "UPDATE dailyTerms SET term = ? WHERE term = ?";
                    String sql2 = "UPDATE dailyTerms SET bMaterial = ? WHERE term = ?";
                    
                    System.out.println("enter (A) to edit term, (B) to edit b-material...");
                    String choice = scanner.nextLine();
                    switch (choice) {
                        case "a" -> {
                            System.out.println("enter replacement term: ");
                            String replacementTerm = scanner.nextLine();
                            System.out.println("your new replaced term is: " + replacementTerm);
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, replacementTerm);
                            ps.setString(2, term.term);
                            ps.executeUpdate();
                        }
                        case "b" -> {
                            System.out.println("enter replacement b-material/description: ");
                            String replacementBMaterial = scanner.nextLine();
                            System.out.println("your new replaced b-material is: " + replacementBMaterial);
                            PreparedStatement ps = conn.prepareStatement(sql2);
                            ps.setString(1, replacementBMaterial);
                            ps.setString(2, term.term);
                            ps.executeUpdate();
                        }
                        default -> System.out.println("invalid choice");
                    }
                    return;
                }
            }
            System.out.println("that term does not exist");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeTerm(String removedTerm, Scanner scanner) {
        try (Connection conn = JDBC.getConnection()) {
            ArrayDeque<DailyTerm> currentTerms = showTerms();
            
            for (DailyTerm term : currentTerms) {
                if (removedTerm.equalsIgnoreCase(term.term)) {
                    System.out.print("type 'remove' to confirm: ");
                    String input = scanner.nextLine();
                    if (input.equals("remove")) {
                        //remove term MySQL string
                        PreparedStatement ps = conn.prepareStatement("DELETE FROM dailyTerms WHERE term = ?;");
                        ps.setString(1, removedTerm);
                        ps.executeUpdate();
                        System.out.println("It has been removed from the list");
                        return;
                    } else {
                        System.out.println("seems you've changed your mind");
                    }
                    break;
                } 
            }
            System.out.println("term does not exist in database.");

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
                term = new DailyTerm(rs.getString("term"), rs.getTimestamp("created_date").toLocalDateTime(), rs.getTimestamp("renewed_date").toLocalDateTime(), rs.getString("bMaterial"));
                currentTerms.add(term);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentTerms;
    }

    
}
