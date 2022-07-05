import java.util.*;

public class Main {

    public static void main(String[] args) {        
        Queue<Integer> queue = new ArrayDeque<>(Arrays.asList(1, 2, 3, 4));

        queue.poll();
        queue.poll();
        queue.poll();
        queue.poll();
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);


        // write your code here

        System.out.println(queue);
    }
}