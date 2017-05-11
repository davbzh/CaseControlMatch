package nkcx;

/**
 * Created by davbzh on 2017-02-08.
 */
import org.apache.commons.cli.*;

public class Cli {
    private String[] args = null;
    private Options options = new Options();

    public Cli(String[] args) {
        this.args = args;
        options.addOption("h", "help", false, "show help.");
        options.addOption("case", "case_input", true, "input case file  location");
        options.addOption("ctrl", "ctrl_input", true, "input ctrl file  location");
        options.addOption("o", "output", true, "output file location");
    }

    public void parse() throws Exception {
        //DefaultParser() is in cli-1.3.1
        CommandLineParser parser = new DefaultParser();
        //CommandLineParser parser = new GnuParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("case") ) {
                CaseControlMatching.process(
                        cmd.getOptionValue("case"),
                        cmd.getOptionValue("ctrl"),
                        cmd.getOptionValue("o")
                );


            } else {
                help();
            }

        } catch (ParseException e) {
            help();
        }
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("brush assembler", options);
        System.exit(0);
    }
}
