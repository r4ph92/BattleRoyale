public class Console {
    private static final String RESET = "\033[0m";// Text Reset

    public static void println(String message, Color color) {
        System.out.println(color.getCode() + message + RESET);
    }

    public static void print(String message, Color color) {
        System.out.print(color.getCode() + message + RESET);
    }
}
