package msmartpay.in.rechargeBillPay;

import java.util.Hashtable;

/**
 * Created by Yuganshu on 27/01/2017.
 */

public class OperatorCode {

    public static final Hashtable<String, String> operator_value = new Hashtable<String, String>();

    public static String[] mobile_key = {"Select Operator", "Aircel", "Jio recharge","Jio Top Up","Airtel", "Idea", "Vodafone", "BSNL - Top Up", "BSNL - Validity",
            "Reliance - CDMA", "Reliance - GSM", "Tata Indicom", "Docomo - Top Up", "Docomo - Special", "Telenor - Top Up",
            "Telenor - Special", "Videocon - Top Up", "Videocon - Special", "Virgin - CDMA", "Virgin - GSM", "MTNL - Top Up",
            "MTNL - Validity", "MTS", "Loop (BPL)"};
    public static String[] dth_key = {"Select Operator", "Dish TV", "Tata Sky", "Airtel DTH", "BIG TV", "Videocon D2H", "Sun Direct"};
    public static String[] datacard_key = {"Select Operator", "Tata Photon+", "Tata Photon Whiz", "BSNL Data Card", "Reliance Netconnect", "Reliance Netconnect+", "Docomo Photon+", "Idea Data Card", "Aircel Data Card", "MTS Mblaze", "MTS Mbrowse"};
    public static String[] electricity_addition = {"Select Operator", "Customer-ID or Account Number", "Account Number or Consumer"};
    public static String[] bill_mobile_key = {"Select Operator", "Airtel Postpaid","Jio Addon", "Idea Postpaid", "Vodafone Postpaid", "BSNL Postpaid", "Reliance CDMA Postpaid", "Aircel Postpaid", "Reliance GSM Postpaid", "Tata Indicom Postpaid", "Tata Docomo Postpaid"};
    public static String[] bill_landline_key = {"Select Operator", "BSNL Landline", "Airtel Landline", "MTNL Delhi Landline", "Tikona Broadband", "Reliance Landline"};
    public static String[] bill_electricity_key = {"Select Operator", "Calcutta Electric Supply Corp","Muzzaffarpur Vidyut Vitran Ltd", "Bhagalpur Electricity Distribution Company(BEDCPL)","South Bihar Power Distribution", "North Bihar Power Distribution", "Reliance Energy Mumbai", "BSES Rajdhani", "BSES Yamuna", "MSEB Mumbai", "BEST", "Chhatisgarh Electricity Board", "Noida Power Company Ltd", "Jaipur Vidyut Vitran Nigam Ltd", "Banglore Electricity Supply Company Ltd", "MP Paschim Vidyut Vitran-INDORE", "Jamsedpur HttpURL & Services Company", "U P Power Corporation ltd"
            , "Torrent Power Ltd"};

    public static String[] bank_name = {"ICICI-BANK", "Punjab-National-Bank", "Cash"};

    public static String[] insurance_operators = {"Bajaj Allianz Life", "ICICI Pru Life", "India First Life", "Birla Sun Life", "Life Insurance Corporation"};

    public static String[] waterkey = {"Select Operator", "Delhi Jal Board(DJB)", "Aurangabad City Water HttpURL Comp.Ltd", "Bangalore Water Bill"};

    public static String[] gas_operators = {"Select Operator", "Adani Gas Limited", "Gujrat Gas Company LTD", "Indraprastha Gas Limited", "Mahanagar Gas-Mumbai", "Siti Energy Ltd"};

