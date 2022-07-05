import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int taskList = scanner.nextInt();
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> maxStack = new Stack<>();
        Queue<Integer> result = new ArrayDeque<>();


        for (int i = 0; i < taskList; i++) {
            String cmd = scanner.next();

            if (cmd.equals("push")) {
                int element = scanner.nextInt();
                int max = element;
                if(!maxStack.isEmpty() && max < maxStack.peek()) {
                    max = maxStack.peek();
                }

                stack.push(element);
                maxStack.push(max);
            }
            if (cmd.equals("pop")) {
                maxStack.pop();
                stack.pop();
            }

            if (cmd.equals("max")) {
                result.offer(maxStack.peek());
            }
        }

        for (int x : result) {
            System.out.println(x);
        }



    }
}