public class Student {

    String UID;
    int age;
    String name;
    int phone_no;

    Student() {
        UID = "24BCS";
        age = 19;
    }
    Student(String name, int phone_no) {
        this.name = name;
        this.phone_no = phone_no;
    }
    public static void display(Student s) {
        System.out.println(s.UID);
        System.out.println(s.name);
        System.out.println(s.age);
        System.out.println(s.phone_no);
    }

    public static void main(String[] args) {

        Student s = new Student();
        Student s1 = new Student("XXYYZZ", 123456789);

        s.name = s1.name;
        s.phone_no = s1.phone_no;
        display(s);
    }
}
