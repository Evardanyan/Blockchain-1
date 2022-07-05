import java.util.*;

public class Main {

    public static void main(String[] args) {  
        // write your code here
        Queue<Integer> queue = new ArrayDeque<>();

        queue.offer(2);
        queue.offer(0);
        queue.offer(1);
        queue.offer(7);

        System.out.println(queue);
    }
}