package cz.vutbr.networkemulator.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandRunner {

    public static String runCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String lineOutput;
            while ((lineOutput = reader.readLine()) != null) {
                output.append(lineOutput).append("\n");
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            output.append("Error executing command: ").append(e.getMessage()).append("\n");
        }
        return output.toString().trim();
    }
}
