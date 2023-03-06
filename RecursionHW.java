import java.io.*;

public class RecursionHW{
    public static void main(String[]args)throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        
        System.out.println("Enter the number you want to find the factorial for");
        System.out.println(factorial(Integer.parseInt(br.readLine())));

        System.out.println("Enter two numbers you want to find the gcf to");
        int one = Integer.parseInt(br.readLine());
        int two = Integer.parseInt(br.readLine());
        System.out.println(gcf(one, two));

        System.out.println("Enter the base and the power");
        int base = Integer.parseInt(br.readLine());
        int power = Integer.parseInt(br.readLine());
        System.out.println(exponent(base, power));

        System.out.println("Enter the value you want to find in the array");
        int array[] = {1, 3, 5, 6, 7, 8, 9 , 9, 9};
        System.out.println(binarySearch(array, Integer.parseInt(br.readLine()), 0, 9));
        
        System.out.println("Enter the number of fibonacci numbers you want to calculate");
        System.out.println(fibonacci(Integer.parseInt(br.readLine()), 1, 1));
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

    public static int exponent(int base, int power){
        if(power<0){
            return 1/exponent(base, -1*power);
        }
        else if(power == 0){
            return 1;
        } 
        else{
            return base*exponent(base, power-1);
        }
    } 

    public static int binarySearch(int array[], int target, int start, int end){
        int middle = (start+end)/2;
        if(array[middle] == target){
            return middle;
        }
        else if(array[middle]>target){ //take lower half
            return binarySearch(array, target, start, middle);
        }
        else{
            return binarySearch(array, target, middle, end);
        }
        
    }

    //another method to sort using binarysearch
    public static int fibonacci(int targetI, int x, int y){
        int z = x+y;
        if(targetI>1){
            return fibonacci(targetI-1, y, z);
        } else{
            return z;
        }
    }
}