 package io.doctorApp.doc;
 import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.PreparedStatement;

public class ProcessMySqlSelectCommandResult {
	
	
	
	ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("applicationContext.xml");
	DatabaseConnection db=(DatabaseConnection) context.getBean("mydbconnection");
	Connection con=db.getConnection();
	 
	
	static List<PatientInformation> GetPatientGeneralInformationFromSearchResult(ResultSet rs, boolean isFromSearchQuery)
    {
        List<PatientInformation> patienList = new ArrayList<PatientInformation>();
       try {
        rs.beforeFirst();
        while(rs.next())
      
        {
        System.out.println(rs.getString(1)+"  at line 33");
            PatientInformation patient = new PatientInformation();
            patient.setPatientRegId(rs.getString(1));
            patient.setPatientFirstName(rs.getString(2));
            patient.setPatientSurName(rs.getString(3))  ;
            patient.setPatientGender(rs.getString(10))  ;
            patient.setPatientEmailId( rs.getString(9)) ;
            patient.setPatientAddress(rs.getString(7)) ;
            patient.setPatientPhoneNo(rs.getString(8));
            if (isFromSearchQuery == true)
            {
                if(rs.getString(5)==null)
                {
                	patient.setImagePath("none");
                }
                else
                {
                	patient.setImagePath(rs.getString(5));
                }
            	
               
				if (patient.getImagePath().isEmpty() == false) {
					File parent = new File("http:\\PatientProfilesImages");
				    File child = new File(parent,patient.getImagePath());
				    patient.setImagePath(child.toString());
				}
            }
            patienList.add(patient);
        }
       }
       catch(Exception e) {
    	   System.out.println(e);
       }
        return patienList;
    }
	static List<PatientInformation> GetPatientGeneralInformationFromSearchResult1(ResultSet rs, boolean isFromSearchQuery)
    {
        List<PatientInformation> patienList = new ArrayList<PatientInformation>();
       try {
        rs.beforeFirst();
        while(rs.next())
      
        {
       
            PatientInformation patient = new PatientInformation();
            
            patient.setPatientRegId(rs.getString(1));
           
            
            patient.setPatientFirstName(rs.getString(2));
           
            
            patient.setPatientSurName(rs.getString(3))  ;
           
            
            patient.setPatientGender(rs.getString(4))  ;
            
            
            patient.setPatientEmailId( rs.getString(5)) ;
           
            
            patient.setPatientAddress(rs.getString(6)) ;
            
            
            patient.setPatientPhoneNo(rs.getString(7));
            
            if (isFromSearchQuery == true)
            {
                patient.setImagePath(rs.getString(8));
               
				if (patient.getImagePath().isEmpty() == false) {
					File parent = new File("http:\\PatientProfilesImages");
				    File child = new File(parent,patient.getImagePath());
				    patient.setImagePath(child.toString());
				}
            }
            patienList.add(patient);
        }
       }
       catch(Exception e) {
    	   System.out.println(e);
       }
        return patienList;
    }
    static List<Diagnoses> ExtractDiagnosesList(ResultSet dt) throws Exception
    {
        List<Diagnoses> diagnosesList = new ArrayList<Diagnoses>();
        dt.beforeFirst();
     while(dt.next())
        {
            Diagnoses diagnoses = new Diagnoses();
            diagnoses.setFileId(dt.getString(1));
            diagnoses.setFileName(dt.getString(2))  ;
            diagnoses.setUploadedDate( dt.getString(3));
            diagnosesList.add(diagnoses);
        }
        return diagnosesList;
    }
    //**********************************************************************************************************************************************
    public  String InsertPatientRecords(PatientInformation patientInfo, String userid)
    {
    	//DatabaseConnection db=new DatabaseConnection();
		//Connection con=db.getConnection();
    	
    	String subscription = new DocServices().getSubscriptionId(userid);
    	
		 Gson gson = new GsonBuilder().serializeNulls().create();
    	String result = "";
        DrServiceResponce drResponce = new DrServiceResponce();
        PatientInformation pi=new PatientInformation();
        try
        {
        	
                    String insertRecord = "Insert into patient_information_master(patient_reg_id,patient_first_name,patient_last_name,patient_gender,"
                    		+ "patient_date_of_birth,patient_country,patient_state,patient_city,patient_address,patient_zip_code,patient_phone_number,"
                    		+ "patient_profession,patient_marital_status,patient_religion,patient_email_id,patient_Symptoms, subscriptionid)" +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                   
                    try {
                    	PreparedStatement pst=con.prepareStatement(insertRecord);
                    	pst.setString(1, pi.getPatientRegId());
                    	pst.setString(2, pi.getPatientFirstName());
                    	pst.setString(3,pi.getPatientSurName());
                    	pst.setString(4,pi.getPatientGender());
                    	pst.setString(5,pi.getPatientDOB());
                    	pst.setString(6,pi.getPatientCountry());
                    	pst.setString(7,pi.getPatientState());
                    	pst.setString(8,pi.getPatientCity());
                    	pst.setString(9,pi.getPatientAddress());
                    	pst.setString(10,pi.getPatientZipCode());
                    	pst.setString(11,pi.getPatientPhoneNo() );
                    	pst.setString(12,pi.getPatientProfession() );
                    	pst.setString(13, pi.getPatientMaritalStatus());
                    	pst.setString(14, pi.getPatientReligion());
                    	pst.setString(15, pi.getPatientEmailId());
                    	pst.setString(16, pi.getPatientSymptoms());
                    	
                    	pst.setString(17, subscription);
                    	
                    	pst.executeUpdate();
                    }
                    catch(Exception e) {
                    	System.out.println(e);
                    }
                if (patientInfo.getDynamicColumns() != null)
                {
                    for (int index = 0; index < patientInfo.getDynamicColumns().size(); index++)
                    {
                        // Add Columns in Cache object
                       // Caching.AsyncCall_Delegate asyncDelegateCall = new Caching.AsyncCall_Delegate(Caching.AddDynamicColumnsInCacheList);
                        //IAsyncResult asyncResult = asyncDelegateCall.BeginInvoke(patientInfo.DynamicColumns, null, null);
                        // End of calling
                        DynamicColumns ColumnInfo = patientInfo.getDynamicColumns().get(index);
                       
                        String dynamictable = "Insert into patient_dynamic_table(patient_reg_id,column_name,column_value, subscriptionid)"
                        		+ " values(?,?,?,?)";
                        try {
                        	PreparedStatement pst1=con.prepareStatement(dynamictable);
                        	
                        	pst1.setString(1, patientInfo.getPatientRegId());
                        	pst1.setString(2, ColumnInfo.getColumnName());
                        	pst1.setString(3, ColumnInfo.getColumnValue());
                        	pst1.setString(4, subscription);
                        	
                        	pst1.executeUpdate();
                        	
                        }
                        catch(Exception e) {
                        	System.out.println(e);
                        }
                                            }
                }
        }
        catch (Exception ex)
        {
            result += "exception--" + ex.getMessage() ;//+ "---" + ex.InnerException.getMessage() + "---" + ex.Source;
        }
        drResponce.setMessage( ": " + result);
        return gson.toJson(drResponce);
    }
    
//------------------------------------------------------------------------------------------------------------------------------------------------------------
     static String GeneratePatientRegistrationId()
    {
    	
    	 int PatientRegId = 0;
       
    	 	//DatabaseConnection db=new DatabaseConnection();
    	 //	Connection con=db.getConnection();
    	 
    	 ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("applicationContext.xml");
    		DatabaseConnection db=(DatabaseConnection) context.getBean("mydbconnection");
    		Connection con=db.getConnection();
    	 	
                String CounttotalVisits = "select patient_reg_id  from patient_information_master order by patient_id desc limit 1 ";
                try {
                	Statement st=con.createStatement();
                	ResultSet rs=st.executeQuery(CounttotalVisits);
                	while(rs.next()){
                		 String TempPatientRegId = rs.getString(1);
                		 int RedId=Integer.parseInt(TempPatientRegId);
                		 PatientRegId=RedId+1;
                		
                	}
                	
                }
                catch(Exception e) {
                	System.out.println(e);
                }
            String RedId1 = String.valueOf(PatientRegId);
          
                return RedId1;
                
            }
  
