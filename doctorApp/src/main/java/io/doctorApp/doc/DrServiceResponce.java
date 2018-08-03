package io.doctorApp.doc;

import java.util.List;

public class DrServiceResponce {

	private int StatusCode;
	private String Message;
	private String ExceptionDetails;
	
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int StatusCode) 
	{
		this.StatusCode = StatusCode;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String Message) {
		this.Message = Message;
	}
	public String getExceptionDetails() {
		return ExceptionDetails;
	}
	public void setExceptionDetails(String ExceptionDetails) {
		this.ExceptionDetails = ExceptionDetails;
	}
	
	
    public DrServiceResponce(int StatusCode, String Message, String ExceptionDetails)
    {
        this.StatusCode = StatusCode;
        this.Message = Message;
        this.ExceptionDetails = ExceptionDetails;
    }
    public DrServiceResponce()
    {

    }
    private List<PatientInformation> PatientsList;
    private PatientInformation PatientsDetails;
    private List<VisitMedicin> PatientsVisitmedicin;
    
    private PaymentDetails PatientsPaymentVisitDetails;
    private List<PatientVisitInformation> PatientsVisitInformationDetails;
    private List<PatientAppoientment> PatientAppointmentInformationDetails;
    public PatientVisitInformation PatientVisitDetails;
    private List<String> ListOfColumns;
    private List<Diagnoses> DiagnosesList;

   
	
	
	public List<PatientInformation> getPatientList()
    {
		return PatientsList;
	}

	
    public void setPatientList(List<PatientInformation> patientList)
	{
		PatientsList = patientList;
	}
	
    public List<VisitMedicin> getPatientsVisitmedicin()
    {
		return PatientsVisitmedicin;
	}
	
    public void setPatientsVisitmedicin(List<VisitMedicin> patientsVisitmedicin)
    {
		PatientsVisitmedicin = patientsVisitmedicin;
	}
	
    public List<PatientVisitInformation> getPatientsVisitInformationDetails() {
		return PatientsVisitInformationDetails;
	}
	
    public void setPatientsVisitInformationDetails(List<PatientVisitInformation> patientsVisitInformationDetails) {
		PatientsVisitInformationDetails = patientsVisitInformationDetails;
	}
	
    public List<PatientAppoientment> getPatientAppointmentInformationDetails() {
		return PatientAppointmentInformationDetails;
	}
	
    public void setPatientAppointmentInformationDetails(List<PatientAppoientment> patientAppointmentInformationDetails) {
		PatientAppointmentInformationDetails = patientAppointmentInformationDetails;
	}
	
    public List<String> getListOfColumns() {
		return ListOfColumns;
	}
	
    public void setListOfColumns(List<String> listOfColumns) {
		ListOfColumns = listOfColumns;
	}
	
    public List<Diagnoses> getDiagnosesList() {
		return DiagnosesList;
	}
	
    public void setDiagnosesList(List<Diagnoses> diagnosesList) {
		DiagnosesList = diagnosesList;
	}
	
    public PatientInformation getPatientDetails() {
		return PatientsDetails;
	}
	
    public void setPatientDetails(PatientInformation patientDetails) {
		PatientsDetails = patientDetails;
	}
	
    public PaymentDetails getPatientPaymentVisitDetails() {
		return PatientsPaymentVisitDetails;
	}
	
    public void setPatientPaymentVisitDetails(PaymentDetails patientPaymentVisitDetails) {
		PatientsPaymentVisitDetails = patientPaymentVisitDetails;
	}
	
    public PatientVisitInformation getPatientVisitDetails() {
		return PatientVisitDetails;
	}
	
    public void setPatientVisitDetails(PatientVisitInformation patientVisitDetails) {
		PatientVisitDetails = patientVisitDetails;
	}
	
	public enum ServiceStatusCodeEnum
	    {
		  LoginSUCCESS (1000),
		LoginFalis(9999),
	        
		 	SUCCESS(200),
	        NOTFOUND(404),
	        NULL_REFERENCE_EXCEPTION (5000),
	        FORMAT_UNSUPPORTED(5001),
	        MYSQL_EXCEPTION(5002),
	        USER_NOT_EXIST(5003),
	        TRANSACTION_ROLLBACK (5004),
	        PARAMETER_NULL(5005),
	        SEARCH_QUERY_EMPTY (5006),
	        RECORD_NOT_FOUND(5007),
	        RECORDS_LIMIT_ZERO(5008),
	        RECORDS_OFFSET_NEGATIVE(5009),
	        NO_MORE_RECORDS_EXIST(5010),
	        INVALID_OPERATION_EXCEPTION(5011),
	        PATIENT_REGISTRATION_ID_NULL(5012),
	        GENERIC_EXCEPTION(5013);
	        
		
		
		
		private int numVal;

			  ServiceStatusCodeEnum(int numVal) {
			        this.numVal = numVal;
			    }

			    public int getNumVal() {
			        return numVal;
			    }
	    }
	 
	 public  enum MedicinTypeCode
	  {
		 	  globules(100),
	          syrup(101),
	          tablets(102),
	          biochemic(103) ,
	          powder (104),
	          liquid (105),
		      ointment (106);
		  
		  private int numVal;

		  MedicinTypeCode(int numVal) {
		        this.numVal = numVal;
		    }

		    public int getNumVal() {
		        return numVal;
		    }
	  }

}
