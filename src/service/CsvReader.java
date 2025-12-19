package service;

import model.SaleRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<SaleRecord> read(String path) {
        List<SaleRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 8) continue;

                String region = parts[0];
                String country = parts[1];
                String product = parts[2];

                int unitsSold = Integer.parseInt(parts[6]);
                double totalRevenue = Double.parseDouble(parts[7]);

                records.add(new SaleRecord(
                        region, country, product, unitsSold, totalRevenue
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}

