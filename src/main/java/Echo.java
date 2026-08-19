import java.util.Scanner;

/**
 * Entry point for the E.C.H.O. chatbot.
 * E.C.H.O. stands for Everyday Conversational and Helpful Operator.
 */
public class Echo {
    /** Separates user commands and E.C.H.O.'s responses in the console. */
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = """
       ______ _____ _   _  ____  
      |  ____/ ____| | | |/ __ \\ 
      | |__ | |    | |_| | |  | |
      |  __|| |    |  _  | |  | |
      | |___| |____| | | | |__| |
      |______\\_____|_| |_|\\____/ 
      [E]veryday [C]onversational & [H]elpful [O]perator
      """;

    /** Starts E.C.H.O. and processes commands until the user enters {@code bye}. */
    public static void main(String[] args) {
        // Display the banner
        System.out.println(BANNER);
        System.out.println("Greetings! How can I assist you today?");

        System.out.println(SEPARATOR);

        System.out.println("E.C.H.O. signing off. Have a productive day!");

        System.out.println(SEPARATOR);
    }
}