    static {
        /************** rechargesplash ***************/

        operator_value.put("Aircel", "ACMR");
        operator_value.put("Jio Addon", "JADR");            //Jio  Launch
        operator_value.put("Jio recharge", "JIOR");         //Jio  Launch
        operator_value.put("Jio Top Up", "JIOT");           //Jio  Launch
        operator_value.put("BSNL - Validity", "BSMV");
        operator_value.put("Videocon - Special", "VDMR");
        operator_value.put("Videocon - Top Up", "VDMT");
        operator_value.put("Telenor - Top Up", "UNMR");
        operator_value.put("Telenor - Special", "UNMS");
        operator_value.put("BSNL - Top Up", "BSMR");
        operator_value.put("Idea", "IDMR");
        operator_value.put("Reliance - CDMA", "RCMR");
        operator_value.put("Reliance - GSM", "RGMR");
        operator_value.put("Tata Indicom", "TIMR");
        operator_value.put("Virgin - GSM", "VGMR");
        operator_value.put("Vodafone", "VFMR");
        operator_value.put("MTS", "MTMR");
        operator_value.put("Loop (BPL)", "LMMR");
        operator_value.put("Docomo - Top Up", "TDMR");
        operator_value.put("Docomo - Special", "TDMS");
        operator_value.put("Virgin - CDMA", "VCMR");
        operator_value.put("Airtel", "AAMR");
        operator_value.put("MTNL - Validity", "MVMR");
        operator_value.put("MTNL - Top Up", "MTMT");

        operator_value.put("Airtel DTH", "AIDR");
        operator_value.put("Tata Sky", "TSDRN");
        operator_value.put("Dish TV", "DTDR");
        operator_value.put("Sun Direct", "SDDR");
        operator_value.put("Videocon D2H", "VDDR");
        operator_value.put("BIG TV", "BTDR");

        operator_value.put("Tata Photon+", "TTCR");
        operator_value.put("Tata Photon Whiz", "TTCR");
        operator_value.put("BSNL Data Card", "BSCR");
        operator_value.put("Reliance Netconnect", "RLCR");
        operator_value.put("Reliance Netconnect+", "RLCR");
        operator_value.put("Docomo Photon+", "TTCR");
        operator_value.put("Idea Data Card", "IDCR");
        operator_value.put("Aircel Data Card", "ACCR");
        operator_value.put("MTS Mblaze", "MTCR");
        operator_value.put("MTS Mbrowse", "MTCR");


        /******** Bill pay *********/

        operator_value.put("Aircel Postpaid", "ACMB");
        operator_value.put("Vodafone Postpaid", "VDMB");
        operator_value.put("BSNL Postpaid", "BSNB");
        operator_value.put("Reliance CDMA Postpaid", "RCMB");
        operator_value.put("Reliance GSM Postpaid", "RCMB");
        operator_value.put("Airtel Postpaid", "AIMB");
        operator_value.put("Idea Postpaid", "IDMB");
        operator_value.put("Tata Indicom Postpaid", "TIMB");
        operator_value.put("Tata Docomo Postpaid", "TDMB");

        operator_value.put("BSNL Landline", "BSMB");
        operator_value.put("Airtel Landline", "AILB");
        operator_value.put("MTNL Delhi Landline", "MTDB");
        operator_value.put("Tikona Broadband", "TBBB");
        operator_value.put("Reliance Landline", "RCLB");


        operator_value.put("Reliance Energy Mumbai", "RLEB");
        operator_value.put("BSES Rajdhani", "BSRB");
        operator_value.put("BSES Yamuna", "BSYB");
        operator_value.put("MSEB Mumbai", "MSEB");
        operator_value.put("Tata Power Delhi Distribution Ltd", "TTPD");
        operator_value.put("BEST", "BESB");
        operator_value.put("Chhatisgarh Electricity Board", "CEBB");
        operator_value.put("Noida Power Company Ltd", "NPCB");
        operator_value.put("Jaipur Vidyut Vitran Nigam Ltd", "JVNB");
        operator_value.put("Banglore Electricity Supply Company Ltd", "BELSB");
        operator_value.put("MP Paschim Vidyut Vitran-INDORE", "MPVB");
        operator_value.put("Jamsedpur HttpURL & Services Company", "JUSB");
        operator_value.put("U P Power Corporation ltd", "UPPB");


        operator_value.put("Calcutta Electric Supply Corp", "CESC");
        operator_value.put("North Bihar Power Distribution", "NBPD");
        operator_value.put("South Bihar Power Distribution", "SBPD");
        operator_value.put("Dakshin Gujrat Vij Company", "DGVB");
        operator_value.put("EASTERN POWER DISTRIBUTION COMPANY LTD", "EPDB");
        operator_value.put("Madhya Gujrat Vij Company", "MGVB");
        operator_value.put("North Delhi Power Limited", "NDLB");
        operator_value.put("Paschim Gujrat Vij Company", "PGVB");
        operator_value.put("Uttar Gujrat Vij Company", "UGVB");
        operator_value.put("Torrent Power Ltd", "TOPL");

        operator_value.put("Bhagalpur Electricity Distribution Company(BEDCPL)", "BEDC");
        operator_value.put("Muzzaffarpur Vidyut Vitran Ltd", "MVVL");
//        operator_value.put("Customer-ID or Account Number", "UGVB");
//        operator_value.put("Account Number or Consumer", "TOPL");



        //Water Payment
        operator_value.put("Delhi Jal Board(DJB)", "DEJB");
        operator_value.put("Bangalore Water Bill)", "BWBB");
        operator_value.put("Aurangabad City Water HttpURL Comp.Ltd", "ACWCL");


        //Insurance Payment
        operator_value.put("Bajaj Allianz Life", "BJAB");
        operator_value.put("ICICI Pru Life", "ICPB");
        operator_value.put("India First Life", "IFLB");
        operator_value.put("Birla Sun Life", "BSLB");
        operator_value.put("Life Insurance Corporation", "LICB");

        // Gas Payment
        operator_value.put("Adani Gas Limited", "AGEL");
        operator_value.put("Indraprastha Gas Limited", "IGLB");
        operator_value.put("Mahanagar Gas-Mumbai", "MGLB");
        operator_value.put("Gujrat Gas Company LTD", "GGCB");
        operator_value.put("Siti Energy Ltd", "SIEL");

    }

}
