package main.refactoring;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryCalculator {

    public static final int FIRST_NAME_INDEX = 1;
    public static final int LAST_NAME_INDEX = 2;
    public static final int WORKER_EARNING_INDEX = 3;
    public static final int ID_INDEX = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader csvReader = readCsv(args);
        String row;
        Map<Integer, Worker> workers = new HashMap<>();
        while ((row = csvReader.readLine()) != null) {
            String[] readRow = row.split(",");
            System.out.print(String.format("%s %s earned: %s \n", readRow[FIRST_NAME_INDEX], readRow[LAST_NAME_INDEX], readRow[WORKER_EARNING_INDEX]));
            Worker tempWorker = workers.get(Integer.parseInt(readRow[ID_INDEX]));
            if (tempWorker == null) {
                tempWorker = new Worker(Integer.parseInt(readRow[ID_INDEX]), readRow[FIRST_NAME_INDEX], readRow[LAST_NAME_INDEX]);
            }
            tempWorker.addEarning(parseSalary(readRow));
            workers.put(Integer.parseInt(readRow[ID_INDEX]), tempWorker);
        }
        csvReader.close();
        for (Worker worker : workers.values()) {
            Double sumForWorker = 0.0;
            for (Double money : worker.totalEarnings) {
                sumForWorker += money;
            }
            System.out.println(String.format("Total earnings for worker %s %.2f", worker.firstName, sumForWorker));
        }

        double x = 0.0;
        for (Worker worker : workers.values()) {
            for (Double money : worker.totalEarnings) {
                x += money;
            }
        }
        System.out.println(String.format("Total earnings for all workers %.2f", x));
    }

    private static Double parseSalary(String[] row) {
        return Double.parseDouble(row[WORKER_EARNING_INDEX]);
    }

    public static BufferedReader readCsv(String[] args) throws FileNotFoundException {
        if (args.length < 1) {
            InputStream resourceAsStream = SalaryCalculator.class.getResourceAsStream("/resources/account_data.csv");
            return new BufferedReader(new InputStreamReader(resourceAsStream));
        } else if (args.length == 1) {
            return new BufferedReader(new FileReader(args[0]));
        } else {
            throw new IllegalArgumentException("Unsupported number of arguments, expecting single file path");
        }
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

        public void addEarning(Double earning){
            this.totalEarnings.add(earning);
        }
    }
}
