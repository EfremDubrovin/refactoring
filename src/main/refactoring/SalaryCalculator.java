package main.refactoring;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryCalculator {

    public static void main(String[] args) throws IOException {
        BufferedReader csvReader;
        if (args.length < 1) {
            InputStream resourceAsStream = SalaryCalculator.class.getResourceAsStream("/resources/account_data.csv");
            csvReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        } else if (args.length == 1) {
            csvReader = new BufferedReader(new FileReader(args[0]));
        } else {
            throw new IllegalArgumentException("Unsupported number of arguments, expecting single file path");
        }
        String row;
        Map<Integer, Worker> dataMap = new HashMap<>();
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            System.out.print(String.format("%s %s earned: %s \n", data[1], data[2], data[3]));
            Worker tempWorker = dataMap.get(Integer.parseInt(data[0]));
            if (tempWorker == null) {
                tempWorker = new Worker(Integer.parseInt(data[0]), data[1], data[2]);
                tempWorker.totalEarnings.add(Double.parseDouble(data[3]));
                dataMap.put(Integer.parseInt(data[0]), tempWorker);
            } else {
                tempWorker.totalEarnings.add(Double.parseDouble(data[3]));
            }
        }
        csvReader.close();
        for (Worker worker : dataMap.values()) {
            Double sumForWorker = 0.0;
            for (Double money : worker.totalEarnings) {
                sumForWorker += money;
            }
            System.out.println(String.format("Total earnings for worker %s %.2f", worker.firstName, sumForWorker));
        }

        double x = 0.0;
        for (Worker worker : dataMap.values()) {
            for (Double money : worker.totalEarnings) {
                x += money;
            }
        }
        System.out.println(String.format("Total earnings for all workers %.2f", x));
    }

    private static class Worker {
        private Integer id;
        private String firstName;
        private String lastName;
        private List<Double> totalEarnings = new ArrayList<>();

        Worker(Integer id, String firstName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
}
