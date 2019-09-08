import java.io.FileWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

    Noo_compiler(String input, FileWriter fw) {
        this.input = input;
        this.fw = fw;
        final String[] tokens = input.split("'");
        List tmp = Arrays.asList(tokens).subList(1, tokens.length);
        this.head = generator(tmp.iterator());
    }

    public <T> Node generator(Iterator<T> iter) {
        T token = iter.next();
        if (token.equals("\"\"\"")) {
            return new Node(token); // """ 면 leaf 노드 니까 노드 생성하고 return
        }
        if (token.equals("\"\"\"\"")) {
            return new Node(token, generator(iter)); // """"면 leaf 노드 아니니까 노드 생성하고 next에 다음 node
        }
        if (token.equals("\"\"\"\"\"")) {
            return new Node(token, generator(iter)); // """""면 leaf 노드 아니니까 노드 생성하고 next에 다음 node
        } else {
            Node head = new Node(token);
            head.inner_next = generator(iter);
            if (iter.hasNext()) {
                head.next = generator(iter);
            }
            return head;                            // """"","""",""" 아닌 경우는 leaf가 아닌 inner node에 속한다.
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
