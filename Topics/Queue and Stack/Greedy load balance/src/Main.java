import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        final Scanner scanner = new Scanner(System.in);
        int tasks = scanner.nextInt();
        Queue<Integer> first = new ArrayDeque<>();
        int firstLoad = 0;
        Queue<Integer> second = new ArrayDeque<>();
        int secondLoad = 0;

        for (var i = 0; i < tasks; i++) {
            int task = scanner.nextInt();
            int load = scanner.nextInt();

            if (firstLoad <= secondLoad) {
                first.offer(task);
                firstLoad += load;
            } else {
                second.offer(task);
                secondLoad += load;
            }
        }

        first.forEach(num -> System.out.print(num + " "));
        System.out.println();
        second.forEach(num -> System.out.print(num + " "));


    }
}