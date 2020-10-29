package in.msmartpay.agent.flight;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Harendra on 9/22/2017.
 */

public class AvailPaxFareDetailsModel implements Serializable {


    private String ClassCodeDesc;
    private String ChangePenalty;
    private String CancellationCharges;
    private String ClassCode;

    private String InfantTotalTaxAmount;
    private String InfantCommission;
    private String InfantBasicAmount;
    private String InfantFareType;
    private String InfantGrossAmount;
    private HashMap<String,String> InfantTaxDetails;
    private String InfantFareBasis;
    private String InfantYQ;

    private String ChildTotalTaxAmount;
    private String ChildCommission;
    private String ChildBasicAmount;
    private String ChildFareType;
    private String ChildGrossAmount;
    private HashMap<String,String> ChildTaxDetails;
    private String ChildFareBasis;
    private String ChildYQ;

    private String AdultTotalTaxAmount;
    private String AdultCommission;
    private String AdultBasicAmount;
    private String AdultFareType;
    private String AdultGrossAmount;
    private HashMap<String,String> AdultTaxDetails;
    private String AdultFareBasis;
    private String AdultYQ;

    public String getClassCodeDesc() {
        return ClassCodeDesc;
    }

    public void setClassCodeDesc(String classCodeDesc) {
        ClassCodeDesc = classCodeDesc;
    }

    public String getChangePenalty() {
        return ChangePenalty;
    }

    public void setChangePenalty(String changePenalty) {
        ChangePenalty = changePenalty;
    }

    public String getCancellationCharges() {
        return CancellationCharges;
    }

    public void setCancellationCharges(String cancellationCharges) {
        CancellationCharges = cancellationCharges;
    }

    public String getClassCode() {
        return ClassCode;
    }

    public void setClassCode(String classCode) {
        ClassCode = classCode;
    }

    public String getInfantTotalTaxAmount() {
        return InfantTotalTaxAmount;
    }

    public void setInfantTotalTaxAmount(String infantTotalTaxAmount) {
        InfantTotalTaxAmount = infantTotalTaxAmount;
    }

    public String getInfantCommission() {
        return InfantCommission;
    }

    public void setInfantCommission(String infantCommission) {
        InfantCommission = infantCommission;
    }

    public String getInfantBasicAmount() {
        return InfantBasicAmount;
    }

    public void setInfantBasicAmount(String infantBasicAmount) {
        InfantBasicAmount = infantBasicAmount;
    }

    public String getInfantFareType() {
        return InfantFareType;
    }

    public void setInfantFareType(String infantFareType) {
        InfantFareType = infantFareType;
    }

    public String getInfantGrossAmount() {
        return InfantGrossAmount;
    }

    public void setInfantGrossAmount(String infantGrossAmount) {
        InfantGrossAmount = infantGrossAmount;
    }

    public HashMap<String, String> getInfantTaxDetails() {
        return InfantTaxDetails;
    }

    public void setInfantTaxDetails(HashMap<String, String> infantTaxDetails) {
        InfantTaxDetails = infantTaxDetails;
    }

    public String getInfantFareBasis() {
        return InfantFareBasis;
    }

    public void setInfantFareBasis(String infantFareBasis) {
        InfantFareBasis = infantFareBasis;
    }

    public String getInfantYQ() {
        return InfantYQ;
    }

    public void setInfantYQ(String infantYQ) {
        InfantYQ = infantYQ;
    }

    public String getChildTotalTaxAmount() {
        return ChildTotalTaxAmount;
    }

    public void setChildTotalTaxAmount(String childTotalTaxAmount) {
        ChildTotalTaxAmount = childTotalTaxAmount;
    }

    public String getChildCommission() {
        return ChildCommission;
    }

    public void setChildCommission(String childCommission) {
        ChildCommission = childCommission;
    }

    public String getChildBasicAmount() {
        return ChildBasicAmount;
    }

    public void setChildBasicAmount(String childBasicAmount) {
        ChildBasicAmount = childBasicAmount;
    }

    public String getChildFareType() {
        return ChildFareType;
    }

    public void setChildFareType(String childFareType) {
        ChildFareType = childFareType;
    }

    public String getChildGrossAmount() {
        return ChildGrossAmount;
    }

    public void setChildGrossAmount(String childGrossAmount) {
        ChildGrossAmount = childGrossAmount;
    }

    public HashMap<String, String> getChildTaxDetails() {
        return ChildTaxDetails;
    }

    public void setChildTaxDetails(HashMap<String, String> childTaxDetails) {
        ChildTaxDetails = childTaxDetails;
    }

    public String getChildFareBasis() {
        return ChildFareBasis;
    }

    public void setChildFareBasis(String childFareBasis) {
        ChildFareBasis = childFareBasis;
    }

    public String getChildYQ() {
        return ChildYQ;
    }

    public void setChildYQ(String childYQ) {
        ChildYQ = childYQ;
    }

    public String getAdultTotalTaxAmount() {
        return AdultTotalTaxAmount;
    }

    public void setAdultTotalTaxAmount(String adultTotalTaxAmount) {
        AdultTotalTaxAmount = adultTotalTaxAmount;
    }

    public String getAdultCommission() {
        return AdultCommission;
    }

    public void setAdultCommission(String adultCommission) {
        AdultCommission = adultCommission;
    }

    public String getAdultBasicAmount() {
        return AdultBasicAmount;
    }

    public void setAdultBasicAmount(String adultBasicAmount) {
        AdultBasicAmount = adultBasicAmount;
    }

    public String getAdultFareType() {
        return AdultFareType;
    }

    public void setAdultFareType(String adultFareType) {
        AdultFareType = adultFareType;
    }

    public String getAdultGrossAmount() {
        return AdultGrossAmount;
    }

    public void setAdultGrossAmount(String adultGrossAmount) {
        AdultGrossAmount = adultGrossAmount;
    }

    public HashMap<String, String> getAdultTaxDetails() {
        return AdultTaxDetails;
    }

    public void setAdultTaxDetails(HashMap<String, String> adultTaxDetails) {
        AdultTaxDetails = adultTaxDetails;
    }

    public String getAdultFareBasis() {
        return AdultFareBasis;
    }

    public void setAdultFareBasis(String adultFareBasis) {
        AdultFareBasis = adultFareBasis;
    }

    public String getAdultYQ() {
        return AdultYQ;
    }

    public void setAdultYQ(String adultYQ) {
        AdultYQ = adultYQ;
    }
}
