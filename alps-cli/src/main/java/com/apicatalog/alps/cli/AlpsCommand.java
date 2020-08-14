/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
