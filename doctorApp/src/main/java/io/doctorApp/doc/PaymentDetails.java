package io.doctorApp.doc;

public class PaymentDetails {
	
	private String AdvancedAmount;
	private String DueAmount;
	private String PatientVisitAmount;
	private String PatientVisitPaidAmount;
	private String PatientVisitConcessionAmount;
	private String ConsultancyCharge;
	private String NewRegistrationCharge;
	private String Re_RegistrationCharge;
	private boolean IsNewRegistration;
	private boolean IsReRegistration;
	private boolean IsRepeateMedicien;
    private String PaymentMode;
    
	public String getPaymentMode() {
		return PaymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		PaymentMode = paymentMode;
	}
	public String getAdvancedAmount() {
		return AdvancedAmount;
	}
	public void setAdvancedAmount(String AdvancedAmount) {
		this.AdvancedAmount = AdvancedAmount;
	}
	public String getDueAmount() {
		return DueAmount;
	}
	public void setDueAmount(String DueAmount) {
		this.DueAmount = DueAmount;
	}
	public String getPatientVisitAmount() {
		return PatientVisitAmount;
	}
	public void setPatientVisitAmount(String PatientVisitAmount) {
		this.PatientVisitAmount = PatientVisitAmount;
	}
	public String getPatientVisitPaidAmount() {
		return PatientVisitPaidAmount;
	}
	public void setPatientVisitPaidAmount(String PatientVisitPaidAmount) {
		this.PatientVisitPaidAmount = PatientVisitPaidAmount;
	}
	public String getPatientVisitConcessionAmount() {
		return PatientVisitConcessionAmount;
	}
	public void setPatientVisitConcessionAmount(String PatientVisitConcessionAmount) {
		this.PatientVisitConcessionAmount = PatientVisitConcessionAmount;
	}
	public String getConsultancyCharge() {
		return ConsultancyCharge;
	}
	public void setConsultancyCharge(String ConsultancyCharge) {
		this.ConsultancyCharge = ConsultancyCharge;
	}
	public String getNewRegistrationCharge() {
		return NewRegistrationCharge;
	}
	public void setNewRegistrationCharge(String NewRegistrationCharge) {
		this.NewRegistrationCharge = NewRegistrationCharge;
	}
	public String getRe_RegistrationCharge() {
		return Re_RegistrationCharge;
	}
	public void setRe_RegistrationCharge(String Re_RegistrationCharge) {
		this.Re_RegistrationCharge = Re_RegistrationCharge;
	}
	public boolean isIsNewRegistration() {
		return IsNewRegistration;
	}
	public void setIsNewRegistration(boolean IsNewRegistration) {
		this.IsNewRegistration = IsNewRegistration;
	}
	public boolean isIsReRegistration() {
		return IsReRegistration;
	}
	public void setIsReRegistration(boolean IsReRegistration) {
		this.IsReRegistration = IsReRegistration;
	}
	public boolean isIsRepeateMedicien() {
		return IsRepeateMedicien;
	}
	public void setIsRepeateMedicien(boolean IsRepeateMedicien) {
		this.IsRepeateMedicien = IsRepeateMedicien;
	}

}
