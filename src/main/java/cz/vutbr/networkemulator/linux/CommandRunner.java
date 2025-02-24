package cz.vutbr.networkemulator.linux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CommandRunner {

    public CommandOutput runCommand(String command) {

        String output = "";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(List.of("tc", "qdisc"));
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.concat(line);
                output.concat("\n");
            }
            process.waitFor();

            return new CommandOutput(output);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
