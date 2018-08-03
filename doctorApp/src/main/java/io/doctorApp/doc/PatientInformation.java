package io.doctorApp.doc;

import java.util.List;

public    class PatientInformation //implements Comparable<PatientInformation>
{
	 
		        private String PKColumnId ;

		        
		        private String PatientRegId;
		        
		        private String PatientFirstName ;
		        
		        private String PatientSurName ;
		        
		        private String PatientGender;
		        
		        private String PatientDOB ;
		        
		        private String PatientCountry;
		       
		        private String PatientState ;
		        
		        private String PatientCity ;
		        
		        private String PatientAddress;
		        
		        private String PatientZipCode;
		        
		        private String PatientPhoneNo ;
		       
		        private String PatientProfession ;
		        
		        private String PatientMaritalStatus;
		         
		        private String PatientBloodGroup;
		         
		        private String PatientAge ;
		        
		        private String PatientReligion;
		        
		        private String PatientEmailId;
		        
		        private String PatientSymptoms;
		        
		        private String ImagePath;
		        
		        private String NumberOfPatient ;
		      
		        
		        private List<DynamicColumns> DynamicColumns;
		        
		        private List<PatientVisitInformation> ListOfVisitInformation;
		        
		        private String ServiceResult;
		        
		        private List<String> ListOfColumns ;

		        
		        private ColumnData ColumnData;
		        
		        private List<PatientAppoientment> ListOfAppointmentInformation ;

		        
		        private List<String> ListOfMediciens ;
		        
		        private String NextVisitDate ;

		        // Total expacted patients visits
		        
		        private String ExpactedPatientsVisitCount ;
		        
		        
		        public String getPKColumnId() {
					return PKColumnId;
				}


				public void setPKColumnId(String pKColumnId) {
					PKColumnId = pKColumnId;
				}


				public String getPatientRegId() {
					return PatientRegId;
				}


				public void setPatientRegId(String patientRegId) {
					PatientRegId = patientRegId;
				}


				public String getPatientFirstName() {
					return PatientFirstName;
				}


				public void setPatientFirstName(String patientFirstName) {
					PatientFirstName = patientFirstName;
				}


				public String getPatientSurName() {
					return PatientSurName;
				}


				public void setPatientSurName(String patientSurName) {
					PatientSurName = patientSurName;
				}


				public String getPatientGender() {
					return PatientGender;
				}


				public void setPatientGender(String patientGender) {
					PatientGender = patientGender;
				}


				public String getPatientDOB() {
					return PatientDOB;
				}


				public void setPatientDOB(String patientDOB) {
					PatientDOB = patientDOB;
				}


				public String getPatientCountry() {
					return PatientCountry;
				}


				public void setPatientCountry(String patientCountry) {
					PatientCountry = patientCountry;
				}


				public String getPatientState() {
					return PatientState;
				}


				public void setPatientState(String patientState) {
					PatientState = patientState;
				}


				public String getPatientCity() {
					return PatientCity;
				}


				public void setPatientCity(String patientCity) {
					PatientCity = patientCity;
				}


				public String getPatientAddress() {
					return PatientAddress;
				}


				public void setPatientAddress(String patientAddress) {
					PatientAddress = patientAddress;
				}


				public String getPatientZipCode() {
					return PatientZipCode;
				}


				public void setPatientZipCode(String patientZipCode) {
					PatientZipCode = patientZipCode;
				}


				public String getPatientPhoneNo() {
					return PatientPhoneNo;
				}


				public void setPatientPhoneNo(String patientPhoneNo) {
					PatientPhoneNo = patientPhoneNo;
				}


				public String getPatientProfession() {
					return PatientProfession;
				}


				public void setPatientProfession(String patientProfession) {
					PatientProfession = patientProfession;
				}


				public String getPatientMaritalStatus() {
					return PatientMaritalStatus;
				}


				public void setPatientMaritalStatus(String patientMaritalStatus) {
					PatientMaritalStatus = patientMaritalStatus;
				}


				public String getPatientBloodGroup() {
					return PatientBloodGroup;
				}


				public void setPatientBloodGroup(String patientBloodGroup) {
					PatientBloodGroup = patientBloodGroup;
				}


				public String getPatientAge() {
					return PatientAge;
				}


				public void setPatientAge(String patientAge) {
					PatientAge = patientAge;
				}


				public String getPatientReligion() {
					return PatientReligion;
				}


				public void setPatientReligion(String patientReligion) {
					PatientReligion = patientReligion;
				}


				public String getPatientEmailId() {
					return PatientEmailId;
				}


				public void setPatientEmailId(String patientEmailId) {
					PatientEmailId = patientEmailId;
				}


				public String getPatientSymptoms() {
					return PatientSymptoms;
				}


				public void setPatientSymptoms(String patientSymptoms) {
					PatientSymptoms = patientSymptoms;
				}


				public String getImagePath() {
					return ImagePath;
				}


				public void setImagePath(String imagePath) {
					ImagePath = imagePath;
				}


				public String getNumberOfPatient() {
					return NumberOfPatient;
				}


				public void setNumberOfPatient(String numberOfPatient) {
					NumberOfPatient = numberOfPatient;
				}


				public List<DynamicColumns> getDynamicColumns() {
					return DynamicColumns;
				}


				public void setDynamicColumns(List<DynamicColumns> dynamicColumns) {
					DynamicColumns = dynamicColumns;
				}


				public List<PatientVisitInformation> getListOfVisitInformation() {
					return ListOfVisitInformation;
				}


				public void setListOfVisitInformation(List<PatientVisitInformation> listOfVisitInformation) {
					ListOfVisitInformation = listOfVisitInformation;
				}


				public String getServiceResult() {
					return ServiceResult;
				}


				public void setServiceResult(String serviceResult) {
					ServiceResult = serviceResult;
				}


				public List<String> getListOfColumns() {
					return ListOfColumns;
				}


				public void setListOfColumns(List<String> listOfColumns) {
					ListOfColumns = listOfColumns;
				}


				public ColumnData getColumnData() {
					return ColumnData;
				}


				public void setColumnData(ColumnData columnData) {
					ColumnData = columnData;
				}


				public List<PatientAppoientment> getListOfAppointmentInformation() {
					return ListOfAppointmentInformation;
				}


				public void setListOfAppointmentInformation(List<PatientAppoientment> listOfAppointmentInformation) {
					ListOfAppointmentInformation = listOfAppointmentInformation;
				}


				public List<String> getListOfMediciens() {
					return ListOfMediciens;
				}


				public void setListOfMediciens(List<String> listOfMediciens) {
					ListOfMediciens = listOfMediciens;
				}


				public String getNextVisitDate() {
					return NextVisitDate;
				}


				public void setNextVisitDate(String nextVisitDate) {
					NextVisitDate = nextVisitDate;
				}


				public String getExpactedPatientsVisitCount() {
					return ExpactedPatientsVisitCount;
				}


				public void setExpactedPatientsVisitCount(String expactedPatientsVisitCount) {
					ExpactedPatientsVisitCount = expactedPatientsVisitCount;
				}


				public int CompareTo(PatientInformation obj)
		        {
		            // Alphabetic sort name[A to Z]
		            return this.NextVisitDate.compareTo(obj.NextVisitDate);
		        }


}
