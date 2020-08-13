package com.apicatalog.alps.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class AlpsCommand {

    public static void main(String...args) {
        
        final PrintStream output = System.out;
        
        if (args == null || args.length == 0) {
            PrintUtils.printUsage(output);
            return;
        }
        
        try {
            
            switch (args[0]) {
            case Constants.VALIDATE:
                Validator.validate(output, Arrays.copyOfRange(args, 1, args.length));
                break;
                
//            case Constants.TRANSFORM:
//                transform(output, Arrays.copyOfRange(args, 1, args.length));
//                break;
                
            default:
                PrintUtils.printUsage(output);
                return;
            }
            
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
