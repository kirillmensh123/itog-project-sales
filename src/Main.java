import model.SaleRecord;
import service.CsvReader;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.Connection;
import service.DatabaseManager;



public class Main {
    public static void main(String[] args) {

        DatabaseManager.initDatabase();

        List<SaleRecord> data =
                CsvReader.read("data/Продажа продуктов в мире.csv");

        DatabaseManager.clearSales();
        DatabaseManager.insertSales(data);
        DatabaseManager.printCount();
        DatabaseManager.printUnitsByRegionSQL();
        DatabaseManager.printTopCountryEuropeAsiaSQL();
        DatabaseManager.printCountryInRangeSQL();


        System.out.println("Загружено записей: " + data.size());

        for (int i = 0; i < Math.min(5, data.size()); i++) {
            SaleRecord r = data.get(i);
            System.out.println(
                    r.getCountry() + " | " +
                            r.getRegion() + " | " +
                            r.getTotalRevenue()
            );
        }

        // ЗАДАНИЕ 1
        Map<String, Integer> unitsByRegion = new HashMap<>();

        for (SaleRecord r : data) {
            unitsByRegion.merge(
                    r.getRegion(),
                    r.getUnitsSold(),
                    Integer::sum
            );
        }

        System.out.println("\nОбщее количество проданных товаров по регионам:");
        for (Map.Entry<String, Integer> entry : unitsByRegion.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        //график зад.1
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : unitsByRegion.entrySet()) {
            dataset.addValue(
                    entry.getValue(),
                    "Units Sold",
                    entry.getKey()
            );
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Общее количество проданных товаров по регионам",
                "Регион",
                "Количество проданных товаров",
                dataset
        );

        ChartFrame frame = new ChartFrame("Продажи по регионам", chart);
        frame.pack();
        frame.setVisible(true);
        //  ЗАДАНИЕ 2
// Страна с максимальным общим доходом в Европе и Азии

        Map<String, Double> revenueByCountry = new HashMap<>();

        for (SaleRecord r : data) {
            String region = r.getRegion();

            if (region.equals("Europe") || region.equals("Asia")) {
                revenueByCountry.merge(
                        r.getCountry(),
                        r.getTotalRevenue(),
                        Double::sum
                );
            }
        }

        String bestCountry = null;
        double maxRevenue = 0;

        for (Map.Entry<String, Double> entry : revenueByCountry.entrySet()) {
            if (entry.getValue() > maxRevenue) {
                maxRevenue = entry.getValue();
                bestCountry = entry.getKey();
            }
        }

        System.out.println("\nЗадание 2:");
        System.out.println(
                "Страна с максимальным доходом в Европе и Азии: "
                        + bestCountry + " (" + maxRevenue + ")"
        );

        // ЗАДАНИЕ 3
// Страна с доходом 420–440 тыс в MENA и Sub-Saharan Africa

        Map<String, Double> revenueByCountryTask3 = new HashMap<>();

        for (SaleRecord r : data) {
            String region = r.getRegion();

            if (region.equals("Middle East and North Africa")
                    || region.equals("Sub-Saharan Africa")) {

                revenueByCountryTask3.merge(
                        r.getCountry(),
                        r.getTotalRevenue(),
                        Double::sum
                );
            }
        }

        String bestCountryTask3 = null;
        double bestRevenueTask3 = 0;

        for (Map.Entry<String, Double> entry : revenueByCountryTask3.entrySet()) {
            double revenue = entry.getValue();

            if (revenue >= 420_000 && revenue <= 440_000) {
                if (revenue > bestRevenueTask3) {
                    bestRevenueTask3 = revenue;
                    bestCountryTask3 = entry.getKey();
                }
            }
        }

        System.out.println("\nЗадание 3:");
        if (bestCountryTask3 != null) {
            System.out.println(
                    "Страна с доходом 420–440 тыс (MENA и Sub-Saharan Africa): "
                            + bestCountryTask3 + " (" + bestRevenueTask3 + ")"
            );
        } else {
            System.out.println(
                    "Страна с доходом в диапазоне 420–440 тыс не найдена"
            );
        }

    }
}

