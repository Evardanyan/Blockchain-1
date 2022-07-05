import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        Stack<Integer> queue = new Stack<>();
        for (int i = 0; i < size; i++) {
            queue.push(scanner.nextInt());
        }

        while (!queue.empty()) {
            System.out.println(queue.pop());
        }



    }
}