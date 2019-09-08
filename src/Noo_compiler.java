import java.io.FileWriter;
import java.io.IOException;
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
    public void compile(){
        try {
            fw.write("#include<stdio.h>\n" +
                    "int main(void){\n" +
                    "\tint r;\n");
            compile_ntc(this.head);
            fw.write("\treturn 0;\n" +
                    "}");
            fw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void compile_ntc(Node node){
        if(node.token.equals(Noo_symbol.IFXYZ.getString())){
            // """"" 경우 x,y,z 에 대해서 inner_run을 한뒤 x가 !=0 인지 확인하고 참이면 y 아니면 z 하도록 한다.
            Node x = node.next;
            inner_run(x);
            try {
                fw.write("\tif(r != 0) {\n\t");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Node y = x.next;
            inner_run(y);
            try {
                fw.write("\t}\n\telse{\n\t");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Node z = y.next;
            inner_run(z);
            try {
                fw.write("\t}\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            node = z.next;
        }
        if(node != null){
            // """""" 가 아닌 경우에는 inner_run을 해주면 된다.
            inner_run(node);
        }
        if(node != null && node.next !=null){
            compile_ntc(node.next);
            // next가 있는 경우는 next에 대해서 compile 해준다.
        }
    }



    public void inner_run(Node node) {
        if (node.inner_next != null) {
            // leaf 노드에서 부터 거꾸로 올라오면서 진행
            inner_run(node.inner_next);
        }
        if (node.token.equals(Noo_symbol.ZERO.getString())) {
            try {
                //""" 인경우 r=0
                fw.write("\tr=0;\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (node.token.equals(Noo_symbol.ADDONE.getString())) {
            try {
                // "" 인 경우 r = r + 1
                fw.write("\tr = r+1;\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (node.token.equals(Noo_symbol.PRINT.getString())) {
            try {
                // " 인 경우 printf("%d",r)
                fw.write("\tprintf(\"%d\",r);\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
