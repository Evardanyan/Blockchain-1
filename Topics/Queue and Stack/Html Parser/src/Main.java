import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
//        String[] text = scanner.next().split("^\\<\\/?.*\\>$");
        String text = scanner.next();
        String result = text.replaceAll("<[^>]*>", "");
        System.out.println(result);
//        System.out.println(Arrays.toString(text));
    }
}