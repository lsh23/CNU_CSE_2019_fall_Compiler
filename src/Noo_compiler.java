import java.io.FileWriter;

public class Noo_compiler {

    String input;
    Node<String> head;
    FileWriter fw;

    enum Noo_symbol {
        // 상수("연결할 문자")
        IFXYZ("\"\"\"\"\""), ATHANB("\"\"\"\""), ADDONE("\"\""), ZERO("\"\"\""), PRINT("\"");

        final private String name;

        private Noo_symbol(String name) { //enum에서 생성자 같은 역할
            this.name = name;
        }

        public String getString() { // 문자를 받아오는 함수
            return name;
        }
    }

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
