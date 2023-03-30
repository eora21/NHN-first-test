package terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import socket.Client;

import java.io.IOException;

public class Terminal {

    public static void main(String[] args) {
        Options options = new Options();
        Option host = Option.builder("H")
                .hasArg()
                .argName("host")
                .desc("접속할 서버의 호스트 네임 또는 IP를 지정합니다.")
                .build();

        options.addOption(host);

        Option help = Option.builder("h")
                .desc("도움말")
                .build();

        options.addOption(help);

        Option id = Option.builder("I")
                .hasArg()
                .argName("user id")
                .desc("채팅에서 사용할 사용자 아이디를 지정합니다.")
                .build();

        options.addOption(id);

        Option port = Option.builder("p")
                .hasArg()
                .argName("port")
                .desc("접속할 서버의 서비스 포트를 지정합니다.")
                .build();

        options.addOption(port);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            Client client = new Client();


            if (cmd.hasOption("H")) {
                client.setHost(cmd.getOptionValue("H"));
            }

            if (cmd.hasOption("h")) {
                printHelp(options);
            }

            if (cmd.hasOption("I")) {
                client.setUserId(cmd.getOptionValue("I"));
            }

            if (cmd.hasOption("p")) {
                client.setPort(Integer.parseInt(cmd.getOptionValue("p")));
            }

            client.start();

        } catch (ParseException | NumberFormatException e) {
            printHelp(options);
        } catch (IOException e) {
            System.err.println("소켓 에러");
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
