package KeyGenerate;

import java.io.*;
import java.math.*;
import java.util.*;

public class Parameter {

    //将字符串格式转换成十进制表示形式的大整数
    public static BigInteger ONE=new BigInteger("1");
    public static BigInteger TWO=new BigInteger("2");

    public static void main(){

        //程序开始时间
        long startTime=System.currentTimeMillis();

        Scanner input=new Scanner(System.in);

        //生成公共参数g,h,p
        ArrayList<BigInteger> parameter=parametergen(150);//需要填入素数的位数
        BigInteger g=parameter.get(0);
        BigInteger h=parameter.get(1);
        BigInteger p=parameter.get(2);
        /*System.out.println("g="+g.toString());
        System.out.println("h="+h.toString());
        System.out.println("p="+p.toString());*/

        //将公共参数以顺序g,h,p写入文件
        System.out.println("Please enter the storage location of the public parameter file");
        String parakeep=input.next();//d:/BIDtext/parameter.txt
        writepara(parakeep,g);
        writepara(parakeep,h);
        writepara(parakeep,p);

        //获取程序结束时间
        long endTime=System.currentTimeMillis();
        System.out.println("Parameter Program running time="+(endTime-startTime)+"ms");

    }

    //生成公共参数
    public static ArrayList<BigInteger> parametergen(int nbit){
        //nbit表示生成素数的位长

        //生成素数p,也是模数p
        BigInteger p=getPrime(nbit, 40, new Random());

        //在Zp中生成两个生成元，g和h
        //先随机生成一个大整数g，其范围是[0,p-1]
        BigInteger g=randNum(p, new Random());
        //p1=(p-1)/2
        BigInteger p1=p.subtract(ONE).divide(TWO);
        while(!g.modPow(p1, p).equals(ONE)) {//modPow为取幂且模，在这里为g^m1 mod m
            if (g.modPow(p1.multiply(TWO),p).equals(ONE)) {
                g=g.modPow(TWO, p);
            }
            else {
                g=randNum(p, new Random());
            }
        }
        //同理，先随机生成一个大整数h，其范围是[0,p-1]
        BigInteger h=randNum(p, new Random());
        while(!h.modPow(p1, p).equals(ONE)) {
            if (h.modPow(p1.multiply(TWO),p).equals(ONE)) {
                h=h.modPow(TWO, p);
            }
            else {
                h=randNum(p, new Random());
            }
        }

        ArrayList<BigInteger> parameter=new ArrayList<>(Arrays.asList(g,h,p));
        return parameter;

    }

    //随机生成一个大素数
    public static BigInteger getPrime(int bitlen, int pro, Random rand ) {
        /* 参数解释
         * bitlen--bitlength为生成素数的位数
         * pro--probability为成功生成一个素数的概率，该概率大于(1-(1/2)^pro)
         * rand--random为随机比特，用其选择进行素数测试的候选数
         */

        //随机生成一个大整数
        BigInteger bigint=new BigInteger(bitlen, pro, rand);
        //p=bigint*2+1
        BigInteger p = bigint.multiply(TWO).add(BigInteger.ONE);

        //判断p是否为素数，当为素数的概率大于(1-(1/2)^pro)时，停止循环
        while(!p.isProbablePrime(pro)) {
            bigint=new BigInteger(bitlen, pro, rand);
            p = bigint.multiply(TWO).add(BigInteger.ONE);
        }

        //返回素数p;
        return p;
    }

    //随机生成一个大整数，且该大整数的范围是[0,N-1]
    public static BigInteger randNum(BigInteger N, Random rand) {
        return new BigInteger(N.bitLength()+100, rand).mod(N);
    }

    //将公共参数写入文件
    public static void writepara(String file, BigInteger x) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(String.valueOf(x)+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