	public static PatientInformation ProcessPatientsInformation(String fixedFields, String dyamicFields)
    {
		 PatientInformation objPatient = new PatientInformation();
        // PatientInformation objPatient=null;
		 ArrayList<DynamicColumns> dynamicColumnsArrayList = new ArrayList<DynamicColumns>();
         if (fixedFields.isEmpty() == false)
         {
             //String[] fiexdColumns = fixedFields.split(',');
        	
        	 String[] fiexdColumns = fixedFields.split(",");
        	for(int j=0;j<fiexdColumns.length;j++)
         if (fiexdColumns.length > 0)
             {
            	 String[] property =null;
                 for(int i=0;i<fiexdColumns.length;i++)
                
                 {
                	 String strFields=fiexdColumns[i];
                	  property = strFields.split(":");
                     if (property.length == 2)
                     {
                         switch (property[0])
                         {
                             case "PatientFirstName":
                              
                            	 objPatient.setPatientRegId(GeneratePatientRegistrationId());
                            	 objPatient.setPatientFirstName(property[1]);
                            	 
                            	 
                             
                            	 break;
                             case "PatientSurName":
                             
                                 
                                 objPatient.setPatientSurName(property[1]);
                                 break;
                             case "PatientGender":
                              
                                 
                                 objPatient.setPatientGender(property[1]);
                                 break;
                             case "PatientDOB":
                               
                                 
                                 objPatient.setPatientDOB(property[1]);
                              
                                 break;
                             case "PatientCountry":
                           
                                 
                                 objPatient.setPatientCountry(property[1]);
                                 break;
                             case "PatientState":
                            
                                 
                                 objPatient.setPatientState(property[1]);
                                 break;
                             case "PatientCity":
                         
                                 
                                 objPatient.setPatientCity(property[1]);
                                 break;
                             case "PatientAddress":
                               
                                 
                                 objPatient.setPatientAddress(property[1]);
                                 break;
                             case "PatientZipCode":
                                 int zip =Integer.parseInt(property[1]);
                                 
                               
                                 
                                 objPatient.setPatientZipCode(String.valueOf(zip));
                                 break;
                             case "PatientPhoneNo":
                            	  long phone = Long.parseLong(property[1]);
                                
                            
                                 
                                 objPatient.setPatientPhoneNo(String.valueOf(phone));
                                 break;
                             case "PatientProfession":
                              
                                 
                                 objPatient.setPatientProfession(property[1]);
                                 break;
                             case "PatientMaritalStatus":
                        
                                 
                                 objPatient.setPatientMaritalStatus(property[1]);
                                 break;
                             case "PatientBloodGroup":
                       
                           
                                 objPatient.setPatientBloodGroup(property[1]);
                                 
                                 break;
                             case "PatientAge":
                            	 long age = Long.parseLong(property[1]);
                                
                            
                                 
                                 objPatient.setPatientAge(String.valueOf(age));

                                 break;
                             case "PatientReligion":
                         
                                 objPatient.setPatientReligion(property[1]);
                                 
                                 break;
                             case "PatientEmailId":
                               
                                 
                                 objPatient.setPatientEmailId(property[1]);
                                 
                                 break;
                             case "PatientSymptoms":
                             
                                 
                                 objPatient.setPatientSymptoms(property[1]);
                                 break;
                             case "ImagePath":
                             		objPatient.setImagePath(property[1]);
                             		break;
                             		
                             default:
                                 break;
                         }
                     }
                 }
             }
         
             if (dyamicFields.isEmpty() == false && dyamicFields.length()> 2)
             {
            	 
                 String resultString = dyamicFields.substring(2); 
                 String[] dynamicColumns = resultString.split(",");
               
                 if (dynamicColumns.length > 0)
                 {
                	 String[] column=null;
                     for(int i=0;i<dynamicColumns.length;i++)
                
                     {
                    	 String strColumns=dynamicColumns[i];
                         column = strColumns.split(Pattern.quote(":"));
                     
                     
                         if (column.length == 2)
                         {
                             DynamicColumns dynColumn = new DynamicColumns();
                             dynColumn.setColumnName(column[0]);
                             dynColumn.setColumnValue(column[1]);
                        
                             dynamicColumnsArrayList.add(dynColumn);
                             objPatient.setDynamicColumns(dynamicColumnsArrayList);
                          
                         }
                     }
                 }
             }
           
            return objPatient;
         }
    
         return objPatient;
    
    }





}
	
	

