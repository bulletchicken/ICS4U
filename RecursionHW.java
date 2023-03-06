import java.io.*;

public class RecursionHW{
    public static void main(String[]args){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(gcf(4, 3));
    }

    public static int factorial(int n){
        if(n==1){
            return 1;
        } else if(n< 0){
            return -1;
        }
        return n*factorial(n-1);
    }

    public static int gcf(int m, int n) {
        if(m>n){
            return gcf(n, m-n);
        } else if(m<n){
            return gcf(n, m);
        } else {
            return m;
        }
    }
}