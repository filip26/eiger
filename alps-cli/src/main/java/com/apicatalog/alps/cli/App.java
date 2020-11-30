package com.apicatalog.alps.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;

@Command(
    name = "alps", 
    description = "Transform and validate ALPS documents",
    subcommands = {Validator.class, Transformer.class},
//    synopsisSubcommandLabel = "(validate | transform)",
    mixinStandardHelpOptions = false,
    descriptionHeading = "%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    commandListHeading = "%nCommands:%n"
    )
public final class App {

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "display a help message")
    boolean help = false;
    
    public static void main(String[] args) {
        
        final CommandLine commandLine = new CommandLine(new App());
        
        final ParseResult result = commandLine.parseArgs(args);
        
        if (commandLine.isUsageHelpRequested()) {

            if (result.subcommand() != null) {
                result.subcommand().commandSpec().commandLine().usage(System.out);
                return;
            }
            
            commandLine.usage(System.out);
            
            return;
        }

        commandLine.execute(args);
    }
}
