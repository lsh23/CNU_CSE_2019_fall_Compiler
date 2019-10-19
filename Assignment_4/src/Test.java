public class Test {
    /*
    .class public Test
    .super java/lang/Object
     */

    public Test(){
        super();
    }
    /*
    ; strandard initializer
    .method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
    .end method
    */



    public static int add(int a, int b){
        int c = a + b;
        return c;
    }
    /*
    .method public static add(II)I
    .limit stack 32
    .limit locals 32
    iload_0
    iload_1
    iadd
    istore_2
    iload_2
    ireturn
    .end method
    */


    public static void main(String[] args) {
        int a = 33;
        System.out.println(Test.add(a,1));
    }
    /*
    .method public static main([Ljava/lang/String;)V
    .limit stack 32
    .limit locals 32
    ldc 33
    istore_2
    getstatic java/lang/System/out Ljava/io/PrintStream;
    ldc 1
    iload_2
    invokestatic Test/add(II)I
    invokevirtual java/io/PrintStream/println(I)V
    return
    .end method
     */



}
