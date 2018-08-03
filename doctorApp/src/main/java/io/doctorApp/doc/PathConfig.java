package io.doctorApp.doc;

public class PathConfig
{

	private String path;

	private String pdfPath;
	
	private String notesPath;
	
	private String logoPath;
	private String otpPath;
	
	



	public PathConfig() 
	{
		
	}
	
	
	
	public PathConfig(String path, String pdfPath) 
	{
		
		this.path = path;
		this.pdfPath = pdfPath;
		
	}



	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPdfPath()
	{
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) 
	{
		this.pdfPath = pdfPath;
	}



	public String getNotesPath() {
		return notesPath;
	}



	public void setNotesPath(String notesPath) {
		this.notesPath = notesPath;
	}



	public String getLogoPath() {
		return logoPath;
	}



	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getOtpPath() {
		return otpPath;
	}



	public void setOtpPath(String otpPath) {
		this.otpPath = otpPath;
	}
	
	
	
	
}
