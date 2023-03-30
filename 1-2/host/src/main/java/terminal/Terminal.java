package terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import socket.Server;

public class Terminal {

    public static void main(String[] args) {
        Options options = new Options();

        Option host = Option.builder("b")
                .hasArg()
                .argName("arg")
                .desc("블랙리스트")
                .build();

        options.addOption(host);

        Option help = Option.builder("h")
                .desc("도움말")
                .build();

        options.addOption(help);

        Option port = Option.builder("p")
                .hasArg()
                .argName("arg")
                .desc("서비스 포트")
                .build();

        options.addOption(port);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            Server server = new Server();


            if (cmd.hasOption("b")) {
                server.addBlackList(cmd.getOptionValue("b"));
            }

            if (cmd.hasOption("h")) {
                printHelp(options);
            }

            if (cmd.hasOption("p")) {
                server.setPort(Integer.parseInt(cmd.getOptionValue("p")));
            }

            server.start();

        } catch (ParseException | NumberFormatException e) {
            printHelp(options);
        }
    }

    private static void printHelp(Options options) {
        options.getOptions()
                .forEach(option -> {
                    String helpText = String.format("-%s ", option.getOpt()) +
                            String.format("%-15s",
                                    option.hasArg() ? "<" + option.getArgName() + ">" : "") +
                            option.getDescription();

                    System.out.println(helpText);
                });
    }
}
