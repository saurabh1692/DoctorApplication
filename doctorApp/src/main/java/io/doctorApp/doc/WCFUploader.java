package io.doctorApp.doc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.doctorApp.doc.DrServiceResponce.ServiceStatusCodeEnum; 

@Service
public class WCFUploader
{
	final static  org.apache.logging.log4j.Logger logger= LogManager.getLogger(WCFUploader.class);
	//DatabaseConnection db=new DatabaseConnection();
	//Connection con=db.getConnection();
	
	
	ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("applicationContext.xml");
	DatabaseConnection db=(DatabaseConnection) context.getBean("mydbconnection");
	Connection con=db.getConnection();
	
	PathConfig filePath = (PathConfig) context.getBean("myPath");
	
	String path = filePath.getPath();
	
	
	
	 //**********U1******************************************************************************************************************
	public String insertPatientRecords(PatientInformation patientInfo, String userid)
     {
		System.out.println("filepath: "+path);
		String subscription = new DocServices().getSubscriptionId(userid);
		
		 Gson gson = new GsonBuilder().serializeNulls().create();
		
		 String result = null;
         PatientInformation pp = new PatientInformation();
         
         
         if (patientInfo == null)
         {
        	
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "PatientInformation class object is null", ""));
                  }
         
        else if (patientInfo.getDynamicColumns() == null)
         {
        	 
        	
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "DynamicColumns property null", ""));
         }
         else if (patientInfo.getDynamicColumns() != null && patientInfo.getDynamicColumns().size() < 0)
         {
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "DynamicColumns count zero", ""));
         }
         else if (patientInfo != null && patientInfo.getDynamicColumns() != null && patientInfo.getDynamicColumns().size() >= 0)
         {
             try
             {
              int flag=0;
          		
          		String insert = "Insert into patient_information_master(patient_reg_id,patient_first_name,patient_last_name,patient_gender,"
                         		+ "patient_date_of_birth,patient_country,patient_state,patient_city,patient_address,"
                         		+ "patient_zip_code,patient_phone_number,patient_profession,patient_marital_status,"
                         		+ "patient_religion,patient_email_id,patient_Symptoms, subscriptionid)" +
                             " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
          				try 
          				{
                        	
                        	PreparedStatement pst=con.prepareStatement(insert);
                        	pst.setString(1, patientInfo.getPatientRegId());
                        	pst.setString(2, patientInfo.getPatientFirstName());
                        	pst.setString(3, patientInfo.getPatientSurName());
                        	pst.setString(4, patientInfo.getPatientGender());
                        	pst.setString(5, patientInfo.getPatientDOB());
                        	pst.setString(6, patientInfo.getPatientCountry());
                        	pst.setString(7, patientInfo.getPatientState());
                        	pst.setString(8, patientInfo.getPatientCity());
                        	pst.setString(9, patientInfo.getPatientAddress());
                        	pst.setString(10, patientInfo.getPatientZipCode());
                        	pst.setString(11, patientInfo.getPatientPhoneNo());
                        	pst.setString(12, patientInfo.getPatientProfession());
                        	pst.setString(13, patientInfo.getPatientMaritalStatus());
                        	pst.setString(14, patientInfo.getPatientReligion());
                        	pst.setString(15, patientInfo.getPatientEmailId());
                        	pst.setString(16, patientInfo.getPatientSymptoms());
                        	
                        	pst.setString(17, subscription);
                        	
                        	flag=pst.executeUpdate();
                     
                         
                         if (flag >= 1)
                         {
                             result += "--Success Master command";
                             pp.setPatientFirstName(result);
                         }
                         else
                         {
                             result += "--Fail Master command";
                             pp.setPatientFirstName(result);
                         }
                        }
                        catch(Exception e) {
                        	System.out.println(e);
                        }

                         int flag1=0;
                       
                     if (patientInfo.getDynamicColumns() != null)
                     {
                       
                    	 for (int index = 0; index < patientInfo.getDynamicColumns().size(); index++)
                         {
                             
                             DynamicColumns ColumnInfo = patientInfo.getDynamicColumns().get(index);
                           
                             String dynamictable = "Insert into patient_dynamic_table(patient_reg_id,column_name,column_value,subscriptionid)"
                             		+ " values(?,?,?,?)";
                                    try {
                                   	 PreparedStatement pst1=con.prepareStatement(dynamictable);
                                   	 pst1.setString(1, patientInfo.getPatientRegId());
                                   	// System.out.println(patientInfo.getPatientRegId());
                                   	 pst1.setString(2, ColumnInfo.getColumnName());
                                   	 pst1.setString(3, ColumnInfo.getColumnValue());
                                   	 
                                   	 pst1.setString(4, subscription);
                                   	 
                                    flag1=pst1.executeUpdate();
                                   
                             
                             if (flag1 >= 1)
                             {
                                 result += "--Success Dynamic command" ;
                                 pp.setPatientFirstName( result);
                             }
                             else
                             {
                                 result += "--Fail Dynamic command";
                                 pp.setPatientFirstName( result);
                             }
                                    }
                                    catch(Exception e) {
                                   	 System.out.println(e);
                                   	 
                                    }
                             //}
                         }
                     }
                 
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.SUCCESS.getNumVal(), "Success--:" + result, ""));
             }
             catch (Exception ex)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.INVALID_OPERATION_EXCEPTION.getNumVal(), ex.getMessage(), ex.getMessage()));
             }

         }
       else
         {
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.INVALID_OPERATION_EXCEPTION.getNumVal(), "Unknown condition", ""));
         }
     }
	 //*****U2***********************************************************************************************************************
	DocServices service = new DocServices();
	
	 public String GetpaitentimageById(String patientRegId, String userid)
     {
		 
		 String subscription = service.getSubscriptionId(userid);
		 
         DrServiceResponce drServiceResponce = new DrServiceResponce();
         Gson gson = new GsonBuilder().serializeNulls().create();
         if(patientRegId.isEmpty()== true)
         {
             new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "patientRegId parameter is null", "");
         }
         else
         {
             List<PatientVisitImage> ImageList = new ArrayList<PatientVisitImage>();
           
                     String selectMedicien = "Select image_id, patient_image, image_name, uploaded_date from patient_image_table where patient_reg_id= '" + patientRegId + "' and subscriptionid='"+subscription+"'";
                     try {
                    	 Statement st=con.createStatement();
                    	 ResultSet rs=st.executeQuery(selectMedicien);
                    	 while(rs.next())
                    	 {
                    	 PatientVisitImage ImageInfo = new PatientVisitImage();
                         ImageInfo.ImageId = rs.getString(1);
                         ImageInfo.PatientImage = rs.getString(2);
                         ImageInfo.ImageName = rs.getString(3);
                         ImageInfo.ImageDate = rs.getString(4);
                         ImageList.add(ImageInfo); 
                     }
                     }
                     catch(Exception e) {
                    	 System.out.println(e);
                     }
             
             }

        
             drServiceResponce.setStatusCode(200);
             drServiceResponce.setMessage("Success");
             drServiceResponce.setExceptionDetails(patientRegId);
             return gson.toJson(drServiceResponce);
         }
     
     //*********U3*******************************************************************************************************************
     public String upload(MultipartFile Uploading, String fileName,String RegId, String userid)
   
     {
    	 
    	 String subscription = new DocServices().getSubscriptionId(userid);
    	 
    	 String newfile=Uploading.getOriginalFilename();
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 byte[] BlobValue = null;
         UploadedFile upload = new UploadedFile();
         UUID uuid = UUID.randomUUID();
         String newFileName=uuid.toString()+newfile;
         try
         {
        	
        	// Object a=context.getBean("myPath");
        	 //System.out.println(a.toString());
        	
          
        	 //File parent = new File(System.getProperty("java.io.tmpdir"));
        	
        	 //System.out.println(parent);
        	 
        	 String apPath=getPath();
        	 
             //File child1 = new File(apPath,"PatientProfilesImages");
        	 
        	 //File child1 = new File("H:\\tomcat 8.0\\webapps\\DoctorStudio\\PatientProfilesImages");
        	 
        	 
        	 File child1 = new File(path);
        	 
             
        	 // for server path
        	 //File child1 = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\DoctorStudio\\PatientProfilesImages");
        	
			 File child = new File(child1,newFileName);
			
			 upload.setFilePath(child.toString()); 
			 upload.setFileName(fileName);
			 Uploading.transferTo(child);
			
			 long length1 = child.length();
			 
			 upload.setFileLength(length1);
			 
            		FileInputStream in = new FileInputStream(upload.getFilePath());
            		BufferedInputStream buf=new BufferedInputStream(in);
            		int numtype=buf.available();
            		BlobValue=new byte[numtype];
            		buf.read(BlobValue);
             in.close();
             buf.close();
         }
         catch (Exception ex)
         {
        	 logger.error("Error in WCFUploader U3: "+ex);
        	 System.out.println(ex);
             upload.setFileName(ex.getMessage());
             return gson.toJson(upload);
         }
             DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
          Date today = Calendar.getInstance().getTime();       
          String uploadDate = df.format(today);
            
             String CmdString = "Insert into patient_diagnosis_table(patient_reg_id, uploaded_date, uploaded_file, file_name,subscriptionid) values(?,?,?,?,?)";
             {
            	 try 
            	 {
            		 PreparedStatement pst=con.prepareStatement(CmdString);
            		 pst.setString(1,RegId);
            		 pst.setString(2,  uploadDate);
            		 pst.setBytes(3, BlobValue);
            		 pst.setString(4, newFileName);
            		 
            		 pst.setString(5, subscription);
            		// System.out.println(CmdString);
            		 pst.executeUpdate();
            	 }
            	 catch(Exception e)
            	 {
            		 logger.error("Error in WCFUploader U3: "+e);
            		 System.out.println(e);
            	 }
             }
             
           
             return gson.toJson(upload);
     }

     //*********U4*******************************************************************************************************************
  
     public ResponseEntity<byte[]> Download(String Id, String fileName, String userid) 
     {
    	
    	 
    	 String subscription = service.getSubscriptionId(userid);
    	
    	 String sql = "Select file_name, uploaded_file from patient_diagnosis_table where id=" + Integer.parseInt(Id)+" and subscriptionid='"+subscription+"'";
         String extension=null;
    	 try
    	 {
    		 Statement st=con.createStatement();
        
    		 ResultSet result = st.executeQuery(sql);
         
         if (result.next())
         {
        	 String name=result.getString(1);
        	 extension=name.substring(name.lastIndexOf('.') + 1);
        	 System.out.println("name: "+name);
        	 System.out.println("ext: "+extension);
         }
         else
         {
        	 extension="jpg";
         }
    	 }catch(Exception e)
    	 {
    		 System.out.println(e);
    	 }
    	 
        
    	// System.out.println( fileName.);
    	 //String filename = "H:\\tomcat 8.0\\webapps\\DoctorStudio\\PatientProfilesImages\\" + fileName + "."+extension;
    	 
    	 String filename = path+"\\" + fileName + "."+extension;
    	 
    	 // for server path
    	// String filename = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\DoctorStudio\\PatientProfilesImages\\" + fileName + "."+extension;
         
    	 InputStream inputImage=null;
		
    	 try
    	 {
			inputImage = new FileInputStream(filename);
		 } 
    	 catch (FileNotFoundException e1)
    	 {
			System.out.println(e1);
			
		 }
    	 
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
         byte[] buffer = new byte[512];
        
         int l=0;
		
         try
         {
			l = inputImage.read(buffer);
		 }
         catch (IOException e)
         {
			System.out.println(e);
		 }
         
         while(l >= 0)
         {
             outputStream.write(buffer, 0, l);
             
             try
             {
				l = inputImage.read(buffer);
			 } 
             catch (IOException e)
             {
				System.out.println(e); 
			}
         }
        
         HttpHeaders headers = new HttpHeaders();
         headers.set("Content-Type", "image/jpeg");
         headers.set("Content-Disposition", "attachment; filename=\"" + fileName+ ".\""+extension);
         return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
     }
     
