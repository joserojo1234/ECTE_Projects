package Project1ECTE331;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static final Random rng = new Random();
    public static double previousValidSensorReading = -1.0;

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Enter the maximum temperature value: ");
        double maxAllowedTemp = inputScanner.nextDouble();

        double tempReading = createTemperature(maxAllowedTemp);
        System.out.println("Random Temperature: " + tempReading);

        double humidityReading = createHumidity();
        System.out.println("Random Humidity: " + humidityReading);

        double[] sensorGroup = new double[3];
        for (int idx = 0; idx < 3; idx++) {
            sensorGroup[idx] = simulateSensor3();
            System.out.println("Sensor 3." + (idx + 1) + " reads: " + sensorGroup[idx]);
        }

        double finalSensorValue = resolveMajority(sensorGroup);
        System.out.println("Final chosen Sensor 3 value: " + finalSensorValue);
    }

    public static double createTemperature(double maxLimit) {
        return rng.nextFloat() * maxLimit;
    }

    public static double createHumidity() {
        return 20 + rng.nextDouble() * 60;
    }

    public static double simulateSensor3() {
        return rng.nextDouble() * 100;
    }

    public static double resolveMajority(double[] readings) {
        double val1 = readings[0];
        double val2 = readings[1];
        double val3 = readings[2];

        double margin = 0.001;

        if (Math.abs(val1 - val2) < margin || Math.abs(val1 - val3) < margin) {
            previousValidSensorReading = val1;
            return val1;
        } else if (Math.abs(val2 - val3) < margin) {
            previousValidSensorReading = val2;
            return val2;
        } else {
            String errorLog = String.format("Discrepancy in Sensor 3 readings: [%.2f, %.2f, %.2f] — No agreement. Fallback to previous: %.2f",
                    val1, val2, val3, previousValidSensorReading);
            FileLogger.log("log", errorLog);
            return previousValidSensorReading;
        }
    }
}
