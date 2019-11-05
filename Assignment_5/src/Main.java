public class Main {

    public static int sum(int n){
        int result = 0;
        while(n>0){
            result += n;
            n = n-1;
        }
        return result;
    }
    public static void main(String[] args) {
        int n = 100;
        System.out.println(sum(n));
    }
}
