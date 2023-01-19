package com.aepssdkssz.network.model;

import java.util.List;

public class ValidateUserData {
    private String initiator_id;
    private String transaction_url;
    private String device_type;
    private String device_number;
    private List<BiometricDevice> biometric_providers;
    private List<BankModel> bank_list;
    private List<BankModel> preferred_bank_list;

    private boolean allow;//for process further




    private boolean kyc;//for process further
    private String merchant_name;
    private String merchant_mobile;
    private String merchant_email;
    private String user_code;

    private String merchant_company;
    private String merchant_pan;
    private String merchant_location;
    private String merchant_id;
    private String authorization_key;

    private String partner_logo_url;
    private String partner_name;
    private String partner_powered_by;
    private String partner_domain;

    private String partner_contact_us;

    public boolean isKyc() {
        return kyc;
    }

    public void setKyc(boolean kyc) {
        this.kyc = kyc;
    }
    public String getPartner_logo_url() {
        return partner_logo_url;
    }

    public void setPartner_logo_url(String partner_logo_url) {
        this.partner_logo_url = partner_logo_url;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_powered_by() {
        return partner_powered_by;
    }

    public void setPartner_powered_by(String partner_powered_by) {
        this.partner_powered_by = partner_powered_by;
    }

    public String getPartner_domain() {
        return partner_domain;
    }

    public void setPartner_domain(String partner_domain) {
        this.partner_domain = partner_domain;
    }

    public String getPartner_contact_us() {
        return partner_contact_us;
    }

    public void setPartner_contact_us(String partner_contact_us) {
        this.partner_contact_us = partner_contact_us;
    }
    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getInitiator_id() {
        return initiator_id;
    }

    public void setInitiator_id(String initiator_id) {
        this.initiator_id = initiator_id;
    }


    public String getTransaction_url() {
        return transaction_url;
    }

    public void setTransaction_url(String transaction_url) {
        this.transaction_url = transaction_url;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public List<BiometricDevice> getBiometric_providers() {
        return biometric_providers;
    }

    public void setBiometric_providers(List<BiometricDevice> biometric_providers) {
        this.biometric_providers = biometric_providers;
    }

    public List<BankModel> getBank_list() {
        return bank_list;
    }

    public void setBank_list(List<BankModel> bank_list) {
        this.bank_list = bank_list;
    }

    public List<BankModel> getPreferred_bank_list() {
        return preferred_bank_list;
    }

    public void setPreferred_bank_list(List<BankModel> preferred_bank_list) {
        this.preferred_bank_list = preferred_bank_list;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_mobile() {
        return merchant_mobile;
    }

    public void setMerchant_mobile(String merchant_mobile) {
        this.merchant_mobile = merchant_mobile;
    }

    public String getMerchant_email() {
        return merchant_email;
    }

    public void setMerchant_email(String merchant_email) {
        this.merchant_email = merchant_email;
    }

    public String getMerchant_company() {
        return merchant_company;
    }

    public void setMerchant_company(String merchant_company) {
        this.merchant_company = merchant_company;
    }

    public String getMerchant_pan() {
        return merchant_pan;
    }

    public void setMerchant_pan(String merchant_pan) {
        this.merchant_pan = merchant_pan;
    }

    public String getMerchant_location() {
        return merchant_location;
    }

    public void setMerchant_location(String merchant_location) {
        this.merchant_location = merchant_location;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }



}
