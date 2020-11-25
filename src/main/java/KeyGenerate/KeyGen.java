package KeyGenerate;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class KeyGen {
    //将字符串格式转换成十进制表示形式的大整数
    public static BigInteger ONE=new BigInteger("1");
    public static BigInteger TWO=new BigInteger("2");

    public static void main(String[] args) throws IOException {
        //程序开始时间
        long startTime=System.currentTimeMillis();
        Scanner input=new Scanner(System.in);

        //从parameter.txt文件中读取公共参数g,h,p
        System.out.println("Please enter the storage location of the public parameter file");
        String paraInput=input.next();//d:/BIDtext/parameter.txt
        BigInteger[] parameter=readparameter(paraInput);
        BigInteger h=parameter[1];
        BigInteger p=parameter[2];

        /*System.out.println("测试读取到的公共参数是否正确：");
        System.out.println("h="+h.toString());
        System.out.println("p="+p.toString());*/

        BigInteger[] psk=keygen(h,p);
        //公私钥
        BigInteger x=psk[0];
        BigInteger y=psk[1];

        /*检验y=h^x
        BigInteger y1=h.modPow(x,p);
        if(y1.equals(y)){
            System.out.println("公钥生成正确");
        }
        else {
            System.out.println("公钥生成错误");
        }*/

        //将公钥私钥分别存入文件中
        System.out.println("Please enter the storage location of the public key file");//d:/BIDtext/apublickey.txt（每个竞拍者应该不同）
        String pkKeep=input.next();
        //pkin();
        writekey(pkKeep,y);
        System.out.println("Please enter the storage location of the private key file");//d:/BIDtext/asecretkey.txt（每个竞拍者应该不同）
        String skKeep=input.next();
        writekey(skKeep,x);

        //获取程序结束时间
        long endTime=System.currentTimeMillis();
        System.out.println("KeyGen Program running time="+(endTime-startTime)+"ms");

    }

    //从文件中读取公共参数
    public static BigInteger[] readparameter(String file) throws IOException {
        FileReader fr = new FileReader(file);//读取该位置的文件
        BufferedReader br = new BufferedReader(fr);//以缓冲的形式读取字符
        String line = br.readLine();
        String[] input=new String[3];
        for (int i = 0; line != null; i++) {
            input[i]=line;
            line = br.readLine();
        }
        br.close();
        fr.close();

        //将String数组类型转换为Biginteger数组类型
        BigInteger[] parameter=new BigInteger[3];
        for(int i=0; i<3; i++){
            parameter[i]=new BigInteger(input[i]);
        }


        return parameter;
    }

    //为每个竞拍者生成其公私钥，私钥为x，公钥为y=h^x mod p
    public static BigInteger[] keygen(BigInteger h, BigInteger p){

        //p1=(p-1)/2
        BigInteger p1=p.subtract(ONE).divide(TWO);

        //生成私钥x,范围是[0,((p-1)/2)-1]
        //生成对应的公钥y=h^x mod p
        BigInteger x=randNum(p1.subtract(ONE), new Random());
        BigInteger y=h.modPow(x, p);

        //将公私钥放入大整数数组psk中
        BigInteger[] psk=new BigInteger[2];
        psk[0]=x;
        psk[1]=y;

        return psk;

    }

    //随机生成一个大整数，且该大整数的范围是[0,N-1]
    public static BigInteger randNum(BigInteger N, Random rand) {
        return new BigInteger(N.bitLength()+100, rand).mod(N);
    }

    //将公钥写入一个文件公开，私钥写入一个文件,由竞拍者自己保留
    public static void writekey(String file, BigInteger x) {
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
