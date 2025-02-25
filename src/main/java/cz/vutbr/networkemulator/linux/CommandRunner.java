package cz.vutbr.networkemulator.linux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CommandRunner {

    public CommandOutput runCommand(String command) {

        List<String> commandList = Arrays.asList(command.split("\\s+"));

        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandList);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "start";
            
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            return new CommandOutput(output.toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }

        return new CommandOutput("null");
    }


}
