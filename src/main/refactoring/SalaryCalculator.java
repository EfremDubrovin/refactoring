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
        List<Worker> workers = getWorkersFromCsv(csvReader);
        csvReader.close();
        printAccumulatedResults(getAccumulatedWorkersPoints(workers));
    }

    private static void printAccumulatedResults(Map<Integer, Worker> distinctWorkers) {
        double totalPoints = 0.0;
        for (Worker worker : distinctWorkers.values()) {
            totalPoints += worker.getPoints();
            System.out.println(String.format("Total earnings for worker %s %.2f", worker.getFirstName(), worker.getPoints()));
        }
        System.out.println(String.format("Total earnings for all workers %.2f", totalPoints));
    }

    private static Map<Integer, Worker> getAccumulatedWorkersPoints(List<Worker> workers) {
        Map<Integer, Worker> distinctWorkers = new HashMap<>();
        for (Worker worker : workers) {
            Worker distinctWorker = distinctWorkers.get(worker.getId());
            if (distinctWorker != null) {
                distinctWorker.addPoints(worker.getPoints());
            } else {
                distinctWorkers.put(worker.getId(), worker);
            }
        }
        return distinctWorkers;
    }

    private static List<Worker> getWorkersFromCsv(BufferedReader csvReader) throws IOException {
        String row;
        List<Worker> workers = new ArrayList<>();
        while ((row = csvReader.readLine()) != null) {
            String[] readRow = row.split(",");
            System.out.print(String.format("%s %s earned: %s \n", readRow[FIRST_NAME_INDEX], readRow[LAST_NAME_INDEX], readRow[WORKER_EARNING_INDEX]));
            workers.add(new Worker(parseId(readRow), readRow[FIRST_NAME_INDEX], readRow[LAST_NAME_INDEX], parseSalary(readRow)));
        }
        return workers;
    }

    private static Double parseSalary(String[] row) {
        return Double.parseDouble(row[WORKER_EARNING_INDEX]);
    }

    private static Integer parseId(String[] row) {
        return Integer.parseInt(row[ID_INDEX]);
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
        private Double points;

        Worker(Integer id, String firstName, String lastName, Double points) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.points = points;
        }

        public void addPoints(Double points) {
            this.points += points;
        }

        public Integer getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public Double getPoints() {
            return points;
        }
    }
}
