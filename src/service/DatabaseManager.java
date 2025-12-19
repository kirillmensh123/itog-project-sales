package service;

import model.SaleRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:data/sales.db";

    // Создание БД и таблицы
    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = """
                    CREATE TABLE IF NOT EXISTS sales (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        region TEXT,
                        country TEXT,
                        product TEXT,
                        units_sold INTEGER,
                        total_profit REAL
                    );
                    """;

            stmt.execute(sql);
            System.out.println("База данных и таблица sales готовы");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Вставка данных в таблицу
    public static void insertSales(List<SaleRecord> data) {
        String sql = """
                INSERT INTO sales (region, country, product, units_sold, total_profit)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (SaleRecord r : data) {
                ps.setString(1, r.getRegion());
                ps.setString(2, r.getCountry());
                ps.setString(3, r.getProduct());
                ps.setInt(4, r.getUnitsSold());
                ps.setDouble(5, r.getTotalRevenue());

                ps.addBatch();
            }

            ps.executeBatch();
            System.out.println("Данные успешно сохранены в таблицу sales");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Проверка количества записей
    public static void printCount() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sales")) {

            if (rs.next()) {
                System.out.println(
                        "Количество записей в таблице sales: " + rs.getInt(1)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //запрос 1
    public static void printUnitsByRegionSQL() {
        String sql = """
            SELECT region, SUM(units_sold) AS total_units
            FROM sales
            GROUP BY region
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nSQL-запрос 1:");
            System.out.println("Общее количество проданных товаров по регионам:");

            while (rs.next()) {
                String region = rs.getString("region");
                int totalUnits = rs.getInt("total_units");

                System.out.println(region + " -> " + totalUnits);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void clearSales() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM sales");
            System.out.println("Таблица sales очищена");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Запрос 2
    public static void printTopCountryEuropeAsiaSQL() {
        String sql = """
            SELECT country, SUM(total_profit) AS total_income
            FROM sales
            WHERE region IN ('Europe', 'Asia')
            GROUP BY country
            ORDER BY total_income DESC
            LIMIT 1
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nSQL-запрос 2:");
            System.out.println("Страна с максимальным доходом в Европе и Азии:");

            if (rs.next()) {
                String country = rs.getString("country");
                double income = rs.getDouble("total_income");

                System.out.printf("%s (%.2f)%n", country, income);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 3 запрос
    public static void printCountryInRangeSQL() {
        String sql = """
            SELECT country, SUM(total_profit) AS total_income
            FROM sales
            WHERE region IN ('Middle East and North Africa', 'Sub-Saharan Africa')
            GROUP BY country
            HAVING total_income BETWEEN 420000 AND 440000
            ORDER BY total_income DESC
            LIMIT 1
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nSQL-запрос 3:");
            System.out.println("Страна с доходом 420–440 тыс (MENA и Sub-Saharan Africa):");

            if (rs.next()) {
                String country = rs.getString("country");
                double income = rs.getDouble("total_income");

                System.out.printf("%s (%.2f)%n", country, income);
            } else {
                System.out.println("Страна с доходом в заданном диапазоне не найдена");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
