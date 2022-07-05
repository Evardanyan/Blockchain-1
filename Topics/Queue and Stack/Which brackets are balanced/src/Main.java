import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String text = scanner.next();

        System.out.println(isValid(text));


    }

    static boolean isValid(String text) {
        List<String> bracketLists = new ArrayList<>();
        char[] arr = text.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (String.valueOf(arr[i]).equals("{") || String.valueOf(arr[i]).equals("(") || String.valueOf(arr[i]).equals("[")
                    || String.valueOf(arr[i]).equals(")") || String.valueOf(arr[i]).equals("]") || String.valueOf(arr[i]).equals("}")) {
                bracketLists.add(String.valueOf(arr[i]));
            }
        }

        boolean flag = true;
        List<String> tmpRight = new ArrayList<>();
        List<String> tmpReverse = new ArrayList<>();

        for (int j = 0; j < bracketLists.size(); j++) {
            if (bracketLists.get(j).equals("(") || bracketLists.get(j).equals("{") || bracketLists.get(j).equals("[")) {
                tmpRight.add(bracketLists.get((j)));
            } else if (bracketLists.get(j).equals(")") || bracketLists.get(j).equals("}") || bracketLists.get(j).equals("]")) {
                tmpReverse.add(bracketLists.get((j)));
            }
        }
        int delta = tmpReverse.size() - 1;

        if (bracketLists.size() == 0) {
            flag = true;
        } else {
            for (int i = 0; i < tmpRight.size(); i++) {
                if (tmpRight.get(i).equals("{")) {
                    String reversed1 = "}";
                    if (!(reversed1.equals(tmpReverse.get(delta--)))) {
                        flag = false;
                        break;
                    }
                } else if (tmpRight.get(i).equals("(")) {
                    String reversed2 = ")";
                    if (!(reversed2.equals(tmpReverse.get(delta--)))) {
                        flag = false;
                        break;
                    }
                } else if (tmpRight.get(i).equals("[")) {
                    String reversed3 = "]";
                    if (!(reversed3.equals(tmpReverse.get(delta--)))) {
                        flag = false;
                        break;
                    }
                }
            }
        }
        return flag;
    }
}