package com.apicatalog.alps;

import java.io.PrintStream;

public final class Command {

    public static void main(String[] args) {
        
        final PrintStream output = System.out;
        
        printUsage(output);
    }

    private static void printUsage(final PrintStream output) {
        output.println("Usage:");
        output.println("   alps validate [input]");
        output.println("   alps transform [{-s|--source}={json|xml}] [input] {-t|--target}={json|xml} [output]");
        output.println("   alps [{-h|--help}]");
    }
    
}
