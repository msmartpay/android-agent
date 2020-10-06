package msmartpay.in.utility;


/**
 * Created by Yuganshu on 11/02/2017.
 */

public class Service {

    public String validateMobileNumber(String number)
    {
        if (number.startsWith("+91")) {
            String no = number.substring(3);
            String newno = no.trim();
            newno = newno.replaceAll(" ", "");
            if (newno.length() == 10) {

                return newno;

            }else if (number.startsWith("+91-")||number.startsWith("+91 ")) {

                String no1 = number.substring(4);
                String newno1 = no1.trim();
                newno = newno1.replaceAll(" ", "");
                if (newno.length() == 10) {
                    return  newno1;
                } else {
                    return null;
                }

            } else {
               return null;
            }
        } else if (number.startsWith("91")) {
            String no = number.substring(2);
            String newno = no.trim();
            newno = newno.replaceAll(" ", "");
            if (newno.length() == 10) {
                return newno;
            }  else if (number.startsWith("91 ")) {

                String no1 = number.substring(1);
                String newno1 = no1.trim();
                newno1 = newno1.replaceAll(" ", "");
                if (newno1.length() == 10) {
                    return  newno1;
                } else {
                    return null;

                }
            }else {
                return null;
            }

        }  else if (number.startsWith("0")) {

            String no = number.substring(1);
            String newno = no.trim();
            newno = newno.replaceAll(" ", "");
            if (newno.length() == 10) {
                return  newno;
            } else {
                return null;

            }
        } else if (number.length() == 10) {
            return number;
        } else {
            return null;

        }
    }
   /* public static boolean isValidEmail(String enteredEmail){
        Pattern p =  Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+", Pattern.CASE_INSENSITIVE);
        //Pattern p =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(enteredEmail);
        if(m.find()){
            return true;
        } else {
            return false;
        }
    }*/
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
   /* public static boolean isValidEmail(String paramString)
    {
        return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(paramString).find();
    }*/

}
