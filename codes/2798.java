import java.util.Arrays;
import java.util.Scanner;

public class Main {
    //5시 33분 시작
    public static void main(String[] args) {
        int n,m;//카드갯수, 딜러가 외치는 숫자
        int x=0;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        int []arr = new int[n];
        for(int i=0;i<n;i++){
            arr[i] = sc.nextInt();
        }
        //세개의 합을 확인해봐야해
        //일단 저 숫자들을 정렬해보자
        Arrays.sort(arr);


        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                for(int k=j+1;k<n;k++){
                    if(arr[i] + arr[j] + arr[k] <= m && arr[i] + arr[j] + arr[k] > x) x = arr[i] + arr[j] + arr[k];

                }
            }
        }
        System.out.println(x);
    }

}
