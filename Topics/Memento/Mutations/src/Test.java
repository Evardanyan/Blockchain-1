import java.util.*;

class Originator {
    @Override
    public String toString() {
        return "Originator{" +
                "numbers=" + numbers +
                '}';
    }

    private List<Integer> numbers = new ArrayList<>();

    public void addNumber(Integer number) {
        numbers.add(number);
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public Memento getMemento() {
        return new Memento(numbers);

    }

    public void setMemento(Memento memento) {
        this.numbers = memento.getNumbers();
    }

    static class Memento {
        private final List<Integer> numbers;

        private Memento(List<Integer> numbers) {
            this.numbers = numbers;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }


    }
}

class Caretaker {
    private final Originator originator;
    private Originator.Memento snapshot = null;
    private final Deque<Originator.Memento> history = new ArrayDeque<>();

    Caretaker(Originator originator) {
        this.originator = originator;
    }

    public void save() {
        snapshot = originator.getMemento();
        history.push(snapshot);
    }

    public void restore() {
        if (snapshot != null && !history.isEmpty()) {
            originator.setMemento(history.pop());
        }
    }
}

class Main {
    public static void main(String[] args) {
        Originator originator = new Originator();
        Caretaker caretaker = new Caretaker(originator);

        caretaker.save();
        System.out.println(originator);
        originator.addNumber(1);

        System.out.println(originator);
        caretaker.save();
        originator.addNumber(2);

        System.out.println(originator);

    }
}