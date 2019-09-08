import java.io.FileWriter;

public class Noo_compiler {

    String input;
    Node<String> head;
    FileWriter fw;

    public static class Node<T> {

        T token;
        Node<T> inner_next;
        Node<T> next;

        Node(T token) {
            this.token = token;
            this.next = null;
        }

        Node(T token, Node<T> next) {
            this.token = token;
            this.next = next;
        }

    }

}
