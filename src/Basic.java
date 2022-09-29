import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This program reads a file to lex, parse, and interpret.
 *
 * Christian Schmidt
 * CSI 311 - Michael Phipps
 * 2021
 */
public class Basic {

    /**
     * The main method executes at program execution.
     * @param args arguments from command line.
     */
    public static void main(String[] args) {

        // if there is not only one argument, inform user and quit
        if (args.length != 1){
            System.out.println("Please pass one argument for filename.");
            System.exit(1);
        }

        // make list for tokens
        ArrayList<Token> tokens = new ArrayList<>();

        // attempt to read file and iterate over lines
        try {
            for(String line : Files.readAllLines(Path.of(args[0]))){
                Lexer lexer = new Lexer();
                try{
                    tokens.addAll(lexer.lex(line));
                } catch (Exception e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e){
            System.out.println("Failed to read file, please check filename.");
        }

        // attempt to parse line then interpret it
        Parser parser = new Parser(tokens);
        if (tokens.size() > 0) {
            try {
                // parse
                StatementsNode node = parser.parse();
                // interpret
                Interpreter interpreter = new Interpreter(node);
                interpreter.initialize();
                // state completion
                System.out.println("\nDone!\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("File not readable (no tokens found).");

    }
}
