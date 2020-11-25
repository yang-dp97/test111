import KeyGenerate.*;

import java.util.Scanner;

public class test1 {

    public static void main(String args[]){

        //程序开始时间
        long startTime=System.currentTimeMillis();

        Parameter.main();



        long endTime=System.currentTimeMillis();
        System.out.println("Program running time="+(endTime-startTime)+"ms");

    }
}
