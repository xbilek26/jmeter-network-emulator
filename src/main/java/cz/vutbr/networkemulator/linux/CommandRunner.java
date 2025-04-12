package cz.vutbr.networkemulator.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandRunner {

    private static class SingletonHolder {

        private static final CommandRunner INSTANCE = new CommandRunner();
    }

    public static CommandRunner getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public String runCommand(String command, CommandType type) {
        StringBuilder output = new StringBuilder();

        switch (type) {
            case NETWORK_CONFIGURATION -> {
                String[] lines = command.split("\n");
                for (String line : lines) {
                    if (line.contains("qdisc")) {
                        String cmd = parseTcOutput(line);
                        if (cmd != null) {
                            try {
                                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", cmd);
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
                        }
                    }
                }
            }
            case GENERIC_COMMAND -> {
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
            }
            case FILTER -> {
                // TODO
            }
            default ->
                throw new IllegalArgumentException("Unsupported command type");
        }

        return output.toString().trim();
    }

    private static String parseTcOutput(String line) {
        String[] parts = line.split("\\s+");

        String dev = null;
        String qdisc = null;
        Map<String, String> params = new LinkedHashMap<>();

        for (int i = 0; i < parts.length; i++) {
            switch (parts[i]) {
                case "dev" ->
                    dev = parts[i + 1];
                case "qdisc" ->
                    qdisc = parts[i + 1];
                case "limit" ->
                    params.put(parts[i], parts[i + 1].substring(0, parts[i + 1].length() - 1));
                case "quantum", "target", "interval", "memory_limit", "drop_batch" ->
                    params.put(parts[i], parts[i + 1]);
                case "ecn" ->
                    params.put("ecn", null);
            }
        }

        StringBuilder command = new StringBuilder("sudo tc qdisc replace dev " + dev + " root handle 0 " + qdisc);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                System.out.println("  " + entry.getKey() + " " + entry.getValue());
                command.append(" ");
                command.append(entry.getKey());
                command.append(" ");
                command.append(entry.getValue());
            }
        }

        System.out.println("parsed command " + command.toString());

        return command.toString();
    }

}
