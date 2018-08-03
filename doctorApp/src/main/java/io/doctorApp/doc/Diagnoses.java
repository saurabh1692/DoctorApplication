package io.doctorApp.doc;

public class Diagnoses {
	private String FileName;
	 private String FileId ;
	 private String UploadedDate; 
     public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getFileId() {
		return FileId;
	}
	public void setFileId(String fileId) {
		FileId = fileId;
	}
	public String getUploadedDate() {
		return UploadedDate;
	}
	public void setUploadedDate(String uploadedDate) {
		UploadedDate = uploadedDate;
	}
	public String getPatientRegId() {
		return PatientRegId;
	}
	public void setPatientRegId(String patientRegId) {
		PatientRegId = patientRegId;
	}
	private String PatientRegId;

}