//*********U5*******************************************************************************************************************************************
     
     public String getPath() throws UnsupportedEncodingException {
    	 String path = this.getClass().getClassLoader().getResource("").getPath();
    	 String fullPath = URLDecoder.decode(path, "UTF-8");
    	String pathArr[] = fullPath.split("/classes/");
    	
    	 fullPath = pathArr[0];
    	 String reponsePath = "";
    	
    	 
    	 reponsePath = new File(fullPath).getPath() + File.separatorChar+"images";
    	System.out.println(reponsePath+" responce path");
    	 return reponsePath;
    	 }
     
     
     public String UploadImage(MultipartFile Uploading, String fileName,String RegId, String userid)
     {
    	
    	 String subscription = new DocServices().getSubscriptionId(userid);
    	 
         byte[] BlobValue = null;
         String originalFilename = Uploading.getOriginalFilename();
         Gson gson = new GsonBuilder().serializeNulls().create();
         try
         {
        	 
          String apPath=getPath();
          //File child1 = new File(apPath,"PatientProfilesImages");
          
          //File child1 = new File("H:\\tomcat 8.0\\webapps\\DoctorStudio\\PatientProfilesImages");
          
          File child1 = new File(path);
          
          // For server path
         // File child1 = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\DoctorStudio\\PatientProfilesImages");
         
          
			 String imageDirPath=child1.toString();
			 
        	 UploadedFile upload = new UploadedFile();
        	 
        	 File child = new File(imageDirPath+"\\"+originalFilename);
			
        	 upload.setFilePath(child.toString());
			
        	 upload.setFileName(fileName);
			
        	 Uploading.transferTo(child);
			
        	 long length = child.length();
			
        	 upload.setFileLength(length);
			 
             upload.setFileLength(length);
             
             InputStream input=new FileInputStream(upload.getFilePath());
             
             BufferedInputStream b=new BufferedInputStream(input);
             
             int numbyte1=b.available();
             
             BlobValue=new byte[numbyte1];
             input.read();
             //input.reset();
             input.close();
             
             b.close();
             
             DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
             Date today = Calendar.getInstance().getTime();       
             String uploadDate = df.format(today);
            // System.out.println(RegId);
             
             
             String getimageid = "select image_id from patient_image_table where patient_reg_id='"+RegId+"'";
             
             Statement st_get_image_id = con.createStatement();
             ResultSet rs_get_image_id = st_get_image_id.executeQuery(getimageid);
             
             rs_get_image_id.next();
             String image_id = rs_get_image_id.getString(1);
             
            
             String CmdString =  "DELETE FROM patient_image_table WHERE patient_reg_id ='"+RegId+"' and subscriptionid='"+subscription+"' and image_id='"+image_id+"'";
            		 
             String CmdString1= "Insert into patient_image_table(patient_reg_id, uploaded_date, patient_image, image_name, image_path, subscriptionid) "
            		 		+ "values('"+RegId+"','"+uploadDate+"','"+BlobValue+"','"+fileName+"','"+originalFilename+"','"+subscription+"')";
             try 
             {
            	 PreparedStatement pst=con.prepareStatement(CmdString);
            	 pst.executeUpdate();
            	 PreparedStatement pst1=con.prepareStatement(CmdString1);
            	
            	 pst1.executeUpdate();
             }
             catch(Exception e)
             {
            	 System.out.println(e);
             }
             upload.setFilePath(imageDirPath+"\\"+originalFilename);
             return gson.toJson(upload);
         }
         catch (Exception ex)
         {
             UploadedFile upload = new UploadedFile();
             upload.setFileName(ex.getMessage());
             return gson.toJson(upload);
         }
     }
     //*********U6*******************************************************************************************************************************************
      public String UpdateImage(MultipartFile Uploading, String fileName,String RegId, String userid)
     {
    	 String subscription = new DocServices().getSubscriptionId(userid);
    	  
    	 byte[] BlobValue = null;
    	 String originalFilename = Uploading.getOriginalFilename();
    	 Gson gson = new GsonBuilder().serializeNulls().create();
         try
         {
        	
          String apPath=getPath();
         // File child1 = new File(apPath,"PatientProfilesImages");
          //File child1 = new File("H:\\tomcat 8.0\\webapps\\DoctorStudio\\PatientProfilesImages");
          
          File child1 = new File(path);
          
          // For server path
         // File child1 = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\DoctorStudio\\PatientProfilesImages");
          
          String imageDirPath=child1.toString();
        	 
        	 UploadedFile upload = new UploadedFile();
			   
        	 File child = new File(imageDirPath+"\\"+originalFilename);
			    upload.setFilePath(child.toString());
			    upload.setFileName(fileName);
			    Uploading.transferTo(child);
			    Uploading.transferTo(child);
			    
			    long length = child.length();
			    upload.setFileLength(length);
             
             InputStream input=new FileInputStream(upload.getFilePath());
         
             BufferedInputStream b=new BufferedInputStream(input);
             
             int numbyte1=b.available();
             byte[] buf=new byte[numbyte1];
             BlobValue=new byte[numbyte1];
             
             input.read();
          
             input.close();
            
             b.close();
      
             
             DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
         
             Date today = Calendar.getInstance().getTime();       
         
          String uploadDate = df.format(today);  
          
          String query  = "Select patient_reg_id, patient_image, image_name,image_path from patient_image_table where patient_reg_id=" + RegId +" and subscriptionid='"+subscription+"'";
            
          try {
            	 Statement st=con.createStatement();
            	 ResultSet rs=st.executeQuery(query);
            	 
                 String CmdString = "Insert into patient_image_backup(patient_reg_id, uploaded_date, patient_image, image_name, image_path, subscriptionid) "
                 					+ "values('"+RegId+"','"+uploadDate+"','"+BlobValue+"','"+fileName+"','"+upload.getFilePath()+"', '"+subscription+"')";
                 PreparedStatement pst2=con.prepareStatement(CmdString);
         		 pst2.executeUpdate();
            	 
                 if(rs!=null) 
                 {
                	 while(rs.next())
                	 {
            		  String cmdUpdateImage = "Update patient_image_table set image_path='"+upload.getFilePath()+"', patient_image='"+BlobValue+"' where patient_reg_id ='"+RegId+"' and subscriptionid='"+subscription+"'";
            		  PreparedStatement pst=con.prepareStatement(cmdUpdateImage);
            		  pst.executeUpdate();
            		  upload.setFilePath(imageDirPath+"\\"+originalFilename);
                	 }
            	 }
            	 else
            	 {
            		 String CmdString1 =  "Insert into patient_image_table(patient_reg_id, uploaded_date, patient_image, image_name, image_path, subscriptionid) "
               		 		+ "values('"+RegId+"','"+uploadDate+"','"+BlobValue+"','"+fileName+"','"+upload.getFilePath()+"', '"+subscription+"')";
                 	 PreparedStatement pst1=con.prepareStatement(CmdString1);
             		 pst1.executeUpdate();
             		 
            		 
            	 }
            	
             }
             catch(Exception e) {
            	 System.out.println(e);
             }
             
            
             return gson.toJson(upload);
         }
         catch (Exception ex)
         {
             UploadedFile upload = new UploadedFile();
             upload.setFileName(ex.getMessage());
             return gson.toJson(upload);
         }

     }
     
     
   //*********U7*******************************************************************************************************************************************
     
     
     public String UploadVisitImage(MultipartFile Uploading, String fileName, String RegId, String userid)
     {
    	 
    	String subscription = new DocServices().getSubscriptionId(userid);
    	 
    	 byte[] BlobValue = null;
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 String originalFilename = Uploading.getOriginalFilename();
    	 UploadedFile upload = new UploadedFile();
    	 try
         {
        	 
          String apPath=getPath();
          
          File child1 = new File(path);
          
         //File child1 = new File("H:\\tomcat 8.0\\webapps\\DoctorStudio\\PatientProfilesImages");
          
          // For Server path
        // File child1 = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\DoctorStudio\\PatientProfilesImages");
			 
         String imageDirPath=child1.toString();
			   
        	 File child = new File(imageDirPath+"\\"+originalFilename);
			    upload.setFilePath(child.toString());
			    upload.setFileName(fileName);
			    Uploading.transferTo(child);
			    long length = child.length();
			    

             upload.setFileLength(length);
             
             InputStream input=new FileInputStream(upload.getFilePath());
            
         
             BufferedInputStream b=new BufferedInputStream(input);
             
             int numbyte1=b.available();
             byte[] buf=new byte[numbyte1];
             BlobValue=new byte[numbyte1];
             input.read();
             //input.reset();
             input.close();
             b.close();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
            Date today = Calendar.getInstance().getTime();       
            String uploadDate = df.format(today);  
            String lastvisitid = "";
            String query = "SELECT visit_id  FROM patient_visit_details where subscriptionid='"+subscription+"' order by visit_id desc limit 1";
            	 Statement st=con.createStatement();
            	 ResultSet rs=st.executeQuery(query);
            	 
            	 while(rs.next()) 
            	 {
            		 lastvisitid=rs.getString(1);
            		 System.out.println(lastvisitid);
                	 String cmdUpdateImage = "Update patient_visit_details set patient_visit_image_path='"+originalFilename+"', patient_visit_image='"+BlobValue+"', patient_visit_image_name ='"+fileName+"' where patient_reg_id='"+RegId+"' AND visit_id='" + lastvisitid+"' and subscriptionid='"+subscription+"'" ;
                	 PreparedStatement pst=con.prepareStatement(cmdUpdateImage);
                	 pst.executeUpdate();
                	 upload.setFilePath(imageDirPath+"\\"+originalFilename);
            	 }
             //return gson.toJson(upload);
         }
         catch (Exception ex)
         {
             UploadedFile upload1 = new UploadedFile();
             upload1.setFileName(ex.getMessage());
             return gson.toJson(upload1);
         }
    	 return gson.toJson(upload);
     }
   //*********U8*******************************************************************************************************************************************
     
     public String UploadGroupImage(MultipartFile Uploading,String fileName, String Name ,String Descripton , String userid)
     {
    	
    	 String subscription = new DocServices().getSubscriptionId(userid);
    	 
    	Gson gson = new GsonBuilder().serializeNulls().create();
    	
    	String originalFilename = Uploading.getOriginalFilename();
    	
    	byte[] BlobValue = null;
         try
         {
        	
          String apPath=getPath();
         // File child1 = new File(apPath,"PatientProfilesImages");
         // File child1 = new File("H:\\tomcat 8.0\\webapps\\DoctorStudio\\PatientProfilesImages");
          
          File child1 = new File(path);
          
          // For server path
          //File child1 = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\DoctorStudio\\PatientProfilesImages");
          
          //String imageDirPath=child1.toString();
        	 
        	 UploadedFile upload = new UploadedFile();
			   
        	 File child = new File(child1+"\\"+originalFilename);
			    upload.setFilePath(child.toString());
			    upload.setFileName(fileName);
			    Uploading.transferTo(child);
			    
	              long length = child.length();
            
	             String imagepath="PatientProfilesImages/"+originalFilename;
             upload.setFileLength(length);
             System.out.println(length+" length file ");
             InputStream input=new FileInputStream(upload.getFilePath());
            
         
             BufferedInputStream b=new BufferedInputStream(input);
             
             int numbyte1=b.available();
             BlobValue=new byte[numbyte1];
             
             input.read();
             //input.reset();
             input.close();
             b.close();
             
               DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");

          
            Date today = Calendar.getInstance().getTime();       
           
            String uploadDate = df.format(today);  
            
             String CmdString = "Insert into group_master(group_name, group_descriptionl,image_name, group_image, group_path, group_upload_date, subscriptionid)"
             		+ " values('"+Name+"','"+Descripton+"','"+fileName+"','"+BlobValue+"','"+imagepath+"','"+uploadDate+"', '"+subscription+"')";
             
            	PreparedStatement pst=con.prepareStatement(CmdString);
            	pst.executeUpdate();
            	
            // upload.setFilePath(String.valueOf(imageDirPath));
             return gson.toJson(upload);
         }
         catch (Exception ex)
         {
        	 System.out.println(ex);
             UploadedFile upload = new UploadedFile();
             upload.setFileName(ex.getMessage());
             return gson.toJson(upload);
         }
     }
     
   
   //*********U9*******************************************************************************************************************************************
     public String Delete(String id,String filename1,String delvalue, String userid)
     {
    	 
    	 String subscription = service.getSubscriptionId(userid);
    	 
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
    	 DrServiceResponce drServiceResponce = new DrServiceResponce();
    	 if (delvalue.isEmpty() == true)
         {
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
         }	
    	 
    	// if(delvalue=="Delete") 
    	 {
    		String filename="'"+filename1+"'";
    		String query="Delete from patient_diagnosis_table where id=" + Integer.parseInt(id)+" and file_name="+filename +" and subscriptionid='"+subscription+"'";
    		try
    		{
    			PreparedStatement pst=con.prepareStatement(query);
    			pst.executeUpdate();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    		drServiceResponce.setStatusCode(200);
            drServiceResponce.setMessage("Success");
            drServiceResponce.setExceptionDetails(id);
        
        
     }
    	 return gson.toJson(drServiceResponce);
     }

     
  //85.-----------------------------------------Upload Logo -------------------------------------------------------------------//
	public String uploadLogo(MultipartFile uploading, String userid) 
	{
		
		JSONObject response = new JSONObject();
		
		 String subscription = new DocServices().getSubscriptionId(userid);
    	 
	    	Gson gson = new GsonBuilder().serializeNulls().create();
	    	
	    	 UUID uuid = UUID.randomUUID();
	    	
	    	String originalFilename = uploading.getOriginalFilename();
	    	
	    	String finalFileName = uuid.toString()+originalFilename;
	    	byte[] BlobValue = null;
	         try
	         {
	        	
	        	 PathConfig filePath = (PathConfig) context.getBean("myPath");
	        		
	        		String logopath = filePath.getLogoPath();
	          
	        		File child1 = new File(logopath);
	          
	         
	        	 
	        		UploadedFile upload = new UploadedFile();
				   
	        		File child = new File(child1+"\\"+finalFileName);
				   
	        		upload.setFilePath(child.toString());
				    upload.setFileName(finalFileName);
				    uploading.transferTo(child);
				    
		              long length = child.length();
	            
		           //  String imagepath="PatientProfilesImages/"+originalFilename;
	            
		             upload.setFileLength(length);
	             
		            // System.out.println("originalFilename: "+ finalFileName);
	            
		             InputStream input=new FileInputStream(upload.getFilePath());
	            
		             BufferedInputStream b=new BufferedInputStream(input);
	             
		             int numbyte1=b.available();
		             BlobValue=new byte[numbyte1];
	             
		             input.read();
		             //input.reset();
		             input.close();
		             b.close();
	             
	           
		             //check if logo already exist
		             String getLogo = "select *from logo_path where subscriptionid='"+subscription+"'";
		             Statement st = con.createStatement();
		             ResultSet rs= st.executeQuery(getLogo);
		            
		             if(rs.next() == false)
		             {
		            	 
		            	
		            	 System.out.println("in if");
		            	 String setLogo = "insert into logo_path (logo_name , logo_path_name, subscriptionid) values(?,?,?)";
		            	 
		            	 PreparedStatement ps = con.prepareStatement(setLogo);
		            	 ps.setString(1, finalFileName);
		            	 ps.setString(2, child.toString());
		            	 ps.setString(3, subscription);
		            	 
		            	 ps.executeUpdate();
		            	 
		             }
		             else
		             {
		            	 ResultSet rs1= st.executeQuery(getLogo);
		            	 rs1.next();
			             String oldlogoName = rs1.getString(2);
		            	 System.out.println("in else");
		            	 File file = new File(child1+"\\"+oldlogoName);
		            	 
		            	 file.delete();
		            	 
		            	 String updateLogo = "update logo_path set logo_name=?, logo_path_name=? where subscriptionid='"+subscription+"'";
		            	 
		            	 PreparedStatement ps = con.prepareStatement(updateLogo);
		            	 ps.setString(1, finalFileName);
		            	 ps.setString(2, child.toString());
		            	
		            	 ps.executeUpdate();
		            	 
		             }
		             
		             response.put("StatusCode", "200");
		             response.put("Message", "success");
		             
	            
	         }
	         catch (Exception ex)
	         {
	        	 System.out.println(ex);
	            // UploadedFile upload = new UploadedFile();
	            // upload.setFileName(ex.getMessage());
	        	 
	        	 try
	        	 {
	        		 response.put("StatusCode", "500");
		             response.put("Message", ex);
		             
		             return gson.toJson(response.toString());
	        	 }
	        	 catch(Exception e)
	        	 {
	        		 System.out.println(e);
	        	 }
	        	 
	             
	         }
	         
	         return response.toString();
	}
      
     
     
     
}
     

