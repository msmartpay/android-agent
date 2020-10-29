package agent.msmartpay.in.utility;

import java.util.Random;

/**
 * Created by Yuganshu on 26/03/2017.
 */

public class RandomNumber {


    public static String getTranId_14()
    {
        int n = 4;
        Random randGen = new Random();
        int startNum = (int) Math.pow(10, n-1);
        int range = (int) (Math.pow(10, n) - startNum);
        int randomNum = randGen.nextInt(range) + startNum;
        String txn_4 = String.valueOf(randomNum);

        n = 9;
        randGen = new Random();
        startNum = (int) Math.pow(10, n-1);
        range = (int) (Math.pow(10, n) - startNum);
        randomNum = randGen.nextInt(range) + startNum;
        String txn_14 =txn_4+"0"+ String.valueOf(randomNum);

        System.out.println(txn_14);
        return txn_14;
    }
    public static String getTranId_20(String agentId)
    {
        int n = 4;
        Random randGen = new Random();
        int startNum = (int) Math.pow(10, n-1);
        int range = (int) (Math.pow(10, n) - startNum);
        int randomNum = randGen.nextInt(range) + startNum;
        String txn_4 = String.valueOf(randomNum);

        n = 9;
        randGen = new Random();
        startNum = (int) Math.pow(10, n-1);
        range = (int) (Math.pow(10, n) - startNum);
        randomNum = randGen.nextInt(range) + startNum;
        String txn_14 =agentId+txn_4+"0"+ String.valueOf(randomNum);


        return txn_14;
    }


    public static void main(String[] args){
        getTranId_14();
    }
}
