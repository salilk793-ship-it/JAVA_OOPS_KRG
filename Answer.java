public class Answer {

    public static void main(String[] args) {
        System.out.println(area(5));
        System.out.println(area(6, 10));
    }
    public static int area(int l) {
        int area = l * l;
        return area;
    }
    public static int area(int l, int b) {
        int area = l * b;
        return area;
    }
}

