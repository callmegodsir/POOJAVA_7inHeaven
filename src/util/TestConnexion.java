package util;

import config.DatabaseConfig;
import java.sql.Connection;

public class TestConnexion {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            System.out.println("Connexion réussie !");
            conn.close();
        } catch (Exception e) {
            System.out.println("Échec de connexion :");
            e.printStackTrace();
        }
    }
}