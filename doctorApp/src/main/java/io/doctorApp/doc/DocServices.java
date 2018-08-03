package io.doctorApp.doc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.json.JsonValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

//import org.apache.log4j.Logger;
//import ch.qos.logback.classic.Logger;

import io.doctorApp.doc.DrServiceResponce.ServiceStatusCodeEnum;

@Service
public class DocServices {
	//final static  Logger logger=Logger.getLogger(DocServices.class);
	final static  Logger logger=LogManager.getLogger(DocServices.class);
	
	ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("applicationContext.xml");
	DatabaseConnection db=(DatabaseConnection) context.getBean("mydbconnection");
	
	PathConfig filePath1 = (PathConfig) context.getBean("myPath");
	String pathcon = filePath1.getOtpPath();
	Connection con=db.getConnection();
	
 //||||1||||||||||OK|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
	public String createNewUser( UserInformation pr, String userid)
	{
		
		
		
		//DatabaseConnection db=new DatabaseConnection();
		//Connection con=db.getConnection();
		
		String subscription = getSubscriptionId(userid);
		
		 Gson gson = new GsonBuilder().serializeNulls().create();
		 UserInformation responce = new UserInformation();
         if(pr !=null)
         {
            String sql1 = "insert into login_master(user_name,user_password,user_roll,user_email_id,user_isActive,subscription)" +
                           "Values(?,?,?,?," + 0 + ", '"+subscription+"')";
            try 
            {
            	PreparedStatement pst1=con.prepareStatement(sql1);
                        	
                pst1.setString(1,pr.getLoginUserName());
                pst1.setString(2, pr.getUserPassword());
                pst1.setString(3,pr.getUserRoll());
                pst1.setString(4, pr.getUserEmail());
             // pst1.setString(5, pr.getUserIsActive());
                pst1.executeUpdate();
                if(pst1.getFetchSize()>0)
                {
             		 responce.UserFirstName= "Success Login Command";
                }
                else 
                {
                  	 responce.UserFirstName= "Fail Login Command";
                }
             }
             catch(Exception e)
            	{
                      responce.UserFirstName = e.getMessage();
                      return gson.toJson(responce);
                      
                }
                         
                         String sql2 = "insert into user_registration_table(user_first_name,user_last_name,user_gender,user_country,"
                         		+ "user_date_of_birth,user_parmanent_address,user_temp_address,user_city,user_zip_code,"
                         		+ "user_mobile_number,user_email_id,user_profession,user_marital_status,user_religion,user_roll, subscriptionid)" +
                             "Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                                  
                     try {
                    	 PreparedStatement pst2=con.prepareStatement(sql2);
                    	pst2.setString(1, pr.getUserFirstName());
                    	 pst2.setString(2, pr.getUserLastName());
                    	 pst2.setString(3, pr.getUserGender());
                    	 pst2.setString(4, pr.getUserCountry());
                    	 pst2.setString(5, pr.getUserDateOfBirth());
                    	 pst2.setString(6,pr.getUserPemanentAddres() );
                    	 pst2.setString(7, pr.getUserTempAddress());
                    	 pst2.setString(8, pr.getUserCity());
                    	 pst2.setString(9, pr.getUserZipcode());
                    	 pst2.setString(10, pr.getUserMobileNo());
                    	 pst2.setString(11, pr.getUserEmail());
                    	 pst2.setString(12, pr.getUserProfession());
                    	 pst2.setString(13, pr.getUserMaritalStatus());
                    	 pst2.setString(14, pr.getUserReligion());
                    	 pst2.setString(15, pr.getUserRoll());
                    	 pst2.setString(16, subscription);
                    	pst2.executeUpdate();
                    	
                    	if(pst2.getFetchSize()>0) {
                    		//int code = (int)ServiceStatusCode.TransactionRollBack;
                    		//String serviceCode = code.ToString();
                   		 responce.UserFirstName= "Row Affected zero User Table";
                   	 }
                   	 else {
                   		 responce.UserFirstName= "Success UserTable";
                   	 }
                    	 
                     }
                     catch(Exception e) {
                    	 logger.error("Error in Service : "+e);
                    	 responce.UserFirstName = e.getMessage();
                         return gson.toJson(responce);
                     }
     
         }                
         else
         {
        	 responce.UserFirstName = "Object Null";
         }    
         return gson.toJson(responce);
 }
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------
	
	 private String RandomString(int size, boolean lowerCase)
     {
         StringBuilder builder = new StringBuilder();
         Random random = new Random();
         char ch;
         for (int i = 0; i < size; i++)
         {
             ch =(char) Math.floor(26 * random.nextDouble()+ 65);
             builder.append(ch);
         }
         if (lowerCase)
             return builder.toString().toLowerCase();
         return builder.toString();
     }
     private int RandomNumber(int max)
     {
         Random random = new Random();
         return random.nextInt( max);
     }
     public String GetPassword()
     {
         StringBuilder builder = new StringBuilder();
         builder.append(RandomString(4, true));
         builder.append(RandomNumber(9999));
         builder.append(RandomString(2, false));
         return builder.toString();
     }
     
     //||2---------------------------------------------------------------------------------------------------------------------------------------------
    
     public String CreateNewAppointment(String Appointment, String userid)
     {
      // DatabaseConnection db=new DatabaseConnection();
      // Connection con=db.getConnection();

    	 String subscription = getSubscriptionId(userid);
    	 
     	PatientAppoientment AI = new PatientAppoientment();
     	PatientAppoientment responce = new PatientAppoientment();
     	 LocalDate currentDate = LocalDate.now();
     	 
     	 Gson gson = new GsonBuilder().serializeNulls().create();
     	 
     	 DateFormat dateFormat = new SimpleDateFormat("HH:mm");
         Date date = new Date();
         String VisitTime=dateFormat.format(date);
 
         try 
         {
        	 JSONObject obj=new JSONObject(Appointment);
         
        	 String patientdetail=obj.getString("AppointmentInformation");
        	 JSONObject patientobject=new JSONObject(patientdetail);
         
         
         
        	 AI.setAppoientmentDate(patientobject.getString("AppointmentDate"));
         
        	 AI.setAppoientmentStartTime(patientobject.getString("AppointmentStartTime"));
         
        	 AI.setAppoientmentEndTime(patientobject.getString("AppointmentEndTime"));
         
        	 AI.setAppoientmentDescription(patientobject.getString("AppointmentDescription"));
         
        	 AI.setAppoientmentTitle(patientobject.getString("AppointmentTitle"));
        	 //AI.setAppoientmentVisitDate(patientobject.getString("AppointmentVisitDate"));
        	// AI.setAppoientmentVisitTime(patientobject.getString("AppointmentVisitTime"));
        	// AI.setPatientRegId(patientobject.getString("PatientRegId"));
         
         }
         catch(Exception e)
    	 {
        	 logger.error("Error in Service : "+e);
    		 System.out.println(e);
    	 }
     
         
     	 System.out.println(AI.getAppoientmentDate());
     	 if (AI != null)
         {
     	  String newAppointment = "insert into patient_appointment_table(patient_registration_id, appointment_visit_date,appointment_date,"
                      		     + " appointment_visit_time, appointment_start_time, appointment_end_time, appointment_title, appointment_discription,subscriptionid)" +
                                   " Values(?,'"+currentDate+"',?,'"+VisitTime+"',?,?,?,?,?)";
                         
                       try  
                       {
                    	   PreparedStatement pst=con.prepareStatement(newAppointment);
                    	   
                    	   pst.setString(1, AI.getPatientRegId());
                    	 
                    	   pst.setString(2, AI.getAppoientmentDate());
                    	  
                    	   pst.setString(3, AI.getAppoientmentStartTime());
                    	   
                    	   pst.setString(4, AI.getAppoientmentEndTime());
                    	  
                    	   pst.setString(5, AI.getAppoientmentTitle());
                    	  
                    	   pst.setString(6,AI.getAppoientmentDescription() );
                    	   
                    	   pst.setString(7, subscription);
                    	   
                    	   int i=pst.executeUpdate();
                    	  
                    	   if(i>0) 
                    	   {
                    		   responce.setPatientRegId(responce.getPatientRegId()+"Success login Command "); 
                    	   }
                    	   else
                    	   {
                    		   responce.setPatientRegId(responce.getPatientRegId()+"Failed login command");
                    	   }
                    	   
                     }  
                       catch(Exception e)
                       {
                    	   logger.error("Error in Service : "+e);
                     	  responce.setPatientRegId(e.getMessage());
                           return gson.toJson(responce);
                       }
                 }
         else
         {
             responce.setPatientRegId("Object Null");
         }
         return gson.toJson(responce);
     }

     
     //|||||3---------------------------------------------------------------------------------------------------------------------------------------------
   
     public String GetPatientAppointmentDetails(String userid)
     {
    	 
    	 logger.info("In service number 3333");
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	// DatabaseConnection db=new DatabaseConnection();
  		//Connection con=db.getConnection();
    	 
    	 
    	 String subscription=getSubscriptionId(userid);
    	 
    	 
  		List<ResultSet> list=new ArrayList<ResultSet>();
         DrServiceResponce drServiceResponce = new DrServiceResponce();
         LocalDate local = LocalDate.now();//For reference
         System.out.println(local+"at line 227");
       
         List<PatientAppoientment> AL = new ArrayList<PatientAppoientment>();
            
         String CounttotalVisits = "Select  appointment_id,patient_registration_id,appointment_visit_date,appointment_visit_time,"+
                                   "appointment_start_time,appointment_end_time,appointment_title,appointment_discription ,appointment_date "
                                   + "from patient_appointment_table where appointment_isdelete="+0+" and appointment_date ="+"'"+local+"' and subscriptionid='"+subscription+"'"; 
                     try 
                     {
                    	 Statement st =con.createStatement();
                    	 ResultSet rs=st.executeQuery(CounttotalVisits);
                    	
                    	 while(rs.next()) 
                    	 {
                             rs.getString(1);
                    		 list.add(rs);
                    		 PatientAppoientment appointmentDetails = new PatientAppoientment();
                            
                             appointmentDetails.setAppoientmentStartTime(rs.getString(5));
                             appointmentDetails.setAppoientmentEndTime(rs.getString(6));
                             if (String.valueOf(rs.getString(9)) == null)
                             {
                                 String dateformate = String.valueOf(rs.getString(9));
                                 String[] splitArray = dateformate.split("-");
                                 if (splitArray.length == 3)
                                 {
                                     String tempdate = splitArray[2] + "-" + splitArray[1] + "-" + splitArray[0];
                                     appointmentDetails.setAppoientmentDate(tempdate);
                                 }
                             }
                             else
                             {
                            	 String dateformate = String.valueOf(rs.getString(9));
                                 String[] splitArray = dateformate.split("-");
                                 if (splitArray.length == 3)
                                 {
                                     String tempdate = splitArray[0] + "-" + splitArray[1] + "-" + splitArray[2];
                                    
                                     appointmentDetails.setAppoientmentDate(tempdate);
                                 }
                             }
                             appointmentDetails.setAppoientmentTitle(rs.getString(7));
                             appointmentDetails.setAppoientmentDescription(rs.getString(8));
                            AL.add(appointmentDetails);
                     /*if (list.size() > 0)
                     {
                         for(int i=0;i<list.size();i++)
                         {
                             PatientAppoientment appointmentDetails = new PatientAppoientment();
                             appointmentDetails.setAppoientmentId(list.get(i).getInt(1));
                             appointmentDetails.setPatientRegId(list.get(i).getString(2));
                             appointmentDetails.setAppoientmentVisitDate(list.get(i).getString(3));
                             appointmentDetails.setAppoientmentVisitTime(list.get(i).getString(4));
                             appointmentDetails.setAppoientmentStartTime(list.get(i).getString(5));
                             appointmentDetails.setAppoientmentEndTime(list.get(i).getString(6));
                             if (String.valueOf(list.get(i).getString(9)) == null)
                             {
                                 String dateformate = String.valueOf(list.get(i).getString(9));
                                 String[] splitArray = dateformate.split("-");
                                 if (splitArray.length == 3)
                                 {
                                     String tempdate = splitArray[2] + "-" + splitArray[1] + "-" + splitArray[0];
                                     appointmentDetails.setAppoientmentDate(tempdate);
                                 }
                             }
                             else
                             {
                            	 String dateformate = String.valueOf(list.get(i).getString(9));
                                 String[] splitArray = dateformate.split("-");
                                 if (splitArray.length == 3)
                                 {
                                     String tempdate = splitArray[0] + "-" + splitArray[1] + "-" + splitArray[2];
                                    
                                     appointmentDetails.setAppoientmentDate(tempdate);
                                 }
                             }
                             appointmentDetails.setAppoientmentTitle(list.get(i).getString(7));
                             appointmentDetails.setAppoientmentDescription(list.get(i).getString(8));
                            AL.add(appointmentDetails);
                    	 }
                     }*/
                    	 }
                    	 drServiceResponce.setPatientAppointmentInformationDetails(AL);//.PatientAppointmentInformationDetails= AL;
                     }
                    	 catch(Exception e) 
                     	 {
                    		 logger.error("Error in Service : "+e);
                    		 System.out.println(e);
                    	 }	
                     //System.out.println("data not found");
                 drServiceResponce.setStatusCode (200);
                 drServiceResponce.setMessage("Success");
                 
                 drServiceResponce.setExceptionDetails("No Record Found");
               
                 return gson.toJson(drServiceResponce);
     }
     
     //||||||4---------------------------------------------------------------------------------------------------------------------------------------------

     public String GetAllPatientPaymentDetails(String Date_Detail, String userid) 
     {
    	// DatabaseConnection db=new DatabaseConnection();
  		// Connection con=db.getConnection();
  		
    	 
    	 String subscription = getSubscriptionId(userid);
    	 
  		 Gson gson = new GsonBuilder().serializeNulls().create();
  		
         DrServiceResponce drServiceResponce = new DrServiceResponce();
         PaymentDetails patientvisitAmount = new PaymentDetails();
		 List<PatientVisitInformation> PatientPaymentDetails = new ArrayList<PatientVisitInformation>();
    	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    	 Date startDate=null;
    	 String newDateString="";
    	 try 
    	  {
    		    startDate = df.parse(Date_Detail);
    		    newDateString = df.format(startDate);
    		  // System.out.println(newDateString);
    	  } 
    	 catch (ParseException e)
    	  {
    		    e.printStackTrace();
    	  }
    	 String CounttotalVisits = "select patient_information_master.patient_first_name,patient_visit_details.visit_id,"
                 		+ "patient_visit_details.patient_visit_date,"
                 		+ "patient_visit_details.patient_visit_time,patient_visit_details.patient_reg_id,"
                 		+ "patient_visit_details.patient_visit_due_amount,patient_visit_details.patient_visit_advance_amount,"
                 		+ " patient_visit_details.patient_visit_total_amount,patient_visit_details.patient_visit_paid_amount, "
                 		+ "patient_visit_details.patient_visit_fee_concession, patient_visit_details.patient_next_visit_date,payment_mode"
                 		+ " from patient_visit_details INNER JOIN patient_information_master on "
                 		+ "patient_information_master.patient_reg_id= patient_visit_details.patient_reg_id"
                 		+ " where patient_visit_details.patient_visit_isdelete='0' and patient_visit_details.patient_visit_date= "+"'"+newDateString +"' and patient_information_master.subscriptionid='"+subscription+"' and patient_visit_details.subscriptionid='"+subscription+"'";
                 		
    	try 
    	  {
              Statement st=con.createStatement();
              ResultSet rs=st.executeQuery(CounttotalVisits);
              double tempDue = 0;
              double tempAdv = 0;
              double temptotal = 0;
              double tempPaid = 0;
              double tempConsection = 0;	 
              while(rs.next())
               {
                		 PatientVisitInformation PaymentVisitDetails = new PatientVisitInformation();
                		
                         
                		 PaymentVisitDetails.setVisitId(rs.getString(2));
                         PaymentVisitDetails.setRegistrationId(rs.getString(5));
                        
                         PaymentVisitDetails.setVisitDate(rs.getString(3));
                         PaymentVisitDetails.setPatientVisitName(rs.getString(1));
                         PaymentVisitDetails.setVisitdueamount(rs.getString(6));
                         PaymentVisitDetails.setVisitadvanceamount(rs.getString(7));
                         PaymentVisitDetails.setVisittotalamount(rs.getString(8));
                      
                         PaymentVisitDetails.setPatientPaidAmount(rs.getString(9));
                         PaymentVisitDetails.setPatientFeeConcession(rs.getString(10));
                         PaymentVisitDetails.setPatientNextvisitdate(rs.getString(11));
                         PaymentVisitDetails.setPaymentMode(rs.getString(12));
                		 PatientPaymentDetails.add(PaymentVisitDetails);
                	
                			double tempDueAmt = 0;
                         
               				tempDueAmt=Double.parseDouble(PaymentVisitDetails.getVisitdueamount());
               				tempDue =tempDue+tempDueAmt;
                              
               				double tempAdvanceAmt = 0;
                          
                              tempAdvanceAmt=Double.parseDouble( PaymentVisitDetails.getVisitadvanceamount());
                              tempAdv=tempAdv + tempAdvanceAmt;
                              
                              double temptotalamunt = 0;
                            
                              temptotalamunt=Double.parseDouble( PaymentVisitDetails.getVisittotalamount());
                              temptotal=temptotal + temptotalamunt;
                              
                              double temppaidamount = 0;
                            
                              temppaidamount=Double.parseDouble(PaymentVisitDetails.getPatientPaidAmount());
                              tempPaid=tempPaid +temppaidamount;
                              double tempconcessionAmount = 0;
                          
                              tempconcessionAmount=Double.parseDouble(PaymentVisitDetails.getPatientFeeConcession());
                              tempConsection=tempConsection + tempconcessionAmount;
                		
                	 patientvisitAmount.setDueAmount(String.valueOf(tempDue));
                     patientvisitAmount.setAdvancedAmount(String.valueOf(tempAdv));
                     patientvisitAmount.setPatientVisitAmount(String.valueOf(temptotal));
                     patientvisitAmount.setPatientVisitPaidAmount(String.valueOf(tempPaid));
                     patientvisitAmount.setPatientVisitConcessionAmount(String.valueOf(tempConsection));
               }
                  
    	 }
                	 
    	catch(Exception e)
    	 {
    		 logger.error("Error in Service : "+e);
        	 System.out.println(e);
         }    
    	drServiceResponce.setPatientPaymentVisitDetails(patientvisitAmount);//PatientPaymentVisitDetails = patientvisitAmount;
    	drServiceResponce.setPatientsVisitInformationDetails(PatientPaymentDetails);//PatientsVisitInformationDetails = PatientPaymentDetails;
    	drServiceResponce.setStatusCode(200);
    	drServiceResponce.setMessage("Success");
    	drServiceResponce.setExceptionDetails( "No Record Found");
      
    	return gson.toJson(drServiceResponce);
     }
     
     //|||||5---------------------------------------------------------------------------------------------------------------------------------------------
 public String GetIndividualPatientPaymentDetails(String  RegistrationId, String userid)
     {
	
	 	String subscription=getSubscriptionId(userid);
	 	// PatientVisitInformation PaymentVisitDetails = new PatientVisitInformation();
         DrServiceResponce drServiceResponce = new DrServiceResponce();
        
         Gson gson = new GsonBuilder().serializeNulls().create();
         
        //String pattern="yyyy-MM-dd";
       
         List<PatientVisitInformation> PatientPaymentDetailsList = new ArrayList<PatientVisitInformation>();
         PaymentDetails patientvisitAmount = new PaymentDetails();
       //  DatabaseConnection db=new DatabaseConnection();
   		// Connection con=db.getConnection();
   		 
         String CounttotalVisits = "select  visit_id, patient_visit_date, patient_visit_time,patient_reg_id,patient_visit_due_amount,"
                 		+ " patient_visit_advance_amount, patient_visit_total_amount,patient_visit_paid_amount,"
                 		+ " patient_visit_fee_concession, patient_next_visit_date,payment_mode,payment_remark from patient_visit_details"
                 		+ " where patient_visit_isdelete='0' and patient_reg_id = '" + RegistrationId + "' and subscriptionid='"+subscription+"' order by visit_id desc ";
         		try 
         		  {
         			
         			 Statement st=con.createStatement();
         			 ResultSet rs=st.executeQuery(CounttotalVisits);
         			 double tempDue = 0;
         	         double tempAdv = 0;
         	         double temptotal = 0;
         	         double tempPaid = 0;
         	         double tempConsection = 0;
         			
         			 rs.next();
         			//int count=0;
         			do
         			{	
         				//System.out.println(rs.getString(8));
         				 //System.out.println("visit id1: "+rs.getString(1));
         				PatientVisitInformation PaymentVisitDetails = new PatientVisitInformation();
         			
         				PaymentVisitDetails.setVisitId(rs.getString(1));
         				
         				PaymentVisitDetails.setVisitDate(rs.getString(2));
         				
         				
         				
         				PaymentVisitDetails.setPatientRegId(rs.getString(4));
         				
         				PaymentVisitDetails.setVisitdueamount(rs.getString(5));
         				
         				PaymentVisitDetails.setVisitadvanceamount(rs.getString(6));
         				
         				PaymentVisitDetails.setVisittotalamount(rs.getString(7));
         				
         				
         				if(rs.getString(8)==null || rs.getString(8).equals(""))
         				{
         					PaymentVisitDetails.setPatientPaidAmount("0");
         				}
         				else 
         				{
         					PaymentVisitDetails.setPatientPaidAmount(rs.getString(8));
         				}
         				
         				if(rs.getString(9)==null)
         				{
         					PaymentVisitDetails.setPatientFeeConcession("0"); 
         				}
         				else 
         				{
         					PaymentVisitDetails.setPatientFeeConcession(rs.getString(9)); 
         				}
         				PaymentVisitDetails.setPatientNextvisitdate(rs.getString(10));
         				PaymentVisitDetails.setPaymentMode(rs.getString(11));
         				PaymentVisitDetails.setPaymentRemark(rs.getString(12));
         				PatientPaymentDetailsList.add(PaymentVisitDetails);
         				
         				//System.out.println(PaymentVisitDetails.toString()+" at454");
         				
         				double tempDueAmt = 0;
                      
         				tempDueAmt=Double.parseDouble(PaymentVisitDetails.getVisitdueamount());
         				tempDue =tempDue+tempDueAmt;
         				
         				double tempAdvanceAmt = 0;
                      
                        tempAdvanceAmt=Double.parseDouble( PaymentVisitDetails.getVisitadvanceamount());
                        tempAdv=tempAdv + tempAdvanceAmt;
                        
                        double temptotalamunt = 0;
                     
                        temptotalamunt=Double.parseDouble( PaymentVisitDetails.getVisittotalamount());
                        temptotal=temptotal + temptotalamunt;
                        //System.out.println(temptotal+" at469");
                        double temppaidamount = 0;
                      
                        temppaidamount=Double.parseDouble(PaymentVisitDetails.getPatientPaidAmount());
                        tempPaid=tempPaid +temppaidamount;
                        //System.out.println(tempPaid+" at 474");
                        double tempconcessionAmount = 0;
                      
                        tempconcessionAmount=Double.parseDouble(PaymentVisitDetails.getPatientFeeConcession());
                        tempConsection=tempConsection + tempconcessionAmount;
                       // System.out.println(tempConsection+" at479");
                        
         				patientvisitAmount.setDueAmount(String.valueOf(tempDue));
                        patientvisitAmount.setAdvancedAmount(String.valueOf(tempAdv));
                        patientvisitAmount.setPatientVisitAmount(String.valueOf(temptotal));
                        patientvisitAmount.setPatientVisitPaidAmount(String.valueOf(tempPaid));
                        patientvisitAmount.setPatientVisitConcessionAmount(String.valueOf(tempConsection));
                       // System.out.println(" at 573 "+count);
         			}while(rs.next());
         			
                    //System.out.println("after while"); 
         			drServiceResponce.setPatientPaymentVisitDetails(patientvisitAmount);
                    drServiceResponce.setPatientsVisitInformationDetails(PatientPaymentDetailsList);
                 
                    return gson.toJson(drServiceResponce);
         		}
         	  catch(Exception e) 
         		{
         			 logger.error("Error in Service : "+e);
         			System.out.println(e);
         		}
         		drServiceResponce.setStatusCode(200);
         		drServiceResponce.setMessage("Success");
         		drServiceResponce.setExceptionDetails("No Record Found");
         		return gson.toJson(drServiceResponce);
     	}
     
     //|||||6---------------------------------------------------------------------------------------------------------------------------------------------
 
     public String GetExpactedPatientsAppointmentVisitByWeek(String currentDate, String daysInterval, String userid)
     {
    	 
    	 String subscription = getSubscriptionId(userid);
    	 
         DrServiceResponce drServiceResponce = new DrServiceResponce();
        
         List<PatientAppoientment> AppointmentList = new ArrayList<PatientAppoientment>();
         
         
        // DatabaseConnection db=new DatabaseConnection();
        
         System.out.println(currentDate+" current date and Day interval "+daysInterval);
    	 
       //  Connection con=db.getConnection();
    	 
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
    	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    	 
    	 Date startDate=null;
    	 
    	 String newDateString="";
    	 
    	 try 
    	  {
    		    startDate = df.parse(currentDate);
    		    newDateString = df.format(startDate);
    		    System.out.println(newDateString);
    	  }
    	 catch (ParseException e)
    	    {
    		    logger.error("Error in Service : "+e);
    		    System.out.println(e);
    		}
    
    	 String CounttotalVisits = "Select distinct appointment_id,patient_registration_id,appointment_visit_date,"
             		+ "appointment_visit_time,appointment_start_time,appointment_end_time," +
                     "appointment_title,appointment_discription,appointment_date" +
                     " from patient_appointment_table where appointment_isdelete='0' and "
                     + "appointment_date >= '" +currentDate+ "' and appointment_date <='" +daysInterval+ "' and subscriptionid='"+subscription+"'";
                 try 
                 {
                	 Statement st=con.createStatement();
                	 ResultSet rs=st.executeQuery(CounttotalVisits);
                	 while(rs.next()) 
                	 {
                		 PatientAppoientment appointmentDetails = new PatientAppoientment();    
                	 
                		
                		 appointmentDetails.setAppoientmentDescription(rs.getString(8));
                		 
                		 if(rs.getString(5)=="")
                		 {
                			
                			 appointmentDetails.setAppoientmentStartTime("00:00:00");
                		 }
                		 else
                		 {
                			 
                			
                			 appointmentDetails.setAppoientmentStartTime(rs.getString(5));
                			 
                		 }
                	
                		 if ( rs.getString(6)=="")
                		 {
                			 appointmentDetails.setAppoientmentEndTime("00:00:00");
                		 }
                		 else
                		 {
                    	
                			 appointmentDetails.setAppoientmentEndTime(rs.getString(6));
                		 }
                	 
                	
                		 appointmentDetails.setAppoientmentTitle(rs.getString(7));
                		
                		
                		 if (rs.getString(9)=="")
                		 {
                        
                			 String dateformate =rs.getString(9);
                			
                			 String[] splitArray = dateformate.split("/");
                			
                			 if (splitArray.length== 3)
                			 {
                				
                				 String tempdate = splitArray[0] + "-" + splitArray[1] + "-" + splitArray[2];
                				 appointmentDetails.setAppoientmentDate( tempdate);
                			 }
                			 
                		 }
                		 else
                		 {
                			 String dateformate1 =rs.getString(9);
                			 String[] arrayDate = dateformate1.split(" ");
                			
                			 String[] splitArray = arrayDate[0].split("-");//dateformate.Split('-');
                			
                			 if (splitArray.length == 3)
                			 {
                				
                				 String tempdate = splitArray[0] + "-" + splitArray[1] + "-" + splitArray[2];
                				 appointmentDetails.setAppoientmentDate(tempdate);
                				
                			 }
                			 
                         
                		 }
                		
                
                		 AppointmentList.add(appointmentDetails);
                		 
                		 drServiceResponce.setPatientAppointmentInformationDetails(AppointmentList);
                	 }
                
                	// return gson.toJson(drServiceResponce);
                 }
                 catch(Exception e)
                 {
                	 logger.error("Error in Service : "+e);
                	 System.out.println(e);
                 }
            //drServiceResponce.PatientAppointmentInformationDetails = AppointmentList;
             
             drServiceResponce.setStatusCode (200);
             drServiceResponce.setMessage("Success");
             drServiceResponce.setExceptionDetails("No Record Found");
         
         return gson.toJson(drServiceResponce);
     }
     
     //||||||7---------------------------------------------------------------------------------------------------------------------------------------------

     
     
     public String GetExpactedPatientsAppointmentVisitTime(String VisitTime, String VisitDate, String userid)
     {
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();

    	 String subscription = getSubscriptionId(userid);
    	 
         DrServiceResponce drServiceResponce = new DrServiceResponce();

         Gson gson = new GsonBuilder().serializeNulls().create();
         List<PatientAppoientment> AppointmentList = new ArrayList<PatientAppoientment>();
         
         DateFormat formatter = new SimpleDateFormat("HH:mm");
         java.sql.Time timeValue=null;
		try 
		{
			timeValue = new java.sql.Time(formatter.parse(VisitTime).getTime());
		
		} 
		catch (ParseException e1) 
		{
			 logger.error("Error in Service : "+e1);
			System.out.println(e1);
		}
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    	 Date startDate=null;
    	 String newDateString="";
    	 try 
    	  {
    		  startDate = df.parse(VisitDate);
    		  newDateString = df.format(startDate);
    	  }
    	 catch (ParseException e) 
    	  {
    		 logger.error("Error in Service : "+e);
    		 System.out.println(e);
    	  }
    	 
    	 		 int count=0;
                 String CounttotalVisits = "Select distinct appointment_id,patient_registration_id,appointment_visit_date,"
                 		+ "appointment_visit_time,appointment_start_time,appointment_end_time,appointment_title,"
                 		+ "appointment_discription,appointment_date from patient_appointment_table "
                 		+ "where appointment_isdelete='0' and appointment_start_time = "+"'"+timeValue+"'"
                 		+ "and appointment_date = " +"'"+newDateString +"' and subscriptionid='"+subscription+"'";
                 
               // PatientAppoientment appointmentDetails = new PatientAppoientment();
                 try 
                 {
                	 Statement st=con.createStatement();
                	 ResultSet rs=st.executeQuery(CounttotalVisits);
                	
                	while( rs.next())
                	{
                		 count=rs.getRow();
                		 PatientAppoientment appointmentDetails = new PatientAppoientment();
                			
                		 appointmentDetails.setAppoientmentTitle(rs.getString(7));
                		 appointmentDetails.setAppoientmentDescription(rs.getString(8));
                		
                		 AppointmentList.add(appointmentDetails);
                		
                	}
                	
                	drServiceResponce.setPatientAppointmentInformationDetails(AppointmentList);
                	
                 }
                 catch(Exception e) 
                 {
                	 logger.error("Error in Service : "+e);
                	 System.out.println(e);
                 }
            	 
             
            	 drServiceResponce.setStatusCode (200);
                 drServiceResponce.setMessage("Success");
                 drServiceResponce.setExceptionDetails("No Record Found");
          
             
             return gson.toJson(drServiceResponce);
        }
     
     //||||||8---------------------------------------------------------------------------------------------------------------------------------------------

     public String GetRegistrationIdBeforeRegistration()
     {
         DrServiceResponce drServiceResponce = new DrServiceResponce();
         List<ResultSet> list=new ArrayList<ResultSet>();
         
         Gson gson = new GsonBuilder().serializeNulls().create();
        
        // DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
         
         String GetRegId = "select patient_reg_id  from patient_information_master order by patient_id desc limit 1 ";
         String TempPatientRegId=null;
         try 
         {
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(GetRegId);
            while(rs.next())
            {
            	TempPatientRegId = rs.getString(1);
                int RegId = Integer.parseInt(TempPatientRegId);
            	System.out.println(RegId);
            }
            drServiceResponce.setMessage(TempPatientRegId);
            	
         }
         catch(Exception e)
         {
            logger.error("Error in Service : "+e);
            System.out.println(e);
         }
          drServiceResponce.setStatusCode(200);
          return gson.toJson(drServiceResponce);
     }
     
     //|||||||9---------------------------------------------------------------------------------------------------------------------------------------------
     public String SavePatientInformation(String fixedFields, String dynamicFields, String userid)
     {
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
    	 String subscription=getSubscriptionId(userid);
    	 
         String patient = "patient";
         DrServiceResponce drServiceResponce = new DrServiceResponce();
         String pattern="yyyy-MM-dd";
         String currentDate=new SimpleDateFormat(pattern).format(new Date());
         
         
         Gson gson = new GsonBuilder().serializeNulls().create();
         
         DateFormat dateFormat = new SimpleDateFormat("HH:mm");
         Date date = new Date();
         String VisitTime=dateFormat.format(date);
         
       
         
       //  String RegId = GetPassword();
         
         if(fixedFields.isEmpty() == true)
         {
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(),"Parameter null", " exception"));
         }
         else if (fixedFields.isEmpty()== false)
         {
             PatientInformation patientInfo = ProcessMySqlSelectCommandResult.ProcessPatientsInformation(fixedFields, dynamicFields);
         
             try {
            	 	
                     int PatientRegId = 0;
                     String CounttotalVisits = "select patient_reg_id  from patient_information_master order by patient_id "
  							+ "desc limit 1 ";
                     		
                     try {
                    	 Statement st=con.createStatement();
                    	 ResultSet rs=st.executeQuery(CounttotalVisits);
                    	 while(rs.next()) {
                       	 String TempPatientRegId =rs.getString(1);       
                    	 int RedId =Integer.parseInt(TempPatientRegId);
                         PatientRegId = RedId + 1;
                        
                        }
                     }
                     
                     catch(Exception e) 
                     {
                    	 logger.error("Error in Service : "+e);
                    	 System.out.println(e);
                     }
                     
                   String insertpatient= "Insert into patient_information_master(patient_reg_id,patient_reg_date,"
                         		+ "patient_first_name,patient_last_name,patient_gender,patient_date_of_birth,patient_country,patient_state,"
                         		+ "patient_city,patient_address,patient_zip_code,patient_phone_number,patient_profession,patient_marital_status,"
                         		+ "patient_bloodgroup,patient_age,patient_religion,patient_email_id,patient_Symptoms,subscriptionid)" +
                             "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                         try {
                        	 PreparedStatement pst=con.prepareStatement(insertpatient);
                        	pst.setInt(1, PatientRegId);
                        	pst.setString(2, currentDate);
                         	pst.setString(3, patientInfo.getPatientFirstName());
                         	pst.setString(4,patientInfo.getPatientSurName());
                         	pst.setString(5,patientInfo.getPatientGender());
                         	pst.setString(6,patientInfo.getPatientDOB());
                         	pst.setString(7,patientInfo.getPatientCountry());
                         	pst.setString(8,patientInfo.getPatientState());
                         	pst.setString(9,patientInfo.getPatientCity());
                         	pst.setString(10,patientInfo.getPatientAddress());
                         	pst.setString(11,patientInfo.getPatientZipCode());
                         	pst.setString(12,patientInfo.getPatientPhoneNo() );
                         	pst.setString(13,patientInfo.getPatientProfession() );
                         	pst.setString(14,patientInfo.getPatientMaritalStatus());
                         	pst.setString(15,patientInfo.getPatientBloodGroup( ));
                         	pst.setString(16,patientInfo.getPatientAge());
                         	pst.setString(17,patientInfo.getPatientReligion());
                         	pst.setString(18,patientInfo.getPatientEmailId());
                         	pst.setString(19,patientInfo.getPatientSymptoms());
                         	pst.setString(20, subscription);
                         	pst.executeUpdate();
                         }
                         catch(Exception e)
                         {
                        	 logger.error("Error in Service : "+e);
                        	 System.out.println(e);
                         }
             String insertregistrationtable = "Insert into user_registration_table(user_registration_id,user_first_name, "
            		 						+ "user_last_name, user_email_id,user_date_of_birth, user_gender, user_parmanent_address,user_city, "
            		 						+ "user_mobile_number, user_roll,subscriptionid) values(?,?,?,?,?,?,?,?,?,?,?)";
             try {
            	 	PreparedStatement pst1=con.prepareStatement(insertregistrationtable);
            	 	pst1.setInt(1, PatientRegId);
            	 	pst1.setString(2, patientInfo.getPatientFirstName());
            	 	pst1.setString(3,patientInfo.getPatientSurName());
            	 	pst1.setString(4, patientInfo.getPatientEmailId());
            	 	pst1.setString(5,patientInfo.getPatientDOB());
            	 	pst1.setString(6,patientInfo.getPatientGender());
            	 	pst1.setString(7,patientInfo.getPatientAddress());
            	 	pst1.setString(8, patientInfo.getPatientCity());
            	 	pst1.setString(9, patientInfo.getPatientPhoneNo());
            	 	pst1.setString(10, patient);
            	 	pst1.setString(11, subscription);
            	 	pst1.executeUpdate();
             }
             catch(Exception e)
             {
            	 logger.error("Error in Service : "+e);
            	 System.out.println(e);
             }
             			
             String imagetable = "Insert into patient_image_table(patient_reg_id,patient_image,image_name,uploaded_date,image_path,subscriptionid)" +
                          "values(?,?,?,?,?,?)";
                        try {
                       	 PreparedStatement pst2=con.prepareStatement(imagetable);
                       	 pst2.setInt(1, PatientRegId);
                       	 pst2.setInt(2, 0);
                       	 pst2.setInt(3, 0);
                       	 pst2.setInt(4, 0);
                       	 pst2.setString(5, "none.jpg");
                       	 pst2.setString(6, subscription);
                       	 
                       	 pst2.executeUpdate();
                        }
                        catch(Exception e) 
                        {
                        	 logger.error("Error in Service : "+e);
                        	 System.out.println(e);
                        }
                       
                        

           String appointmentTable = "insert into patient_appointment_table(patient_registration_id, appointment_visit_date,"
           		+ "appointment_date,appointment_visit_time, appointment_start_time, appointment_title,subscriptionid)"
                		+ " Values(?,?,?,?,?,?,?)";
           try {
        	   
             	 PreparedStatement pst3=con.prepareStatement(appointmentTable);
             	
             	 pst3.setInt(1,PatientRegId );
             	 pst3.setString(2,currentDate);
             	 pst3.setString(3, currentDate);
             	pst3.setString(4, VisitTime);
             	pst3.setString(5, VisitTime);
             	pst3.setString(6, patientInfo.getPatientFirstName());
             	pst3.setString(7, subscription);
             	
             	pst3.executeUpdate();
           }
           catch(Exception e)
           {
        	   logger.error("Error in Service : "+e);
             	 System.out.println(e);
              }
           
           //               GENERATE RANDOM NO FOR CREATING USERNAME
           String password =PasswordGenerator(8);
           
           String username=patientInfo.getPatientFirstName().substring(0, 4);
	            //String mobileno=mobile.substring(6, 10);
           
           Random rrndm_method = new Random();
       	  int len=4;
   		  char []rand= new char[len];
   		  String numbers = "0123456789";
   		  for (int i = 0; i < 4; i++) 
   		  {
   			  rand[i]= numbers.charAt(rrndm_method.nextInt(numbers.length()));
   		  }
   		
   		  String randomnumber=new String(rand);
   		  System.out.println(randomnumber);
  		  String newusername=username+randomnumber;
                         
                         
                         String login_master = "insert into login_master(user_RegistrationId, user_password, user_roll, user_email_id,"
                        		 + "user_name,subscriptionid) Values(?,?,?,?,?,?)";
                         try {
                         	 PreparedStatement pst4=con.prepareStatement(login_master);
                         	 pst4.setInt(1, PatientRegId);
                         	 pst4.setString(2,password);
                         	 pst4.setString(3,patient);
                         	 pst4.setString(4, patientInfo.getPatientEmailId());
                         	// pst4.setString(5, patientInfo.getPatientFirstName());
                         	 pst4.setString(5, newusername);
                         	 pst4.setString(6, subscription);
                         	 
                         	 pst4.executeUpdate();
                         }
                         catch(Exception e) {
                         	 System.out.println(e);
                          }
                         
                      //SendPasswordAtEmail(patientInfo.getPatientEmailId(), password, patientInfo.getPatientFirstName());
                         SendPasswordAtEmail(patientInfo.getPatientEmailId(), password, newusername);
                     if (patientInfo.getDynamicColumns() != null)
                     {
                    	
                         for (int index = 0; index < patientInfo.getDynamicColumns().size(); index++)
                         {
                        	
                             // Add Columns in Cache object
                        	// Caching asyncDelegateCall = new Caching.AddDynamicColumnsInCacheList;
                        	// IAsyncResult asyncResult = asyncDelegateCall.BeginInvoke(patientInfo.DynamicColumns, null, null);
                             // End of calling
                             DynamicColumns ColumnInfo = patientInfo.getDynamicColumns().get(index);
                         
                             //patient_dynamic_table  
                             
                             String dynamictable = "Insert into patient_dynamic_table(patient_reg_id,column_name,column_value,subscriptionid) "
                             		+ "values(?,?,?,?)";
                             try {
                             	 PreparedStatement pst5=con.prepareStatement(dynamictable);
                             	 pst5.setInt(1,PatientRegId);
                             	 pst5.setString(2, ColumnInfo.getColumnName());
                             	 pst5.setString(3, ColumnInfo.getColumnValue());
                             	 pst5.setString(4, subscription);
                             	 
                             	 pst5.executeUpdate();
                             }
                             catch(Exception e)
                             {
                            	 logger.error("Error in Service : "+e);
                             	 System.out.println(e);
                              }
                         }
                     }
                    
             }
             
             catch(Exception e)
             {
            	 logger.error("Error in Service : "+e);
            	 System.out.println(e);
             }
           
             drServiceResponce.setStatusCode (200);
             drServiceResponce.setMessage("Success");
             drServiceResponce.setExceptionDetails(patientInfo.getPatientRegId());
             
           
     
             return gson.toJson(drServiceResponce);
             }
         return gson.toJson(drServiceResponce);
     }
     //||||10|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

     public String SaveGroupPatientInformation(String grouplist1, String patientRegId, String userid)
     {
    	 String subscription = getSubscriptionId(userid);
    	 
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
         DrServiceResponce drServiceResponce = new DrServiceResponce();
         if (patientRegId.isEmpty() == true)
         {
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
         }
         else if (patientRegId.isEmpty() == false)
         {
                         String[] delimiterChars = { " ", ",", ".", ":", "\t" };
                         String grouplist = grouplist1;
                         for(int  j=0; j<delimiterChars.length; j++) 
                         { 
                         String groups[] = grouplist.split(delimiterChars[j]);
                         for(int k=0;k<groups.length;k++)
                         {		 
                             if (grouplist != null)
                             {
                            	 String name=groups[k];
                                 //String name = groupname;
                   String insertGroupTable = "Insert into patient_group_table(patient_reg_id ,patient_group_name,subscriptionid)" +
                                     "values('" + patientRegId + "','" + name + "', '"+subscription+"')";
                             try 
                             {
                             PreparedStatement pst=con.prepareStatement(insertGroupTable);
                           
                             pst.executeUpdate();
                             }
                             catch(Exception e)
                             {
                            	 logger.error("Error in Service : "+e);
                            	 System.out.println(e);
                             }
                                 
                             drServiceResponce.setStatusCode (200);
                             drServiceResponce.setMessage("Success");
                             drServiceResponce.setExceptionDetails(patientRegId);
                    
                 
             return gson.toJson(drServiceResponce);
         }
     }
                         } 
         }
         return gson.toJson(drServiceResponce);
     }
     
     //|||11||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	 public String GetAllPatients(String userid)
     {
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
		 String subscription = getSubscriptionId(userid);
		 
    	 Gson gson = new GsonBuilder().serializeNulls().create();

         List<PatientInformation> patientRecordList = new ArrayList<PatientInformation>();
    
         ResultSet rs1;
         ResultSet rs;
       
         List<ResultSet> list1=new ArrayList<ResultSet>();
         
         DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
         Date date = new Date();
         String VisitTime=dateFormat.format(date);
 
         System.out.println(VisitTime);
         
        /* SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss ");
       	SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");

       	Date time=null;
       	try 
       	{
       		time = parseFormat.parse(VisitTime);
       	}	
       	catch (ParseException e1)
       	{
       		e1.printStackTrace();
       	}
		 String time1=displayFormat.format(time);
         System.out.println(time1);
         
         */
         
         
         
         
         
         String sqlQueryGetPatientRecords = "SELECT patient_reg_id,patient_first_name,patient_last_name,patient_gender,patient_date_of_birth,patient_address,patient_phone_number,patient_profession,patient_marital_status,patient_religion,patient_email_id,patient_symptoms FROM patient_information_master where patient_isDelete= 0  and subscriptionid='"+subscription+"' order by patient_reg_id";
         int count=1;
         try {
        	 Statement st=con.createStatement();
        	  rs=st.executeQuery(sqlQueryGetPatientRecords);
        	  PatientInformation patientRecord=null;
        	  while(rs.next())
        	 {
        		
        		 patientRecord = ExtractPatientInformationFromDataRow1(rs);
        		//rs.first();
        	//System.out.println(patientRecord.getPatientFirstName());
                
        	String sqlQueryGetDynamicColumns = "SELECT patient_reg_id,column_name,column_value FROM "
                                 					+ "patient_dynamic_table where subscriptionid='"+subscription+"' order by patient_reg_id";
                                 
                                	 Statement st1=con.createStatement();
                                	 rs1=st1.executeQuery(sqlQueryGetDynamicColumns);
                                	
                                	 while(rs1.next())
                                	 {
                                		 if(rs1.getString(1).equals(patientRecord.getPatientRegId())) 
                                		 {
                                			 list1.add(rs1); 
                                		 patientRecordList.add(ExtractDynamicColumnsFromRows(patientRecord, rs1));
                                		 
                                		 }
                                		 
                                   }
                                 }
                                 }
         catch(Exception e)
         {
        	 logger.error("Error in Service : "+e);
        	 System.out.println(e);
         }
         
     return gson.toJson(patientRecordList);
     }
     //||||||12|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String GetSelectedPatientRecords(String patientRegId, String userid)
     {
        // List<DataTable> listDataTable=new ArrayList<DataTable>();
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
    	 String subscription = getSubscriptionId(userid);
    	 
    	 PatientInformation patientRecord = new PatientInformation();
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
    	
    	 
    	 // Check for patient registration id is null or empty if true return
         if(patientRegId.isEmpty()== true)
         {
             PatientInformation patientRecord1 = new  PatientInformation();
            // return gson.toJson(String.valueOf(patientRecord1.setPatientFirstName("patient registration id is null")));
           //  return patientRecord1;
         }
         // If patient registration id not null|empty than execute command to get patient records from database
         else
         {
             //PatientInformation patientRecord = new PatientInformation();
               
            String sqlQueryGetPatientRecords = "SELECT * FROM patient_information_master left join patient_image_table "
        	+" on patient_information_master.patient_reg_id = patient_image_table.patient_reg_id"
        	+ " where patient_information_master.patient_reg_id ='" + patientRegId + "' and "
        	+ "patient_information_master.patient_isDelete=0 and patient_information_master.subscriptionid='"+subscription+"'";
            
            String sqlQueryGetDynamicColumns = "SELECT column_id,patient_reg_id,column_name,column_value FROM "
            									+ "patient_dynamic_table where patient_reg_id ='" + patientRegId + "' and subscriptionid='"+subscription+"'";
            
            try
            {
            	Statement st=con.createStatement();
            	ResultSet rs=st.executeQuery(sqlQueryGetPatientRecords);
            	while(rs.next())
            	{
            		patientRecord = ExtractPatientInformationFromDataRow(rs);  
            		rs.first();
            		patientRecord.setPKColumnId( rs.getString(1));
            		patientRecord.setPatientCountry(rs.getString(8));
            		patientRecord.setPatientState(rs.getString(9));
            		patientRecord.setPatientCity(rs.getString(10));
            		patientRecord.setPatientAddress(rs.getString(11));
            		 patientRecord.setPatientZipCode(rs.getString(22));
            		 patientRecord.setImagePath(rs.getString(32));
            		
            		 
            		Statement st1=con.createStatement();
                 	ResultSet rs1=st1.executeQuery(sqlQueryGetDynamicColumns);
                 while(rs1.next())
                 {
                 		 String expression = "'"+patientRegId+"'" ;
                 		 String query="select * from patient_dynamic_table where  patient_reg_id="+expression +" and subscriptionid='"+subscription+"'";
                 		 Statement st2=con.createStatement();
                 		 ResultSet rs2=st2.executeQuery(query);
                 		 if(rs2.next()) 
                 		 {
                 			 return gson.toJson(ExtractDynamicColumnsFromRows(patientRecord, rs2));
                 		 }
                 		 else 
                 		 {
                 			 return gson.toJson(patientRecord);
                 		 }
                 }
          	
              }
            }
            catch(Exception e)
            {
            	 logger.error("Error in Service : "+e);
            	 System.out.println(e);
            }
       
            //return gson.toJson(patientRecord);    
         }
         return gson.toJson(patientRecord); 
   }
     
     
     
  //||||13---------------------------------Selected Patient Record List------------------------------------------------------------------------------------
     
     public String GetSelectedPatientRecordsList(String userid) 
     {
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
    	 String subscriptid=getSubscriptionId(userid);
    	 
    	 
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
    	 //List<ResultSet> list=new ArrayList<ResultSet>();
    	// ResultSet resultSet=new ResultSet();
         String pattern="yyyy-MM-dd";
         String tempcurrentDate=new SimpleDateFormat(pattern).format(new Date());
         System.out.println(tempcurrentDate);
         DateFormat dateformat=new SimpleDateFormat("HH:mm:ss");
         Date date=new Date();
         
         String VisitCurrentTime=dateformat.format(date);
         
         DrServiceResponce drResponce = new DrServiceResponce();
         
         drResponce.setPatientList(new ArrayList<PatientInformation>());
         
         String sqlQueryGetPatientRecords = "select distinct patient_information_master.patient_reg_id, patient_information_master.patient_first_name,patient_information_master.patient_last_name, patient_information_master.patient_gender,patient_information_master.patient_date_of_birth,patient_information_master.patient_address, patient_information_master.patient_phone_number,patient_information_master.patient_profession, patient_information_master.patient_marital_status,patient_information_master.patient_religion, patient_information_master.patient_email_id,patient_information_master.patient_bloodgroup,patient_information_master.patient_age,patient_information_master.patient_Symptoms,patient_image_table.image_path from patient_information_master,patient_image_table where patient_information_master.subscriptionid='"+subscriptid+"' and patient_image_table.subscriptionid='"+subscriptid+"' and patient_information_master.patient_reg_id = patient_image_table.patient_reg_id and patient_information_master.patient_isDelete= '0' and patient_information_master.patient_issuspend ='0' and patient_information_master.patient_reg_id in (SELECT * FROM(select distinct patient_registration_id from patient_appointment_table where patient_appointment_table.subscriptionid='"+subscriptid+"' and patient_appointment_table.appointment_date in ('"+tempcurrentDate+"')) AS T)";
         
         try
         
         {	
        	 Statement st=con.createStatement();
        	 ResultSet rs=st.executeQuery(sqlQueryGetPatientRecords);
        	
        	 List<PatientInformation> listPatientInformation=new ArrayList<>();
        	 int i=0;
        	 
        	 while(rs.next())
        	 {
        		 System.out.println(i);
        		 PatientInformation patientRecord = new PatientInformation();
    	    	 try
    	    	 {
    				 patientRecord = ExtractPatientInformationFromDataRow1(rs);
    				if(rs.getString(15)==null)
    				{
    					patientRecord.setImagePath("none");
    				}
    				else
    				{
    					patientRecord.setImagePath(rs.getString(15));
    				}
    	    	 }
    	    	 catch (SQLException e) 
    	    	 {
    	    		 logger.error("Error in Service : "+e);
    				e.printStackTrace();
    			 }
    	    	  //System.out.println(patientRecord.getImagePath());
    	    	  File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
    	    	  File child = new File(parent,patientRecord.getImagePath());
    	    	  patientRecord.setImagePath(child.toString());
  			      listPatientInformation.add(patientRecord);
  			      drResponce.setPatientList(listPatientInformation);
			  //  drResponce.PatientList.add(patientRecord);
    	    	i++;
    	    }
        	 
    	  drResponce.setListOfColumns(new ArrayList<String>());
    
    	  drResponce.setListOfColumns(GetListOfColumns());
    
       }
       catch(Exception e)
       {
        	logger.error("Error in Service : "+e);
        	System.out.println(e);
       }
       return gson.toJson(drResponce);
     
     }
     
  //|||||14---------------------------------------------Current Date Patient Appointment --------------------------------------------------------------------------
     public String GetTodayPassedAppointedPatient(int offset, int limit, String userid)
     {
    	 String subscriptid=getSubscriptionId(userid);
    	 
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	System.out.println(limit+" "+ offset);
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
    	 List<ResultSet> list=new ArrayList<ResultSet>();
        
    	 //List<PatientInformation> list = new ArrayList<PatientInformation>();
    	 
    	 String pattern="yyyy-MM-dd";
         String tempcurrentDate=new SimpleDateFormat(pattern).format(new Date());
        
         // PatientInformation patientRecord = new PatientInformation();
         DrServiceResponce drResponce = new DrServiceResponce();
         drResponce.setPatientList(new ArrayList<PatientInformation>());
        // DataTable dataTable = new DataTable();
         System.out.println(tempcurrentDate);
         
         String sqlQueryGetPatientRecords = "select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name,"
         		+ "patient_information_master.patient_last_name, patient_information_master.patient_gender,patient_information_master.patient_date_of_birth,"
         		+ "patient_information_master.patient_address, patient_information_master.patient_phone_number,patient_information_master.patient_profession,"
         		+ " patient_information_master.patient_marital_status,patient_information_master.patient_religion, patient_information_master.patient_email_id,"
         		+ "patient_information_master.patient_bloodgroup,patient_information_master.patient_age,patient_information_master.patient_Symptoms,"
         		+ "patient_image_table.image_path from patient_information_master,patient_image_table where "
         		+ "patient_information_master.patient_reg_id = patient_image_table.patient_reg_id and patient_information_master.patient_isDelete= '0' and "
         		+ "patient_information_master.patient_issuspend ='0' and patient_information_master.patient_reg_id in (SELECT * FROM (select distinct patient_registration_id from patient_appointment_table where subscriptionid='"+subscriptid+"' and patient_appointment_table.appointment_date not in ('"+tempcurrentDate+"')  LIMIT  " + limit  + " OFFSET " + offset + ") AS T) and patient_image_table.subscriptionid='"+subscriptid+"' and patient_information_master.subscriptionid='"+subscriptid+"'";
         try
         {
        	 List<PatientInformation> listPatientInformation=new ArrayList<>();
        	 Statement st= con.createStatement();
        	 ResultSet rs=st.executeQuery(sqlQueryGetPatientRecords);
        	 int i=0;
        	 while(rs.next()) 
        	 {
        		 PatientInformation patientRecord = new PatientInformation();
        		 
        		   patientRecord = ExtractPatientInformationFromDataRow1(rs);
        		   
        		   if(rs.getString(15)==null)
        		   {
        			   patientRecord.setImagePath("none");
        		   }
        		   else
        		   {
        			   patientRecord.setImagePath(rs.getString(15));
        		   }
            		File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
     			    File child = new File(parent,patientRecord.getImagePath());
     			    patientRecord.setImagePath(child.toString());
     			   
     			    listPatientInformation.add(patientRecord);
     			    
     			    drResponce.setPatientList(listPatientInformation);
        	 }
         }
         catch(Exception e) 
         {
        	 logger.error("Error in Service : "+e);
        	 System.out.println(e);
         }
       
           //  drResponce.ListOfColumns = new ArrayList<String>();
            // drResponce.ListOfColumns.addAll(GetListOfColumns());
         drResponce.setListOfColumns(new ArrayList<String>());
         drResponce.setListOfColumns(GetListOfColumns());
         
         return gson.toJson(drResponce);
     }
     //||||15------------------------------------Individual Patient Information--------------------------------------------------------------------------
 
     public String GetIndividualPatientInfo(String RegistrationId, String userid) ////string patientRegId
         {
         //List<PatientInformation> list = new ArrayList<PatientInformation>();

    	 //DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
    	 String subscription = getSubscriptionId(userid);
    	 
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 DrServiceResponce drResponce = new DrServiceResponce();
    	 
         drResponce.setPatientList(new ArrayList<PatientInformation>());
         
         String sqlQueryGetPatientRecords = "SELECT * FROM  patient_information_master left join  patient_image_table on" +
                                            " patient_information_master.patient_reg_id = patient_image_table.patient_reg_id where"
                                               + " patient_information_master.patient_isDelete= '0' and "
                                             + "patient_information_master.patient_reg_id ='"+RegistrationId+"' and patient_information_master.subscriptionid='"+subscription+"'  order by patient_id desc";
                 
                   try 
                   {
                	   List<PatientInformation> listPatientInformation=new ArrayList<>();
                	   Statement st=con.createStatement();
                	   ResultSet rs=st.executeQuery(sqlQueryGetPatientRecords);
                	   while(rs.next())
                	   {
                		
                		   PatientInformation patientRecord = new PatientInformation(); 
                   	
                		   patientRecord =ExtractPatientInformationFromDataRow(rs);
                		   
                		   //rs.previous();
                		   
                		   if(rs.getString(32)==null)
                		   {
                			   patientRecord.setImagePath("none");
                		   }
                		   else
                		   {
                			   patientRecord.setImagePath ( rs.getString(32));
                		   }
                		   File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
        			 
                		   File child = new File(parent,patientRecord.getImagePath());
                		   
                		   patientRecord.setImagePath(child.toString());
                		   
                		   listPatientInformation.add(patientRecord);
                		   
                		   drResponce.setPatientList(listPatientInformation);
     			 
                	   }               	 
                 }
                 catch(Exception e) 
                 {
                	   logger.error("Error in Service : "+e);
                	   System.out.println(e);
                  }
                  drResponce.setListOfColumns(new ArrayList<String>());
                  drResponce.setListOfColumns(GetListOfColumns());
         return gson.toJson(drResponce);
     }
 //||||||16----------------------------------------Total Patient Record---------------------------------------------------------------------------
     public String GettotalPatientRecord(String userid) 
     {
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
    	 String subscription = getSubscriptionId(userid);
    	 
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
    	 List<ResultSet> list=new ArrayList<ResultSet>(); 
         
    	 DrServiceResponce drResponce = new DrServiceResponce();
        
    	 List<PatientVisitInformation> patientList = new ArrayList<PatientVisitInformation>();
         	  
    	 String pattern="yyyy-MM-dd";
              
    	 String tempcurrentDate=new SimpleDateFormat(pattern).format(new Date());
              
    	 DateFormat dateformat=new SimpleDateFormat("HH:mm:ss");
              
    	 Date date=new Date();
              
    	 String VisitCurrentTime=dateformat.format(date);
        	
    	 String starttime = "10:00:00";
                  
    	 String sqlQueryGetPatientRecords = "select Count(*) as TotalPatient from patient_information_master, patient_image_table,"
                  		+ "patient_appointment_table where patient_information_master.patient_reg_id = patient_image_table.patient_reg_id"
                  		+ " and patient_information_master.patient_reg_id = patient_appointment_table.patient_registration_id"
                  		+ " and patient_information_master.patient_isDelete= '"+0+"'  and patient_appointment_table.appointment_date='"
                  		+ tempcurrentDate + "' and patient_appointment_table.appointment_start_time"
                  				+ " BETWEEN '" + VisitCurrentTime + "' and '" + starttime + "' and patient_information_master.subscriptionid='"+subscription+"'";
                  try 
                  {
                	  Statement st=con.createStatement();
                	  
                	  ResultSet rs=st.executeQuery(sqlQueryGetPatientRecords);
                	  
                	  PatientVisitInformation patientRecord = new PatientVisitInformation();
                	  
                	  while(rs.next()) 
                	  {
                		  patientRecord.setNumberOfPatient(rs.getString(1));
                	  }
                	  patientList.add(patientRecord);
                  }
                  catch(Exception e) 
                  {
                	  logger.error("Error in Service : "+e);
                	  System.out.println(e);
                  }
                 drResponce.setPatientsVisitInformationDetails(patientList);

         return gson.toJson(drResponce);
     }
     //|||||||17--------------------------------------------------------------------------------------------------------------------
     public String GetMedicineByRegistrationIdAndVisitId(String searchCondition, String visitid, String userid)
     {
    	 
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
    	 String subscription = getSubscriptionId(userid);
    	 System.out.println(subscription);
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 PatientInformation patient = new PatientInformation();
    	 if(searchCondition.isEmpty()== true)
         {
             patient.setServiceResult( "Parameter null or empty");
             return gson.toJson(patient);
         }
         else
         {
                 String selectCommand = "SELECT * FROM patient_information_master left join  patient_image_table on" +
                		 " patient_information_master.patient_reg_id = patient_image_table.patient_reg_id "
                		 + "where patient_information_master.patient_reg_id ='" + searchCondition + "'"
                		 + " and patient_information_master.patient_isDelete= 0 and patient_information_master.subscriptionid='"+subscription+"'";
                 try
                 {
                 	Statement st=con.createStatement();
                 	ResultSet rs=st.executeQuery(selectCommand);
                 	if(rs.next()) 
                 	{

                 		String patientRegId=rs.getString(3);
                 		
                 		 String multipleQuery="Select visit_id, patient_visit_date,patient_visit_time,patient_visit_information,patient_visit_medicien_information,"
                          		+ "patient_visit_title,patient_condition_status, patient_visit_due_amount, patient_visit_advance_amount,"
                          		+ " patient_visit_total_amount, patient_next_visit_date,patient_next_visit_time, patient_visit_bp,"
                           		+ "patient_visit_weight,patient_visit_image,patient_visit_image_name,patient_visit_image_path,"
                          		+ " patient_visit_diagnostic,type_conversation from patient_visit_details where"
                          		+ " patient_reg_id = '" + patientRegId + "' and visit_id ='" + visitid + "' and"
                          				+ " patient_visit_isdelete= 0 and subscriptionid='"+subscription+"'  order by visit_id desc "; 
                         
                          try 
                          {
                        	  
                           	Statement st1=con.createStatement();
                           	ResultSet rs1=st1.executeQuery(multipleQuery);
                           
                          
                            String queryMedicien = "Select patient_visit_date,patient_visit_time,medicien_name, medicien_dos,patient_medicien_power,"
                          		+ "patient_medicien_type,patient_medicien_quentity,patient_medicien_day,patient_medicien_totalquntity,"
                          		+ "patient_medicien_ext_int_medicine, patient_medicien_from_date, patient_medicien_to_date "
                          		+ "from patient_medicien_details where patient_reg_id = '" + patientRegId + "'   and subscriptionid='"+subscription+"'"
                          				+ "order by patient_visit_date desc";
                         
                          try 
                          {
                           	Statement st2=con.createStatement();
                           	ResultSet rs2=st2.executeQuery(queryMedicien);
                          
                         
                            String queryDynamicColumn = "Select column_name,column_value from patient_dynamic_table where "
                          		+ "patient_reg_id= '" + patientRegId + "' and subscriptionid='"+subscription+"'";

                          try 
                          {
                           	Statement st3=con.createStatement();
                           	ResultSet rs3=st3.executeQuery(queryDynamicColumn);
                         
                           	patient = ProcessDataSet(rs1,rs2,rs3,rs);
                         
                            patient.setListOfColumns(new ArrayList<String>());
                         
                            patient.setListOfColumns( GetListOfColumns());
                         
                       if(rs.getString(32)==null)
                       {
                    	   patient.setImagePath("none"); 
                       }
                       else
                       {
                    	   patient.setImagePath(rs.getString(32));
                       }
                       File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
                       
     				   File child = new File(parent,patient.getImagePath());
     				    
     				   patient.setImagePath(child.toString());
                         
                       patient.setListOfMediciens(new ArrayList<String>());
                       patient.getListOfMediciens().addAll(GetMedicieList());
                       return gson.toJson(patient);
                     }
                     catch(Exception e)
                     {
                        logger.error("Error in Service : "+e);
                        System.out.println(e);
                     }
                          
                  }
                  catch(Exception e)
                  {
                      logger.error("Error in Service : "+e);
                      System.out.println(e);
                  }
                }
                catch(Exception e)
                {
                	logger.error("Error in Service : "+e);
                 	System.out.println(e);
                }
              }
           }
           catch(Exception e) 
           {
              logger.error("Error in Service : "+e);
      		  System.out.println(e);
      	   }
         }
    	 return gson.toJson(patient);
     }
     //||||||18---------------------------------------------------------------------------------------------------------------------------------------------
     
     public String GetPatientBySearchCondition(String searchCondition, String userid)
     {
    	// DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
    	 String subscription = getSubscriptionId(userid);
    	 
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	 
     // logger.info("GetPatientBySearchCondition");
         if (searchCondition.isEmpty() == true)
         {
             PatientInformation patient = new PatientInformation();
             patient.setServiceResult("Parameter null or empty");
             return gson.toJson(patient);
         }
         else
         {
        	 ResultSet rs=null;
        System.out.println("at line no 1477"); 
             PatientInformation patient = null;
                 String selectCommand = "SELECT * FROM patient_information_master left join  patient_image_table on" +
               " patient_information_master.patient_reg_id = patient_image_table.patient_reg_id where "
               + "patient_information_master.patient_reg_id ='" + searchCondition + "' and"
               		+ " patient_information_master.patient_isDelete= 0 and patient_information_master.subscriptionid='"+subscription+"'";
                 try 
                 {
                	 Statement st=con.createStatement();
                	rs=st.executeQuery(selectCommand);
                	 if(rs.next())
                	 {
                		 String patientRegId =rs.getString(3);
                		 
                		 System.out.println(rs.getString(3));
                         String multipleQuery = "Select visit_id, patient_visit_date,patient_visit_time,patient_visit_information,"
                         		+ "patient_visit_medicien_information,patient_visit_title,patient_condition_status, patient_visit_due_amount,"
                         		+ " patient_visit_advance_amount, patient_visit_total_amount, patient_next_visit_date,patient_next_visit_time,"
                         		+ " patient_visit_bp,patient_visit_weight,patient_visit_image,patient_visit_image_name,patient_visit_image_path,"
                         		+ " patient_visit_diagnostic,type_conversation from patient_visit_details where patient_reg_id = '" + patientRegId + "'"
                         	    + " and subscriptionid='"+subscription+"' and  patient_visit_isdelete= 0 order by  patient_visit_date desc";
                         try {
                        	 Statement st1=con.createStatement();
                        	 ResultSet rs1=st1.executeQuery(multipleQuery);
                         
                         String queryMedicien = "Select patient_visit_date,patient_visit_time,medicien_name, medicien_dos,patient_medicien_power,"
                         		+ "patient_medicien_type,patient_medicien_quentity,patient_medicien_day,patient_medicien_totalquntity,"
                         		+ "patient_medicien_ext_int_medicine, patient_medicien_from_date, patient_medicien_to_date "
                         		+ "from patient_medicien_details where patient_reg_id = '" + patientRegId + "' and subscriptionid='"+subscription+"' order by patient_visit_date desc";
                         
                         
                         try {
                        	 Statement st2=con.createStatement();
                        	 ResultSet rs2=st2.executeQuery(queryMedicien);
                         
                         
                         String queryDynamicColumn = "Select column_name,column_value from patient_dynamic_table"
                         		+ " where patient_reg_id= '" + patientRegId + "' and subscriptionid='"+subscription+"'";
                         
                         try {
                        	 Statement st3=con.createStatement();
                        	 ResultSet rs3=st3.executeQuery(queryDynamicColumn);
                        	
                        	 
                         patient = ProcessDataSet(rs1,rs2,rs3,rs);
                        // System.out.println("18 "+patient.getv);
                        
                        // System.out.println("patient: ");
                        patient.setListOfColumns(new ArrayList<String>());
                        
                        patient.setListOfColumns( GetListOfColumns());
                       // rs.first();
                      //  System.out.println("hello");
                        // System.out.println(rs.getString(31)+" at line 1");
                         
                        if(rs.getString(32)==null)
                        {
                        	 patient.setImagePath("none");
                        }
                        else
                        {
                        	patient.setImagePath(String.valueOf(rs.getString(32)));
                        }
                         
                        
                         File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
                         //File parent = new File("D:\\images");
                         
         			    File child = new File(parent,patient.getImagePath());
         			    
         			    System.out.println("patient.getImagePath(): "+patient.getImagePath());
         			    System.out.println("child: "+child);
         			    
         			   patient.setImagePath(child.toString());
         			   
                         patient.setListOfMediciens(new ArrayList<String>());
                        // patient.getListOfMediciens().addAll(GetMedicinList());
                         patient.setListOfMediciens(GetMedicinList());
                         	//  return patient;
                         }
                         catch(Exception e) {
                        	 logger.error("Error in Service : "+e);
                        	 System.out.println(e);
                         }
                      }
                      catch(Exception e) 
                      {
                    	  logger.error("Error in Service : "+e);
                    	  System.out.println(e);
                      } 
                   }
                   catch(Exception e)
                   {
                	   logger.error("Error in Service : "+e);
                   	 System.out.println(e);
                   } 
                 } 
               }
                 catch(Exception e) {
                	 logger.error("Error in Service : "+e);
                	 System.out.println(e);
                 }
                        
                 return gson.toJson(patient);
                 }
     }
     
    //||||19|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
   public String GetSelectedPatientImages(String PatientRegID, String userid)
     {
    	 //DatabaseConnection db=new DatabaseConnection();
    	// Connection con=db.getConnection();
    	 
	   String subscription = getSubscriptionId(userid);
	   
    	 Gson gson = new GsonBuilder().serializeNulls().create();
    	
         DrServiceResponce drServiceResponce = new DrServiceResponce();

         List<PatientVisitInformation> visitList = new ArrayList<PatientVisitInformation>();
         if (PatientRegID.isEmpty() == true)
         {
             return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
         }
         else
         {
           
                     String CounttotalVisits = "SELECT patient_visit_image_path,visit_id,patient_visit_date FROM "
                     		+ "patient_visit_details where patient_reg_id ='" + PatientRegID + "'"
                    				+ " and subscriptionid='"+subscription+"' order by visit_id desc";
                    try
                     {
                    	 Statement st=con.createStatement();
                      	 ResultSet rs=st.executeQuery(CounttotalVisits);
                      	 while(rs.next())
                      	 {
                      		
                      		 PatientVisitInformation visitDetails = new PatientVisitInformation();
                      		
                      		 String imgpath="";
                      		 if(rs.getString(1)==null)
                      		 {
                      			imgpath = "none.jpg";
                      		 }
                      		 else
                      		 {
                      			imgpath = rs.getString(1);
                      		 }
                      		 
                             visitDetails.setPopUpImagePath(imgpath);
                             visitDetails.setPopUpImageDate(rs.getString(3));
                             visitDetails.setVisitId(rs.getString(2));
                             
                             File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
               			    File child = new File(parent,visitDetails.getPopUpImagePath());
               			    visitDetails.setPopUpImagePath(child.toString());
                      		visitList.add(visitDetails);
                      		
                      		
                      	 }
                      	  drServiceResponce.setPatientsVisitInformationDetails(visitList);
                     }
                     catch(Exception e) {
                    	 logger.error("Error in Service : "+e);
                    	 System.out.println(e);
                     }
                    drServiceResponce.setStatusCode (200);
                    drServiceResponce.setMessage("Success");
                    drServiceResponce.setExceptionDetails(PatientRegID);
            
         }
         return gson.toJson(drServiceResponce);
         }
         //||||20|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public String GetGroupDetails(String userid)
         {
        	 String subcription=getSubscriptionId(userid);
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	// logger.info("GetGroupDetails");
        	 
        	 System.out.println("subcription: "+subcription);
             DrServiceResponce drServiceResponce = new DrServiceResponce();
             Gson gson = new GsonBuilder().serializeNulls().create();
             
             List<PatientVisitInformation> visitList = new ArrayList<PatientVisitInformation>();
               
              String CounttotalVisits = "SELECT distinct group_id, group_name, image_name, group_descriptionl,"
              		+ " group_image, group_path, group_upload_date, group_upload_time FROM group_master  where subscriptionid='"+subcription+"' order by group_id desc";
              try
              {
            	  Statement st=con.createStatement();
              	 ResultSet rs=st.executeQuery(CounttotalVisits);
              	 while(rs.next())
              	 {
              		PatientVisitInformation visitDetails = new PatientVisitInformation();
             
              		visitDetails.setGroupImagePath(rs.getString(6));
                    visitDetails.setGroupImageDate(rs.getString(7));
                    visitDetails.setGroupImageId(rs.getString(1));
                    visitDetails.setGroupImageName(rs.getString(2));
                   // visitDetails.GroupImagePath = Path.Combine("PatientProfilesImages", Path.GetFileName(visitDetails.PopUpImagePath));
                    visitList.add(visitDetails); 
              	 }
              	drServiceResponce.setPatientsVisitInformationDetails(visitList);
              }
              catch(Exception e) {
            	  logger.error("Error in Service : "+e);
             	 System.out.println(e);
              }
                                
              drServiceResponce.setStatusCode (200);
              drServiceResponce.setMessage("Success");
           
                 return gson.toJson(drServiceResponce);
  
         }

         //|||||21||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
public String GetPatientsRecordsByLimit(int offSet, int limit, String userid)
         {
	
			 Gson gson = new GsonBuilder().serializeNulls().create();
	
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
			
			 String subscription = getSubscriptionId(userid);
			
			
        	 String pattern="dd-mm-yyyy";
             String tempcurrentDate=new SimpleDateFormat(pattern).format(new Date());
             DateFormat dateformat=new SimpleDateFormat("hh:mm:ss ");
             Date date=new Date();
             String VisitCurrentTime="10:30:00";//dateformat.format(date);
        	 String starttime = "10:30:00";
        	 if(limit == 0)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORDS_LIMIT_ZERO.getNumVal(), "Limit should be grater than zero.", ""));
             }
             else
             {
                     DrServiceResponce drResponce = new DrServiceResponce();
                   
                       
         String strQuery = "select * from patient_information_master, patient_image_table,patient_appointment_table"
            + " where patient_information_master.patient_reg_id = patient_image_table.patient_reg_id"
            + " and patient_information_master.patient_reg_id = patient_appointment_table.patient_registration_id"
            + " and patient_information_master.patient_isDelete= '" + 0 + "' and"
            + " patient_information_master.patient_issuspend ='" + 0 + "' and "
            + "patient_appointment_table.appointment_date='" + tempcurrentDate + "'"
            + " and patient_appointment_table.appointment_start_time BETWEEN '" + VisitCurrentTime + "' and patient_information_master.subscriptionid='"+subscription+"' and '" 
            + starttime + "' order by patient_appointment_table.appointment_start_time "
            + "LIMIT " + limit + " OFFSET " + offSet;
              try
              {
            	  Statement st1=con.createStatement();
              	 ResultSet rs=st1.executeQuery(strQuery);
              	 if(rs.next()==true)
              	 	{
                                 drResponce.setPatientList(new ArrayList<PatientInformation>());
                                 
									drResponce.setPatientList(ProcessMySqlSelectCommandResult.GetPatientGeneralInformationFromSearchResult(rs, false));
									 drResponce.setStatusCode(ServiceStatusCodeEnum.SUCCESS.getNumVal());
	                                 drResponce.setMessage("Success");
	                                 return gson.toJson(drResponce);
              	 	         
              	 	} 
                     else
                    {               
              String strRemaningRecords = "SELECT * FROM patient_information_master left join  patient_image_table on" +
                    " patient_information_master.patient_reg_id = patient_image_table.patient_reg_id where patient_information_master.subscriptionid='"+subscription+"' order by patient_id "
                    + "desc LIMIT "+limit;
             
              try
              {
            	  Statement st2=con.createStatement();
             	 ResultSet rs1=st2.executeQuery(strRemaningRecords);
             	 if(rs1.next()==true)
             	 {
             	 drResponce.setPatientList(new ArrayList<PatientInformation>());
                 
					drResponce.setPatientList((ProcessMySqlSelectCommandResult.GetPatientGeneralInformationFromSearchResult(rs1, false)));
				
                 drResponce.setStatusCode(ServiceStatusCodeEnum.SUCCESS.getNumVal());
                 drResponce.setMessage("Success");
                 return gson.toJson(drResponce);
              }
             	else
                {
                    return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.NO_MORE_RECORDS_EXIST.getNumVal(), "No more records exist.", ""));
                }
              }
              catch(Exception e)
              {
            	  logger.error("Error in Service : "+e);
             	 System.out.println(e);
              
              }
              }
              }
              catch(Exception e) 
				{
            	  logger.error("Error in Service : "+e);
            	  System.out.println(e);
           }
              
              
                             return gson.toJson(drResponce);
                             }
             }
         
                 
 //|||||||22||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String GetPatientsVisitDetailsByLimit(int offSet, int limit, String patientRegId, String userid)
         {
             
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 ResultSet rs=null;
        	 ResultSet rs1=null;
        	 ResultSet rs2=null;
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
        	 if (patientRegId.isEmpty())
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PATIENT_REGISTRATION_ID_NULL.getNumVal(), "Patient registration id is null\\empty.", ""));
             }
             else if (limit <= 0)
             {
                 return gson.toJson( new DrServiceResponce(ServiceStatusCodeEnum.RECORDS_LIMIT_ZERO.getNumVal(), "Limit should be grater than zero.", ""));
             }
             else if (offSet < 0)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORDS_OFFSET_NEGATIVE.getNumVal(), "Offset should be grater than zero.", ""));
             }
             else
             {
                   
                        
                       String multipleQuery = "Select visit_id,patient_visit_date,patient_visit_time,patient_visit_information,"
                       		+ "patient_visit_medicien_information,patient_visit_title,patient_visit_due_amount,"
                       		+ " patient_visit_advance_amount, patient_visit_total_amount, patient_next_visit_date,"
                       		+ " patient_visit_bp, patient_visit_weight, patient_visit_diagnostic,"
                       		+ "patient_visit_image,patient_visit_image_name,patient_visit_image_path "
                       		+ "from patient_visit_details where patient_reg_id = '" + patientRegId + "' and subscriptionid='"+subscription+"' order by "
                       				+ "patient_visit_date desc LIMIT " + limit + " OFFSET " + offSet;
                       
                       try
                       {
                    	   Statement st1=con.createStatement();
                        	  rs=st1.executeQuery(multipleQuery);
                        	 
                       }
                       catch(Exception e) {
                    	   logger.error("Error in Service : "+e);
                      	   System.out.println(e);
                       }
                      
                 String queryMedicien = "Select  patient_visit_date,patient_visit_time,medicien_name, "
                   + "medicien_dos,patient_medicien_power,patient_medicien_type, patient_medicien_quentity,"
                   + "patient_medicien_totalquntity, patient_medicien_day,patient_medicien_ext_int_medicine,"
                   + " patient_medicien_from_date, patient_medicien_to_date from patient_medicien_details "
                   + "where patient_reg_id = '" + patientRegId + "' and subscriptionid='"+subscription+"' order by patient_visit_date desc";

                 try
                       {
                      	 Statement st2=con.createStatement();
                      	 rs1=st2.executeQuery(queryMedicien);
                       }
                       catch(Exception e) 
                       {
                    	   logger.error("Error in Service : "+e);
                      	  System.out.println(e);
                       }
                             try {
								if (rs.next()==true)
								 {
								    // DrServiceResponce drServiceResponce = new DrServiceResponce();
								     drServiceResponce.setPatientsVisitInformationDetails(new ArrayList<PatientVisitInformation>());
								     try {
										drServiceResponce.setPatientsVisitInformationDetails(ProcessVisitInformationDataTable1(rs, rs1));//.PatientsVisitInformationDetails.addAll(ProcessVisitInformationDataTable1(rs, rs1));
										return gson.toJson(drServiceResponce);
									} 
								     catch (Exception e) {
								    	 logger.error("Error in Service : "+e);
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								     drServiceResponce.setMessage(String.valueOf(drServiceResponce.getPatientsVisitInformationDetails().size()));
								     //return drServiceResponce;
								 }
								 else
								 {
								     String GetRemainingVisits = "Select visit_id,patient_visit_date,patient_visit_time,patient_visit_information,patient_visit_medicien_information,"
								     		+ "patient_visit_title,patient_visit_due_amount,patient_visit_advance_amount, patient_visit_total_amount, "
								     		+ "patient_next_visit_date, patient_visit_bp,patient_visit_weight, patient_visit_diagnostic,patient_visit_image,"
								     		+ "patient_visit_image_name,patient_visit_image_path from patient_visit_details where"
								     		+ " patient_reg_id = '" + patientRegId + "' and subscriptionid='"+subscription+"' order by patient_visit_date desc LIMIT " + 4;
								     

								     try
								     {
								    	 Statement st=con.createStatement();
								    	 rs2=st.executeQuery(GetRemainingVisits);
								    	
								     }
								     
								     catch(Exception e) {
								    	 logger.error("Error in Service : "+e);
								    	 System.out.println(e);
								     }
								     try {
										if(rs2.next()==true)
										 {
										     drServiceResponce.setPatientsVisitInformationDetails(new ArrayList<PatientVisitInformation>());
										     try {
												//drServiceResponce.PatientsVisitInformationDetails.addAll(ProcessVisitInformationDataTable1(rs2, rs1));
										    	 drServiceResponce.setPatientsVisitInformationDetails(ProcessVisitInformationDataTable1(rs2, rs1));
										    	 return gson.toJson(drServiceResponce);
											} catch (Exception e) {
												
												e.printStackTrace();
											}
										     drServiceResponce.setMessage(String.valueOf(drServiceResponce.getPatientsVisitInformationDetails().size()));
										     return gson.toJson(drServiceResponce);
										 }
										 else
										 {
										     return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.NO_MORE_RECORDS_EXIST.getNumVal(), "No more records found...!", ""));
										 }
									} catch (SQLException e) {
										 logger.error("Error in Service : "+e);
										
										e.printStackTrace();
									}
								 }
							} catch (SQLException e) {
								 logger.error("Error in Service : "+e);
								
								e.printStackTrace();
							}

                         }
        	 return gson.toJson(drServiceResponce);
                     }
                 
      //|||23||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public String GetPatientsListBySearchQuery(String searchQueryString, String dynamicColumns, String userid)
         {
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 
        	 SearchQuery searchQuery =new SearchQuery();
             DrServiceResponce drResponce = new DrServiceResponce();
             if (searchQueryString.isEmpty() == false)
             {
                 searchQuery.setSearchQueryString(searchQueryString); 
                 searchQuery.setSelectedDynamicColumnsList(new ArrayList<String>());
                 if (dynamicColumns.isEmpty() == false)
                 {
                    String dynamicColumnsList = dynamicColumns.replace(",","");
                    System.out.println(dynamicColumnsList+" line no 2064");
                   String[] dt1=dynamicColumnsList.split(",");
                   
                   searchQuery.setSelectedDynamicColumnsList(Arrays.asList(dt1));
                 }

                 if (searchQuery == null | (searchQuery.getSearchQueryString()).isEmpty() == true)
                 {
                     new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "SearchQuery parameter is null", "");
                 }
                 else
                 {
                     try
                     {
                         String patientSearchQuery = GenerateSearchOprations.GenerateSearchQuery(searchQuery, subscription);
                        
                         System.out.println(patientSearchQuery);
                         if (patientSearchQuery.isEmpty() == false)
                         {
                        	 Statement st=con.createStatement();
                        	 ResultSet rs=st.executeQuery(patientSearchQuery);
                               if(rs!=null)
                                     {
                                         drResponce.setPatientList (new ArrayList<PatientInformation>());
                                         drResponce.setPatientList(ProcessMySqlSelectCommandResult.GetPatientGeneralInformationFromSearchResult1(rs, true));
                                         drResponce.setStatusCode(ServiceStatusCodeEnum.SUCCESS.getNumVal());
                                         drResponce.setMessage("Success");
                                         drResponce.setListOfColumns(new ArrayList<String>());//GetListOfColumns();
                                         drResponce.setListOfColumns((GetListOfColumns()));
                                         return gson.toJson(drResponce);
                                     }
                                     else
                                     {
                                         return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORD_NOT_FOUND.getNumVal(), "Record not found.!", ""));
                                     }
                         }
                     
                     else
                     {
                         new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "SearchQuery parameter is null", "");
                     }
                 return gson.toJson(drResponce);
             }
                     catch(Exception e) {
                    	 logger.error("Error in Service : "+e);
                    System.out.println();	 
                     }
                 }
         }
             return gson.toJson(drResponce);
         }
         //|||||24||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String GetPatientsListBymedicine(String searchpatientmedicine,String userid)
         {
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 //DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
             DrServiceResponce drServiceResponce = new DrServiceResponce();
         
           
             if(searchpatientmedicine.isEmpty() == true)
             {
                 new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "searchpatientmedicine parameter is null", "");
             }
            
             else
             {
                         String selectMedicien = "SELECT patient_first_name,patient_last_name,patient_date_of_birth,"
                         		+ "patient_address FROM patient_information_master where  "
                         		+ "(patient_information_master.patient_reg_id) in "
                         		+ "( SELECT patient_reg_id from patient_medicien_details where "
                         		+ "(patient_medicien_details.medicien_name) in ('" + searchpatientmedicine + "')  and patient_medicien_details.subscriptionid='"+subscription+"') and patient_information_master.subscriptionid='"+subscription+"'";
                         try
                         {
                        	Statement st=con.createStatement();
                        	ResultSet rs=st.executeQuery(selectMedicien);
                        	
                        	List<PatientInformation> list = new ArrayList<PatientInformation>();
                        	
                        	while(rs.next())
                        	{
                        		PatientInformation ps = new PatientInformation();
                        		
                        		ps.setPatientFirstName(rs.getString(1));
                        		ps.setPatientSurName(rs.getString(2));
                        		ps.setPatientDOB(rs.getString(3));
                        		ps.setPatientAddress(rs.getString(4));
                        		
                        		list.add(ps);
                        	}
                        	
                        	drServiceResponce.setPatientList(list);
                        
                         }
                         catch(Exception e) {
                        	 logger.error("Error in Service : "+e);
                        	 System.out.println(e);
                         }
             }
             return gson.toJson(drServiceResponce);
         }
         //|||||25||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
        
         private boolean convert1(String splitArray[] ) 
         {
        	 int  outDay=0;
        	 outDay=Integer.valueOf(splitArray[0]);
        	if(outDay!=0)
        	return true;
        	else
        		return false;
         }
         public String GetExpactedPatientsVisitByDate(String currentDate, String daysInterval, String userid)
         {
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 List<PatientInformation> listPatientInformation=new ArrayList<>();
        	 
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	  DrServiceResponce drResponce = new DrServiceResponce();
        	 if (currentDate.isEmpty() == true )
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Current date parameter is null or empty or daysInterval is negative value", "Current date can not be empty & daysInterval must be nonNegative value"));
             }
             else
             {
               
                 try
                 {
                         String nextDate =null;
                         if (nextDate==currentDate)
                         {
                             nextDate = currentDate;
                         }
                         else
                         {
                             String[] splitArray = currentDate.split(Pattern.quote("-"));
                             
                           if (splitArray.length == 3 && (convert1(splitArray))==true) {
                                ZonedDateTime today = ZonedDateTime.now();
                             }
                             else
                             { 
                            	 return gson.toJson(new DrServiceResponce((ServiceStatusCodeEnum.FORMAT_UNSUPPORTED.getNumVal()),
                                     "Current date format is not in specified format. date formate should be dd-mm-yyyy.", "Current date format is not in specified format. date formate should be dd-mm-yyyy."));
                             }
                         }
                         
                         
                         String innerquery="Select distinct patient_visit_details.patient_reg_id from patient_visit_details where" +
                                 " patient_visit_details.patient_next_visit_date between '" + currentDate + "' and '" + daysInterval + "' and patient_visit_details.subscriptionid='"+subscription+"'";
                         
                         
                         Statement sta=con.createStatement();
                         ResultSet rsa=sta.executeQuery(innerquery);
                         
                         String innerresult="";
                        
                         while(rsa.next())
                         {
                        	innerresult+=rsa.getString(1)+","; 
                         }
                         
                         innerresult = innerresult.replaceAll(",$","");
                        // System.out.println(innerresult);
                         
                         String SelectQuery = "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name," +
                                 "patient_information_master.patient_last_name,patient_information_master.patient_phone_number" +
                                 " from patient_information_master,patient_visit_details" +
                                  " where patient_information_master.patient_reg_id  in ("+innerresult+") and patient_information_master.subscriptionid='"+subscription+"' and patient_visit_details.subscriptionid='"+subscription+"'";
                         
                         
                         
                         //String SelectQuery = "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name," +
                                                //  "patient_information_master.patient_last_name,patient_information_master.patient_phone_number" +
                                                //  " from patient_information_master,patient_visit_details" +
                                                //   " where patient_information_master.patient_reg_id  in " +
                                                 //  "(Select distinct patient_visit_details.patient_reg_id from patient_visit_details where" +
                                                //   " patient_visit_details.patient_next_visit_date between '" + currentDate + "' and '" + daysInterval + "')";
                         try 
                         {
                        	 Statement st=con.createStatement();
                        	 
                        	 ResultSet rs=st.executeQuery(SelectQuery);
                        	 drResponce.setPatientList(new ArrayList<PatientInformation>()); 
                        	 while(rs.next())
                        	 {
                        		
                        		
                     		   String query = "Select patient_reg_id,patient_next_visit_date FROM patient_visit_details " +
                                        "where patient_next_visit_date between '" + currentDate + "' and '" + daysInterval + "' and patient_visit_details.subscriptionid='"+subscription+"'";
                     		   	int flag=0;
                                try
                                {
                               	 Statement st1=con.createStatement();
                               	 ResultSet rs1=st1.executeQuery(query);
                               	  
                               	 while(rs1.next())
                               	 {
                               		 if(rs.getString(1).equals(rs1.getString(1)))
                               		 {
                               		
                               		 flag++;
                               		 PatientInformation patient = new PatientInformation();
                               		 patient.setPatientRegId(rs.getString(1));
                                      patient.setPatientFirstName( rs.getString(2));
                                      patient.setPatientSurName(rs.getString(3));
                                      patient.setPatientPhoneNo(rs.getString(4));
                                      
                                      String expression =   patient.getPatientRegId();
                                      
                                      String sortOrder = "patient_next_visit_date DESC";
                                    
                                     // System.out.println(rs1.getString(1)+"  "+expression);
                                     if (rs1.getString(1).equals(expression))
                                      {
                                     	
                                         // DataRow row = dtRowArray[0];
                                         if (rs1.getString(2)=="")
                                          {
                                               patient.setNextVisitDate(rs1.getString(2));
                                             
                                          }
                                          else
                                          {
                                         	 
                                              
                                         	 patient.setNextVisitDate(rs1.getString(2));
                                              
                                            
                                          }
                                      }
                                     
                                      
                        			   listPatientInformation.add(patient);
                        			    
                        			    drResponce.setPatientList(listPatientInformation);
                    
                               	 }
                               	
                               	  drResponce.setStatusCode ( ServiceStatusCodeEnum.SUCCESS.getNumVal());
                               	 drResponce.setMessage("Success");
                                  
                               	 
                                }
                                }
                                catch(Exception e) 
                                {
                                	 logger.error("Error in Service : "+e);
                               	 System.out.println(e);
                                }
                                if(flag==0)
                                {
                                    drResponce.setStatusCode(ServiceStatusCodeEnum.NOTFOUND.getNumVal());
                                    drResponce.setMessage( "Expacted patients visit is zero");
                                    return gson.toJson(drResponce);
                                }
                        		  
                        	 }
                        	 
                         }
                         catch(Exception e) 
                         { 
                        	 logger.error("Error in Service : "+e);
                        	 System.out.println(e);
                         }
                             }
                 catch(Exception ex)
                 {
                     drResponce.setStatusCode(ServiceStatusCodeEnum.NULL_REFERENCE_EXCEPTION.getNumVal());
                     drResponce.setMessage(ex.getMessage());
                     drResponce.setExceptionDetails("Exception is :"+ ex);
                     return gson.toJson(drResponce);
                 }
                 
             }
        	return gson.toJson(drResponce);
         }
         //||26|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         // <returns>List of all mediciens quentity consumed in target week.</returns>
         
         private boolean convert2(Iterator<String> iterator ) 
         {
        	double   outDay=Integer.valueOf(iterator.next());
        	if(outDay!=0)
        	return true;
        	else
        		return false;
         }
         
    public String GetLastWeekMedicinConsumption(String currentDate, int offSet, String medininetype, String userid)
         {
    	
    	
    	Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 List<String> list2=new ArrayList<String>();
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
             List<MedicinConsumptionSearchResult> resultList = new ArrayList<MedicinConsumptionSearchResult>();
             MedicinConsumptionSearchResult medicinConsumption = null;
             String getMedicienQuery = "";
             // Check parameters 
             if (currentDate.isEmpty() == true || offSet < 0)
             {
                 MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                 result.setStatusCode(String.valueOf(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal()));
                 result.setMessage("Current date parameter is null or empty/ Offset value can't be negative.");
                 resultList.add(result);
                 return gson.toJson(resultList);
             }
             else
             {
                 try
                 {
                  
                         String  pattern= "yyyy-MM-dd";
                         String tempcurrentDate=new SimpleDateFormat(pattern).format(new Date());
                        
                         String filterDate = "";
                         
                         if (offSet < 0)
                         {
                             MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                             result.setStatusCode((String.valueOf(ServiceStatusCodeEnum.RECORDS_OFFSET_NEGATIVE.getNumVal())));
                             result.setMessage("Offset parameter value is negative. Offset parameter value can not be negative");
                             resultList.add(result);
                             return gson.toJson(resultList);
                         }
                         else
                         {
                             int days = offSet;
                             DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
                            // Date currentDate1=new Date();
                             Calendar c=Calendar.getInstance();
                             c.add(Calendar.DATE, (-days));
                             Date date=c.getTime();
                             filterDate=dateformat.format(date);
                             System.out.println(filterDate);
                         }
                        
                         if (medininetype =="")
                         {
                        	 getMedicienQuery = "Select medicien_name,patient_medicien_type,patient_medicien_totalquntity,patient_visit_date,sum(medicien_calculated_quantity) as Cal_Quentity" +
                             " from patient_medicien_details where patient_visit_date between '" + filterDate + "' and '" + tempcurrentDate + "' and subscriptionid='"+subscription+"' group by medicien_name";
                         }
                         else 
                         {

                             getMedicienQuery = "Select medicien_name,patient_medicien_type,patient_medicien_totalquntity,patient_visit_date,sum(medicien_calculated_quantity) as Cal_Quentity" +
                            " from patient_medicien_details where patient_medicien_type = '" + medininetype + "' and patient_visit_date between '" + filterDate + "' and '" + tempcurrentDate + "' and subscriptionid='"+subscription+"' group by medicien_name";
                             System.out.println( tempcurrentDate);
                         }
                         try 
                         {
                        	 int temp=0;
                           	 Statement st1=con.createStatement();
                           	 ResultSet rs=st1.executeQuery(getMedicienQuery);
                           	
                           	 while(rs.next())
                           	 {
                           		temp++;
                           	  medicinConsumption = new MedicinConsumptionSearchResult();
                           		
                              	medicinConsumption.setMedicienName(rs.getString(1));
                              	
                              	medicinConsumption.setMedicienType(rs.getString(2));
                              	
                              	double tempQuentity = rs.getDouble(5);
                              	
                              	 medicinConsumption.setTotalQuentity(rs.getString(3));
                              	 
                              	//medicinConsumption.setToDate(rs.getString(4));
                              	// System.out.println("name: "+CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity));
                           	
                              	// System.out.println(String.valueOf(CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity)));
                            switch (medicinConsumption.getMedicienType())
                            {
                             
                            case "globules":    
                            case "Globules":
                                    medicinConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                    break;
                                case "tablets":
                                case "Tablets":
                                	 medicinConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                    break;
                                case "biochemic" :  
                                case "Biochemic":
                                	 medicinConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                    break;
                              
                                case "powder":
                                case "Powder":
                                    break;
                                case "liquid":
                                case "Liquid":
                                	 medicinConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity)) + " ML");
                                    break;
                                case "syrup":
                                case "Syrup":
                                	 medicinConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity)) + " ML");
                                    break;
                                    
                                case "ointment":
                                case "Ointment":
                                	 medicinConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity)) + " ML");
                                    break;
                            }
                           // System.out.println("getCalculatedQuantity: "+CalculateQuentity(medicinConsumption.getMedicienType(), tempQuentity));
                            resultList.add(medicinConsumption);
                            
                           // System.out.println("resultList: "+resultList.toString());
                           
                           	 }
                           	 if(temp==0) {
                     		    resultList.add(medicinConsumption); 
                     	 }
                           	 return gson.toJson(resultList); 
                           	
                            }
                            catch(Exception e)
                         {
                           	 System.out.println(e);
                            }                                                 
                         }
                 catch (Exception ex)
                 {
                	 logger.error("Error in Service : "+ex);
                     MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                     result.setStatusCode(String.valueOf(ServiceStatusCodeEnum.NULL_REFERENCE_EXCEPTION.getNumVal()));
                     result.setMessage(ex.getMessage());
                     resultList.add(result);
                     return gson.toJson(resultList);
                 }
               return gson.toJson(resultList);

             }
         }
         //|||27||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String GetMedicinConsumptionDetails(String currentDate, int offSet, String medicintype, String userid)
         {
            // DatabaseConnection db=new DatabaseConnection();
            // Connection con=db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	
        	 List<MedicinConsumptionSearchResult> resultList = new ArrayList<MedicinConsumptionSearchResult>();
             MedicinConsumptionSearchResult medicienConsumption = null;
             String getMedicienQuery =null;
            
             Gson gson = new GsonBuilder().serializeNulls().create();
             
             if (currentDate.isEmpty() == true || offSet < 0)
             {
                 MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                 result.setStatusCode(String.valueOf(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal()));
                 result.setMessage("Current date parameter is null or empty/ Offset value can't be negative.");
                 resultList.add(result);
                 return gson.toJson(resultList);
             }
             else
             {
                 // Start try block
                 try
                 {
                    	 String pattern="yyyy-MM-dd";
                         String tempcurrentDate = new SimpleDateFormat(pattern).format(new Date());
                         String filterDate = "";
                        
                         if (offSet < 0)
                         {
                             MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                             result.setStatusCode(String.valueOf(ServiceStatusCodeEnum.RECORDS_OFFSET_NEGATIVE.getNumVal()));
                             result.setMessage("Offset parameter value is negative. Offset parameter value can not be negative");
                             resultList.add(result);
                             return gson.toJson(resultList);
                         }
                         else
                         {
                             int days = offSet;
                             DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
                             // Date currentDate1=new Date();
                              Calendar c=Calendar.getInstance();
                              c.add(Calendar.DATE, (-days));
                              Date date=c.getTime();
                              filterDate=dateformat.format(date);
                              System.out.println(filterDate);
                             //filterDate = String.Format("{0:yyyy-MM-dd}", DateTime.Now.AddDays(-days));
                         }
                         // Create search query
                     
                         if (medicintype.equals("null"))
                         {
                        	
                                /*{getMedicienQuery = "Select medicien_name,patient_medicien_power,patient_medicien_type,"
                             		+ "patient_medicien_totalquntity from patient_medicien_details where patient_visit_date"
                             		+ "between '" + filterDate + "' and  '" + tempcurrentDate + "'  group by medicien_name";}*/
                                
                                getMedicienQuery = "Select medicien_name,patient_medicien_power,patient_medicien_type,patient_medicien_totalquntity" +
                                        " from patient_medicien_details where  patient_visit_date "
                                        		+ "between '" + filterDate + "' and '" + tempcurrentDate + "' and subscriptionid='"+subscription+"' group by medicien_name";
                         }
                         else
                         {
                        	
                            getMedicienQuery = "Select medicien_name,patient_medicien_power,patient_medicien_type,patient_medicien_totalquntity" +
                           " from patient_medicien_details where patient_medicien_type = '" + medicintype + "' and patient_visit_date "
                           		+ "between '" + filterDate + "' and '" + tempcurrentDate + "' and subscriptionid='"+subscription+"' group by medicien_name";
                         }
                         try {
                        	 int temp=0;
                            	 Statement st=con.createStatement();
                            	 ResultSet rs=st.executeQuery(getMedicienQuery);
                            	 while(rs.next()) 
                            	 {
                            		 
                            		  medicienConsumption = new MedicinConsumptionSearchResult();
                            		 temp++;
                                     // Get the medicien name
                                     medicienConsumption.setMedicienName(rs.getString(1));

                                     // Get the medicien type
                                     medicienConsumption.setMedicienType(rs.getString(3));

                                     // Get the medicine power
                                     medicienConsumption.setMedicienPower(rs.getString(2));

                                   
                                     medicienConsumption.setMedicienTotalQuantity(rs.getString(4));
                                   
                                   
                                     resultList.add(medicienConsumption);
                                 }
                            	 if(temp==0) {
                            		    resultList.add(medicienConsumption); 
                            	 }
                                 return gson.toJson(resultList);
                             }
                             catch(Exception e) {
                            	 logger.error("Error in Service : "+e);
                            	 System.out.println(e);
                             }
                         }
                       
                       
                         
                 catch (Exception ex)
                 {
                	 logger.error("Error in Service : "+ex);
                	 MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                     result.setStatusCode(String.valueOf(ServiceStatusCodeEnum.NULL_REFERENCE_EXCEPTION.getNumVal()));
                     result.setMessage(ex.getMessage());
                     resultList.add(result);
                     return gson.toJson(resultList);
                 }
             }
                 // Return result
                 return gson.toJson(resultList);
  
             }
         
         
         
         //|||||28||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         boolean tryParseInt(String value) {  
             try {  
                 Integer.parseInt(value);  
                 return true;  
              } catch (NumberFormatException e) {  
            	  logger.error("Error in Service : "+e);
                 return false;  
              }  
        }
         
         public String GetYearlyMedicinConsumption(String targetYear, String targetMonth,String medicintype, String userid)
         {
        	 System.out.println("at service no 28");
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 List<MedicinConsumptionSearchResult> resultList = new ArrayList<MedicinConsumptionSearchResult>();
             int outYear = 0;
             String yearlySelectQuery = "";
             // Check parameters 
             if (targetYear.isEmpty() == true || targetMonth.isEmpty() == true)
             {
            	 System.out.println("at line no 2405");
            	 MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                 result.setStatusCode(String.valueOf(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal()));
                 result.setMessage("Year parameter/ month parameter is null or empty. Year/moth value can not be empty.");
                 resultList.add(result);
                 return gson.toJson(resultList);
             }
             if (tryParseInt(targetYear)== false)
             {
            	 System.out.println("at line no 2414");
            	 
            	 MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                 result.setStatusCode(String.valueOf(ServiceStatusCodeEnum.FORMAT_UNSUPPORTED.getNumVal()));
                 result.setMessage("Year parameter value is invalid. Year must be numeric value.");
                 resultList.add(result);
                 return gson.toJson(resultList);
             }
             else
             {
                 // Start try block
                 try
                 {
                	 System.out.println("at line no 2423");
                  int outMonth = 0;
                         String date1 = targetYear + "-" + targetMonth + "-" + "01";
                         System.out.println("at line 2599  "+date1);
                         
                         // Calculate target month last date(eg. for january 31/01/year). If month parameter not convertable into integer, return January month details on target year.
                         String date2 =tryParseInt(targetMonth) == true ? GetDate(targetYear, targetMonth) : targetYear + "-" + targetMonth + "-" + "31";
                         // Create search query base on year & moth parameter values.
                         System.out.println("at line 2604  "+date2);
                         
                         List<ResultSet> list=new ArrayList<ResultSet>();
                         if (medicintype ==null)
                         {
                          String  yearlySelectQuery1 = "Select medicien_name,patient_medicien_type,patient_medicien_totalquntity,"
                          		+ "patient_visit_date,sum(medicien_calculated_quantity) as Cal_Quentity" +
                          		" from patient_medicien_details where  patient_visit_date between"
                          		+ " '" + date1 + "' and '" + date2 + "' and subscriptionid='"+subscription+"' group by medicien_name";
                          try {
                         	 Statement st1=con.createStatement();
                         	 ResultSet rs=st1.executeQuery(yearlySelectQuery1);
                         	 while(rs.next())
                         	 {
                         		 MedicinConsumptionSearchResult medicienConsumption = new MedicinConsumptionSearchResult();
                                
                         		 medicienConsumption.setMedicienName(rs.getString(1));
                                
                                 medicienConsumption.setMedicienType(rs.getString(2));
                                String str=rs.getString(3);
                                String str1=str.replaceAll("[^0-9-.]", "");
                                 double tempQuentity =Double.valueOf( str1);
                                 
                                 medicienConsumption.setTotalQuentity(String.valueOf(tempQuentity));
                                 System.out.println(tempQuentity+"   at ");
                                 
                                 medicienConsumption.setFromDate(date1);
                                 
                                 medicienConsumption.setToDate(date2);
                                
                                 switch (medicienConsumption.getMedicienName())
                                 {
                                    
                                     case "Globules":
                                     case "globules":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                    	 // medicienConsumption.CalculatedQuantity = String.valueOf(CalculateQuentity(medicienConsumption.MedicinType, tempQuentity))+ " Dram";
                                         break;
                                   
                                     case "Tablets":
                                     case "tablets":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                         break;
                                     
                                     case "Biochemic":
                                     case "biochemic":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                         break;
                                    
                                     case "Powder":
                                     case "powder":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " Dram");

                                    	 break;
                                    
                                     case "Liquid":
                                     case "liquid":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " ML");
                                         break;
                                  
                                     case "Syrup":
                                     case "syrup":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " ML");
                                         break;
                                         
                                     case "ointment":
                                     case "Ointment":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " ML");
                                         break;
                                 }
                                 resultList.add(medicienConsumption);
                         	 }
                         	 
                          }
                          catch(Exception e)
                          {
                        	  logger.error("Error in Service : "+e);
                         	 System.out.println(e);
                          }
                       }
                         else
                         {
                           String  yearlySelectQuery1 = "Select medicien_name,patient_medicien_type,patient_medicien_totalquntity,"
                           		+ "patient_visit_date,sum(medicien_calculated_quantity) as Cal_Quentity" +
                       " from patient_medicien_details where patient_medicien_type = '" + medicintype + "' and"
                       		+ " patient_visit_date between '" + date1 + "' and '" + date2 + "' and subscriptionid='"+subscription+"' group by medicien_name";
                         
                           try 
                           {
                           	 Statement st=con.createStatement();
                           	 ResultSet rs=st.executeQuery(yearlySelectQuery1);
                           	 while(rs.next()) {
                           		
                           		rs.getString(1);
                         		rs.getString(2);
                         		rs.getString(3);
                         		rs.getString(4);
                         		list.add(rs);
                         		
                         		 MedicinConsumptionSearchResult medicienConsumption = new MedicinConsumptionSearchResult();
                                 medicienConsumption.setMedicienName(rs.getString(1));
                               
                                 medicienConsumption.setMedicienType(rs.getString(2));
                              
                                 String str=rs.getString(3);
                                 String str1=str.replaceAll("[^0-9-.]", "");
                                  double tempQuentity =Double.valueOf( str1);
                                 medicienConsumption.setTotalQuentity(String.valueOf(tempQuentity));
                                 medicienConsumption.setFromDate(date1);
                                 medicienConsumption.setToDate(date2);
                               
                                 switch (medicienConsumption.getMedicienType())
                                 {
                                     case "Globules":
                                     case "globules":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                         break;
                                     
                                     case "Tablets":
                                     case "tablets":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                         break;
                                    
                                     case "Biochemic":
                                     case "biochemic":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " Dram");
                                         break;
                                   
                                     case "Powder":
                                     case "powder":
                                         break;
                                   
                                     case "Liquid":
                                     case "liquid":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " ML");
                                         break;
                                    
                                     case "Syrup":
                                     case "syrup":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " ML");
                                         break;
                                     case "ointment":
                                     case "Ointment":
                                    	 medicienConsumption.setCalculatedQuantity(String.valueOf(CalculateQuentity(medicienConsumption.getMedicienType(), tempQuentity)) + " ML");
                                         break;
                                 }
                                 resultList.add(medicienConsumption);
                           	 }
                            }
                            catch(Exception e) {
                           	 System.out.println(e);
                            }
                         
                              return gson.toJson(resultList);  
                             }
                         }
                 catch (Exception ex)
                 {
                	 MedicinConsumptionSearchResult result = new MedicinConsumptionSearchResult();
                     result.setStatusCode((String.valueOf(ServiceStatusCodeEnum.NULL_REFERENCE_EXCEPTION.getNumVal())));
                     result.setMessage(ex.getMessage());
                     resultList.add(result);
                     return gson.toJson(resultList);
                 }
                 return gson.toJson(resultList);
             }
         }
         //||||||29|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         
         
         String CalculateNextDate(int day, String month, String year)
         {
             String nextDate = "";
             int temMonth = Integer.parseInt(month);
             int tempDay = day;
             int tempYear = Integer.parseInt(year);
             switch (month)
             {
                 // Months those have 31 days
                 case "1":
                 case "3":
                 case "5":
                 case "7":
                 case "8":
                 case "10":
                     if (day > 31)
                     {
                         temMonth = temMonth + 1;
                         day = 01;
                     }
                     break;
                 // December month
                 case "12":
                     if (day > 31)
                     {
                         temMonth = 1;
                         day = 01;
                         tempYear = tempYear + 1;
                     }
                     break;
                 // Feb month have only 28 days.
                 case "2":
                     if (day > 28)
                     {
                         temMonth = temMonth + 1;
                         day = 01;
                     }
                     break;
                 // Months those have 30 days.
                 default:
                     if (day > 30)
                     {
                         temMonth = temMonth + 1;
                         day = 01;
                     }
                     break;
             }
             nextDate = tempYear + "-" + temMonth + "-" + tempDay;
             return nextDate;
         }
         
         
         
         
         
         //|||||||30||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         String GetDate(String year, String month)
         {
             String dayCount = "";
             // Get total days in target month.
             switch (month)
             {
                 // Month those have 31 days.
                 case "1":
                 case "3":
                 case "5":
                 case "7":
                 case "8":
                 case "10":
                 case "12":
                     dayCount = "31";
                     break;
                 // Feb month have only 28 days.
                 case "2":
                     dayCount = "28";
                     break;
                 // Months those have 30 days.
                 default:
                     dayCount = "30";
                     break;
             }

             // Create last date of target month & return to caller.
             return year + "-" + month + "-" + dayCount;
         }
         
         
         
         
         
         
         //||||||||31|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         
         
         private double CalculateQuentity(String medicienType, double quentity)
         {
             double calculatedResult = 0;
             switch (medicienType)
             {
                 case "Globules":
                     calculatedResult = quentity ;
                     break;
                 case "Tablets":
                     calculatedResult = quentity ;
                     break;
                 case "Biochemic":
                     calculatedResult = quentity ;
                     break;
                 case "Powder":
                     break;
                 case "Liquid":
                 case "Syrup":
                     calculatedResult = quentity;
                     break;
                     
                 case "Ointment":
                     calculatedResult = quentity;
                     break;
             }
             calculatedResult = Math.round(calculatedResult);
          System.out.println(calculatedResult+"  at ");
            // System.out.println("medicienType: "+medicienType +"quentity:"+quentity);
             return calculatedResult;
         }
         
         //||||||||32|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         
         public String GetPatientsListBasedOnGroupSearchQuery(String searchQueryString, String groupName, int limit, int offset, String userid)
         {
        	 String subscription=getSubscriptionId(userid);
        	 
        	 System.out.println("subscription: "+subscription);
           // DatabaseConnection db=new DatabaseConnection();
            //Connection con=db.getConnection();
        	 DrServiceResponce drResponce = new DrServiceResponce();
        	 Gson gson = new GsonBuilder().serializeNulls().create();
             if (searchQueryString.isEmpty()== true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.SEARCH_QUERY_EMPTY.getNumVal(), "Search Query is null or empty. Search query can not be null or empty.", "Search query can not be empty."));
             }
             else
             {
                 try
                 {
                     String patientSearchQuery = GenerateSearchOprations.GenerateSearchQueryForGroupSearch(searchQueryString, groupName, limit, offset, subscription);
                    System.out.println(patientSearchQuery+" at line 2688");
                     if (patientSearchQuery.isEmpty() == false)
                     {
                    	 Statement st=con.createStatement();
                    	 ResultSet rs=st.executeQuery(patientSearchQuery);
                            if(rs!=null)   
                            	{
                                     drResponce.setPatientList (new ArrayList<PatientInformation>());
                                     drResponce.setPatientList(ProcessMySqlSelectCommandResult.GetPatientGeneralInformationFromSearchResult(rs, true));
                                     drResponce.setStatusCode(ServiceStatusCodeEnum.SUCCESS.getNumVal());
                                     drResponce.setMessage("Success");
                                     drResponce.setListOfColumns(new ArrayList<String>());
                                     drResponce.setListOfColumns(GetListOfColumns());
                                     return gson.toJson(drResponce);
                                 }
                                 else
                                 {
                                     return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORD_NOT_FOUND.getNumVal(), "Record not found.!", ""));
                                 }
                             
                         }
                     else
                     {
                         return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORD_NOT_FOUND.getNumVal(), "Record not found.!", ""));
                     }
                     
                     }
                 catch (Exception odbcEx)
                 {
                     return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.MYSQL_EXCEPTION.getNumVal(), odbcEx.getMessage(), odbcEx.getMessage()));
                 }
            }
         }
         
  //|||||||||33||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
  public String GetPatientsListBasedOnGroupSearchQueryGrid(String searchQueryString, String groupName, String userid) 
         {
        	// DatabaseConnection db=new DatabaseConnection();
            // Connection con=db.getConnection();
	  
	  		String subscription = getSubscriptionId(userid);
	  
             Gson gson = new GsonBuilder().serializeNulls().create();
             DrServiceResponce drResponce = new DrServiceResponce();
             System.out.println("searchQueryString: "+searchQueryString);
             if (searchQueryString.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.SEARCH_QUERY_EMPTY.getNumVal(), "Search Query is null or empty. Search query can not be null or empty.", "Search query can not be empty."));
             }
             else
             {
                 try
                 {
                     String patientSearchQuery = GenerateSearchOprations.GenerateSearchQueryForGroupSearchGrid(searchQueryString, groupName, subscription);
                     if (patientSearchQuery.isEmpty() == false)
                     {
                    	 System.out.println("After process query: "+patientSearchQuery);
                    	 Statement st=con.createStatement();
                    	 ResultSet rs=st.executeQuery(patientSearchQuery);
                        System.out.println(patientSearchQuery+"  serach query");
                    	 if(rs!=null)        
                    	 {
                                     drResponce.setPatientList(new ArrayList<PatientInformation>());
                                   
                                     //drResponce.getPatientList().addAll(ProcessMySqlSelectCommandResult.GetPatientGeneralInformationFromSearchResult(rs, true));
                                     drResponce.getPatientList().addAll(ProcessMySqlSelectCommandResult.GetPatientGeneralInformationFromSearchResult(rs, true));
                                     drResponce.setStatusCode(ServiceStatusCodeEnum.SUCCESS.getNumVal());
                                   
                                     drResponce.setMessage ("Success");
                                     drResponce.setListOfColumns(new ArrayList<String>());
                                 
                                     return gson.toJson(drResponce);
                                 }
                                 else
                                 {
                                     return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORD_NOT_FOUND.getNumVal(), "Record not found.!", ""));
                                 }
                             }
                     else
                     {
                         return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORD_NOT_FOUND.getNumVal(), "Record not found.!", ""));
                     }
                     }
                     
                 
                 catch (Exception odbcEx)
                 {
                     return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.MYSQL_EXCEPTION.getNumVal(), odbcEx.getMessage(), odbcEx.getMessage()));
                 }

             }
          
         }
         
         //|||||||||||||34--------------------------------------------------------------------------------------------------------------
         
         public String UpdatePatientInformation(String patient1, String userid)
         {
        	 //DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 PatientInformation patient=new PatientInformation();
        	 try
        	 {
        		// SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
   	        	//SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");
   	      
     		 
     		 JSONObject obj=new JSONObject(patient1);
     		 
     		 String patientdetail=obj.getString("patient");
     		 JSONObject patientobject=new JSONObject(patientdetail);
    		 
     		
    		 //Date time = parseFormat.parse(patientobject.getString("VisitTime"));
    		 //String time1=displayFormat.format(time);
    		
     		// System.out.println("FirstName: "+patientobject.getString("PatientFirstName"));
    		 
    		 patient.setPKColumnId((patientobject.getString("PKColumnId")));
    		 
    		 patient.setPatientRegId(patientobject.getString("PatientRegId"));
    		 
    		 patient.setPatientAge(patientobject.getString("PatientAge"));
    		 
    		 patient.setPatientDOB(patientobject.getString("PatientDOB"));
    		 
    		 patient.setPatientBloodGroup(patientobject.getString("PatientBloodGroup"));
    		 
    		 patient.setPatientAddress(patientobject.getString("PatientAddress"));
    		 
    		 patient.setPatientCity(patientobject.getString("PatientCity"));
    		 
    		 patient.setPatientState(patientobject.getString("PatientState"));
    		 
    		 patient.setPatientCountry(patientobject.getString("PatientCountry"));
    		 
    		 patient.setPatientPhoneNo(patientobject.getString("PatientPhoneNo"));
    		 
    		 patient.setPatientGender(patientobject.getString("PatientGender"));
    		 
    		
    		 patient.setPatientMaritalStatus(patientobject.getString("PatientMaritalStatus"));
    		 
    		
    		 patient.setPatientProfession(patientobject.getString("PatientProfession"));
    		 
    		 patient.setPatientReligion(patientobject.getString("PatientReligion"));
    		 
    		 
    		 patient.setPatientFirstName(patientobject.getString("PatientFirstName"));
    		 
    		 patient.setPatientSurName(patientobject.getString("PatientSurName"));
    		 
    		 patient.setPatientEmailId(patientobject.getString("PatientEmailId"));
    		 
    		 patient.setPatientSymptoms(patientobject.getString("PatientSymptoms"));
    		
    		 if(patientobject.getString("PatientZipCode").equals("") || patientobject.getString("PatientZipCode")==null)
    		 {
    			// System.out.println("Zip code empty");
    		 }
    		 else
    		 {
    			 patient.setPatientZipCode(patientobject.getString("PatientZipCode"));
    		 }
    		
    		 
    		 JSONArray arr=patientobject.getJSONArray("DynamicColumns");
    		
    		 List<DynamicColumns> li1=new ArrayList<DynamicColumns>();
    		 
    		 for(int i=0; i<arr.length(); i++)
    		 {
    			 DynamicColumns li=new DynamicColumns();
    			
    			    String str=arr.getString(i);
    			    
    				JSONObject ja1=new JSONObject(str);	
    				
    				li.setColumnName(ja1.getString("ColumnName"));
    				li.setColumnValue(ja1.getString("ColumnValue"));
    				
    				if(ja1.length()>2)
    				{
    					li.setColumnId(ja1.getString("ColumnId"));
    				}
    				else
    				{
    					li.setColumnId("");
    				}
    				li1.add(li);
    				
    				
    		 }
    		
    		 patient.setDynamicColumns(li1);
        	 }
        	 catch(Exception e) 
        	 {
        		 System.out.println(e);
        	 }
        	 
        	 String msg ="";
        	 if (patient == null)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "PatientInformation class object is null", ""));
             }
             else
             {
                 try
                 {
                    
                             String updatepatientInformation = "Update patient_information_master set patient_first_name =?," +
                             "patient_last_name=?,patient_gender=?,patient_date_of_birth=?," +
                             "patient_country=?,patient_state=?,patient_city=?," +
                             "patient_address=?,patient_zip_code=?," +
                             "patient_phone_number=? ,patient_profession=?," +
                             "patient_marital_status=?,patient_religion=?," +
                             "patient_email_id=?,patient_Symptoms=?,patient_bloodgroup=?,patient_age=?, subscriptionid=? where patient_id=?";
                             try {
                            	 System.out.println(patient.getPKColumnId());
                            	 
                            	 PreparedStatement pst=con.prepareStatement(updatepatientInformation);
                            	 pst.setString(1,patient.getPatientFirstName());
                            	 pst.setString(2,patient.getPatientSurName());
                            	 pst.setString(3,patient.getPatientGender());
                                 pst.setString(4,patient.getPatientDOB());
                                 pst.setString(5,patient.getPatientCountry());
                                 pst.setString(6,patient.getPatientState());
                                 pst.setString(7,patient.getPatientCity());
                                 pst.setString(8,patient.getPatientAddress());
                                 pst.setString(9,patient.getPatientZipCode());
                                 pst.setString(10,patient.getPatientPhoneNo() );
                                 pst.setString(11,patient.getPatientProfession());
                                 pst.setString(12,patient.getPatientMaritalStatus());
                                 pst.setString(13,patient.getPatientReligion());
                                 pst.setString(14,patient.getPatientEmailId());
                                 pst.setString(15,patient.getPatientSymptoms());
                                
                                 pst.setString(16,patient.getPatientBloodGroup());
                                 pst.setString(17,patient.getPatientAge());
                                pst.setString(18, subscription);
                                 
                                 pst.setString(19,patient.getPKColumnId());
                                
                                int result= pst.executeUpdate();      
                                 msg += "Master" + String.valueOf(result);
                                 
                            if (patient.getDynamicColumns() != null && patient.getDynamicColumns().size() > 0)
                             {
                            	
                                     for(int i=0;i<patient.getDynamicColumns().size();i++)
                                     {
                                    	
                                    	//System.out.println("size: "+ patient.getDynamicColumns().get(0).);
                                    	// DynamicColumns column=patient.getDynamicColumns().get(i);
                                    	 
                                    	 //System.out.println("column "+column.getColumnId().isEmpty());
                                         if (patient.getDynamicColumns().get(i).getColumnId().isEmpty()==false)//column.getColumnId().isEmpty() == false)
                                         {
                                        	
                                             String updateCommand = "Update patient_dynamic_table set column_name=?," +
                                             "column_value=?, subscriptionid=? where patient_reg_id=? and column_id=?";
                                             
                                             
                                             
                                             try
                                             {
                                            	PreparedStatement pst1=con.prepareStatement(updateCommand);
                                            	pst1.setString(1, patient.getDynamicColumns().get(i).getColumnName());//column.getColumnName());
                                            	pst1.setString(2, patient.getDynamicColumns().get(i).getColumnValue());//column.getColumnValue());
                                            	pst1.setString(3, subscription);
                                            	
                                            	pst1.setString(4, patient.getPatientRegId());
                                            	//System.out.println("columnId");
                                            	pst1.setString(5,patient.getDynamicColumns().get(i).getColumnId() );//column.getColumnId());
                                            	
                                            	int rowAffected =pst1.executeUpdate();
                                            	
                                            	 msg += "Dynamic:" + String.valueOf(rowAffected) + patient.getPatientRegId() + "and column_id=" + patient.getDynamicColumns().get(i).getColumnId();
                                             }
                                             catch(Exception e) 
                                             {
                                            	 System.out.println(e);
                                             }
                                      
                                         }
                                         else
                                         {
                                             String insertCommand = "Insert into patient_dynamic_table(patient_reg_id,column_name,column_value, subscriptionid)" +
                                             "values(?,?,?,?)";
                                             try 
                                             {
                                            	 PreparedStatement st=con.prepareStatement(insertCommand);
                                            	 st.setString(1, patient.getPatientRegId());
                                            	 st.setString(2,  patient.getDynamicColumns().get(i).getColumnName());
                                            	 st.setString(3, patient.getDynamicColumns().get(i).getColumnValue());
                                            	 st.setString(4, subscription);
                                            	  st.executeUpdate();
                                            	  msg += "Inserted new columns: ColumnName is: " +  patient.getDynamicColumns().get(i).getColumnName() + ", ColumnValue is: " + patient.getDynamicColumns().get(i).getColumnValue();
                                             }
                                             
                                             catch(Exception e) 
                                             {
                                            	 System.out.println(e);
                                             }
                                             
                                         }
                                         
                                     }
                                 }
                             }
                             catch(Exception e) {
                            	 System.out.println(e);
                             }
                             
                         }
                 catch (Exception odbcEx)
                 {
                     return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.MYSQL_EXCEPTION.getNumVal(), odbcEx.getMessage(), odbcEx.getMessage()));
                 }
                     }
                     return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.SUCCESS.getNumVal(), "Patient information updated successfull. "+msg, ""));
                 }
                

         //|||||||||||35--------------------------------------------------------------------------------------------------------------
         
         public String SavePatientVisitInformation(String patientVisitInformation1, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	
        	 String subscription = getSubscriptionId(userid);
        	 
        	 
        	 String result = " \"\" ";
        	 PatientVisitInformation patientVisitInformation=new PatientVisitInformation();
        	 
        	 try
        	 {
        		 	SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
      	        	SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");
      	      
        		 
        		 JSONObject obj=new JSONObject(patientVisitInformation1);
        		 
        		 String patientdetail=obj.getString("patientVisitInformation");
        		 
        		 JSONObject patientobject=new JSONObject(patientdetail);
        		 
        		
        		 Date time = parseFormat.parse(patientobject.getString("VisitTime"));
        		 String time1=displayFormat.format(time);
        		
        		 patientVisitInformation.setRegistrationId((patientobject.getString("RegistrationId")));
        		 patientVisitInformation.setVisitTime(time1);
        		 patientVisitInformation.setVisitInformation(patientobject.getString("VisitInformation"));
        		
        		 patientVisitInformation.setVisitTitle(patientobject.getString("VisitTitle"));
        		 patientVisitInformation.setVisitStatus(patientobject.getString("VisitStatus"));
        		 patientVisitInformation.setVisitdueamount("0");
        		 patientVisitInformation.setVisitadvanceamount("0");
        		 patientVisitInformation.setVisittotalamount("0");
        		 patientVisitInformation.setPatientPaidAmount("0");
        		 patientVisitInformation.setPatientFeeConcession("0");
        		 patientVisitInformation.setPatientNextvisitdate((patientobject.getString("PatientNextvisitdate")));
        		 patientVisitInformation.setVisitBp((patientobject.getString("VisitBp")));
        		 patientVisitInformation.setVisitWeight((patientobject.getString("VisitWeight")));
        		 patientVisitInformation.setVisitDiagonastic((patientobject.getString("VisitDiagonastic")));
        		 patientVisitInformation.setTypeConversation((patientobject.getString("TypeConversation")));
        		 JSONArray arr=patientobject.getJSONArray("VisitMedicienList");
        		 
        		
        		 List<VisitMedicin> li1=new ArrayList<VisitMedicin>();
        		 for(int i=0; i<arr.length(); i++)
        		 {
        			 VisitMedicin li=new VisitMedicin();
        			
        			    String str=arr.getString(i);
        			    
        				JSONObject ja1=new JSONObject(str);	
        				
        				li.setMedicinName(ja1.getString("MedicienName"));
        				li.setMedicinpower(ja1.getString("Medicienpower"));
        				li.setMedicintype(ja1.getString("Medicientype"));
        				li.setMedicinDos(ja1.getString("MedicienDos"));
        				li.setMedicinQuntity(ja1.getString("MedicienQuentity"));
        				li.setMedicinday(ja1.getString("Medicienday"));
        				li.setVisitExternalInternal(ja1.getString("VisitExternalInternal"));
        				li.setVisitFromDate(ja1.getString("VisitFromDate"));
        				li.setVisitToDate(ja1.getString("VisitToDate"));
        				
        				li1.add(li);
        				 System.out.println("in for");
        		 }
        		 patientVisitInformation.setVisitMedicinList(li1);
        		// System.out.println("type1 :"+patientVisitInformation.getVisitMedicinList().get(0).getMedicintype());
        		 //System.out.println("type2: "+patientVisitInformation.getVisitMedicinList().get(1).getMedicintype());
        		 
        	 }
        	 catch(Exception e)
        	 {
        		 System.out.println(e);
        	 }
         
        	 
        	//logger.info(patientVisitInformation.toString());
        	
        	try
        	{
        			
        			
        			if (patientVisitInformation != null && patientVisitInformation.getVisitMedicinList() != null)
        			{
                	
                	
        				LocalDate localDate = LocalDate.now();//For reference
        				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-LLLL-dd");
        				String tempcurrentDate = localDate.format(formatter);

        				String insertvisitdetails = "Insert into patient_visit_details(patient_reg_id,patient_visit_date,patient_visit_time,patient_visit_information,"
                             		+ "patient_visit_medicien_information,patient_visit_title,patient_condition_status,patient_visit_due_amount,"
                             		+ " patient_visit_advance_amount, patient_visit_total_amount,patient_visit_paid_amount, patient_next_visit_date,patient_next_visit_time,"
                             		+ " patient_visit_bp, patient_visit_weight, patient_visit_diagnostic,patient_visit_image_path,patient_visit_image_name,patient_visit_total_medicine_quntity,type_conversation,subscriptionid)" +
                                 "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        try
                        {
                        	
                            	PreparedStatement st=con.prepareStatement(insertvisitdetails);
                            	st.setString(1, patientVisitInformation.getRegistrationId());
                            	st.setString(2,tempcurrentDate);
                            	st.setString(3, patientVisitInformation.getVisitTime());
                            	st.setString(4, patientVisitInformation.getVisitInformation());
                            	String str=createVisitMedicienInfo(patientVisitInformation.getVisitMedicinList());
                            	st.setString(5,str);
                            	
                            	st.setString(6, patientVisitInformation.getVisitTitle());
                            	st.setString(7, patientVisitInformation.getVisitStatus());
                            	st.setString(8, patientVisitInformation.getVisitdueamount());
                            	st.setString(9, patientVisitInformation.getVisitadvanceamount());
                            	st.setString(10, patientVisitInformation.getVisittotalamount());
                            	st.setString(11, patientVisitInformation.getPatientPaidAmount());
                            	st.setString(12, patientVisitInformation.getPatientNextvisitdate());
                            	st.setString(13, patientVisitInformation.getVisitTime());
                            	st.setString(14, patientVisitInformation.getVisitBp());
                            	st.setString(15, patientVisitInformation.getVisitWeight());
                            	st.setString(16, patientVisitInformation.getVisitDiagonastic());
                            	st.setInt(17, 0);
                            	st.setInt(18, 0);
                            	st.setInt(19, 0);
                            	st.setString(20, patientVisitInformation.getTypeConversation());
                            	st.setString(21, subscription);
                            	
                            	//System.out.println("patientVisitInformation.getVisitTime(): "+patientVisitInformation.getVisitTime());
                            	
                            	st.executeUpdate();
                            	
                        }
                        
                        catch(Exception e)                         
                        {
                        	System.out.println(e);
                       	 	return e.toString();
                            	 
                        }
                  
                        String insertappointment =   " Insert into patient_appointment_table(patient_registration_id, appointment_visit_date,appointment_date,"
                             					   + " appointment_visit_time, appointment_start_time, appointment_end_time, appointment_title, subscriptionid)" +
                                                     "Values(?,?,?,?,?,?,?,?)";
                        try
                        {
                             		PreparedStatement st1=con.prepareStatement(insertappointment);
                             		st1.setString(1, patientVisitInformation.getRegistrationId());
                             		st1.setString(2, tempcurrentDate);
                             		st1.setString(3, patientVisitInformation.getPatientNextvisitdate());
                             		st1.setString(4, patientVisitInformation.getVisitTime());
                             		st1.setString(5, patientVisitInformation.getPatientAppointmentStartTime());
                             		st1.setString(6, patientVisitInformation.getPatientAppointmentEndTime());
                             		st1.setString(7, patientVisitInformation.getVisitTitle());
                             		st1.setString(8, subscription);
                             		st1.executeUpdate();
                        }
                        
                        catch(Exception e)
                        {
                       	 	System.out.println(e);
                       	 	return e.toString();
                        }
                        
                        
                       // System.out.println(patientVisitInformation.getVisitMedicinList().size());
                        
                        for (int i = 0; i < patientVisitInformation.getVisitMedicinList().size(); i++)     
                        {
                                VisitMedicin visitMedicin =patientVisitInformation.getVisitMedicinList().get(i);
                               // System.out.println(visitMedicin);
                                PatientMedicinCalculation patientmedicincalculation = new PatientMedicinCalculation();
                            
                                String totalCalculatedQuantity = patientmedicincalculation.CalculateTotalQuntity(visitMedicin);
                                System.out.println("total=="+totalCalculatedQuantity);
                               
                                String a = totalCalculatedQuantity;
                                String b = "";
                                int val=0;

                                for (int j = 0; j < a.length(); j++)
                                {
                                    if (Character.isDigit(a.charAt(j)))
                                        b += a.charAt(j);
                                }

                                if (b.length() > 0)
                                {
                                    val = Integer.parseInt(b);
                                    System.out.println(val+" 3249");
                                }
                                
                                
                                String patientmedicindetails = "Insert into patient_medicien_details(patient_reg_id,patient_visit_date,patient_visit_time,medicien_name,medicien_dos,"
                                							+ "patient_medicien_power,patient_medicien_type,patient_medicien_quentity,patient_medicien_day,"
                                							+ "patient_medicien_totalquntity,patient_medicien_ext_int_medicine, patient_medicien_from_date,patient_medicien_to_date,"
                                							+ "medicien_calculated_quantity,subscriptionid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                	PreparedStatement st2=con.prepareStatement(patientmedicindetails);
                                	st2.setString(1, patientVisitInformation.getRegistrationId());
                                	st2.setString(2, tempcurrentDate);
                                	st2.setString(3, patientVisitInformation.getVisitTime());
                                	st2.setString(4, visitMedicin.getMedicinName());
                                	st2.setString(5, visitMedicin.getMedicinDos());
                                	st2.setString(6, visitMedicin.getMedicinpower());
                                	st2.setString(7, visitMedicin.getMedicintype());
                                	st2.setString(8,visitMedicin.getMedicinQuntity());
                                	st2.setString(9, visitMedicin.getMedicinday());
                                	st2.setString(10, totalCalculatedQuantity);
                                	st2.setString(11,visitMedicin.getVisitExternalInternal());
                                	st2.setString(12, visitMedicin.getVisitFromDate());
                                	st2.setString(13, visitMedicin.getVisitToDate());
                                	st2.setInt(14, val);
                                	st2.setString(15, subscription);
                                	st2.executeUpdate();
                                	logger.info("SuccessFully inserted");
                         }
        			}  
        	}
        	
        	catch (Exception odbcEx)
    		{
    			System.out.println(odbcEx);
    			return odbcEx.toString();
    		}
        	
             return result;
         }

         //||||||||||36|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
        
		public String OverridePatientVisitInformation(String patientVisitInformation1, String userid)
         {
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 // PatientVisitInformation patientVisitInformation=new PatientVisitInformation();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 
        	  String result = " \"\" ";
        	 PatientVisitInformation patientVisitInformation=new PatientVisitInformation();
        	 
        	 try
        	 
        	 {
        		 	SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
      	        	SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");
      	      
      	        	
        		 JSONObject obj=new JSONObject(patientVisitInformation1);
        		 
        		 String patientdetail=obj.getString("patientVisitInformation");
        		 
        		 JSONObject patientobject=new JSONObject(patientdetail);
        		 
        		 String time1="";
        		 
        		 if (patientobject.getString("VisitTime").toLowerCase().contains("am") || patientobject.getString("VisitTime").toLowerCase().contains("pm"))
        		 {
        			 Date time = parseFormat.parse(patientobject.getString("VisitTime"));
            		  time1=displayFormat.format(time);
        		 }
        		 else
        		 {
        			 time1 = patientobject.getString("VisitTime");
        		 }
        		
        		
        		
        		 patientVisitInformation.setRegistrationId((patientobject.getString("RegistrationId")));
        		 patientVisitInformation.setVisitTime(time1);
        		 patientVisitInformation.setVisitInformation(patientobject.getString("VisitInformation"));
        		 patientVisitInformation.setVisitId(patientobject.getString("VisitId"));
        		 
        		 patientVisitInformation.setVisitTitle(patientobject.getString("VisitTitle"));
        		 patientVisitInformation.setVisitStatus(patientobject.getString("VisitStatus"));
        		 patientVisitInformation.setVisitdueamount("0");
        		 patientVisitInformation.setVisitadvanceamount("0");
        		 patientVisitInformation.setVisittotalamount("0");
        		 patientVisitInformation.setPatientPaidAmount("0");
        		 patientVisitInformation.setPatientNextvisitdate((patientobject.getString("PatientNextvisitdate")));
        		 patientVisitInformation.setVisitBp((patientobject.getString("VisitBp")));
        		 patientVisitInformation.setVisitWeight((patientobject.getString("VisitWeight")));
        		 patientVisitInformation.setVisitDiagonastic((patientobject.getString("VisitDiagonastic")));
        		 
        		 JSONArray arr=patientobject.getJSONArray("VisitMedicienList");
        		 
        		 List<VisitMedicin> li1=new ArrayList<VisitMedicin>();
        		 for(int i=0; i<arr.length(); i++)
        		 
        		 {
        			 VisitMedicin li=new VisitMedicin();
        			
        			    String str=arr.getString(i);
        			    
        				JSONObject ja1=new JSONObject(str);	
        				
        				li.setMedicinName(ja1.getString("MedicienName"));
        				li.setMedicinpower(ja1.getString("Medicienpower"));
        				li.setMedicintype(ja1.getString("Medicientype"));
        				li.setMedicinDos(ja1.getString("MedicienDos"));
        				li.setMedicinQuntity(ja1.getString("MedicienQuentity"));
        				li.setMedicinday(ja1.getString("Medicienday"));
        				li.setVisitExternalInternal(ja1.getString("VisitExternalInternal"));
        				li.setVisitFromDate(ja1.getString("VisitFromDate"));
        				li.setVisitToDate(ja1.getString("VisitToDate"));
        				
        				li1.add(li);
        				System.out.println("count :"+i);
        				
        		 }
        		 
        		 patientVisitInformation.setVisitMedicinList(li1);
        		// System.out.println("type1 :"+patientVisitInformation.getVisitMedicinList().get(0).getMedicintype());
        		// System.out.println("type2: "+patientVisitInformation.getVisitMedicinList().get(1).getMedicintype());
        		 
        	 }
        	 
        	 catch(Exception e)
        	
        	 {
        		 System.out.println(e);
        	 }
        	 
        	logger.info(patientVisitInformation.toString());
            
        	try
             {
                 if (patientVisitInformation != null && patientVisitInformation.getVisitMedicinList() != null)
                 {
                     String tempcurrentDate = new SimpleDateFormat("dd-mm-yyyy").format(new Date());

             String updatepatientvisit = "Update patient_visit_details set patient_visit_information=?,"
                      	+ "patient_visit_medicien_information=? ,"
                      	  + "patient_visit_title=?,patient_condition_status=?,patient_visit_due_amount=?,"
                           		  + "patient_visit_advance_amount=?,patient_visit_total_amount=?,"
                           			 + "patient_next_visit_date= ?,patient_next_visit_time=?,"
                           				+ "patient_visit_bp=?,patient_visit_weight=?,"
                           				+ "patient_visit_diagnostic=?  where visit_id= ? and subscriptionid=?";
                           try 
                           {
                        	   
                        	   PreparedStatement st=con.prepareStatement(updatepatientvisit);
                        	  
                        	   st.setString(1, patientVisitInformation.getVisitInformation());
                        	   String str=createVisitMedicienInfo(patientVisitInformation.getVisitMedicinList());
                           	   st.setString(2,str);
                        	   st.setString(3, patientVisitInformation.getVisitTitle());
                        	   st.setString(4, patientVisitInformation.getVisitStatus());
                        	   st.setString(5, patientVisitInformation.getVisitdueamount());
                        	   st.setString(6, patientVisitInformation.getVisitadvanceamount());
                        	   st.setString(7, patientVisitInformation.getVisittotalamount());
                        	   st.setString(8, patientVisitInformation.getPatientNextvisitdate());
                        	   st.setString(9, patientVisitInformation.getPatientAppointmentStartTime());
                        	   st.setString(10, patientVisitInformation.getVisitBp());
                        	   st.setString(11, patientVisitInformation.getVisitWeight());
                        	   st.setString(12, patientVisitInformation.getVisitDiagonastic());
                        	   st.setString(13, patientVisitInformation.getVisitId());
                        	  
                        	   st.setString(14, subscription);
                        	   
                        	   st.executeUpdate();
                          
                        	  
                           }
                           
                           catch(Exception e) 
                           {
                        	   System.out.println(e);
                           }
                       }
                          String updateappointment =  "Update patient_appointment_table set "
                             		+ "appointment_date=?,appointment_start_time=?,appointment_end_time=?"
                             		+ ",appointment_title=? where appointment_visit_time='" + patientVisitInformation.getVisitTime() + "'"
                             		+ " and patient_registration_id='" + patientVisitInformation.getRegistrationId() + "' and subscriptionid=?";
                             
                          try 
                             {
                          	   PreparedStatement st1=con.prepareStatement(updateappointment);
                          	   st1.setString(1, patientVisitInformation.getVisitDate());
                          	   st1.setString(2, patientVisitInformation.getPatientAppointmentStartTime());
                          	   st1.setString(3, patientVisitInformation.getPatientAppointmentEndTime());
                               st1.setString(4, patientVisitInformation.getVisitTitle());
                               
                               st1.setString(5, subscription);
                               
                               st1.executeUpdate();
                               
                               //System.out.println("patientVisitInformation.getVisitDate(): "+patientVisitInformation.getVisitDate());
                               //System.out.println("patientVisitInformation.getPatientAppointmentStartTime(): "+patientVisitInformation.getPatientAppointmentStartTime());
                               //System.out.println("patientVisitInformation.getPatientAppointmentEndTime() :"+patientVisitInformation.getPatientAppointmentEndTime());
                               //System.out.println("patientVisitInformation.getVisitTitle() :"+patientVisitInformation.getVisitTitle());
                               
                               
                             }
                          
                            catch(Exception e) 
                          	{
                            	System.out.println(e);
                            }
                           
                          for (int i = 0; i < patientVisitInformation.getVisitMedicinList().size(); i++)
                          {
                                 VisitMedicin visitMedicin = patientVisitInformation.getVisitMedicinList().get(i);
                                 PatientMedicinCalculation patientmedicincalculation = new PatientMedicinCalculation();
                                
                                 String totalCalculatedQuantity = patientmedicincalculation.CalculateTotalQuntity(visitMedicin);
                                 
                                 String aString=totalCalculatedQuantity;
                                 char a[]=aString.toCharArray();
                                 
                                 String b = "";
                                 int val;
                                 for (int j = 0; j < a.length; j++)
                                 {
                                     if (Character.isDigit(a[j]))
                                         b += a[j];
                                 }
                                 if (b.length() > 0)
                                     val = Integer.parseInt(b);
                                 String updatepatientmedicindetails = "Update patient_medicien_details set "
                                		+ "patient_medicien_details.medicien_name=?,patient_medicien_details.medicien_dos=?,"
                                		+ "patient_medicien_details.patient_medicien_power=?,patient_medicien_details.patient_medicien_type=?,"
                                		+ "patient_medicien_details.patient_medicien_quentity=?, patient_medicien_details.patient_medicien_day=?"
                                		+ ",patient_medicien_details.patient_medicien_totalquntity=?,patient_medicien_details.patient_medicien_ext_int_medicine=?"
                                	    + ",patient_medicien_details.patient_medicien_from_date=?,patient_medicien_details.patient_medicien_to_date=?,"
                                        + "patient_medicien_details.medicien_calculated_quantity=?  where patient_medicien_details.patient_visit_time=? "
                                        + "and patient_medicien_details.patient_reg_id=?  and subscriptionid=?";
                                try {
                                	PreparedStatement pst=con.prepareStatement(updatepatientmedicindetails);
                                	pst.setString(1, visitMedicin.getMedicinName());
                                	pst.setString(2, visitMedicin.getMedicinDos());
                                	pst.setString(3, visitMedicin.getMedicinpower());
                                	pst.setString(4, visitMedicin.getMedicintype());
                                	pst.setString(5, visitMedicin.getMedicinQuntity());
                                	pst.setString(6, visitMedicin.getMedicinday());
                                	pst.setString(7, visitMedicin.getMedicinTotalQuntity());
                                	//pst.setString(7, visitMedicin.getMedicinTotalCalculatedQuntity());
                                	pst.setString(8, visitMedicin.getVisitExternalInternal());
                                	pst.setString(9, visitMedicin.getVisitFromDate());
                                	pst.setString(10, visitMedicin.getVisitToDate());
                                	pst.setString(11, b);
                                	
                                	pst.setString(12,patientVisitInformation.getVisitTime());
                                	pst.setString(13, patientVisitInformation.getRegistrationId());
                                	
                                	pst.setString(14, subscription);
                                	
                                	pst.executeUpdate();
                                	
                                	System.out.println("patientVisitInformation.getRegistrationId(): "+patientVisitInformation.getRegistrationId());
                                	System.out.println("After last execute");
                                }
                                catch(Exception e) {
                                	System.out.println(e);
                                }
                          }
                         }
             catch (Exception e)
             {
            	 System.out.println(e);
             }
             return result;
         }

         //|||||||||37|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
  
         public String DeleteCurrentVisitInfo(String PatientVisitId, String DelVisitDetails, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (PatientVisitId.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
                         String DeleteVisitRow = "DELETE FROM patient_visit_details WHERE visit_id='" + PatientVisitId + "'"
                         		+ " and patient_reg_id='" + DelVisitDetails + "' and subscriptionid='"+subscription+"'";
                         
                         String deleteAppointment="delete FROM patient_appointment_table where patient_registration_id='" + DelVisitDetails + "' and subscriptionid='"+subscription+"' order by appointment_id desc limit 1";
                         
                         String medicineDetails="delete from patient_medicien_details where patient_reg_id='" + DelVisitDetails + "' and subscriptionid='"+subscription+"' order by id desc limit 1";
                         try {
                        	 PreparedStatement st=con.prepareStatement(DeleteVisitRow);
                        			 st.executeUpdate();
                        			 
                        	 PreparedStatement st1=con.prepareStatement(deleteAppointment);
                        			 st1.executeUpdate();
                        			 
                        	 PreparedStatement st2=con.prepareStatement(medicineDetails);
                        			 st2.executeUpdate();
                        			 
                         }
                         catch(Exception e) {
                        	 System.out.println(e);
                         }
            drServiceResponce.setStatusCode(200);
                 drServiceResponce.setMessage("Success");
                 drServiceResponce.setExceptionDetails(PatientVisitId);
             }
             return gson.toJson(drServiceResponce);
         }
         
         //||||||||||||38|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String GetPatientVisitInformationByRegistrationId(String patientRegId, String userid) 
         {
           // DatabaseConnection db=new DatabaseConnection();
           // Connection con=db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
            Gson gson = new GsonBuilder().serializeNulls().create();
            System.out.println("hey 38");
            ResultSet rs=null;
            ResultSet rs1=null;
           
            
            List<PatientVisitInformation> patientVisitList = new ArrayList<PatientVisitInformation>();
            if(patientRegId.isEmpty())
            {
                PatientVisitInformation visitInfo = new PatientVisitInformation();
                visitInfo.setRegistrationId("Registration Id Empty.");
                patientVisitList.add(visitInfo);
                return gson.toJson(patientVisitList);
             }
             else
             {
                     //  String multipleQuery = "Select visit_id, patient_visit_date,patient_visit_time,patient_visit_information,"
                        		//+ "	patient_visit_medicien_information,patient_visit_title,patient_visit_due_amount, patient_visit_advance_amount,"
                        	//	+ "	patient_visit_total_amount,patient_next_visit_date,patient_visit_image,patient_visit_image_name,patient_visit_image_path,"
                        	//	+ "	patient_visit_bp, patient_visit_weight, patient_visit_diagnostic"
                        	//	+ "	from patient_visit_details where patient_reg_id = '" + patientRegId + "' and patient_visit_isdelete=0 order "
                        	//	+ "by patient_visit_date desc";
                       
                       
                       
                       String multipleQuery = "Select patient_visit_details.visit_id, patient_visit_details.patient_visit_date,patient_visit_details.patient_visit_time,patient_visit_details.patient_visit_information,"
                       		+ "	patient_visit_details.patient_visit_medicien_information,patient_visit_details.patient_visit_title,patient_visit_details.patient_visit_due_amount, patient_visit_details.patient_visit_advance_amount,"
                       		+ "	patient_visit_details.patient_visit_total_amount,patient_visit_details.patient_next_visit_date,patient_visit_details.patient_visit_image,patient_visit_details.patient_visit_image_name,patient_visit_details.patient_visit_image_path,"
                       		+ "	patient_visit_details.patient_visit_bp, patient_visit_details.patient_visit_weight, patient_visit_details.patient_visit_diagnostic, patient_information_master.patient_first_name,patient_information_master.patient_last_name,patient_information_master.patient_age,patient_information_master.patient_gender,patient_information_master.patient_date_of_birth"
                       		+ "	from patient_visit_details,patient_information_master  where patient_visit_details.patient_reg_id = '" + patientRegId + "' and patient_visit_details.subscriptionid='"+subscription+"' and patient_information_master.patient_reg_id='" + patientRegId +"' and patient_information_master.subscriptionid='"+subscription+"' and  patient_visit_details.patient_visit_isdelete=0 order "
                       		+ "by  patient_visit_details.patient_visit_date desc";
                       
                       
                       
                       
                        String queryMedicien = "Select patient_visit_date,patient_visit_time,medicien_name, medicien_dos,"
                        					+ "patient_medicien_power,patient_medicien_type,patient_medicien_quentity,patient_medicien_day,"
                        					+ "patient_medicien_ext_int_medicine,patient_medicien_totalquntity, patient_medicien_from_date,"
                        					+ " patient_medicien_to_date from patient_medicien_details "
                        					+ "where patient_reg_id = '" + patientRegId + "' and subscriptionid='"+subscription+"' order by patient_visit_date and "
                        					+ "patient_visit_time desc";
                        
                        
                        
                        
                       // String patientdetails="select patient_first_name,patient_last_name,patient_age from patient_information_master where patient_reg_id='" + PatientRegID +"'";
                    	//Statement sta=con.createStatement();
                      //  rsa=sta.executeQuery(patientdetails);
                                              
                        try 
                                              
                        {
                        		Statement st=con.createStatement();
                        		rs=st.executeQuery(multipleQuery);
                        		
                        
                           	Statement st1=con.createStatement();
                        	 rs1=st1.executeQuery(queryMedicien);
                      
                         
                                               
                        }
                        catch(Exception e)
                                               
                        {
                                System.out.println(e);
                                               
                        }
    }
				return gson.toJson(ProcessVisitInformationDataTable(rs, rs1));
			
                 }
           
         //|||||||||||||39||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         
         public String RepeatMedicineByRegistrationId(String patientRegId, String visitid, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (patientRegId.isEmpty() == true)
             {
                 new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "patientRegId parameter is null", "");
             }
             else
             {
                  List<VisitMedicin> visitList = new ArrayList<VisitMedicin>();
                // String selectMedicien = "Select id, patient_visit_date,patient_visit_time,medicien_name, medicien_dos,patient_medicien_power,patient_medicien_type,patient_medicien_quentity,patient_medicien_day,patient_medicien_totalquntity,patient_medicien_ext_int_medicine, patient_medicien_from_date, patient_medicien_to_date from patient_medicien_details  where (patient_visit_date,patient_visit_time) in (select patient_visit_date,patient_visit_time from  patient_visit_details where visit_id in  (select visit_id  from patient_visit_details where patient_reg_id = '" + patientRegId + "' and visit_id = '" + visitid + "' )) and  patient_reg_id ='" + patientRegId + "'";
                 
                  String selectvisitdtatetime="select patient_visit_date,patient_visit_time, patient_visit_title, patient_visit_information  from  patient_visit_details where visit_id ='"+visitid+"' and patient_reg_id='"+patientRegId+"' and subscriptionid='"+subscription+"'";
                  		
                 
                  String date="";
                  String time="";
                  String title ="";
                  String info ="";
                  try
                  {
                	  Statement sta=con.createStatement();
                	  ResultSet rsa=sta.executeQuery(selectvisitdtatetime);
                  
                	  if(rsa.next())
                	  {
                		  date=rsa.getString(1);
                		  time=rsa.getString(2);
                		  title=rsa.getString(3);
                		  info=rsa.getString(4);
                	  }
                  
                  }
                  catch(Exception e)
                  {
                	  System.out.println(e);
                  }
                
                  String selectMedicien = "Select id, patient_visit_date,patient_visit_time,medicien_name, medicien_dos,patient_medicien_power,"
                  		+ "patient_medicien_type,patient_medicien_quentity,patient_medicien_day,patient_medicien_totalquntity,"
                  		+ "patient_medicien_ext_int_medicine, patient_medicien_from_date, patient_medicien_to_date from patient_medicien_details  "
                  		+ "where patient_visit_date='"+date+"' and patient_visit_time='"+time+"'  and  patient_reg_id ='" + patientRegId + "' and subscriptionid='"+subscription+"'";
                  
                
                  
                  try {
                         			Statement st=con.createStatement();
                         			ResultSet rs=st.executeQuery(selectMedicien);
                         			
                         			while(rs.next())
                         			{
                         				 
                         				VisitMedicin medicienInfo = new VisitMedicin();
                         				
                         				medicienInfo.setMedicinName(rs.getString(4));
                         				
                         				medicienInfo.setMedicinDos(rs.getString(5));
                         				medicienInfo.setMedicinpower(rs.getString(6));
                         				medicienInfo.setMedicintype( rs.getString(7));
                         				medicienInfo.setMedicinQuntity(rs.getString(8));
                         				medicienInfo.setMedicinday(rs.getString(9));
                         				
                         				medicienInfo.setVisitExternalInternal(rs.getString(11));
                         				medicienInfo.setVisitFromDate(rs.getString(12));
                         				medicienInfo.setVisitToDate(rs.getString(13));
                         				visitList.add(medicienInfo);
                         				
                         				System.out.println(visitList.get(0).getMedicinName());
                         			}
                         		}
                         		catch(Exception e)
                         		{
                         			System.out.println(e);
                         		}
                  
                  
                  //Start Ashis Code For Patient Visit Information and Title 08-05-18
                  
                
                  List<PatientVisitInformation> ptlist =new  ArrayList<PatientVisitInformation>();
                  PatientVisitInformation ptinfo =new PatientVisitInformation();
                  
                  ptinfo.setVisitTitle(title);
                  ptinfo.setVisitInformation(info);
                  ptlist.add(ptinfo);
                  drServiceResponce.setPatientsVisitInformationDetails(ptlist);
                
                  //End
                   
                         drServiceResponce.setPatientsVisitmedicin(new ArrayList<VisitMedicin>());
                         drServiceResponce.setPatientsVisitmedicin(visitList);
                drServiceResponce.setStatusCode (200);
                 drServiceResponce.setMessage("Success");
                 drServiceResponce.setExceptionDetails(patientRegId);
                 return gson.toJson(drServiceResponce);
             }
             return gson.toJson(drServiceResponce);
         }
       
         //|||||||||||||40||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public List<String> GetMedicinList()
         
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con=db.getConnection();
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 List<String> lstMedicienList = new ArrayList<String>();
                     String selectMedicien = "Select medicien_name,medicien_power from medicien_list";
                     try {
                    	 Statement st=con.createStatement();
                    	 ResultSet rs=st.executeQuery(selectMedicien);
                    	 int res=0;
                    	 while(rs.next()) {
                    		 lstMedicienList.add(rs.getString(1));
                    		 lstMedicienList.add(rs.getString(2));
                    		  res=rs.getRow();
                    	 }
                    	
                    	 }
                     catch(Exception e) {
                    	 System.out.println(e);
                     }
                 return lstMedicienList;
         }
     //|||||||||||||41||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         
         public String DeletePatientVisitById(String VisitID, String userid)
         {
            
        	// DatabaseConnection db=new DatabaseConnection();
        	 //Connection con =db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (VisitID.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
                
                         String DeleteVisitInfo = "update patient_visit_details set patient_visit_isdelete = 1 "
                         	+ "where visit_id ='" + VisitID + "' and subscriptionid='"+subscription+"'";
                         try {
                        	 PreparedStatement st=con.prepareStatement(DeleteVisitInfo);
                        	 st.executeUpdate();
                         }
                         catch(Exception e) {
                        	 System.out.println(e);
                         }
             }
                 drServiceResponce.setStatusCode(200);
                 drServiceResponce.setMessage("Success");
                 drServiceResponce.setExceptionDetails(VisitID);
             
             return gson.toJson(drServiceResponce);
         }
         //||||||||||||||42|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String DeletePatientInfoById(String PatientRegID, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
             DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (PatientRegID.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
                         String DeletePatientInfo = "update patient_information_master set patient_isDelete = 1"
                         		+ " where patient_reg_id ='" + PatientRegID + "' and subscriptionid='"+subscription+"'";
                      try {  
                    	  PreparedStatement st=con.prepareStatement(DeletePatientInfo);
                         st.executeUpdate();
                      }
                      catch(Exception e) {
                    	  System.out.print(e);
                      }
                    }
                 drServiceResponce.setStatusCode(200);
                 drServiceResponce.setMessage("Success");
                 drServiceResponce.setExceptionDetails(PatientRegID);
             return gson.toJson(drServiceResponce);
         }
    //|||||||||||||||43||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String SuspendPatientInfoById(String PatientRegID, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
    	 
    	 	 String subscription = getSubscriptionId(userid);
    	 	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (PatientRegID.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
                String DeletePatientInfo = "update patient_information_master set patient_issuspend = 1 where"
                         				 + " patient_reg_id ='" + PatientRegID + "'  and subscriptionid='"+subscription+"'";
                try 
                { 
                  PreparedStatement st=con.prepareStatement(DeletePatientInfo);
                  st.executeUpdate();
                }
                catch(Exception e)
                {
                  System.out.print(e);
                }
                     
                }
             drServiceResponce.setStatusCode(200);
             drServiceResponce.setMessage("Success");
             drServiceResponce.setExceptionDetails(PatientRegID);
                 return gson.toJson(drServiceResponce);
         }
  //||||||||||||||||44|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
  public String ActivePatientInfoById(String PatientRegID, String userid)
         {
        	 //DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
	  
	  		String subscription = getSubscriptionId(userid);
	  
        	 Gson gson = new GsonBuilder().serializeNulls().create();
             DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (PatientRegID.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
                         String DeletePatientInfo = "update patient_information_master set patient_issuspend = 0 where "
                         		+ "patient_reg_id ='" + PatientRegID + "' and subscriptionid='"+subscription+"'";
                         try {   
                        	 PreparedStatement st=con.prepareStatement(DeletePatientInfo);
                        	 st.executeUpdate();
                      }
                      catch(Exception e) {
                    	  System.out.print(e);
                      }
                                      
                 }
             drServiceResponce.setStatusCode(200);
             drServiceResponce.setMessage("Success");
             drServiceResponce.setExceptionDetails(PatientRegID);
             
             return gson.toJson(drServiceResponce);
         }
       //|||||||||||||||||45||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
 public String UpdatePatientVisitInformation(String PatientVisitId, String newVisitInfo, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	 //Connection con =db.getConnection();
	 
	 		String subscription = getSubscriptionId(userid);
	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
            
             if (PatientVisitId.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
                 String visitTitle = "";
                 
                         String UpdatePatientInfo = "update patient_visit_details set patient_visit_information ='" + newVisitInfo + "'," +
                         "patient_visit_title='" + visitTitle+ "' where visit_id =" + Integer.parseInt(PatientVisitId) +" and subscriptionid='"+subscription+"'";
                         
                         try {  
                        	 PreparedStatement st=con.prepareStatement(UpdatePatientInfo);
                         st.executeUpdate();
                      }
                      catch(Exception e) {
                    	  System.out.print(e);
                      }
                 }

             drServiceResponce.setStatusCode(200);
             drServiceResponce.setMessage("Success");
             drServiceResponce.setExceptionDetails(PatientVisitId);
              
             
             return gson.toJson(drServiceResponce);
         }
//||||||||||||||||46|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
public String AddMedicinesData(String medicinename)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection(); 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (medicinename.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
            	
                  String AddMedicinesDataList = "insert into medicien_list(medicien_list.medicien_name,medicien_list.medicien_isdelete) "
                         		+ "values(?,1)";
                         try {
                        	 PreparedStatement st=con.prepareStatement(AddMedicinesDataList);
                        	 st.setString(1, medicinename);
                        	
                        	 st.executeUpdate();
                         }
                         catch(Exception e) {
                        	 System.out.println(e);
                         }
                 }

             drServiceResponce.setStatusCode(200);
             drServiceResponce.setMessage("Success");
            
             return gson.toJson(drServiceResponce);
         }
       //|||||||||||||||47||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String UpdatePatientEditTotalQuntity(String PatientVisitId, String newMedicinequntity, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
        	 
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();


             if (PatientVisitId.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
               {
                         String UpdatePatientInfo = "update patient_medicien_details set"
                         		+ " patient_medicien_totalquntity ='" + newMedicinequntity + "' where "
                         				+ "id =" + Integer.parseInt(PatientVisitId) +" and subscriptionid='"+subscription+"'";
                        
                         try {  
                        	 PreparedStatement st=con.prepareStatement(UpdatePatientInfo);
                         st.executeUpdate();
                      }
                      catch(Exception e) {
                    	  System.out.print(e);
                      }
                 }


               drServiceResponce.setStatusCode(200);
               drServiceResponce.setMessage("Success");
               drServiceResponce.setExceptionDetails(PatientVisitId);
             
             return gson.toJson(drServiceResponce);
         }
         
         }
         
         //|||||||||||||||||48||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String UpdatePatientVisitInformationAndAmount(String visit_id, String totalAmount, String paydAmount, String ConcessionAmount,String PaymentMode, String userid)
         {
        	
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 float tempTotalamount = 0;
             float tempPaydAmount = 0;
             float Advanceamount = 0;
             float DueAmount = 0;
             String tempconcession = ConcessionAmount;
             String payment=PaymentMode;
              
             DrServiceResponce drServiceResponce = new DrServiceResponce();
             if (visit_id.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else if (totalAmount!=null &&  paydAmount!= null)
             {
            	 tempTotalamount=Float.parseFloat(totalAmount);
            	 tempPaydAmount=Float.parseFloat(paydAmount);
            	
                 String result = null;


                 if (tempTotalamount == tempPaydAmount)
                 {
                     Advanceamount = 0;
                     DueAmount = 0;
                 }
                 else if (tempTotalamount > tempPaydAmount)
                 {
                     DueAmount = tempTotalamount - tempPaydAmount;
                     Advanceamount = 0;
                 }
                 else if (tempTotalamount < tempPaydAmount)
                 {
                     Advanceamount = tempPaydAmount - tempTotalamount;
                     DueAmount = 0;
                 }
                 String visitdetails = "Select visit_id from patient_visit_details where subscriptionid='"+subscription+"' order by visit_id desc limit 1";
                           
                      try {
                            	Statement st1=con.createStatement();
                            	ResultSet rs=st1.executeQuery(visitdetails);
                            	
                            	
                            	rs.next();
                            	result=rs.getString(1);
                            	
                            }
                            catch(Exception e) {
                            	System.out.println(e);
                            }
                          
                             String UpdatePatientInfo = "update patient_visit_details set patient_visit_due_amount ='" + DueAmount + "'," +
                                                         "patient_visit_advance_amount='" + Advanceamount + "',patient_visit_total_amount='" + tempTotalamount + "',"
                           	                             + "patient_visit_paid_amount='" + paydAmount + "'"
                          			                     + ",patient_visit_fee_concession='" + ConcessionAmount + "' ,payment_mode='"+payment+"' where"
                           				                 + " visit_id ='" + Integer.parseInt(result) + "' and patient_reg_id = '" + visit_id + "' and subscriptionid='"+subscription+"'";
                             
                             try {  
                            	 PreparedStatement st=con.prepareStatement(UpdatePatientInfo);
                            	
                            	 st.executeUpdate();
                          }
                          catch(Exception e) {
                        	  System.out.print(e);
                          }
                     }

                     drServiceResponce.setStatusCode(200);
                     drServiceResponce.setMessage("Success");
     return gson.toJson(drServiceResponce);
         }
    //||||||||||||||||||49|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public String GetpatientvisitPayment(String PatientRegID, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	 //Connection con =db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 ResultSet rs=null;
        	 ResultSet rs1=null;
        	 ResultSet rs2=null;
        	 ResultSet rs3=null;
        	 ResultSet rsa=null;

        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
        	  drServiceResponce.setPatientsVisitmedicin(new ArrayList<VisitMedicin>());
        	  
             PaymentDetails paymentDetails = new PaymentDetails();
             
             String tempcurrentDate = new SimpleDateFormat("{yyyy-MM-dd}").format(new Date());
             DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
             Date currentdate = new Date();
           
             if (PatientRegID.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
            	 PatientVisitInformation visitDetails = new PatientVisitInformation();
                     
                         String GetLastTwoRecord = "select visit_id, patient_visit_date,patient_visit_time,patient_visit_bp,"
                         		+ "patient_visit_weight,patient_visit_diagnostic,patient_next_visit_date,patient_visit_due_amount,"
                         		+ "patient_visit_advance_amount,patient_visit_total_amount,patient_visit_paid_amount FROM "
                         		+ "patient_visit_details where patient_reg_id = '" + PatientRegID + "' and subscriptionid='"+subscription+"'  order by visit_id desc limit 2";
                         	int count=0;
                        try
                        {
                        	Statement st=con.createStatement();
                        	rs=st.executeQuery(GetLastTwoRecord);
                        	while(rs.next())
                        	{
                        		count=rs.getRow();

                        	}
                    		
                        	
                        	//Ashish code start for title
                        	
                        	String selectTitle="select appointment_title FROM patient_appointment_table where patient_registration_id='" + PatientRegID + "' and subscriptionid='"+subscription+"' order by appointment_id desc limit 1";
                        	Statement stTitle=con.createStatement();
                        	ResultSet rsTitle=stTitle.executeQuery(selectTitle);
                        	
                        	if(rsTitle.next())
                        	{
                        		visitDetails.setVisitTitle(rsTitle.getString(1));
                        	}
                        	
                        	//End Ashish code
                        	
                        	
                        	
                        	//Ashish Code Start
                        	
                        	String patientdetails="select patient_first_name,patient_last_name,patient_age ,patient_gender ,patient_date_of_birth,patient_phone_number from patient_information_master where patient_reg_id='" + PatientRegID +"'  and subscriptionid='"+subscription+"'";
                        	Statement sta=con.createStatement();
                            rsa=sta.executeQuery(patientdetails);
                        	
                            String nextdate="";
                    		String visitdate="";
                    		String name="";
                    		String age="";
                    		String gender="";
                    		String mobile="";
                    		
                    		
                    		rsa.next();
            				name=rsa.getString(1)+" "+rsa.getString(2);
            				
            				gender=rsa.getString(4);
            				
            				mobile = rsa.getString(6);
            				visitDetails.setMobileNumber(mobile);
            				
            		//Start DOB Calculatio by ASHISH
            				
            				String input=rsa.getString(5);
            				
            				
            	              String todayage = "";
            	              //String input="1990-11-01";
            	              boolean checkformat1= (input.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})"));
            	             
            	              boolean checkformat2= (input.matches("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})"));
            	              
            	              System.out.println("checkformat: "+checkformat1 +" checkformat2: "+checkformat2);
            	              if(checkformat1 == true)
            	              {
            	            	  DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            	                  Date date= df.parse(input);
            	                  
            	                // String formatteddate = df.format(date);
            	                 int dobyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)).getYear();
            	                 int dobmonth = date.getMonth()+1;
            	                 
            	                 
            	                 
            	                 Date currentdate1 = new Date();
            	            	 
            	            	 int currentmonth = currentdate1.getMonth()+1;
            	            	 int currentyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(currentdate1)).getYear();
            	            	 
            	            	 
            	            	 
            	            	 if(currentmonth < dobmonth)
            	            	 {
            	            		 currentmonth = currentmonth+12;
            	            		 currentyear = currentyear-1;
            	            	 }
            	            	 
            	            	 int year = currentyear - dobyear;
            	            	 int month = currentmonth- dobmonth;
            	            	 
            	            	
            	            	 todayage =String.valueOf(year) +" Y "+month +" M";
            	            	 age=todayage;
            	              }
            	              else if(checkformat2 == true)
            	              {
            	            	  DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            	                  Date date= df.parse(input);
            	                  
            	                // String formatteddate = df.format(date);
            	                 int dobyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)).getYear();
            	                 int dobmonth = date.getMonth()+1;
            	                 
            	                
            	                 
            	                 Date currentdate1 = new Date();
            	            	 
            	            	 int currentmonth = currentdate1.getMonth()+1;
            	            	 int currentyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(currentdate1)).getYear();
            	            	 
            	            	 
            	            	 
            	            	 if(currentmonth < dobmonth)
            	            	 {
            	            		 currentmonth = currentmonth+12;
            	            		 currentyear = currentyear-1;
            	            	 }
            	            	 
            	            	 int year = currentyear - dobyear;
            	            	 int month = currentmonth- dobmonth;
            	            	 
            	            	//String  newage = String.valueOf(year +" "+month);
            	            	
            	            	 todayage =String.valueOf(year) +" Y "+month +" M";
            	            	 age=todayage;
            	              }
            	              else
            	              {
            	            	  
            	                   if(rsa.getString(3)==null || rsa.getString(3).equals("null"))
            	                   {
            	                	   age = "";  
            	                   }
            	                   else
            	                   {
            	                	   age = rsa.getString(3);
            	                   }
            	            	  
            	              }
            	              
            	            System.out.println("todayage: "+todayage);
            	             
            	              
                       // End
            	              
            				System.out.println("age : "+age);
                        	if(count==2)
                        	{ 
                        		
                        		for(int i=0;i<count;i++)
                        		{
                        			
                        			if(i==0)
                        			{
                        				rs.beforeFirst();
                        				
                        				rs.next();
                        				
                        				nextdate=rs.getString(7);
                        				visitdate=rs.getString(2);
                        				
                        				//System.out.println("nextdate: "+nextdate);
                        					visitDetails.setVisitId(rs.getString(1));
                        					if(rs.getString(2)=="")
                        					{
                        						visitDetails.setVisitDate(rs.getString(2));
                        					}
                        					else 
                        					{
                        						visitDetails.setVisitDate(rs.getString(2));
                        					}
                        					
                        		 visitDetails.setVisitTime(rs.getString(3));
                        		if(rs.getString(7)=="")
                        		{
                        			  visitDetails.setPatientNextvisitdate(nextdate);
                        		}
                        		else
                        		{
                        			visitDetails.setPatientNextvisitdate(nextdate);
                        		}
                        		
                        		 visitDetails.setVisitBp(rs.getString(4));
                                 visitDetails.setVisitWeight( rs.getString(5));
                                 visitDetails.setVisitDiagonastic(rs.getString(6));
                                 paymentDetails.setPatientVisitAmount(rs.getString(10));
                                 paymentDetails.setPatientVisitPaidAmount(rs.getString(11));
                        	     
                                 visitDetails.setPatientVisitName(name);
                                 visitDetails.setPatientAge(age);
                                 visitDetails.setPatientGender(gender);
                                 
                        				
                        		}
                        		else
                        			{
                        			
                        			// rs.next();
                        			 paymentDetails.setAdvancedAmount(rs.getString(9));
                        			 paymentDetails.setDueAmount(rs.getString(8));
                        			 if (rs.getString(7)=="" || rs.getString(7)==null)
                                     {
                                         visitDetails.setPatientNextvisitdate("");
                                     }
                                     else
                                     {
                                         visitDetails.setPatientNextvisitdate(nextdate);
                                     }
                        			 
                        			 if(rs.getString(2)=="")
                 					{
                 						visitDetails.setVisitDate(visitdate);
                 					}
                 					else 
                 					{
                 						visitDetails.setVisitDate(visitdate);
                 					}
                        			 //System.out.println("weight2: "+rs.getString(5));
                        			 visitDetails.setVisitBp(rs.getString(4));
                                     visitDetails.setVisitWeight(rs.getString(5));
                                     visitDetails.setVisitDiagonastic(rs.getString(6));
                                     paymentDetails.setConsultancyCharge("0");
                                     paymentDetails.setNewRegistrationCharge("0");
                                     paymentDetails.setRe_RegistrationCharge("0");
                                     
                                     visitDetails.setPatientVisitName(name);
                                     visitDetails.setPatientAge(age);
                                     visitDetails.setPatientGender(gender);
                        		}
                        	}
                        		
                        	}
                        	else if(count==1)
                        	{
                        		rs.beforeFirst();      
                        		rs.next();
                        		
                                for (int i = 0; i < count; i++)
                                {
                                    if (i == 0)
                                    {   
                                    	visitDetails.setVisitId(rs.getString(1));
                                        if (rs.getString(2)=="")
                                        {
                                            visitDetails.setVisitDate(rs.getString(2));
                                        }
                                        else
                                        {
                                            visitDetails.setVisitDate(rs.getString(2));
                                        }
                                        visitDetails.setVisitTime(rs.getString(3));
                                        visitDetails.setPatientNextvisitdate(rs.getString(7));
                                        visitDetails.setVisitBp(rs.getString(4));
                                        visitDetails.setVisitWeight(rs.getString(5));
                                        visitDetails.setVisitDiagonastic(rs.getString(6));
                                        
                                        visitDetails.setPatientVisitName(name);
                                        visitDetails.setPatientAge(age);
                                        visitDetails.setPatientGender(gender);
                                       
                                    }
                                }
                        	}
                        	 }
                        	catch(Exception e)
                        	{
                        		System.out.println(e);
                        	}
                        System.out.println(visitDetails.getVisitTime());
                      String GetPatientInfo = "Select id, patient_visit_date,patient_visit_time,medicien_name, medicien_dos,"
                         		+ "patient_medicien_power,patient_medicien_type,patient_medicien_quentity,patient_medicien_day,"
                         		+ "patient_medicien_totalquntity, patient_medicien_ext_int_medicine, patient_medicien_from_date,"
                         		+ "patient_medicien_to_date from patient_medicien_details where"
                         		+ " patient_reg_id = '" + PatientRegID + "' and  patient_visit_time =  '" + visitDetails.getVisitTime() + "'  and subscriptionid='"+subscription+"'";
                      
                         try 	
                         {
                         	Statement st1=con.createStatement();
                         	 rs1=st1.executeQuery(GetPatientInfo);
                         
                         }
                         catch(Exception e)
                         {
                         	System.out.println(e);
                         }
                         drServiceResponce.setPatientVisitDetails (visitDetails);
                         String GetPatientInfo1 = "Select visit_id, patient_visit_date,patient_next_visit_date,patient_visit_time from "
                         							+ "patient_visit_details where patient_reg_id ='" + PatientRegID + "'"
                         				            + " and subscriptionid='"+subscription+"' order by visit_id desc limit 1 offset 1";
                         
                         int count2=0;
                         try 
                         {
                         	Statement st2=con.createStatement();
                         	 rs2=st2.executeQuery(GetPatientInfo1);
                         	while(rs2.next())
                         	{
                         	count2=rs2.getRow();
                         	}
                         	
                         	if (count2 > 0)
                             {
                         		rs2.beforeFirst();       
                         		rs2.next();
                         			String PatientVisitdate1 =rs2.getString(2);
                         			DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
                         			Date PatientVisitdate=null;
                            	 try
                            	 	{
                            		 PatientVisitdate = df.parse(PatientVisitdate1);
                            		   
                            		} 
                            	 
                            	 	catch (ParseException e) 
                            	 	{
                            		    e.printStackTrace();
                            		}
                            	    
                            	 	String PatientVisitTime = rs2.getString(4);
                            	    
                            	    String[] formats = { "d-M-yyyy", "d-MMM-yy" };
                            	    
                            	    long diff = Math.abs(PatientVisitdate.getTime()-currentdate.getTime());
                            	    float differenceInDays = (diff / (1000*60*60*24));
                            	    if (differenceInDays >= 1 && differenceInDays <=5)
                            	    {
                            	    	if (CheckMultileVisitdate(PatientRegID, PatientVisitdate, userid) == true)
                            	    	{

                            	    		paymentDetails.setConsultancyCharge(Constant.ConsultancyCharge);
                            	    		paymentDetails.setRe_RegistrationCharge("0");
                                       
                            	    	}
                                    else
                                    	{
                                        	paymentDetails.setConsultancyCharge("0");
                                        	paymentDetails.setNewRegistrationCharge("0");
                                        	paymentDetails.setRe_RegistrationCharge("0");
                                    	}
                            	    }
                                else if (differenceInDays > 5 && differenceInDays < 90)
                                {
                                    paymentDetails.setConsultancyCharge(Constant.ConsultancyCharge);
                                    paymentDetails.setRe_RegistrationCharge("0");
                                }
                                else if (differenceInDays >= 90)
                                {
                                   
                                    paymentDetails.setConsultancyCharge(Constant.ConsultancyCharge);
                                    paymentDetails.setRe_RegistrationCharge(Constant.Patient_Re_RegistrationCharge);
                                }
                            
                         	}
                         }
                         catch(Exception e) 
                         {
                         	System.out.println(e);
                         }
             		}
             				String GetfirestRegistrationid = "SELECT visit_id FROM patient_visit_details"
                         		+ " where patient_reg_id ='" + PatientRegID + "' and subscriptionid='"+subscription+"'";
             					int count4=0;
             					try 
             					{
                          		Statement st3=con.createStatement();
                          		 rs3=st3.executeQuery(GetfirestRegistrationid);
                          		while(rs3.next())
                          		{
                          		count4=rs3.getRow();
                          	
                          		}
                          		rs3.beforeFirst();        
                          			
                          			if (count4 == 1)
                          			{
                                     paymentDetails.setRe_RegistrationCharge(Constant.Patient_Re_RegistrationCharge);
                                     paymentDetails.setConsultancyCharge(Constant.ConsultancyCharge);
                                     paymentDetails.setAdvancedAmount(rs.getString(9));
                                     paymentDetails.setDueAmount(rs.getString(8));

                          			}
                          	}
                          catch(Exception e)
                          {
                          	System.out.println("Exception e");
                          }
             					
                        drServiceResponce.PatientVisitDetails.setVisitMedicinList(ProcessVisitmedicineDataTable(rs1));//.setPatientsVisitmedicin(ProcessVisitmedicineDataTable(rs1));//PatientVisitDetails.setVisitMedicinList(ProcessVisitmedicineDataTable(rs1));

                        drServiceResponce.setStatusCode(200);
                        drServiceResponce.setMessage("Success");
                        drServiceResponce.setExceptionDetails(PatientRegID);
                        drServiceResponce.PatientVisitDetails.setPaymentDetails(paymentDetails);
                        
                        // drServiceResponce.setPatientVisitDetails()(patientVisitDetails);.setPatientPaymentVisitDetails(paymentDetails);//setPatientVisitDetails.setPaymentDetails(paymentDetails);
                         return gson.toJson(drServiceResponce);
                 }
 //||||||||||||||||||||50|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public boolean CheckMultileVisitdate(String PatientRegID, Date SecondVisitDate, String userid)
         {
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 String PatientVisitdate1;
             long differenceInDays=0;
            
                         String CounttotalVisits = "Select visit_id, patient_visit_date,patient_next_visit_date,patient_visit_time"
                         							+ " from patient_visit_details where patient_reg_id ='" + PatientRegID + "'"
                         						    + " and subscriptionid='"+subscription+"' order by visit_id desc limit 1 offset 2";
                         try {
                        	 Statement st=con.createStatement();
                           	ResultSet rs=st.executeQuery(CounttotalVisits);
                           	while(rs.next()) {
                           		rs.getString(1);
                           	 PatientVisitdate1=rs.getString(2);
                           	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                           	Date PatientVisitdate = df.parse(PatientVisitdate1);
                           	 rs.getString(3);
                           		rs.getString(4);
                           		long diff = PatientVisitdate.getTime()-SecondVisitDate.getTime()  ;
                           		differenceInDays= (diff / (1000*60*60*24));
                           		System.out.println(differenceInDays);
                          
                           	}
                           }
                           catch(Exception e) {
                           	System.out.println(e);
                           }
            if (differenceInDays>=5)
                         {
                             return true; 
                         }
                         else
                         {
                             return false;  
                         }
                     }
//||||||||||||||||||||51|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
public String GetPatientVisitTotalPages(String PatientRegID, String userid)
         {
        	 //DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
	
	
			String subscription = getSubscriptionId(userid);
	
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
        	 PatientVisitInformation visitDetails = new PatientVisitInformation();
             List<PatientVisitInformation> visitList = new ArrayList<PatientVisitInformation>();
             if (PatientRegID.isEmpty() == true)
             {
                 return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
             }
             else
             {
                         String CounttotalVisits = "select count(*) as TotalVisit from "
                         		+ "patient_visit_details where patient_reg_id ='" + PatientRegID + "' and subscriptionid='"+subscription+"'";
                        
                         try {
                        	 Statement st=con.createStatement();
                           	ResultSet rs=st.executeQuery(CounttotalVisits);
                           	while(rs.next()) {
                           	
                           	 visitDetails.setTotalNoOfVisit(rs.getString(1));
                           		                   		
                           	visitList.add(visitDetails);
                           	}
                           }
                           catch(Exception e) {
                           	System.out.println(e);
                           }
                       
                         drServiceResponce.setPatientsVisitInformationDetails (visitList);
                     }
             drServiceResponce.setStatusCode(200);
             drServiceResponce.setMessage("Success");
             drServiceResponce.setExceptionDetails(PatientRegID);
             
             return gson.toJson(drServiceResponce);
         }
 //|||||||||||||||||||52||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
private List<VisitMedicin> ProcessVisitmedicineDataTable(ResultSet dataTable) 
         {
	Gson gson = new GsonBuilder().serializeNulls().create();
             List<VisitMedicin> visitList = new ArrayList<VisitMedicin>();
             if (dataTable != null )//&& dataTable.size() > 0)
             {
                 //for (int i=0;i<dataTable.size();i++)
                try
            	 {
            	 dataTable.beforeFirst();
                	 while(dataTable.next())
                	  {
                     VisitMedicin visitmedicineDetails = new VisitMedicin();
                     visitmedicineDetails.setMedicinName(dataTable.getString(4));//.setMedicienName(dataTable.getString(4));
                     visitmedicineDetails.setMedicinId(dataTable.getString(1));//.setMedicienId(dataTable.getString(1));
                     visitmedicineDetails.setMedicinpower(dataTable.getString(6));//.setMedicienpower = dataTable.getString(6);
                     visitmedicineDetails.setMedicinQuntity(dataTable.getString(8));//.MedicienQuntity = dataTable.getString(8);
                     visitmedicineDetails.setMedicinDos(dataTable.getString(5));//.MedicienDos =dataTable.getString(5);
                     visitmedicineDetails.setMedicintype(dataTable.getString(7));//.Medicientype = dataTable.getString(7);
                     visitmedicineDetails.setMedicinday(dataTable.getString(9));//.Medicienday = dataTable.getString(9);
                     visitmedicineDetails.setMedicinTotalQuntity(dataTable.getString(10));//.MedicienTotalQuntity = dataTable.getString(10);
                     visitmedicineDetails.setVisitExternalInternal(dataTable.getString(11));//.VisitExternalInternal = dataTable.getString(11);
                     visitmedicineDetails.setVisitFromDate(dataTable.getString(12));//.VisitFromDate =dataTable.getString(12);
                     visitmedicineDetails.setVisitToDate(dataTable.getString(13));//.VisitToDate = dataTable.getString(13);
                     String visitTime = dataTable.getString(3);
                     visitList.add(visitmedicineDetails);
                     
                     System.out.println("day: "+dataTable.getString(9));
                	 }
                	
                	 }
                catch(Exception e) {
                	System.out.println(e);
                }
                }
             
             return visitList;
         }
//||||||||||||||||||53|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
public String LoginUser(String userName, String userPassword)
{
	
	 
	 
	 Gson gson = new GsonBuilder().serializeNulls().create();
	 
	 //PatientVisitInformation visitDetails = new PatientVisitInformation();
	 DrServiceResponce drServiceResponce = new DrServiceResponce();
	
	 
    List<PatientVisitInformation> visitList = new ArrayList<PatientVisitInformation>();
    if (userName.isEmpty() == true)
    {
   	 return  gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
        //return new DrServiceResponce(String.valueOf(ServiceStatusCodeEnum.PARAMETER_NULL), "Parameter null", "");
    }
    else
    {	
   	
   	 					
   	 				String CounttotalVisits = "SELECT user_name,user_roll,user_RegistrationId ,subscriptionid,user_isActive FROM"
   	 								+ " login_master where user_name='" + userName + "' and "
   	 								+ "user_password='" + userPassword +"' and user_isActive='0' ";
               
   	 					try 
   	 					{
   	 						Statement st=con.createStatement();
   	 						ResultSet rs=st.executeQuery(CounttotalVisits);
               	
   	 						if(rs.next())
   	 						{
   	 							PatientVisitInformation visitDetails = new PatientVisitInformation();
   	 							visitDetails.setPatientVisitName(rs.getString(1));
               		 
               		
               		 
   	 							visitDetails.setPatientVisitRoll(rs.getString(2));
   	 							visitDetails.setPatientRegId(rs.getString(3));

               		
   	 							String var	=rs.getString(4);
               		 
   	 							//Get parent user id name from subscription name it used only when role is patient
               		 
   	 							String getparentname= "select username from subscriber_module where subscriptionid='"+var+"' ";
   	 							try 
   	 							{
   	 								Statement st_get_parent=con.createStatement();
   	 								ResultSet rs_get_parent=st_get_parent.executeQuery(getparentname);
                	 	
   	 								rs_get_parent.next();
   	 								visitDetails.setParentId(rs_get_parent.getString(1));
   	 							}
   	 							catch(Exception e)
   	 							{
   	 								e.toString();
   	 							}
   	 							visitList.add(visitDetails);
   
   	 						}
   	 						drServiceResponce.setPatientsVisitInformationDetails(visitList);
   	 					}
   	 					catch(Exception e) 
   	 					{
   	 						System.out.println(e);
   	 					}
   	 			
   	 			
            
    
   	 }
   	
    
        drServiceResponce.setStatusCode(200);
        drServiceResponce.setMessage("Success");
    
        
        
      return  gson.toJson(drServiceResponce);

}

//||||||||||||||||||53-A|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
public String AdminLoginUser(String userName, String userPassword)
{
	
	 
	 
	 Gson gson = new GsonBuilder().serializeNulls().create();
	 JSONObject resobj=new JSONObject();
	 //PatientVisitInformation visitDetails = new PatientVisitInformation();
	 DrServiceResponce drServiceResponce = new DrServiceResponce();
	
	 
    List<PatientVisitInformation> visitList = new ArrayList<PatientVisitInformation>();
    if (userName.isEmpty() == true)
    {
   	 return  gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
        //return new DrServiceResponce(String.valueOf(ServiceStatusCodeEnum.PARAMETER_NULL), "Parameter null", "");
    }
    else
    {	
   	 //New code for checking the username or password are exist or not
   	 try 
   	 {		
   	 		String checkusername="select user_name from login_master where user_name='"+userName+"'";
   	 		Statement checkuser=con.createStatement();
   	 		ResultSet check=checkuser.executeQuery(checkusername);
   	 		if(check.next())
   	 		{
   	 			
   	 		
   	 			String checkpassword="select user_password from login_master where user_name='"+userName+"' and  user_password='"+userPassword+"'";
   	 			Statement checkpass=con.createStatement();
   	 			ResultSet check1=checkpass.executeQuery(checkpassword);
   	 			if(check1.next())
   	 			{

   	 				//End New code for checking the username or password are exist or not
   	 					
   	 				String CounttotalVisits = "SELECT user_name,user_roll,user_RegistrationId ,subscriptionid,user_isActive FROM"
   	 								+ " login_master where user_name='" + userName + "' and "
   	 								+ "user_password='" + userPassword +"' and user_isActive='0' ";
               
   	 					try 
   	 					{
   	 						Statement st=con.createStatement();
   	 						ResultSet rs=st.executeQuery(CounttotalVisits);
               	
   	 						if(rs.next())
   	 						{
   	 							PatientVisitInformation visitDetails = new PatientVisitInformation();
   	 							visitDetails.setPatientVisitName(rs.getString(1));
               		 
               		
               		 
   	 							visitDetails.setPatientVisitRoll(rs.getString(2));
   	 							visitDetails.setPatientRegId(rs.getString(3));

               		
   	 							String var	=rs.getString(4);
               		 
   	 							//Get parent user id name from subscription name it used only when role is patient
               		 
   	 							String getparentname= "select username from subscriber_module where subscriptionid='"+var+"' ";
   	 							try 
   	 							{
   	 								Statement st_get_parent=con.createStatement();
   	 								ResultSet rs_get_parent=st_get_parent.executeQuery(getparentname);
                	 	
   	 								rs_get_parent.next();
   	 								visitDetails.setParentId(rs_get_parent.getString(1));
   	 							}
   	 							catch(Exception e)
   	 							{
   	 								e.toString();
   	 							}
   	 							visitList.add(visitDetails);
   
   	 						}
   	 						drServiceResponce.setPatientsVisitInformationDetails(visitList);
   	 					}
   	 					catch(Exception e) 
   	 					{
   	 						System.out.println(e);
   	 					}
   	 			}
   	 			else 
   	 			{
   	 				resobj.put("Message", "User Password is Wrong");
   	 				return resobj.toString();
   	 			}
   	 		}
   	 		else 
   	 		{
   	 			resobj.put("Message", "UserName is Wrong");
   	 			return resobj.toString();
   	 		}
            
    
   	 }
   	 catch(Exception e)
   	 {
   		 e.toString();
   	 }
    }
        drServiceResponce.setStatusCode(200);
        drServiceResponce.setMessage("Success");
    
        
        
      return  gson.toJson(drServiceResponce);

}
//|||||||||||||||||||54||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
 /*protected void Application_AuthenticateRequest(Object sender, Event e)
         {
        	 DatabaseConnection db=new DatabaseConnection();
        	 Connection con =db.getConnection();
        if(HttpContext.UserHostAddress!=null)
        {
         if (HttpContext.Current.User.Identity.IsAuthenticated)
           {
              if (HttpContext.Current.User.Identity == FormsIdentity)
               {
                   FormsIdentity id = (FormsIdentity)HttpContext.Current.User.Identity;
                   FormsAuthenticationTicket ticket = id.Ticket;
                   String userData = ticket.UserData;
                   String[] roles = userData.Split(',');
                   HttpContext.Current.User = new GenericPrincipal(id, roles);
                     }
                 }
             }
         }*/
 //|||||||||||||||||||55||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public String ResetUserPassword(String userName, String userEmailId, String userid)
         {
        	 int flag=0;
        	// DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 
        	 try
             {                        	
                  String resetPasswordCommand = "SELECT user_roll,user_isActive FROM login_master where user_name='" + userName + "' and user_email_id='" + userEmailId + "' and subscriptionid='"+subscription+"'";
                  try {
                        	 Statement st=con.createStatement();
                        	 ResultSet rs=st.executeQuery(resetPasswordCommand);
                        	 rs.first();
                        	 rs.beforeFirst();
                        	 while(rs.next()) {
                        	 rs.getString(2);
                        	 if(rs.getString(2).equals("0")) 
                        	 {
                        		 	String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                        	        StringBuilder salt = new StringBuilder();
                        	        Random rnd = new Random();
                        	        while (salt.length() < 18) 
                        	        { 
                        	            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                        	            salt.append(SALTCHARS.charAt(index));
                        	        }
                        	        String password = salt.toString();
                        	      
                             if (SendPasswordAtEmail(userEmailId,password,userName))
                              { 
                            	  String updatePasswordCommand = "Update login_master set user_password ='" + password + "'" + " where"
                                   		+ " user_name='" + userName + "' and user_email_id='" + userEmailId + "' and subscriptionid='"+subscription+"'";
                                  try {
                                  	PreparedStatement st1=con.prepareStatement(updatePasswordCommand);
                                  	st1.executeUpdate();
                                  	flag=1;
                                  }
                                   catch(Exception e) 
                                  {
                                  	 System.out.println(e);
                                   }
                                  if(flag==1) 
                                	    return "Password has been send at your email " + userEmailId + ". Please check your mail.";
                                  else
                                     return "Some technical problems in reset password. Contact your administrator.";
                              }
                          }
                        	
                        	
                         
                      else
                      {
                          return userName + " is not an active user !";
                      }
                  
                        	}
                         }
                         catch(Exception e) {
                        	 System.out.println(e);
                         }
                         
                         
             }         
             catch(Exception ex)
             {
                 return "Error occured during processing request. Error details is: " + ex.getMessage();
              }
        	 return "resetPassword";
             }
        	 
 //||||||||||||||||||||56|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public String ChangeUserPassword(String userName, String newPassword)
         {
        	 JSONObject resobj=new JSONObject();
        	 String checkuserid="select user_name,user_email_id from login_master where user_name='"+userName+"'";
        	
        	String emailid="";	 
        	 try 
        	 {
        		 Statement st=con.createStatement();
        		 ResultSet rs=st.executeQuery(checkuserid);
        		
        		 if(rs.next()==true)
        		 {
        			emailid=rs.getString(2);
        				 String updatePasswordCommand = "Update login_master set user_password ='" + newPassword + "' where user_name='"+userName+"'";
                         try 
                         {
                        	 PreparedStatement pst=con.prepareStatement(updatePasswordCommand);
                             pst.executeUpdate(updatePasswordCommand);
                          	 
                             
                             
                          	 resobj.put("Message", "User Password Updated");
                          	SendUpdatePasswordAtEmail(emailid,newPassword,userName);
                           		
                        }
                        catch(Exception e)
                        {
                        	System.out.println("hello "+e.toString());
                        	e.toString();
                        }
                    
        		}
        		else
        		{
        			resobj.put("Message", " UserId Information Is Not Correct"); 
        		}
        				
        	}
            catch (Exception ex)
            {
                return "Error occured during processing request. Error details is: " + ex.getMessage();
            }
        	 
             return resobj.toString();
         }
  //||||||||||||||||||||||57-A|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public boolean SendPasswordAtEmail(String emailId, String newPassword,String username)
         {
        	 
         new Thread(new Runnable() {

                 public void run() {
                	 	System.out.println(emailId);
                     try {
                         String body = "Dear " + username + ",\n Following are the credentials for you to login in the portal to look at your prescribed medicines and other details.\n Username : " + username + "\n Password : " + newPassword + " \n Click Here for Login http:54.202.171.22:8080/DoctorStudio/index.html \n Thanks\nAdmin";
                         String Subject = "Your user name and password for portal usage.";
                        
                        
                         //GMailsender sender = new GMailsender("ashish@iratechnologies.com","8601489174");

                         GMailsender sender = (GMailsender) context.getBean("myEmailUserAndPassword");

                         sender.sendMail(Subject,body, emailId);
                         
                     }
                     catch (Exception e)
                     {
                    	 System.out.println(e);
                     }

                 }

             }).start();
         
        
        return true;
        }
         
         
         //||||||||||||||||||||||57-B|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         private boolean SendUpdatePasswordAtEmail(String emailId, String newPassword,String username)
         {
        	 
         new Thread(new Runnable() {

                 public void run() {
System.out.println(emailId);
                     try {
                         String body = "Dear " + username + ",\n Following are the Updated credentials for you to login in the portal to look at your prescribed medicines and other details.\n Username : " + username + "\n New Password : " + newPassword + " \n Thanks\nAdmin";
                         String Subject = "Your user name and password for portal usage.";
                         
                         GMailsender sender = (GMailsender) context.getBean("myEmailUserAndPassword");

                         sender.sendMail(Subject,body, emailId);
                         
//                         GMailsender sender = new GMailsender(
//                        
//                                 "saurabh@iratechnologies.com",
//
//                                 "9956118545");
//
//
//
//                         sender.sendMail(Subject,body,
//
//                                 "saurabh@iratechnologies.com", emailId);
                     }
                     catch (Exception e)
                     {
                    	 System.out.println(e);
                     }

                 }

             }).start();
         
        
        return true;
        }
         

         
         //||||||||||||||||||||||57-c|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         private boolean SendOTPATEmail(String emailId,String username,String OTP)
         {
        	 
         new Thread(new Runnable() {

                 public void run() {
System.out.println(emailId);
                     try {
                         String body ="Dear User :" + username + ",\n YOUR ONE TIME PASSWORD IS :" + OTP + " \n Thanks\nAdmin";
                         String Subject = "Your One Time Password for changing the User id Password.";

                         GMailsender sender = (GMailsender) context.getBean("myEmailUserAndPassword");

                         sender.sendMail(Subject,body, emailId);
                         
                         //                         GMailsender sender = new GMailsender(
//                        
//                                 "saurabh@iratechnologies.com",
//
//                                 "9956118545");
//
//
//
//                         sender.sendMail(Subject,body,
//
//                                 "saurabh@iratechnologies.com", emailId);
                     }
                     catch (Exception e)
                     {
                    	 System.out.println(e);
                     }

                 }

             }).start();
         
        
        return true;
        }
         
//|||||||||||||||||||||||58||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public String SendEmailAtToDayPatient(String userid)
         {
        	 //DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
        	 
        	 String subscription = getSubscriptionId(userid);
        	 
        	 
        	 Gson gson = new GsonBuilder().serializeNulls().create();
        	 List<String> ArrayEmailId = new ArrayList<String>();   
             DrServiceResponce drServiceResponce = new DrServiceResponce();
             String EmailId ="";
             String VisitTime = "";
             String patientName = "";
             String Visitdate = "";
          
             ZonedDateTime today = ZonedDateTime.now();
                   //  DateTime answer = today.AddDays(1);
                     String tempcurrentDate = String.format("{yyyy-MM-dd}", today);
                     String selectCommand = "select * from patient_information_master, patient_appointment_table where patient_information_master.patient_reg_id = patient_appointment_table.patient_registration_id and  patient_information_master.patient_isDelete= '" + 0 + "' and patient_information_master.patient_issuspend ='" + 0 + "' and  patient_appointment_table.appointment_date='"+tempcurrentDate+"' and patient_information_master.subscriptionid='"+subscription+"'  order by  patient_appointment_table.appointment_start_time desc";
                  
                   try {
                	  
                	   Statement st=con.createStatement();
                	   ResultSet rs=st.executeQuery(selectCommand);
                	   rs.next();
                	   VisitTime=rs.getString(31);
                	   patientName=rs.getString(4)+" "+rs.getString(5);
                	   EmailId=rs.getString(20);
                	   Visitdate=rs.getString(29);
                		   System.out.println("values is "+EmailId+"    "+VisitTime+"     "+patientName+"     "+ Visitdate+" " );
                   }
                   catch(Exception e) {
                	   System.out.println(e);
                   }
                            if (!EmailId.isEmpty())
                            {
                                GetSendMail(EmailId, VisitTime, patientName, Visitdate, subscription);
                            }
                            drServiceResponce.setStatusCode(200);
                            drServiceResponce.setMessage("Success");
                            drServiceResponce.setExceptionDetails("No Record Found");
                 //drServiceResponce.ExceptionDetails = ;
             return gson.toJson(drServiceResponce);
             }
 
         
         //|||||||||||59||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

         public boolean GetSendMail(String Emailid, String AppoitementTime, String patientname, String Appoitementdate, String subscription)
         {
        	 
        	 String[] splitArray = Appoitementdate.split(Pattern.quote(" "));
         String datepart = splitArray[0];
        
             String body = "Dear " + patientname + ",\n You have appointment with doctor on " + datepart + " at " + AppoitementTime + ". See you there.\nThanks \n Admin";
             String Subject = "Your appointment is due";
             
         new Thread(new Runnable() {

                 public void run() {

                     try {
                    	 
                    	 
                    	 
                    	//Start Code Get Email Id and Password from Database
         		        
         		        String getEmailAndPassword = "select email, password from email_password where subscriptionid='"+subscription+"'";
         		        
         		        Statement st_getEmail = con.createStatement();
         		        ResultSet rs_getEmail = st_getEmail.executeQuery(getEmailAndPassword);
         		        
         		        GMailsender sender;
         		        
         		        if(rs_getEmail.next() == true) // If User Have set email and password
         		        {
         		        	 sender = new GMailsender(rs_getEmail.getString(1), rs_getEmail.getString(2));
         		        }
         		        else   // Get default email and password from applicationContext.xml bean provided by Ira Technologies  
         		        {
         		        	  sender = (GMailsender) context.getBean("myEmailUserAndPassword");
         		        }
         		        
         		        
         		        //End Code
                    	 
                    	 
                    	 

                        // GMailsender sender = new GMailsender("drrahultewari@gmail.com","aroti_ma76");

                         sender.sendMail(Subject,body,Emailid);

                     } catch (Exception e) {
                     }
                 }

             }).start();
             return true;
         }
//||||||||||||||60|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
         public List<String> GetListOfColumns()
         {
        	
        	 ResultSet dt=GetColumnsFromDB();
        	
        	 try {
				
				dt.beforeFirst();
				
				while(dt.next())
				{
					
				 dt.getString(1);
				}
        	 } 
        	 catch (SQLException e1) {
				System.out.println(e1);
				
			}
            List<String> columnsList=new ArrayList<String>();
            
        	columnsList.addAll(new CacheData().getHashMap().keySet());
        	
        	System.out.println("CacheData "+new CacheData().getHashMap().keySet());
        	 try {
        		 dt.beforeFirst();
				while(dt.next()) 
				{
					     columnsList.add(dt.getString(1));
					 }
			} catch (SQLException e1) {
				
				System.out.println(e1.toString());

			}
      
             return columnsList;
         }


//\\\ok...\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        ColumnData GetAllColumnsData()
         {
        	
        	//DatabaseConnection db=new DatabaseConnection();
       	 //Connection con =db.getConnection();
       	 
       	 String selectcommand = "Select patient_reg_id,patient_first_name,patient_last_name,patient_date_of_birth,"
                		+ "patient_phone_number,patient_profession,patient_email_id,patient_symptoms"
                		+ " FROM patient_information_master where patient_isDelete= 0";
                		
                		
               String selectcommand1="Select column_value FROM patient_dynamic_table";
                		
                String selectcommand2= "Select patient_visit_date FROM patient_visit_details";
                   try {
                	   Statement st=con.createStatement();
                	   ResultSet rs=st.executeQuery(selectcommand);
                	 
                	  Statement st1=con.createStatement();
                	   ResultSet rs1=st1.executeQuery(selectcommand1);
                	   
                	   Statement st2=con.createStatement();
                	   ResultSet rs2=st2.executeQuery(selectcommand2);
                	  
                	   while(rs.next()) {
                		   rs.getString(1);
                		   System.out.println(rs.getString(1));
                		   rs.getString(2);
                		   System.out.println(rs.getString(2));
                		   rs.getString(3);
                		   System.out.println(rs.getString(3));
                		   rs.getString(4);
                		   rs.getString(5);
                		   rs.getString(6);
                		   rs.getString(7);
                		   rs.getString(8);
                		  
                	   }
                	   
                	   while(rs1.next()) {
                		   rs1.getString(1);
                		   System.out.println(rs1.getString(1));
                	   }
                	   
                	   while(rs2.next()) {
                		   rs2.getString(1);
                		   System.out.println(rs2.getString(1));
                	   }
                	   
                   }
                   catch(Exception e) {
                	   System.out.println(e);
                   }
                  
 return new ColumnData();
         }
 //|||||||||||||||61||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
 /*private String createSearchQuery(String searchCondition)
        {
            
        	DatabaseConnection db=new DatabaseConnection();
          	 Connection con =db.getConnection();
        	
        	String strQuery = "";
            String oprationalString = searchCondition;
            for( Entry<String, String> KV : CacheData.dicColumnsMapping)
            {
                String str = oprationalString.Replace(KV.Key, KV.Value);
                strQuery = str;
                oprationalString = str;
            }
            return "Select * from patient_information_master where " + strQuery;
            
        }*/
 //-----------------------------------------------------------------------------------------------------
 PatientInformation ProcessDataSet(ResultSet ds1,ResultSet ds2,ResultSet ds3, ResultSet dtMasterTable)
        {
        	//DatabaseConnection db=new DatabaseConnection();
          	//Connection con =db.getConnection();
          //	System.out.println("at line no 4519");
          	PatientInformation patient = ExtractPatientInformationFromDataRow(dtMasterTable);
            
          	int count=1;
          	if (ds1 != null )
            {
                List<PatientVisitInformation> visitList = new ArrayList<PatientVisitInformation>();
                
                patient.setListOfVisitInformation(new ArrayList<PatientVisitInformation>());
                
            	try 
            	{
					while(ds1.next())  
					{
						//System.out.println("count: "+count++);
					    PatientVisitInformation visitDetails = new PatientVisitInformation();
					    
					    visitDetails.setVisitMedicinList( new ArrayList<VisitMedicin>());
					    
					    visitDetails.setVisitId( ds1.getString(1));
					    
					    visitDetails.setVisitDate(ds1.getString(2));
					    
					    visitDetails.setVisitInformation(ds1.getString(4));
					    
					    if(ds1.getString(5)==null)
					    {
					    	visitDetails.setVisitMedicienString("");
					    }
					    else
					    {
					    	  visitDetails.setVisitMedicienString(ds1.getString(5));
					    }
					  
					  //  System.out.println("setVisitMedicienString: "+ds1.getString(5));
					    String medicinelist = visitDetails.getVisitMedicienString();
					   
					    String[] splitstring = medicinelist.split("@");
					   
					    //System.out.println(splitstring[0]+" after split");
					    
					    List<String> list=new ArrayList<String>();
					    
					    for(int i=0; i<splitstring.length; i++)
					    {
					    	 list.add(splitstring[i]);
					    }
					    
					   
					    visitDetails.setMedicienStringList(new ArrayList<String>());
					    
					    visitDetails.setMedicienStringList(list);
					   
					    
					    visitDetails.setVisitTitle(ds1.getString(6));
					    
					    visitDetails.setVisitStatus(ds1.getString(7));
					    
					    if(ds1.getString(11)==null)
					    {
					    	 visitDetails.setPatientNextvisitdate("0000-00-00");
					    	 
					    }
					    else
					    {
					    	 visitDetails.setPatientNextvisitdate(ds1.getString(11));
					    }
					    
					   
					    
					   // System.out.println("PatientNextvisitdate: "+ds1.getString(11));
					    if(ds1.getString(12)==null)
					    {
					    	 visitDetails.setPatientAppointmentStartTime("");
					    }
					    else
					    {
					    	visitDetails.setPatientAppointmentStartTime(ds1.getString(12));
					    }
					    
					    
					    visitDetails.setVisitDiagonastic(ds1.getString(18));
					    
					    visitDetails.setVisitBp(ds1.getString(13));
					    visitDetails.setTypeConversation(ds1.getString(19));
					    visitDetails.setVisitWeight(ds1.getString(14));
					    
					    String visitTime=ds1.getString(3);
					    
					    if(ds1.getString(17)==null)
					    {
					    	 visitDetails.setPatientVisitImagepath("none");
					    }
					    else
					    {
					    	 visitDetails.setPatientVisitImagepath(ds1.getString(17));
					    }
					   
					    
					   // System.out.println(ds1.getString(17)+"ImagePath");
					    
					    File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
					    
					    File child = new File(parent,visitDetails.getPatientVisitImagepath());
					    
					    visitDetails.setPatientVisitImagepath(child.toString());
					    
					    visitDetails.setVisitTime(visitTime);
					     
					    patient.getListOfVisitInformation().add(visitDetails);
					}
				} 
            	catch (SQLException e) 
            	{
					
					System.out.println(e);
				}

                patient.setDynamicColumns(new ArrayList<DynamicColumns>());
              
                try 
                {
					while(ds3.next())
					{
					    DynamicColumns dynamicColumns = new DynamicColumns();
					    dynamicColumns.setColumnName(ds3.getString(1));
					    dynamicColumns.setColumnValue( ds3.getString(2));
					    patient.getDynamicColumns().add(dynamicColumns);
					}
				} 
                catch (SQLException e) 
                {
					
					System.out.println(e);
				}
            }
          	
            patient.setServiceResult("Success");
            return patient;
        }
     
     //||62|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
 List<PatientVisitInformation> ProcessVisitInformationDataTable(ResultSet rs,ResultSet rs1)
             
        {
          	List<PatientVisitInformation> visitList= new ArrayList<PatientVisitInformation>();
            try 
            {
				if (rs.next()==true)
				{
					
					//Start DOB Calculation by ASHISH
					String input=rs.getString(21);
  	                String todayage = "";
  	              //String input="1990-11-01";
  	            
  	                boolean checkformat1= (input.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})"));
  	             
  	                boolean checkformat2= (input.matches("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})"));
  	              
  	              System.out.println("checkformat: "+checkformat1 +" checkformat2: "+checkformat2);
  	              if(checkformat1 == true)
  	              {
  	            	  DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  	                  Date date= df.parse(input);
  	                  
  	                // String formatteddate = df.format(date);
  	                 int dobyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)).getYear();
  	                 int dobmonth = date.getMonth()+1;
  	                 
  	                 
  	                 
  	                 Date currentdate1 = new Date();
  	            	 
  	            	 int currentmonth = currentdate1.getMonth()+1;
  	            	 int currentyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(currentdate1)).getYear();
  	            	 
  	            	 
  	            	 
  	            	 if(currentmonth < dobmonth)
  	            	 {
  	            		 currentmonth = currentmonth+12;
  	            		 currentyear = currentyear-1;
  	            	 }
  	            	 
  	            	 int year = currentyear - dobyear;
  	            	 int month = currentmonth- dobmonth;
  	            	 
  	            	
  	            	 todayage =String.valueOf(year) +" Y "+month +" M";
  	            	
  	              }
  	              else if(checkformat2 == true)
  	              {
  	            	  DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	                  Date date= df.parse(input);
  	                  
  	                // String formatteddate = df.format(date);
  	                 int dobyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)).getYear();
  	                 int dobmonth = date.getMonth()+1;
  	                 
  	                
  	                 
  	                 Date currentdate1 = new Date();
  	            	 
  	            	 int currentmonth = currentdate1.getMonth()+1;
  	            	 int currentyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(currentdate1)).getYear();
  	            	 
  	            	 
  	            	 
  	            	 if(currentmonth < dobmonth)
  	            	 {
  	            		 currentmonth = currentmonth+12;
  	            		 currentyear = currentyear-1;
  	            	 }
  	            	 
  	            	 int year = currentyear - dobyear;
  	            	 int month = currentmonth- dobmonth;
  	            	 
  	            	//String  newage = String.valueOf(year +" "+month);
  	            	
  	            	 todayage =String.valueOf(year) +" Y "+month +" M";
  	            	
  	              }
  	              else
  	              {
  	            	  
  	                   if(rs.getString(18)==null || rs.getString(18).equals("null"))
  	                   {
  	                	   todayage="";  
  	                   }
  	                   else
  	                   {
  	                	   todayage = rs.getString(21);
  	                   }
  	            	  
  	              }
  	              
  	            System.out.println("todayage: "+todayage);
					
  	            
  	            //End Here
  	            
					try {
					
						do
						{
							System.out.println("in 62");
							System.out.println("visit date: "+rs.getString(17)+" "+rs.getString(18)+" "+rs.getString(19));
						      PatientVisitInformation visitDetails = new PatientVisitInformation();
						       
						      visitDetails.setVisitMedicinList(new ArrayList<VisitMedicin>());
						      //visitDetails.VisitMedicienList=new ArrayList<VisitMedicin>();
						      try
						      {
						    	 
								if (rs.getString(2)==null)
								{
								     visitDetails.setVisitDate("");
								}
								else
								{
								    visitDetails.setVisitDate(rs.getString(2));
								}
								
							}
						      catch (SQLException e1)
						      {
								
								System.out.println(e1);
							}
						    
						      
						      try
						      {
								visitDetails.setVisitTime(rs.getString(3));
							  }
						      catch (SQLException e1) 
						      {
						    	  System.out.println(e1);
							 }
						      System.out.println("test");
						      
						      try
						      {
						    	  visitDetails.setVisitId(rs.getString(1));
						    	  visitDetails.setVisitInformation(rs.getString(4));
						    	  visitDetails.setVisitMedicienString(rs.getString(5));
						    	  
						    	  if(rs.getString(13)==null)
						    	  {
						    		  visitDetails.setPatientVisitImagepath("none");
						    	  }
						    	  else
						    	  {
						    		  visitDetails.setPatientVisitImagepath(rs.getString(13)); 
						    	  }
						    	  
						    	
						    	  File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
						    	  File child = new File(parent,visitDetails.getPatientVisitImagepath());
						    	  visitDetails.setPatientVisitImagepath(child.toString());
						    }
						    catch(Exception e)
						    {
						    	System.out.println(e);
						    }
						   
						     
						      String medicinelist = visitDetails.getVisitMedicienString();
						      if(medicinelist!=null)
						      {  
						      String[] splitstring = medicinelist.split("@");
						      System.out.println(splitstring[0]);
						      List<String> list=Arrays.asList(splitstring);
						     
						      visitDetails.setMedicienStringList(new ArrayList<String>());
						      visitDetails.setMedicienStringList(list);
						      }
						      
						      try
						      {
						    	  visitDetails.setVisitTitle(rs.getString(6));
						    	  
						    	  if (rs.getString(10)== "")
						    	  {
						    		  visitDetails.setPatientNextvisitdate(rs.getString(10));
						    	  }
						    	  else
						    	  {
						    		  visitDetails.setPatientNextvisitdate(rs.getString(10));
						    	  }
						   
						    	  visitDetails.setVisitDiagonastic(rs.getString(16));
						    	  visitDetails.setVisitBp(rs.getString(14));
						    	  visitDetails.setVisitWeight(rs.getString(15));
						    	  
						    	  visitDetails.setPatientVisitName(rs.getString(17)+" "+rs.getString(18));
						    	  visitDetails.setPatientAge(todayage);
						    	  visitDetails.setPatientGender(rs.getString(20));
						    	  String visitTime = rs.getString(3);
						      }
						      catch(Exception e)
						      {
						    	
						    	  System.out.println(e);
						    	  
						     }
						      
						  
						      String expression =visitDetails.getVisitDate();
						      int k=0;
						 
						    try
						    {
						    	rs1.beforeFirst();
								while(rs1.next()) 
								{
									
									System.out.println(rs1.getString(1)+"and "+expression);
									try 
									{
										if(rs1.getString(1).equals(expression))
										{
											//System.out.println("expression equal");
										  VisitMedicin visitmedicineDetails = new VisitMedicin();
								          visitmedicineDetails.setMedicinName(rs1.getString(3));//.MedicienName = ;
								          
								          visitmedicineDetails.setMedicinpower(rs1.getString(5));//.Medicienpower =  ;
								          
								          visitmedicineDetails.setMedicinQuntity(rs1.getString(7));//.MedicienQuntity =   ;
								          
								          visitmedicineDetails.setMedicinDos(rs1.getString(4));//.MedicienDos =  ;
								          
								          visitmedicineDetails.setMedicintype(rs1.getString(6));//.Medicientype =   ;
								          
								          visitmedicineDetails.setMedicinday(rs1.getString(8));//.Medicienday =  ;
								         
								          
								          visitmedicineDetails.setMedicinTotalQuntity(rs1.getString(10));///.MedicienTotalQuntity =   ;
								          visitmedicineDetails.setVisitExternalInternal(rs1.getString(9));//.VisitExternalInternal =  ;
								          visitmedicineDetails.setVisitFromDate(rs1.getString(11));//.VisitFromDate =   ;
								          visitmedicineDetails.setVisitToDate(rs1.getString(12));//.VisitToDate =   ;
								          visitDetails.getVisitMedicinList().add(visitmedicineDetails);
								         //System.out.println("visitmedicineDetails: "+visitmedicineDetails);
								          
										}
									}
									catch(Exception e)
									{
										System.out.println(e);
									}
				
								}
							} 
						    catch (SQLException e)
						    {
								System.out.println(e);
							}
						    visitList.add(visitDetails);
						    
						}while(rs.next());
					}
					catch (SQLException e)
					{
						System.out.println(e);
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
			} 
            catch (SQLException e)
            {
            	System.out.println(e);
			}
            catch(Exception e)
            {
            	System.out.println(e);
            }
            
        	  return visitList;
        }
 
//--------------------------------------------------------------------------------------------------------------------------------------
        String createVisitMedicienInfo(List<VisitMedicin> medicinList)
        {
        	
        	//DatabaseConnection db=new DatabaseConnection();
          	// Connection con =db.getConnection();
          	
            String visitMedicien = "";
            if(medicinList != null && medicinList.size() > 0)
            {
                //foreach(VisitMedicin medicien in medicienList)
            	for(int i=0;i<medicinList.size();i++)
                {
            		VisitMedicin medicien=medicinList.get(i);
                    visitMedicien += medicien.getMedicinName() + ","+medicien.getMedicinpower() + "," +medicien.getMedicintype()+","+medicien.getMedicinDos()+"," +medicien.getMedicinQuntity()+","+medicien.getMedicinday()+"," +medicien.getVisitExternalInternal()+ "," +medicien.getVisitFromDate()+"," +medicien.getVisitToDate()+"@";
                }
                visitMedicien.trim();
            }
            
          //  System.out.println("visitMedicien: "+visitMedicien);
            return visitMedicien;
        }
 //||63|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

        public String Upload(InputStream Uploading, String userid)
        {
        	Gson gson = new GsonBuilder().serializeNulls().create();
        	byte[] BlobValue=null;
        	//DatabaseConnection db=new DatabaseConnection();
        	// Connection con =db.getConnection();
        	
        	String subscription = getSubscriptionId(userid);
        	
		  UploadedFile upload = new UploadedFile();
         try
         {
        	 UUID uuid = UUID.randomUUID();
        	 File parent = new File(System.getProperty("java.io.tmpdir"));
			 File child = new File(parent, uuid.toString());
			 upload.setFilePath(child.toString()); 
			
			 byte[] buffer = new byte[8192];
             int length1 = 0;
             int readCount;
            OutputStream writer=new  FileOutputStream(upload.getFilePath());
            		while((readCount = Uploading.read(buffer, 0, buffer.length)) != 0)
                    {
                        writer.write(buffer, 0, readCount);
                        length1 += readCount;
                        System.out.println("in while");
                    }
            		 writer.flush();
            		 writer.close();
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
        	 
            
             upload.setFileName(ex.getMessage());
             return gson.toJson(upload);
         }
         
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");

         
         Date today = Calendar.getInstance().getTime();       
        
         String uploadDate = df.format(today);  
            
                    String insertdiagnosis = "Insert into patient_diagnosis_table(patient_reg_id,uploaded_date,uploaded_file,file_name, subscriptionid)" +
                    							" values('Arvind125','"+uploadDate+"','" + BlobValue + "','" + upload.getFileName() + "' , '"+subscription+"')";
                   
                    try {
                    	PreparedStatement pst1=con.prepareStatement(insertdiagnosis);
                    	pst1.executeUpdate();
                    }
                    catch(Exception e) {
                    	System.out.println(e);
                    }
                
            return gson.toJson(upload);
      }
	  
	//||64|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

        public String GetPatientDiagnosesList(String RegId, String userid)
        {
        	Gson gson = new GsonBuilder().serializeNulls().create();
        	//DatabaseConnection db=new DatabaseConnection();
          	// Connection con =db.getConnection();
        	
        	String subscription = getSubscriptionId(userid);
        	
        	
        	List<ResultSet> list=new ArrayList<ResultSet>();
        	 ResultSet rs=null;
            if(RegId.isEmpty())
            {
                return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Patient registration id is null\\empty...!", ""));
            }
            else
            {
                DrServiceResponce drResponce = new DrServiceResponce();
                try
                {
                            String selectQuery = "Select id,file_name,uploaded_date from patient_diagnosis_table "
                            						+ "where patient_reg_id='" + RegId + "' and subscriptionid='"+subscription+"' "
                            						+ "order by uploaded_date desc LIMIT 15";
                           int count =0;
                            try {
                        	   Statement st=con.createStatement();
                        	    rs=st.executeQuery(selectQuery);
                        	   rs.next();
                        	  
                        	   list.add(rs);
                        	   count=rs.getRow();
                        	
                           }
                            catch(Exception e) {
                            	System.out.println(e);
                            }
                            if (count> 0)
                            {
                                drResponce.setDiagnosesList(new ArrayList<Diagnoses>());
                                List<Diagnoses> results = ProcessMySqlSelectCommandResult.ExtractDiagnosesList(rs);
                                drResponce.setDiagnosesList(results);
                                drResponce.setStatusCode(ServiceStatusCodeEnum.SUCCESS.getNumVal());//StatusCode = String.valueOf(ServiceStatusCodeEnum.SUCCESS);
                                drResponce.setMessage("Success:" + results.size() + "Actual" + drResponce.getDiagnosesList().size());
                            }
                            else
                            {
                                return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.RECORD_NOT_FOUND.getNumVal(), "Record not found", ""));
                            }
                    return gson.toJson(drResponce);
                }
                catch(Exception odbcEx)
                {
                    return gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.MYSQL_EXCEPTION.getNumVal(), odbcEx.getMessage(), odbcEx.getMessage()));
                }
            }
        }
        
        //||64|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

        String PasswordGenerator(int charLength) {
            return String.valueOf(charLength < 1 ? 0 : new Random()
                    .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                    + (int) Math.pow(10, charLength - 1));
        }
	

        //||65|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

        /* static PatientInformation ExtractPatientInformationFromDataRow(List<PatientInformation> patientList) throws Exception 
        {
            PatientInformation patient = new PatientInformation();
          for(int i=0;i<patientList.size();i++) {
          				patient.PatientRegId = patientList.get(i).getPatientRegId();
			
            patient.PatientFirstName = patientList.get(i).getPatientFirstName();
            patient.PatientSurName = patientList.get(i).getPatientSurName();
            patient.PatientGender = patientList.get(i).getPatientGender();
            patient.PatientDOB =patientList.get(i).getPatientDOB();
            patient.PatientAddress = patientList.get(i).getPatientAddress();
            patient.PatientPhoneNo = patientList.get(i).getPatientPhoneNo();
            patient.PatientProfession = patientList.get(i).getPatientProfession();
            patient.PatientMaritalStatus = patientList.get(i).getPatientMaritalStatus();
            patient.PatientReligion = patientList.get(i).getPatientReligion();
            patient.PatientEmailId =patientList.get(i).getPatientEmailId();
            patient.PatientSymptoms =patientList.get(i).getPatientSymptoms();
            patient.PatientAge = patientList.get(i).getPatientAge();
            patient.PatientBloodGroup = patientList.get(i).getPatientBloodGroup();
            
       
          }
            return patient;
        }
         */
        static PatientInformation ExtractPatientInformationFromDataRow(ResultSet resultSet) 
        {
       	 
            PatientInformation patient = new PatientInformation();
         
            try {
           
       	      patient.setPatientRegId(resultSet.getString(3));
       	      patient.setPatientFirstName(resultSet.getString(4));
              patient.setPatientSurName(resultSet.getString(5));
              patient.setPatientGender(resultSet.getString(6));
              
              patient.setPatientDOB(resultSet.getString(7));
              
              patient.setPatientAddress ( resultSet.getString(11));
              patient.setPatientPhoneNo(resultSet.getString(13));
              patient.setPatientProfession( resultSet.getString(15));
              patient.setPatientMaritalStatus (resultSet.getString(16));
              patient.setPatientReligion(resultSet.getString(19));
              patient.setPatientEmailId(resultSet.getString(20));
              patient.setPatientSymptoms(resultSet.getString(21));
        
              String input=resultSet.getString(7);
              String todayage = "";
              //String input="1990-11-01";
              boolean checkformat1= (input.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})"));
             
              boolean checkformat2= (input.matches("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})"));
              
              System.out.println("checkformat: "+checkformat1 +" checkformat2: "+checkformat2);
              if(checkformat1 == true)
              {
            	  DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                  Date date= df.parse(input);
                  
                // String formatteddate = df.format(date);
                 int dobyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)).getYear();
                 int dobmonth = date.getMonth()+1;
                 
                 
                 
                 Date currentdate = new Date();
            	 
            	 int currentmonth = currentdate.getMonth()+1;
            	 int currentyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(currentdate)).getYear();
            	 
            	 
            	 
            	 if(currentmonth < dobmonth)
            	 {
            		 currentmonth = currentmonth+12;
            		 currentyear = currentyear-1;
            	 }
            	 
            	 int year = currentyear - dobyear;
            	 int month = currentmonth- dobmonth;
            	 
            	System.out.println("currentyear: "+currentyear+" dobyear: "+dobyear+" currentmonth"+ currentmonth+" dobmonth: "+dobmonth);
            	 todayage =String.valueOf(year) +" Y "+month +" M";
            	
              }
              else if(checkformat2 == true)
              {
            	  DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                  Date date= df.parse(input);
                  
                // String formatteddate = df.format(date);
                 int dobyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)).getYear();
                 int dobmonth = date.getMonth()+1;
                 
                
                 
                 Date currentdate = new Date();
            	 
            	 int currentmonth = currentdate.getMonth()+1;
            	 int currentyear = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(currentdate)).getYear();
            	 
            	 
            	 
            	 if(currentmonth < dobmonth)
            	 {
            		 currentmonth = currentmonth+12;
            		 currentyear = currentyear-1;
            	 }
            	 
            	 int year = currentyear - dobyear;
            	 int month = currentmonth- dobmonth;
            	 
            	//String  newage = String.valueOf(year +" "+month);
            	
            	 todayage =String.valueOf(year) +" Y "+month +" M";
            	
              }
              else
              {
            	  
                   if(resultSet.getString(18)==null || resultSet.getString(18).equals("null"))
                   {
                 	  todayage = "";  
                   }
                   else
                   {
                 	  todayage = resultSet.getString(18);
                   }
            	  
              }
              
            System.out.println("todayage: "+todayage);
     
              patient.setPatientAge(String.valueOf(todayage));
              
              patient.setPatientBloodGroup(resultSet.getString(17));
             // System.out.println("age: "+resultSet.getString(18));
              }
            catch(Exception e)
            {
           	 System.out.println(e);
            }
            return patient;
        }
        
        
      
         static PatientInformation ExtractPatientInformationFromDataRow1(ResultSet resultSet) 
         {
        	// System.out.println("count in called fun "+count++);
             PatientInformation patient = new PatientInformation();
          
             try {
            	// System.out.println("at get line no 4959");
            	
                	//System.out.println(resultSet.getString(1));
        	   patient.setPatientRegId(resultSet.getString(1));
        	   patient.setPatientFirstName(resultSet.getString(2));
               patient.setPatientSurName(resultSet.getString(3));
               patient.setPatientGender(resultSet.getString(4));
               patient.setPatientDOB(resultSet.getString(5));
               patient.setPatientAddress ( resultSet.getString(6));
               patient.setPatientPhoneNo(resultSet.getString(7));
               patient.setPatientProfession( resultSet.getString(8));
               patient.setPatientMaritalStatus (resultSet.getString(9));
               patient.setPatientReligion(resultSet.getString(10));
               patient.setPatientEmailId(resultSet.getString(11));
               patient.setPatientSymptoms(resultSet.getString(12));
              // System.out.println("at get line no 4973");
              
             }
             catch(Exception e)
             {
            	 System.out.println(e);
             }
             
           // System.out.println("patient list "+patient.toString());
             return patient;
         }
         
         //||66|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

          static PatientInformation ExtractDynamicColumnsFromRows(PatientInformation patient, ResultSet rs)
         {
             if (rs != null && patient != null)
             {
                 patient.setDynamicColumns(new ArrayList<DynamicColumns>());
                 try {
                	
                 rs.next();
                 {
                	
                     DynamicColumns dynamicColumns = new DynamicColumns();
                   
                     dynamicColumns.setColumnName(rs.getString(2));
                   
                     dynamicColumns.setColumnValue(rs.getString(3));
                    
                     dynamicColumns.setColumnId(rs.getString(1)); 
                     
                     patient.getDynamicColumns().add(dynamicColumns);
                 }
                 }
                	catch(Exception e) 
                 {
                		System.out.println(e);
                	}
                 
             }
            return  patient;
         }

         //||67|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

           ResultSet GetColumnsFromDB()
         {
        	  //DatabaseConnection db=new DatabaseConnection();
        	 // Connection con=db.getConnection();
           // List<ResultSet> dt=new ArrayList<ResultSet>();
            ResultSet rs=null;
           // System.out.println("67.1");
                 String stringCommand = "Select DISTINCT column_name from patient_dynamic_table";
                 try {
                	 Statement st=con.createStatement();
                	 rs=st.executeQuery(stringCommand);
                	// return rs;
                 }
                 catch(Exception e) {
                	 System.out.println(e);
                 }
             return rs;
         }
          
          
          //***************************************68 this service calling from 22*************************************
          List<PatientVisitInformation> ProcessVisitInformationDataTable1(ResultSet rs,ResultSet rs1)
          { 
        	  
        	  System.out.println("in 68");
       try
          {
        	  rs.beforeFirst();
        	  rs1.first();
          }
          catch(Exception e){
        	  System.out.println(e);
          }
              {
                	 System.out.println("hi 62");
                	List<PatientVisitInformation> visitList= new ArrayList<PatientVisitInformation>();
                  try 
                  {
      				if (rs.next()==true)
      				{
      					try 
      					{
      						do
      						{
      						      PatientVisitInformation visitDetails = new PatientVisitInformation();
      						       
      						    visitDetails.setVisitMedicinList(new ArrayList<VisitMedicin>());
      						    try 
      						    {
      								if (rs.getString(2)=="")
      								{
      								     visitDetails.setVisitDate(rs.getString(2));
      								}
      								else
      								{
      								    visitDetails.setVisitDate(rs.getString(2));
      								}
      								
      							} 
      						    catch (SQLException e1) 
      						    {
      								
      								System.out.println(e1);
      							}
      						    try 
      						    {
      						    visitDetails.setVisitId(rs.getString(1));
      						    visitDetails.setVisitInformation(rs.getString(4));
      						    visitDetails.setVisitMedicienString(rs.getString(5));
      						
      						    String imgpath = "";
      						    if(rs.getString(16)==null)
      						    {
      						    	imgpath="none.jpg";
      						    }
      						    else
      						    {
      						    	imgpath = rs.getString(16);
      						    }
      						    visitDetails.setPatientVisitImagepath(imgpath);
      						 
      						    visitList.add(visitDetails);
      						    
      						    File parent = new File("http:\\DoctorStudio\\PatientProfilesImages");
      						  
      						    File child = new File(parent,visitDetails.getPatientVisitImagepath());
      						    visitDetails.setPatientVisitImagepath(child.toString());
      						    }
      						    catch(Exception e)
      						    {
      						    	System.out.println(e);
      						    }
      						    String medicinelist = visitDetails.getVisitMedicienString();
      						    String[] splitstring = medicinelist.split("@");
      						    List<String> list=Arrays.asList(splitstring);
      						     
      						    visitDetails.setMedicienStringList(new ArrayList<String>());
      						    visitDetails.setMedicienStringList(list);
      						    try 
      						    {
      						    visitDetails.setVisitTitle(rs.getString(6));
      						    if (rs.getString(10)== "")
      						    {
      						        visitDetails.setPatientNextvisitdate(rs.getString(10));
      						    }
      						    else
      						    {
      						        visitDetails.setPatientNextvisitdate(rs.getString(10));
      						    }
      						 
      						    visitDetails.setVisitDiagonastic(rs.getString(13));
      						    visitDetails.setVisitBp(rs.getString(11));
      						    visitDetails.setVisitWeight(rs.getString(12));
      						    String visitTime = rs.getString(3);
      						    }
      						    catch(Exception e)
      						    {
      						    	System.out.println(e);
      						    	  
      						    }
      						   
      						   String expression =visitDetails.getVisitDate();
      						   int k=0;
      						  
      						    try
      						    {
      								
      								do
      								{
      									
      								try {
      									if(rs1.getString(1).equals(expression)) 
      									{
      										
      										  VisitMedicin visitmedicineDetails = new VisitMedicin();
      								          visitmedicineDetails.setMedicinName(rs1.getString(3));//.MedicienName = ;
      								          
      								          visitmedicineDetails.setMedicinpower( rs1.getString(5));//.Medicienpower = ;
      								          
      								          visitmedicineDetails.setMedicinQuntity(rs1.getString(7));//.MedicienQuntity =   ;
      								          
      								          visitmedicineDetails.setMedicinDos(rs1.getString(4));//.MedicienDos =  ;
      								          
      								          visitmedicineDetails.setMedicintype( rs1.getString(6));//.Medicientype =  ;
      								          
      								          visitmedicineDetails.setMedicinday(rs1.getString(8));//.Medicienday =  ;
      								        
      								          
      								          visitmedicineDetails.setMedicinTotalQuntity(rs1.getString(10));//.MedicienTotalQuntity =   ;
      								          visitmedicineDetails.setVisitExternalInternal( rs1.getString(9));//.VisitExternalInternal = ;
      								          visitmedicineDetails.setVisitFromDate(rs1.getString(11));//.VisitFromDate =   ;
      								          
      								          visitmedicineDetails.setVisitToDate(rs1.getString(12));//.setVisitToDate();
      								          
      								          visitDetails.getVisitMedicinList().add(visitmedicineDetails);
      								 
      								       
      								          visitList.add(visitDetails);
      									}
      								}
      								catch(Exception e)
      								{
      								System.out.println(e);
      								}
      								}while(rs1.next());
      							} 
      						    catch (SQLException e)
      						    {
      								System.out.println(e);
      								
      							}
      						}while(rs.next());
      					} 
      					catch (SQLException e) 
      					{
      						System.out.println(e);
      					}
      				}
      			} 
                  catch (SQLException e)
                  {
      				System.out.println(e);
      			  }
                  return visitList;
              }
       }
          
     //****** 69 this is called by service no 17 and 18 **************************************************************************************************************     
          
          public List<String> GetMedicieList()
          {
            // DatabaseConnection db=new DatabaseConnection();
            // Connection con=db.getConnection();
              List<String> lstMedicienList = new ArrayList<String>();
             
                      String selectMedicien = "Select medicien_name,medicien_power from medicien_list";
                      try {
                    	  
                    	  Statement st=con.createStatement();
                    	  ResultSet rs=st.executeQuery(selectMedicien);
                    	  while(rs.next()) {
                    		  System.out.println(rs.getString(1));
                    		  System.out.println(rs.getString(2));
                    	  lstMedicienList.add(rs.getString(1));
                    	 lstMedicienList.add(rs.getString(2));
                    	  }
                  
              }
                      catch(Exception e) 
                      {
                    	  System.out.println(e);
                      }
              return lstMedicienList;
          }


          
 //=======================================================Subscriber Module by Ashish========================================================================================//
    	  
    //65-----------------------------------------Add Subscriber Details------------------------------------------------------------------//         

  		public String AddSubscriber(String subscriberdetails)
  		{
  			//DatabaseConnection db=new DatabaseConnection();
           // Connection con=db.getConnection();
  			
  			int error=0;
            JSONObject resobj=new JSONObject();
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
            Date date = new Date();  
           
            String startdate=formatter.format(date);
          
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
           
            String time=dtf.format(now);
           
            String newusername="";
            String reciever_mail="";
            String reciverphonenumber="";
            String password="";
           // System.out.println(subscriberdetails.toString());
  			try 
  			{
				JSONObject obj=new JSONObject(subscriberdetails);
				JSONObject detail=obj.getJSONObject("SubscriptionInformation");
				
				
				// String firstname= detail.getString("Firstname");
				 //String lastname= detail.getString("Lastname");
				 String clinicname=detail.getString("Clinicname");
				 String mobile=detail.getString("MobileNumber");  
				 reciever_mail = detail.getString("EmailId");
			 
				 reciverphonenumber= detail.getString("MobileNumber");
				 password = detail.getString("Password");
				 
				    clinicname= clinicname.replaceAll("\\s", "");
		            String username=clinicname.substring(0, 4);
		            String mobileno=mobile.substring(6, 10);
		           
		             newusername=username.toUpperCase()+mobileno;
		             System.out.println("newusername Testing: "+newusername);
		            
		          String checkmobilenumber="select mobilenumber from subscriber_module where mobilenumber='"+detail.getString("MobileNumber")+"'";
		          Statement stmno=con.createStatement();
		          ResultSet rsmno=stmno.executeQuery(checkmobilenumber);
		          if(rsmno.next()==true)
		          {
		        	  	resobj.put("StatusCode", 403);
						resobj.put("Message", "mobilenoexist");
						return resobj.toString();
		          }
				
				
		          String checkusername="select username from subscriber_module where username='"+newusername+"'";
		          
		          Statement stuser=con.createStatement();
		          ResultSet rsuser=stuser.executeQuery(checkusername);
		          if(rsuser.next())
		          {
		        	  String name=rsuser.getString(1);
		        	  System.out.println("newusername: "+newusername+" rsuser.getString(1): "+rsuser.getString(1));
		        	  while(name.equals(newusername))    // if user name already exist the add one more char and check again till it generate unique user name
		        	  {
		        	 
		        		  Random r = new Random();
		        		  char extrabit='A';
		        		  String alphabet = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
		        		  for (int i = 0; i < 1; i++) {
		        			  extrabit= (alphabet.charAt(r.nextInt(alphabet.length())));
		        		  }
		        		  newusername=newusername+extrabit;
		        		  System.out.println("in while: "+newusername);
		        		  ResultSet rsuser1=stuser.executeQuery(checkusername);
		        		  rsuser1.next();
		        		  name=rsuser1.getString(1);
		        	  }
		          }
		          
				
					String insertintosubscriber="insert into subscriber_module (firstname, lastname, clinicname,clinicaddress,mobilenumber,emailid,username,password,startdate,time,enddate,isactive, userrole) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					String insertintologinmaster="insert into login_master(user_password,user_roll,user_email_id,user_name,user_isActive, subscriptionid) values(?,?,?,?,?,?)";
					
					PreparedStatement ps=con.prepareStatement(insertintosubscriber);
					ps.setString(1, detail.getString("Firstname"));
					ps.setString(2, detail.getString("Lastname"));
					ps.setString(3, detail.getString("Clinicname"));
					ps.setString(4, detail.getString("Address"));
					ps.setString(5, detail.getString("MobileNumber"));
					ps.setString(6, detail.getString("EmailId"));
					ps.setString(7, newusername);
					ps.setString(8, detail.getString("Password"));
					ps.setString(9, startdate);
					ps.setString(10, time);
					ps.setString(11, "2999-12-31");
					ps.setInt(12, 1);
					ps.setString(13, "doctor");
					
					ps.executeUpdate();
					
					
					
					//get subscriber id
					String getsubsid = "select subscriptionid from subscriber_module where mobilenumber='"+mobile+"'";
					
					Statement st_id = con.createStatement();
					ResultSet rs_id = st_id.executeQuery(getsubsid);
					
					rs_id.next();
					String subscription_id = rs_id.getString(1);
					
					
					
					 PreparedStatement ps1=con.prepareStatement(insertintologinmaster);
					 
					 ps1.setString(1, detail.getString("Password"));
					 ps1.setString(2, "doctor");
					 ps1.setString(3, detail.getString("EmailId"));
					 ps1.setString(4, newusername);
					 ps1.setInt(5, 1);
					 ps1.setString(6, subscription_id);
					 
					
					ps1.executeUpdate();
					
					resobj.put("StatusCode", 200);
					resobj.put("Message", "success");
					resobj.put("username",newusername);
					
					System.out.println(resobj.toString());
					
					
			
			}
  			catch (Exception e)
  			{
  				System.out.println(e);
  				
  				try
  				{
  					
  					if(e.toString().contains("mobilenumber_UNIQUE"))
  					{
  						resobj.put("StatusCode", 403);
  						resobj.put("Message", "mobilenoexist");
  						return resobj.toString();
  					}
  					
  					
  				}
  				catch(Exception e1)
  				{
  					System.out.println(e1);
  				}
  				
			}
  			
  			SendPasswordAtEmail(reciever_mail, password,newusername);
  			
//  			MailMail m=(MailMail)context.getBean("mailMail");
// 		
//  			String sender="support";//write here sender gmail id  
//  			String receiver=reciever_mail;//write here receiver id  
//  			m.sendMail(sender,receiver,"User Id Of DoctorStudio ","Dear User this is Your User Id : "+newusername +"  Note That for further Login");  
//  
  			
  			
  			
  			
  			return resobj.toString();
  		}

  	

  //66------------------------------------------------------Update Subscriber Details--------------------------------------------------------//		
		
  		public String UpdateSubscriber(String subscriberdetails)
  		{
  			//DatabaseConnection db=new DatabaseConnection();
           // Connection con=db.getConnection();
            
            JSONObject resobj=new JSONObject();
            
            try
            {
            	JSONObject obj=new JSONObject(subscriberdetails);
            	
            	String subscriber="'"+obj.getString("subscriberid")+"'";
            	String selectsubscriber="select subscriber_id from subscriber_module where subscriber_id="+subscriber;
            	
            	Statement st =con.createStatement();
            	ResultSet rs=st.executeQuery(selectsubscriber);
            	
            	if(rs.next()==false)
            	{
            		resobj.put("StatusCode", 404);
					resobj.put("Message", "subscriberidenotxist");
					return resobj.toString();
            	}
            	else
            	{
            		//System.out.println(obj.getString("clinicname"));
            		String update="update subscriber_module set clinicname=?,clinicaddress=? where subscriber_id="+subscriber;
            		//System.out.println(update);
            		PreparedStatement ps=con.prepareStatement(update);
            		
            		ps.setString(1,obj.getString("clinicname"));
            		ps.setString(2,obj.getString("clinicaddress"));
            		
            		ps.executeUpdate();
            		
            		resobj.put("StatusCode", 200);
					resobj.put("Message", "success");
            		
            	}
            	
            	
            }
            catch(Exception e)
            {
            	try
            	{
            		resobj.put("StatusCode", 403);
            		resobj.put("Message", e);
            		return resobj.toString();
            	}
            	catch(Exception e1)
            	{
            		System.out.println(e1);
            	}
            }
			
            return resobj.toString();
		}


            
  //67--------------------------------------Delete Subscriber Detail--------------------------------------------------------------//  
  	
		public String DeleteSubscriber(String userid)
		{
			//DatabaseConnection db=new DatabaseConnection();
          //  Connection con=db.getConnection();
            
			
			String subscription = getSubscriptionId(userid);
			
            JSONObject resobj=new JSONObject();
            String selectdetail="select *from subscriber_module where subscriber_id="+"'"+subscription+"'";
            
            try
            {
            	Statement st=con.createStatement();
            	ResultSet rs=st.executeQuery(selectdetail);
            	if(rs.next()==false)
            	{
            		resobj.put("StatusCode", 404);
					resobj.put("Message", "subscriberidenotxist");
					return resobj.toString();
            	}
            	else
            	{
            		String updatedetail="update subscriber_module set isactive=0 where subscriber_id="+"'"+subscription+"'";
            		PreparedStatement ps=con.prepareStatement(updatedetail);
            		ps.executeUpdate();
            		
            		resobj.put("StatusCode", 200);
					resobj.put("Message", "success");
            	}
            
            }
            catch(Exception e)
            {
            	System.out.println(e);
            	try
            	{
            		resobj.put("StatusCode", 403);
            		resobj.put("Message", e);
            		return resobj.toString();
            	}
            	catch(Exception e1)
            	{
            		System.out.println(e1);
            	}
            }
            
            System.out.println(selectdetail);
			return resobj.toString();
		}

		
		
//68-----------------------------------------Add Medicine Group Details----------------------------------------------------------//		
		
		
		public String AddMedicineGroup(String medicinedetails, String userid)
		{
			//DatabaseConnection db=new DatabaseConnection();
           // Connection con=db.getConnection();
			
			String subscription = getSubscriptionId(userid);
			
            JSONObject resobj=new JSONObject();
            
            int tracerror = 0;
            
			try
			{
				
				List<MedicineGroupDetails> groupDetailsList = new ArrayList<MedicineGroupDetails>();
				
				JSONObject group = new JSONObject(medicinedetails);
				String groupobject = group.getString("MedicineGroup");
				
				JSONObject groupList = new JSONObject(groupobject);
			
				JSONArray array = groupList.getJSONArray("VisitMedicienList");
				System.out.println(array.length());
				
				String groupname="";
				for(int i = 0; i<array.length(); i++)
				{
					MedicineGroupDetails addmedicinedetail = new MedicineGroupDetails();
					System.out.println(array.getString(i));
					
					JSONObject obj = new JSONObject(array.getString(i));
					
					if(i==0)
					{
						groupname=obj.getString("MedicienGroupName");
						
						addmedicinedetail.setGroupName(groupname);
						addmedicinedetail.setMedicineName(obj.getString("MedicienName"));
						addmedicinedetail.setMedicinePower(obj.getString("Medicienpower"));
						addmedicinedetail.setMedicineType(obj.getString("Medicientype"));
						
						if(groupname.equals(""))
						{
							resobj.put("StatusCode", 500);
							resobj.put("Message", "groupnameempty");
							return resobj.toString();
						}
					}
					else
					{

						addmedicinedetail.setGroupName(groupname);
						addmedicinedetail.setMedicineName(obj.getString("MedicienName"));
						addmedicinedetail.setMedicinePower(obj.getString("Medicienpower"));
						addmedicinedetail.setMedicineType(obj.getString("Medicientype"));
					
					}
					
					groupDetailsList.add(addmedicinedetail);
					
					
				}
				
				System.out.println("after add: "+groupDetailsList.get(0).getMedicineName());
				System.out.println("size: "+groupDetailsList.size());
				
				String checkgroupname="select group_name from medicine_group_name where group_name="+"'"+groupname+"' and subscriptionid='"+subscription+"'";
				
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(checkgroupname);
				
				if(groupDetailsList.size()>0)
				{
					if(rs.next()==true)
					{
						resobj.put("StatusCode", 404);
						resobj.put("Message", "groupnameexist");
						return resobj.toString();
					}
					else
					{
						String insertintogroupname= "insert into medicine_group_name(group_name, subscriptionid) values(?,?)";
					
						String insertintogroupdetails= "insert into medicine_group_details(group_name,medicine_name,medicine_power,medicine_type, subscriptionid) values(?,?,?,?,?)";
					
						for(int i = 0; i<groupDetailsList.size(); i++)
						{
							if(i==0)
							{
								PreparedStatement forgroupname=con.prepareStatement(insertintogroupname);
								forgroupname.setString(1, groupname);
								forgroupname.setString(2, subscription);
								
								PreparedStatement forgroupdetails=con.prepareStatement(insertintogroupdetails);
								forgroupdetails.setString(1, groupname);
								forgroupdetails.setString(2, groupDetailsList.get(i).getMedicineName());
								forgroupdetails.setString(3, groupDetailsList.get(i).getMedicinePower());
								forgroupdetails.setString(4, groupDetailsList.get(i).getMedicineType());
								forgroupdetails.setString(5, subscription);
								
								forgroupname.executeUpdate();
								forgroupdetails.executeUpdate();
							}
							else
							{
								PreparedStatement forgroupdetails=con.prepareStatement(insertintogroupdetails);
								forgroupdetails.setString(1, groupname);
								forgroupdetails.setString(2, groupDetailsList.get(i).getMedicineName());
								forgroupdetails.setString(3, groupDetailsList.get(i).getMedicinePower());
								forgroupdetails.setString(4, groupDetailsList.get(i).getMedicineType());
								forgroupdetails.setString(5, subscription);
								forgroupdetails.executeUpdate();
							}
						}
					
						
						resobj.put("StatusCode", 200);
						resobj.put("Message", "success");
					
					}
				}
				
			} 
			catch (JSONException e)
			{
				tracerror = 1;
				System.out.println(e);
			}
			catch(Exception e)
			{
				tracerror = 1;
				System.out.println(e);
			}
			
			if(tracerror == 1)
			{
				try 
				{
					resobj.put("StatusCode", 401);
					resobj.put("Message", "error");
					return resobj.toString();
				}
				catch (JSONException e)
				{
					
					System.out.println(e);
				}
				
			}
			
			return resobj.toString();
		}
 	
		
//69-----------------------------------------Get Medicine Group Details----------------------------------------------------------//	
		

		public String GetMedicineGroup(String medicinedetails, String userid)
		{
			//DatabaseConnection db=new DatabaseConnection();
          //  Connection con=db.getConnection();
			
			
			String subscription = getSubscriptionId(userid);
			
            Gson gson = new GsonBuilder().serializeNulls().create();
           
            List<MedicineGroupDetails> medicineGroup =new ArrayList<MedicineGroupDetails>();
            JSONObject resobj=new JSONObject();
            int i=0;
            String groupName="'"+medicinedetails+"'";
            int traceError=0;
            String checkGroupExistance="select group_name from medicine_group_name where group_name="+groupName+" and subscriptionid='"+subscription+"'";
            
            try
            {
            	Statement st = con.createStatement();
            	ResultSet rs =st.executeQuery(checkGroupExistance);
            	
            	if(rs.next()==false)
            	{

					resobj.put("StatusCode", 404);
					resobj.put("Message", "notfound");
					return resobj.toString();
            	}
            	else
            	{
            		System.out.println("else");
            		String groupDetails="select *from medicine_group_details where group_name="+groupName+" and subscriptionid='"+subscription+"'";
            		
            		Statement st1 = con.createStatement();
                	ResultSet rs1 =st1.executeQuery(groupDetails);
                	
                	if(rs1.next()==false)
                	{
                		resobj.put("StatusCode", 404);
    					resobj.put("Message", "notfound");
    					return resobj.toString();
                	}
                	else
                	{
                		do
                		{
                			MedicineGroupDetails medicineDetails= new MedicineGroupDetails();
                			
                			medicineDetails.setGroupName(rs1.getString(2));
                			medicineDetails.setMedicineName(rs1.getString(3));
                			medicineDetails.setMedicinePower(rs1.getString(4));
                			medicineDetails.setMedicineType(rs1.getString(5));
                			
                			medicineGroup.add(medicineDetails);
                			
                		}while(rs1.next());
                		
                		
                	}
            	}
            	
            }
            catch(Exception e)
            {
            	traceError=1;
            	System.out.println(e);
            }
            
            if(traceError == 1)
            {
            	try
            	{
            		resobj.put("StatusCode", 500);
            		resobj.put("Message", "internalerror");
            		return resobj.toString();
            	}
            	catch(Exception e)
            	{
            		System.out.println(e);
            	}
            	
            }
            
            
			return gson.toJson(medicineGroup);
		}

			
         	
	//70-----------------------------------------Update Medicine Group Details----------------------------------------------------------//		
		
		public String UpdateMedicineGroup(String medicinedetails, String userid)
		{
			//DatabaseConnection db=new DatabaseConnection();
           // Connection con=db.getConnection();
			
			String subscription = getSubscriptionId(userid);
			
            JSONObject resobj=new JSONObject();
            
            int tracerror = 0;
            
			try
			{
				
				List<MedicineGroupDetails> groupDetailsList = new ArrayList<MedicineGroupDetails>();
				
				JSONObject group = new JSONObject(medicinedetails);
				String groupobject = group.getString("MedicineGroup");
				
				JSONObject groupList = new JSONObject(groupobject);
			
				JSONArray array = groupList.getJSONArray("VisitMedicienList");
				System.out.println(array.length());
				
				String groupname="";
				for(int i = 0; i<array.length(); i++)
				{
					MedicineGroupDetails addmedicinedetail = new MedicineGroupDetails();
					System.out.println(array.getString(i));
					
					JSONObject obj = new JSONObject(array.getString(i));
					
					if(i==0)
					{
						groupname=obj.getString("MedicienGroupName");
						
						addmedicinedetail.setGroupName(groupname);
						addmedicinedetail.setMedicineName(obj.getString("MedicienName"));
						addmedicinedetail.setMedicinePower(obj.getString("Medicienpower"));
						addmedicinedetail.setMedicineType(obj.getString("Medicientype"));
						
						if(groupname.equals(""))
						{
							resobj.put("StatusCode", 500);
							resobj.put("Message", "groupnameempty");
							return resobj.toString();
						}
					}
					else
					{

						addmedicinedetail.setGroupName(groupname);
						addmedicinedetail.setMedicineName(obj.getString("MedicienName"));
						addmedicinedetail.setMedicinePower(obj.getString("Medicienpower"));
						addmedicinedetail.setMedicineType(obj.getString("Medicientype"));
					
					}
					
					groupDetailsList.add(addmedicinedetail);
					
					
				}
				
				System.out.println("after add: "+groupDetailsList.get(0).getMedicineName());
				System.out.println("size: "+groupDetailsList.size());
				
				String checkgroupname="select group_name from medicine_group_name where group_name="+"'"+groupname+"' and subscriptionid='"+subscription+"'";
				
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(checkgroupname);
				
				if(groupDetailsList.size()>0)
				{
					if(rs.next()==true)
					{
						String deletegroupname="delete from medicine_group_name where group_name="+"'"+groupname+"' and subscriptionid='"+subscription+"'";
						
						String deletegroupdetail="delete from medicine_group_details where group_name="+"'"+groupname+"' and subscriptionid='"+subscription+"'";
						
						PreparedStatement deletegroup=con.prepareStatement(deletegroupname);
						PreparedStatement deletedetail=con.prepareStatement(deletegroupdetail);
						deletegroup.executeUpdate();
						deletedetail.executeUpdate();
						
						
					}
					
					
						String insertintogroupname= "insert into medicine_group_name(group_name, subscriptionid) values(?,?)";
					
						String insertintogroupdetails= "insert into medicine_group_details(group_name,medicine_name,medicine_power,medicine_type, subscriptionid) values(?,?,?,?,?)";
					
						for(int i = 0; i<groupDetailsList.size(); i++)
						{
							if(i==0)
							{
								PreparedStatement forgroupname=con.prepareStatement(insertintogroupname);
								forgroupname.setString(1, groupname);
								forgroupname.setString(2, subscription);
								
								
								PreparedStatement forgroupdetails=con.prepareStatement(insertintogroupdetails);
								forgroupdetails.setString(1, groupname);
								forgroupdetails.setString(2, groupDetailsList.get(i).getMedicineName());
								forgroupdetails.setString(3, groupDetailsList.get(i).getMedicinePower());
								forgroupdetails.setString(4, groupDetailsList.get(i).getMedicineType());
								forgroupdetails.setString(5, subscription);
								
								forgroupname.executeUpdate();
								forgroupdetails.executeUpdate();
							}
							else
							{
								PreparedStatement forgroupdetails=con.prepareStatement(insertintogroupdetails);
								forgroupdetails.setString(1, groupname);
								forgroupdetails.setString(2, groupDetailsList.get(i).getMedicineName());
								forgroupdetails.setString(3, groupDetailsList.get(i).getMedicinePower());
								forgroupdetails.setString(4, groupDetailsList.get(i).getMedicineType());
								forgroupdetails.setString(5, subscription);
								
								forgroupdetails.executeUpdate();
							}
						}
					
						
						resobj.put("StatusCode", 200);
						resobj.put("Message", "success");
					
					}
				
				
			} 
			catch (JSONException e)
			{
				tracerror = 1;
				System.out.println(e);
			}
			catch(Exception e)
			{
				tracerror = 1;
				System.out.println(e);
			}
			
			if(tracerror == 1)
			{
				try 
				{
					resobj.put("StatusCode", 401);
					resobj.put("Message", "error");
					return resobj.toString();
				}
				catch (JSONException e)
				{
					
					System.out.println(e);
				}
				
			}
			
			return resobj.toString();
		}

			
		
//71---------------------------------------Set Header Value For Header Space------------------------------------------------------//		
	
		public String SetHeaderSize(String value, String userid) 
		{
			//DatabaseConnection db=new DatabaseConnection();
          //  Connection con=db.getConnection();
			
			
			String subscription = getSubscriptionId(userid);
			
            JSONObject resobj = new JSONObject();
            
            
            
            // check first that subscriptionid exist or not if exist then update or insert
            
            String checksubs= "select *from setting where subscriptionid='"+subscription+"'";
            
            try
            {
            	Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(checksubs);
                
                if(rs.next() == true)
                {
                	
                	 String setheadersize="update setting set headersetting="+value +" where subscriptionid='"+subscription+"'";
                     try 
                     {
         				PreparedStatement ps=con.prepareStatement(setheadersize);
         				ps.executeUpdate();
         				
         				try 
         				{
         					resobj.put("message", "success");
         					//return resobj.toString();
         				} 
         				catch (JSONException e)
         				{
         					System.out.println(e);
         				}
         			}
                     catch (SQLException e) 
                     {
         				System.out.println(e);
         				try 
         				{
         					resobj.put("message", "error");
         					//return resobj.toString();
         				} 
         				catch (JSONException e1)
         				{
         					System.out.println(e1);
         				}
         			}
         			
                	
                	
                }
                else
                {
                	String insert = "insert into setting (headersetting, subscriptionid) values(?,?)";
                	
                	PreparedStatement ins=con.prepareStatement(insert);
                	ins.setString(1,value);
                	ins.setString(2, subscription);
                	
     				ins.executeUpdate();
     				
     				resobj.put("message", "success");
                }
               
            }
            catch(Exception e)
            {
            	System.out.println(e);
            }
            return resobj.toString();
		}

		
	//72---------------------------------------GET Header Value For Header Space------------------------------------------------------//
		
		public String GetHeaderSize(String userid)
		{
			//DatabaseConnection db=new DatabaseConnection();
            //Connection con=db.getConnection();
			
			String subscription = getSubscriptionId(userid);
			
            JSONObject resobj = new JSONObject();
            String getheader="select headersetting from setting where subscriptionid='"+subscription+"'";
            
            try
            {
				Statement st =con.createStatement();
				ResultSet rs=st.executeQuery(getheader);
				
				rs.next();
				try 
				{
					resobj.put("message", rs.getInt(1));
					//return resobj.toString();
				} 
				catch (JSONException e)
				{
					System.out.println(e);
				}
			}
            catch (SQLException e)
            {
            	System.out.println(e);
            	try 
				{
					resobj.put("message", "error");
					//return resobj.toString();
				} 
				catch (JSONException e1)
				{
					System.out.println(e1);
				}
            	
				
			}
            
			return resobj.toString();
		}

		
	//73-------------------------------------------------------------Register  sub user details------------------------------------------------------//
		
		
		
		// this method return subscription id for given user id from any where in this application 
		
		//******************************************************************************************************//
		public String getSubscriptionId(String userid)
		{
			
			
			String subscritionid="";
			
			String checkparentuser = "select parentid from subscriber_module where username='"+userid+"'";
			
			try
			{
				Statement st_get_parent=con.createStatement();
				ResultSet rs_get_parent=st_get_parent.executeQuery(checkparentuser);
				
				
				
				rs_get_parent.next();
				
				if(rs_get_parent.getString(1) != null)
				{
					System.out.println("in if");
					 subscritionid=rs_get_parent.getString(1);
				}
				else
				{
					
					String getsubscriptionid="select subscriptionid from subscriber_module where username='"+userid+"'";
					
					try
					{
						System.out.println("in else");
						Statement st_getid=con.createStatement();
						ResultSet rs_getid=st_getid.executeQuery(getsubscriptionid);
						
						if(rs_getid.next())
						{
							 subscritionid=rs_getid.getString(1);
						}
						
						 
						
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
					
				}
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			System.out.println("subscritionid: "+subscritionid);
			
			return subscritionid;
		}
		
		//****************************************************************************************************************//
		
		//main method
		public String SubUserDetails(String userdetails,String userid) 
		{
			
			String subscriberid=getSubscriptionId(userid);
			
			if(subscriberid.isEmpty() || subscriberid =="")
			{
				//System.out.println("subscritionid_not_found");
				return "subscritionid_not_found";
			}
			
			  System.out.println("subscriberid: "+subscriberid);
			
			  JSONObject resobj=new JSONObject();
              
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
              Date date = new Date();  
             
              String startdate=formatter.format(date);
            
              DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
              LocalDateTime now = LocalDateTime.now();
             
              String time=dtf.format(now);
             
            System.out.println(time);
            
           
            
    			try 
    			{
  				JSONObject obj=new JSONObject(userdetails);
  				JSONObject detail=obj.getJSONObject("SubUser");
  				
  				String adminid=(String) subscriberid;
  				 
  				String clinicname=detail.getString("Clinicname");
  				 //String user=detail.getString("UserName");
  				 String mobile=detail.getString("MobileNumber");  
  				 String emailid=detail.getString("EmailId");
  				String password=detail.getString("Password");
  				String role=detail.getString("UserRole");
  				
  				clinicname = clinicname.replaceAll("\\s", "");
  		            String username=clinicname.substring(0, 4);
  		            String mobileno=mobile.substring(6, 10);
  		           
  		            String newusername=username.toUpperCase()+mobileno;
  		           System.out.println("newusername: "+newusername);
  		            
  		          String checkmobilenumber="select mobilenumber from subscriber_module where mobilenumber='"+detail.getString("MobileNumber")+"'";
		          Statement stmno=con.createStatement();
		          ResultSet rsmno=stmno.executeQuery(checkmobilenumber);
		          if(rsmno.next()==true)
		          {
		        	  	resobj.put("StatusCode", 403);
						resobj.put("Message", "mobilenoexist");
						return resobj.toString();
		          }
  		            
  		            
  		            
  		            
  		            
  		            String checkusername="select username from subscriber_module where username='"+newusername+"'";
		          
		          Statement stuser=con.createStatement();
		          ResultSet rsuser=stuser.executeQuery(checkusername);
		          if(rsuser.next())
		          {
		        	  String name=rsuser.getString(1);
		        	  System.out.println("newusername: "+newusername+" rsuser.getString(1): "+rsuser.getString(1));
		        	  while(name.equals(newusername))    // if user name already exist the add one more char and check again till it generate unique user name
		        	  {
		        	 
		        		  Random r = new Random();
		        		  char extrabit='A';
		        		  String alphabet = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
		        		  for (int i = 0; i < 1; i++) {
		        			  extrabit= (alphabet.charAt(r.nextInt(alphabet.length())));
		        		  }
		        		  newusername=newusername+extrabit;
		        		  System.out.println("in while: "+newusername);
		        		  ResultSet rsuser1=stuser.executeQuery(checkusername);
		        		  rsuser1.next();
		        		  name=rsuser1.getString(1);
		        	  }
		          }
		          
  		            
  		           
  		            
  		            
		          String insertintosubscriber="insert into subscriber_module (firstname, lastname, clinicname,clinicaddress,mobilenumber,emailid,username,password,startdate,time,enddate,isactive, userrole, parentid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			     
		          //String insertdetail="insert into subuser_module (admin_id,clinicname,clinicaddress,mobilenumber,emailid,username,password,startdate,time,enddate,isactive) values(?,?,?,?,?,?,?,?,?,?,?)";
  					
  					String insertintologinmaster="insert into login_master(user_RegistrationId,user_password,user_roll,user_email_id,user_name,user_isActive, subscriptionid) values(?,?,?,?,?,?,?)";
  					
  					PreparedStatement ps=con.prepareStatement(insertintosubscriber);
  					
  					ps.setString(1, detail.getString("Firstname"));
  					ps.setString(2, detail.getString("Lastname"));
  					ps.setString(3, detail.getString("Clinicname"));
  					ps.setString(4, detail.getString("Address"));
  					ps.setString(5, detail.getString("MobileNumber"));
  					ps.setString(6, detail.getString("EmailId"));
  					ps.setString(7, newusername);
  					ps.setString(8, detail.getString("Password"));
  					ps.setString(9, startdate);
  					ps.setString(10, time);
  					ps.setString(11, "2999-12-31");
  					ps.setInt(12, 1);
  					ps.setString(13, role);
  					ps.setString(14, subscriberid);
  					//ps.setString(1, adminid);
  					//ps.setString(2, detail.getString("Clinicname"));
  					//ps.setString(3, detail.getString("Address"));
  					//ps.setString(4, detail.getString("MobileNumber"));
  					//ps.setString(5, detail.getString("EmailId"));
  					//ps.setString(6, newusername);
  					//ps.setString(7, detail.getString("Password"));
  					//ps.setString(8, startdate);
  					//ps.setString(9, time);
  					//ps.setString(10, "2999-12-31");
  					//ps.setInt(11, 1);
  					
  					
  					
  					 PreparedStatement ps1=con.prepareStatement(insertintologinmaster);
  					 ps1.setString(1, adminid);
  					 ps1.setString(2, detail.getString("Password"));
  					 ps1.setString(3, role);
  					 ps1.setString(4, detail.getString("EmailId"));
  					 ps1.setString(5, newusername);
  					 ps1.setInt(6, 0);
  					 ps1.setString(7, adminid);
  					 
  					ps.executeUpdate();
  					ps1.executeUpdate();
  					
  					
  					SendPasswordAtEmail(emailid,password,newusername);
  					
  					resobj.put("StatusCode", 200);
  					resobj.put("Message", "success");
  					resobj.put("username",newusername);
			
    			}
    			catch (Exception e)
    			{
    				System.out.println(e);
    				try
    				{
    					
    					if(e.toString().contains("mobilenumber_UNIQUE"))
    					{
    						resobj.put("StatusCode", 403);
    						resobj.put("Message", "mobilenoexist");
    					}
    					if(e.toString().contains("username_UNIQUE"))
    					{
    						resobj.put("StatusCode", 403);
    						resobj.put("Message", "usernameexist");
    					}
    					
    					return resobj.toString();
    					
    				}
    				catch(Exception e1)
    				{
    					System.out.println(e1);
    				}
    				
  			}
    			return resobj.toString();
          	
  
		}

		
//74.----------------------------Check user is main user or sub user-------------------------------------------------------------//				
		
		public String checkUser(String userid) 
		{

			String getuser="select parentid from subscriber_module where username='"+userid+"'";
			
			JSONObject response = new JSONObject();
			
			try
			{
				Statement st =con.createStatement();
				ResultSet rs = st.executeQuery(getuser);
				
				rs.next();
				
				if(rs.getString(1) == null) // if rs.next is true it means user is subuser other wise it is main user
				{
					response.put("message", "mainuser");
				}
				else
				{
					response.put("message", "subuser");
					
				}
				
				
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			
			return response.toString();
		}

//75.=======================================Get Logo for Doctor =================================================================//	
		
		public String getLogoName(String userid)
		{
			JSONObject response = new JSONObject();
			
			String subscription = getSubscriptionId(userid);
			
			String getlogo="select logo_name from logo_path where subscriptionid='"+subscription+"'";
			System.out.println(System.getProperty("java.io.tmpdir"));
			
			try
			{
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(getlogo);
				
				rs.next();
				//System.out.println(rs.getRow());
				if(rs.getRow() == 0)
				{
					response.put("Message", "images/ira-tech.png");
					response.put("StatusCode", "504");
				}
				else
				{
					response.put("Message", rs.getString(1));
					response.put("StatusCode", "200");
					
				}
				
			}
			catch(Exception e)
			{
				System.out.println(e);
				return e.toString();
			}
			
			return response.toString();
		}

		
//76.---------------------------------Sent Mail To Patient---------------------------------------------------------------------//			
		
		public String sendMail(String PatientRegID, String email, String userid)
		{
			
			JSONObject response = new JSONObject();
			
			String subscription = getSubscriptionId(userid);
			//System.out.println("PatientRegID: "+PatientRegID+" subscription: "+subscription);
			try
			{
				
			
        	String patientdetails="select patient_first_name,patient_last_name,patient_age ,patient_gender ,patient_date_of_birth,patient_phone_number from patient_information_master where patient_reg_id='" + PatientRegID +"'  and subscriptionid='"+subscription+"'";
        	Statement sta=con.createStatement();
            ResultSet rsa=sta.executeQuery(patientdetails);
			
            
            String getTime= "select visit_id, patient_visit_date,patient_visit_time,patient_next_visit_date FROM "
            		+ "patient_visit_details where patient_reg_id ='"+PatientRegID+"' and subscriptionid='"+subscription+"'  order by visit_id desc limit 2";                   

           Statement st_time = con.createStatement();
           ResultSet rs_time = st_time.executeQuery(getTime);
           
           rs_time.next();
           String visitTime = rs_time.getString(3);
           
           String GetPatientInfo = "Select id, patient_visit_date,patient_visit_time,medicien_name, medicien_dos,"
            		+ "patient_medicien_power,patient_medicien_type,patient_medicien_quentity,patient_medicien_day,"
            		+ "patient_medicien_totalquntity, patient_medicien_ext_int_medicine, patient_medicien_from_date,"
            		+ "patient_medicien_to_date from patient_medicien_details where"
            		+ " patient_reg_id = '" + PatientRegID + "' and  patient_visit_time =  '" + visitTime + "'  and subscriptionid='"+subscription+"'";
         
           
            	Statement st1=con.createStatement();
            	ResultSet rs1=st1.executeQuery(GetPatientInfo);
            	
            	PathConfig PathforPdf = (PathConfig) context.getBean("myPath");
            	
            	String pdfPath = PathforPdf.getPdfPath();
            	
            	//String pdfPath ="H:\\tomcat 8.0\\webapps\\DoctorStudio\\PatientPdf";
            	
            	final String FILE_NAME = pdfPath+"\\"+"Prescription_of_"+PatientRegID+"_"+subscription+".pdf";

            	rsa.next();

		        Document document = new Document();

		        try {

		            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

		            //open
		            document.open();

		           
		            
		            	 Paragraph p = new Paragraph();
				            p.add("Name: "+rsa.getString(1) +" "+ rsa.getString(2));
				         Paragraph p1 = new Paragraph();
				            p1.add("Reg Id: "+PatientRegID);
				           // p.setAlignment(Element.ALIGN_CENTER);

				            document.add(p);
				            document.add(p1);
				            document.add(new Phrase("\n")); // for a line space
		           int i=1; 
		           while(rs1.next())
		           {
		        	   Paragraph p2 = new Paragraph();
		        	   
		        	   p2.add(i++ +". "+rs1.getString(4) +" ("+rs1.getString(6)+"), "+rs1.getString(7)+", "+rs1.getString(5)+", Qty:"+rs1.getString(8)+", Day:"+rs1.getString(9)+", "+rs1.getString(12)+" to "+rs1.getString(13));
		        	   
		        	   document.add(p2);
		           }

				            
		         // Paragraph p2 = new Paragraph();
		         // p2.add("This is my paragraph 2"); //no alignment

		           // document.add(p2);

		          //  Font f = new Font();
		           // f.setStyle(Font.BOLD);
		         //   f.setSize(8);

		          //  document.add(new Paragraph("This is my paragraph 3", f));

		            //close
		            document.close();

		            System.out.println("Done");
		         
		        } 
		        catch (FileNotFoundException | DocumentException e)
		        {
		            e.printStackTrace();
		            
		            response.put("StatusCode", 500);
		            response.put("Message", e);
		        } 
            	
		        
		        //Start Code Get Email Id and Password from Database
		        
		        String getEmailAndPassword = "select email, password from email_password where subscriptionid='"+subscription+"'";
		        
		        Statement st_getEmail = con.createStatement();
		        ResultSet rs_getEmail = st_getEmail.executeQuery(getEmailAndPassword);
		        
		        GMailsender sender;
		        
		        if(rs_getEmail.next() == true) // If User Have set email and password
		        {
		        	 sender = new GMailsender(rs_getEmail.getString(1), rs_getEmail.getString(2));
		        }
		        else   // Get default email and password from applicationContext.xml bean provided by Ira Technologies  
		        {
		        	  sender = (GMailsender) context.getBean("myEmailUserAndPassword");
		        }
		        
		        
		        //End Code
		        
		        

		         sender.addAttachment( pdfPath+"\\"+"Prescription_of_"+PatientRegID+"_"+subscription+".pdf","Prescription of "+PatientRegID);
				 sender.sendMail("Prescription Details", "Your Prescription is Given Bellow", email);
            
            }
            catch(Exception e)
            {
            	System.out.println(e);
            	try
            	{
            		response.put("StatusCode", 500);
    	            response.put("Message", e);
            		
            	}
            	catch(Exception ex)
            	{
            		System.out.println(ex);
            	}
            	
            }
			
			try
        	{
        		response.put("StatusCode", 200);
	            response.put("Message", "success");
        		
        	}
        	catch(Exception ex)
        	{
        		System.out.println(ex);
        	}
			
			
			return response.toString();
		}

	
		
//77.-----------------------------------Get Patient Email Id---------------------------------------------------------------//
		
		
		public String getMailId(String regid, String userid)
		{
			
			String subscription = getSubscriptionId(userid);
			
			String getEmail = "select user_email_id from user_registration_table where user_registration_id='"+regid+"' and subscriptionid='"+subscription+"'";
			
			String email="";
			
			JSONObject response = new JSONObject();
			
			try
			{
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(getEmail);
				
				rs.next();
				
				if(rs.getRow() == 0)
				{
					response.put("StatusCode", "200");
					response.put("Message", "");
					
				}
				else
				{
					response.put("StatusCode", "200");
					response.put("Message", rs.getString(1));
				}
				
				
			}
			catch(Exception e)
			{
				System.out.println(e);
				
			
				try
				{
					response.put("StatusCode", "500");
					response.put("Message", e);
				} 
				catch (JSONException e1)
				{
					System.out.println(e);
				}
				
				
			}
			
			
			return response.toString();
		}

		
//78.-----------------------------Save Notes In File----------------------------------------------------------------------//			
		
		public String saveNotesInFile(String notesdetail, String regid, String userid) 
		{
			
			String subscription = getSubscriptionId(userid);
			
			JSONObject response = new JSONObject();
			
			try
			{
				PathConfig NotesPath = (PathConfig) context.getBean("myPath");
				
				String notesPath = NotesPath.getNotesPath();
				//System.out.println(notesdetail);
				JSONObject objmain = new JSONObject(notesdetail);
				JSONArray arr = objmain.getJSONArray("savenotes");
				
				PrintWriter writer = new PrintWriter(notesPath+"\\notes_of_"+regid+"_"+subscription+".txt", "UTF-8");
				
				for(int i=0 ; i<arr.length(); i++)
				{
					writer.println(arr.getString(i));
					
				}
	
				writer.close();
			
				
				
				//Read text File
				File file = new File(notesPath+"\\notes_of_"+regid+"_"+subscription+".txt");
				
				FileReader fileReader = new FileReader(file);
				
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				StringBuffer stringBuffer = new StringBuffer();
				
				String line;
				
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line);
					stringBuffer.append("\n");
					System.out.println("line: "+line);
				}
				fileReader.close();
				System.out.println("Contents of file:");
				System.out.println(stringBuffer.toString());
				
				
				
				
				
				
			}
			catch(Exception e)
			{
				System.out.println(e);	
				
				
				try 
				{
					response.put("StatusCode", "500");
					response.put("Message", e);
					return response.toString();
				} 
				catch (Exception e2) 
				{
					System.out.println(e2);
				}
			}
			
			
			try 
			{
				response.put("StatusCode", "200");
				response.put("Message", "success");
				return response.toString();
			} 
			catch (Exception e2) 
			{
				System.out.println(e2);
			}
			
			
			return response.toString();
		}

		
//79.----------------------------Get Notes From File--------------------------------------------------------------//
		
		public String getNotes(String regid, String userid)
		{
			String subscription = getSubscriptionId(userid);
			
			
			JSONObject response = new JSONObject();
			
			PathConfig NotesPath = (PathConfig) context.getBean("myPath");
			
			String notesPath = NotesPath.getNotesPath();
			
			
			//Read text File
			File file = new File(notesPath+"\\notes_of_"+regid+"_"+subscription+".txt");
			
			List<String> list = new ArrayList<String>();
			
			try {
					FileReader fileReader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					StringBuffer stringBuffer = new StringBuffer();
					String line;
				
					while ((line = bufferedReader.readLine()) != null) 
					{
						//stringBuffer.append(line);
						//stringBuffer.append("\n");
						//System.out.println("line: " + line);
						
						list.add(line);
						
					}
					
					fileReader.close();
					
					response.put("StatusCode", "200");
					response.put("Message", list);
				}
				catch (Exception e) 
				{
					System.out.println(e);
					
					try 
					{
						response.put("StatusCode", "500");
						response.put("Message", e);
						return response.toString();
					} 
					catch (Exception e2)
					{
						System.out.println(e2);
					}
				}
			
			return response.toString();
		}
		
		
		//80-------------------------------------------------------------------Edit Due Amount --------------------------------------------------------------------
		public String  EditPayment(String visitid,int SubmitDueAmount,String paymentRemark) 
		{
			JSONObject resobj=new JSONObject();
			int FinalDueAmount=0;
			int FinalPaidAmount=0;
			int TotalVisitAmount=0;
			int  DueAmount=0;
			int PaidAmount=0;
			int AdvancedAmount=0;
			String previousRemark="";
			String remark="";
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
            Date date = new Date();  
            String startdate=formatter.format(date);
			if(SubmitDueAmount>=0)
			{
				String getdueamount="select patient_visit_due_amount ,patient_visit_paid_amount,patient_visit_total_amount,patient_visit_advance_amount,payment_remark "
									+ "from patient_visit_details where visit_id='"+visitid+"'";
				try 
				{
					Statement st =con.createStatement();
					ResultSet rs=st.executeQuery(getdueamount);
			   
					rs.next();
					DueAmount=rs.getInt(1);
					PaidAmount=rs.getInt(2);
					TotalVisitAmount=rs.getInt(3);
					AdvancedAmount=rs.getInt(4);
					previousRemark=rs.getString(5);
					
					if(previousRemark==null)
					{
						
						 remark=SubmitDueAmount+" Rs Submit on this Date: "+startdate;	
					}
					else
					{
						
						 remark=paymentRemark+"\n"+SubmitDueAmount+" Rs Submit on this Date: "+startdate;
					}
					
				}
				catch(Exception e)
				{
					e.toString();
				}
				
				
				
				FinalDueAmount=DueAmount-SubmitDueAmount;
				FinalPaidAmount=PaidAmount+SubmitDueAmount;
				
//				System.out.println(paymentRemark);
//				System.out.println(previousRemark);
				
				
//				System.out.println(remark);
				
				
				if(TotalVisitAmount<FinalPaidAmount) 
				{
					AdvancedAmount=FinalPaidAmount-TotalVisitAmount;
					String updatePaymentAmount1="update patient_visit_details set patient_visit_due_amount='"+FinalDueAmount+"',patient_visit_paid_amount='"+FinalPaidAmount+"' ,"
												+ " patient_visit_advance_amount='"+AdvancedAmount+"',payment_remark='"+remark+"' where visit_id='"+visitid+"' ";
					try 
					{
						PreparedStatement pst1=con.prepareStatement(updatePaymentAmount1);
						pst1.executeUpdate();
					}
					catch(Exception e)
					{
						 e.toString();
					}
				}
								
				
				
				if(TotalVisitAmount>=FinalPaidAmount)
				{
				
					String updatePaymentAmount="update patient_visit_details set patient_visit_due_amount='"+FinalDueAmount+"',patient_visit_paid_amount='"+FinalPaidAmount+"' ,"
						+ " payment_remark='"+remark+"' where visit_id='"+visitid+"' ";
				
					try 
					{
					PreparedStatement pst=con.prepareStatement(updatePaymentAmount);
					pst.executeUpdate();
				
					}
				
					catch(Exception e)
				
					{
						 e.toString();
					}
				}
				
				
				
				if(TotalVisitAmount==FinalPaidAmount) 
				{
					try 
					{
						resobj.put("Message", "All Due Complete");
					}
					catch (JSONException e) 
					{
						
						e.toString();
					}
				}
				else
				{
				
					String due="Select patient_visit_due_amount from patient_visit_datails where visit_id='"+visitid+"'";	
					try 
					{
						Statement st1=con.createStatement();
						
						ResultSet rs1=st1.executeQuery(due);
						
						rs1.next();
						
						String remainingDueAmount=rs1.getString(1);
						
						resobj.put("Message", "Remaining due is +"+remainingDueAmount+"+ ");
				
					}
					
					catch(Exception e) 
					{
						e.toString();
					}
				}
				
			}
			
			return resobj.toString();

		}
		
		
		//81-----------------------------------------------------Change Payment Mode-----------------------------------// 
		
		
				public String ChangePaymentMode(String visitid,String mode,String visitDate) 
				{
					
					JSONObject resobj=new JSONObject();
					
					
					String changemode="update patient_visit_details set payment_mode='"+mode+"' where patient_reg_id='"+visitid+"' and patient_visit_date='"+visitDate+"' ";
					
					try {
						PreparedStatement pst=con.prepareStatement(changemode);
						pst.executeUpdate();
						resobj.put("Message", "Success");
					}
					catch(Exception e) {
						try {
							resobj.put("Message", "Not Success");
						} catch (JSONException e1) {
							
							e1.toString();
						}
						e.toString();
					}
					
					return resobj.toString();
					
				}
				
				
				
//		
				
				//82-----------------------------------------------------Permission To sub user As Manage User-----------------------------------// 
				public String Permission(String userid) {
					
					JSONObject resobj=new JSONObject();
					
					String get_userid="select user_RegistrationId from login_master where user_name='"+userid+"'";
					try {
						Statement st=con.createStatement();
						ResultSet rs=st.executeQuery(get_userid);
						if(rs.next()==true) 
						{
							if(rs.getString(1)==null) 
							{
								try 
								{
									resobj.put("Message", "Access");
								} catch (JSONException e)
								{
									
									e.toString();
								}
							}
							else  
							{
								try 
								{
									resobj.put("Message", "Not Accessible  Manage User");
								}
								catch (JSONException e)
								{
									e.toString();
								}
							}
						}
							
					}
					catch(SQLException e) {
						 e.toString();
					}
					
				return resobj.toString();
				}
				
				
	//83 -----------------------------------------------------Get sub user Details--------------------------------------------------//
				  
				
				public String getSubUserDetails(String userid)
				{
					 Gson gson = new GsonBuilder().serializeNulls().create();
					 int traceError=0;
					 String subscription_id="";
					 
					 List<SubUserDetails> subuserinfo=new ArrayList<SubUserDetails>();
					 
					 JSONObject resobj=new JSONObject();
					 String get_userid="Select subscriptionid from subscriber_module where username='"+userid+"'";
					 try {
						 Statement st1=con.createStatement();
						 ResultSet rs1=st1.executeQuery(get_userid);
						 if(rs1.next()==false)
						 {
							 	resobj.put("StatusCode", 404);
		    					resobj.put("Message", "NotFound");
		    					return resobj.toString(); 
						 }
						 else 
						 {
							
							  subscription_id=rs1.getString(1);
							  System.out.println("subscription_id:"+subscription_id);
						 }
						 
					 }
					 catch(Exception e) {
						 return e.toString();
					 }
					 
					 String get_subuserInfo="select subscriptionid,firstname,lastname,clinicname,clinicaddress,mobilenumber,userrole,emailid,username from subscriber_module "
					 						+ "where parentid='"+subscription_id+"'";
					 try {
						 	Statement st=con.createStatement();
						 	ResultSet rs= st.executeQuery(get_subuserInfo);
						 	if(rs.next()==false) 
						 	{

		                		resobj.put("StatusCode", 404);
		    					resobj.put("Message", "NotFound");
		    					return resobj.toString(); 
						 	}
						 	else
						 	{
					 
						 		do{
						 			SubUserDetails info=new SubUserDetails();
						 			info.setId(rs.getString(1));
						 			info.setFirstName(rs.getString(2));
						 			
						 			info.setLastName(rs.getString(3));
						 			info.setClinicName(rs.getString(4));
						 			info.setAddress(rs.getString(5));
						 			info.setMobileNumber(rs.getString(6));
						 			info.setRole(rs.getString(7));
						 			info.setEmailId(rs.getString(8));
						 			info.setUserName(rs.getString(9));
								 
						 			subuserinfo.add(info);
						 		}while(rs.next());
						 
						 	}
					 	}
					 
					 catch(Exception e)
			            {
			            	traceError=1;
			            	System.out.println(e);
			            }
			            
			            if(traceError == 1)
			            {
			            	try
			            	{
			            		resobj.put("StatusCode", 401);
			            		resobj.put("Message", "Internalerror");
			            		return resobj.toString();
			            	}
			            	
			            	catch(Exception e)
			            	{
			            		System.out.println(e);
			            	}
			            	
			            }
					 
					return gson.toJson(subuserinfo);
				}

				
//84.Ashish------------------------------------Set Email and Password of Doctor--------------------------------------------------------//				
				
			public String saveEmailAndPassword(String emaildetail, String userid)
			{
				
				String subscription = getSubscriptionId(userid);
				
				//System.out.println(emaildetail);
				JSONObject response = new JSONObject();
				
				try 
				{
					JSONObject objmain = new JSONObject(emaildetail);
					
					
					
					String getEmaildetails = "select *from email_password where subscriptionid='"+subscription+"'";
					
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(getEmaildetails);
					
					
					
					if(rs.next() == false) // if initially user have not set email and password 
					{
						String setEmailDetails = "insert into email_password (email, password, subscriptionid) values(?,?,?)";
						
						con.setAutoCommit(false);  // start transaction
						PreparedStatement ps = con.prepareStatement(setEmailDetails);
						
						ps.setString(1, objmain.getString("email"));
						ps.setString(2, objmain.getString("password"));
						ps.setString(3, subscription);
						
						ps.executeUpdate();
						
						con.commit(); // commit transaction
						
						
						
					}
					else   // if user have already set email and password then update here
					{
						String updateEmailDetails = "update email_password set email=? , password=? where subscriptionid='"+subscription+"'";
						
						con.setAutoCommit(false);
						
						PreparedStatement psupdate = con.prepareStatement(updateEmailDetails);
						
						psupdate.setString(1, objmain.getString("email"));
						psupdate.setString(2, objmain.getString("password"));
						
						psupdate.executeUpdate();
						
						con.commit();
						
						
					}
					
					response.put("StatusCode", "200");
					response.put("Message", "success");
					
				} 
				catch (Exception e)
				{
					System.out.println(e);
					try 
					{
						response.put("StatusCode", "500");
						response.put("Message", e);
						return response.toString();
					}
					catch (Exception e2) 
					{
						System.out.println(e2);
					}
					
				}
					
					return response.toString();
			}
				
			 //-----------------------------------------------------------Creating file and Writing the file -------------------------------------------------------------------------------
			
			 public void WrittingFile(char[] generateotp1,String filename) {
			    	
		    	  try {
		    		  try 
		    		  {
		    			 
		    			  //File file = new File("D:\\Tomcat9\\webapps\\DoctorStudio\\OTP\\"+filename+".txt");
		    			  File file = new File( pathcon+filename+".txt");
		    			  if (file.exists()) 
		    			  {
		    				     file.delete(); 
		    			  }
		    			  
		    			  if (file.createNewFile())
		    			  {
		    				  System.out.println("File is created!");
		    			  }
		    			  else
		    			  {
		    				  System.out.println("File already exists.");
		    			  }
		    		      
		    		  }
		    		  catch (IOException e)
		    		  {
		    		      e.printStackTrace();
		    		  }
		    		  
		             // FileWriter writer = new FileWriter("D:\\Tomcat9\\webapps\\DoctorStudio\\OTP\\"+filename+".txt", true);
		    		  FileWriter writer = new FileWriter(pathcon+filename+".txt", true);
		    		  int len=generateotp1.length;
		             
		             for(int i=0;i<len;i++) 
		             {
		            	 writer.write(generateotp1[i]);
		             }
		            
		             
		              writer.close();
		              
		          } catch (IOException e) {
		              e.printStackTrace();
		          }

		    }
			
			  
			  //----------------------------------------------------------------------Generate 6 digit Random Number-------------------------------------- 
			  static char[] OTP(int len)
				{
					

					// Using numeric values
					String numbers = "0123456789";

					// Using random method
					Random rndm_method = new Random();

					char[] otp = new char[len];

					for (int i = 0; i < len; i++)
					{
						
						otp[i] =numbers.charAt(rndm_method.nextInt(numbers.length()));
					}
					return otp;
				}

			  //-------------------------------------------------Send One Time Password--------------------------------------------------
			  
			  public String SendOTP(String username)
			  {
				 
				 JSONObject resobj=new JSONObject();
				 int flag=0;
				 String emailid="";
				 String userName="";
				 String userid="";
				 String fromusername="";
				 String fileName="";
				 // check userid is valid or not
				 
				 String checkuserid="select user_name,user_email_id,user_id from login_master where user_name='"+username+"'"; 
				 try {
					 Statement st=con.createStatement();
					 ResultSet rs=st.executeQuery(checkuserid);
					 if(rs.next()==true)
					 {
						 userName=rs.getString(1);
						 emailid=rs.getString(2);
						 userid=rs.getString(3);
						 
						 //clearTheFile();
						  fromusername=userName.substring(0, 4);
						  fileName=fromusername+userid;
						 resobj.put("Message", fileName);
						 resobj.put("Message1", userName);
						 resobj.put("Message2", emailid);
						 flag=1;
					 }
					 else
					 {
						 resobj.put("Message", "User Id is not Valid");
					 }
					 //resobj.put("Message", userName);
				 }
				 catch(Exception e)
				 {
					 e.toString();
				 }
				  
				if(flag==1) {
					int length = 6;
					
					 char[] generateotp= OTP(length);
					 
					 System.out.println(generateotp);
					
					 String otp1=new String(generateotp);
					
					 SendOTPATEmail(emailid,userName,otp1);
					
					 WrittingFile(generateotp,fileName);
				}
				 
				 
				 return resobj.toString();
			  }
			  
			  
			  
			//-----------------------------------------------------------------Check the Given OTP by user is correct------------------------------  
			  
			  public String checkotp(String givenotp,String fileName)
			  {
				  System.out.println("clearTheFileAftercheckOTP:"+givenotp);
				  JSONObject resobj=new JSONObject();
			    	try 
			    	{
			    	String line;
			    	//String link = givenotp;
			    	//FileReader reader = new FileReader("D:\\Tomcat9\\webapps\\DoctorStudio\\OTP\\"+fileName+".txt");
			    	FileReader reader = new FileReader(pathcon+fileName+".txt");
			    	System.out.println(pathcon+fileName);
			    	BufferedReader bReader = new BufferedReader(reader);
			    	int flag=0;
			    	while((line=bReader.readLine()) != null){
			    	if(line.equals(givenotp))
			    	{
			    		resobj.put("Message", "OTP is Correct");
			    		flag=1;
			    	}
			    	else
			    	{
			    		resobj.put("Message", "OTP is not Correct");
			    		
			    	}
			    	
			    	}
			    	bReader.close();
		    		reader.close();
		    		if(flag==1)
		    		{
		    			clearTheFileAftercheckOTP(fileName);
		    		}
			    }	
			    catch(Exception e) {
			    	e.toString();
			    }
			    	 
			    return resobj.toString();		
			    }
			  
		//---------------------------------------------------------clear-The-File-After-Success-check-OTP--------------------------------------------------	  
			  public  void clearTheFileAftercheckOTP(String filename)
			  {
			    	try
			    	{
			    		//System.out.println("clearTheFileAftercheckOTP with file Name:- "+filename);
			    		 
			    	
			    		  
			    		
			    		//File file = new File("D:\\Tomcat9\\webapps\\DoctorStudio\\OTP\\"+filename+".txt"); 
			    		File file = new File(pathcon+filename+".txt");
			    		if(file.delete())
			    		{
			    			System.out.println("File Deleted Sucessfully");
			    		}
			    		else
			    		{
			    			System.out.println("Fail to  Deleting the file ");
			    		}
			    	}
			    	catch(Exception e) 
			    	{
			    		e.toString();
			    	}
			    	
			    }
			  
			  
			  //-------------------------------------------------------------------Managing the Admin Portal--------------------------------------------------------//
			  
			  
			  //---------------------------------------------------------------Admin Login---------------------------------------------------------------------
			  
			  public String AdminLogin(String username,String password)
		  		{
		        	 Gson gson = new GsonBuilder().serializeNulls().create();
		        	 DrServiceResponce drServiceResponce = new DrServiceResponce();
		             List<PatientVisitInformation> visitList = new ArrayList<PatientVisitInformation>();
		             if (username.isEmpty() == true)
		             {
		            	 return  gson.toJson(new DrServiceResponce(ServiceStatusCodeEnum.PARAMETER_NULL.getNumVal(), "Parameter null", ""));
		             }
		             else
		             {
		                 String CounttotalVisits = "SELECT user_name,user_roll,user_RegistrationId ,subscriptionid FROM"
		                         		+ " login_master where user_name='" + username + "' and user_password='" + password +"'";
		                 try 
		                 {
		                     Statement st=con.createStatement();
		                     ResultSet rs=st.executeQuery(CounttotalVisits);
		                        	
		                     if(rs.next()) 
		                     {
		                         PatientVisitInformation visitDetails = new PatientVisitInformation();
		                         visitDetails.setPatientVisitName(rs.getString(1));
		                         visitDetails.setPatientVisitRoll(rs.getString(2));
		                         visitDetails.setPatientRegId(rs.getString(3));
		                         String var	=rs.getString(4);
		                        		 
		                        //Get parent user id name from subscription name it used only when role is patient
		                        		 
		                         String getparentname= "select username from subscriber_module where subscriptionid='"+var+"' ";
		                         try
		                         {
		                             Statement st_get_parent=con.createStatement();
		                         	 ResultSet rs_get_parent=st_get_parent.executeQuery(getparentname);
		                         	 	
		                         	 rs_get_parent.next();
		                         	 visitDetails.setParentId(rs_get_parent.getString(1));
		                        }
		                        catch(Exception e) 
		                        {
		                        	e.toString();
		                        }
		                        visitList.add(visitDetails);
		                     }
		                     drServiceResponce.setPatientsVisitInformationDetails(visitList);
		                 }
		                 catch(Exception e) 
		                 {
		                     System.out.println(e);
		                 }
		                        
		          }
		          drServiceResponce.setStatusCode(200);
		          drServiceResponce.setMessage("Success");
		          return  gson.toJson(drServiceResponce);
		      }
			  
			  
			  //---------------------------------------------------------------GEt All Admin Docotr ---------------------------------------------------------------------
			  
			  public String GetAllAdminDoctor() {
				  
				  Gson gson = new GsonBuilder().serializeNulls().create();
					 int traceError=0;
				
					 
					 List<SubUserDetails> subuserinfo=new ArrayList<SubUserDetails>();
					 
					 JSONObject resobj=new JSONObject();
					 
					 String adminrole="doctor";
					 String get_subuserInfo="select subscriptionid,firstname,lastname,clinicname,clinicaddress,mobilenumber,userrole,emailid,username,isactive from subscriber_module "
					 						+ "where userrole='"+adminrole+"' and parentid IS NULL";
					 try {
						 	Statement st=con.createStatement();
						 	ResultSet rs= st.executeQuery(get_subuserInfo);
						 	if(rs.next()==false) 
						 	{

		                		resobj.put("StatusCode", 404);
		    					resobj.put("Message", "NotFound");
		    					return resobj.toString(); 
						 	}
						 	else
						 	{
					 
						 		do{
						 			SubUserDetails info=new SubUserDetails();
						 			info.setId(rs.getString(1));
						 			info.setFirstName(rs.getString(2));
						 			
						 			info.setLastName(rs.getString(3));
						 			info.setClinicName(rs.getString(4));
						 			info.setAddress(rs.getString(5));
						 			info.setMobileNumber(rs.getString(6));
						 			info.setRole(rs.getString(7));
						 			info.setEmailId(rs.getString(8));
						 			info.setUserName(rs.getString(9));
						 			info.setIsActive(rs.getString(10));
						 			subuserinfo.add(info);
						 		}while(rs.next());
						 
						 	}
					 	}
					 
					 catch(Exception e)
			            {
			            	traceError=1;
			            	System.out.println(e);
			            }
			            
			            if(traceError == 1)
			            {
			            	try
			            	{
			            		resobj.put("StatusCode", 401);
			            		resobj.put("Message", "Internalerror");
			            		return resobj.toString();
			            	}
			            	
			            	catch(Exception e)
			            	{
			            		System.out.println(e);
			            	}
			            	
			            }
					 
					return gson.toJson(subuserinfo);
				}
			  
		  		
			  
	//---------------------------------------------------------------GEt All Admin Docotr_sub UserDetails ---------------------------------------------------------------------
			  
			  public String GetAllAdminDoctor_subuserdetails(String parentid) {
				  
				  Gson gson = new GsonBuilder().serializeNulls().create();
					 int traceError=0;
				
					 
					 List<SubUserDetails> subuserinfo=new ArrayList<SubUserDetails>();
					 
					 JSONObject resobj=new JSONObject();
					 
					 String adminrole="doctor";
					 String get_subuserInfo="select subscriptionid,firstname,lastname,clinicname,clinicaddress,mobilenumber,userrole,emailid,username,isactive from subscriber_module "
					 						+ "where parentid='"+parentid+"'";
					 try {
						 	Statement st=con.createStatement();
						 	ResultSet rs= st.executeQuery(get_subuserInfo);
						 	if(rs.next()==false) 
						 	{

		                		resobj.put("StatusCode", 404);
		    					resobj.put("Message", "NotFound");
		    					return resobj.toString(); 
						 	}
						 	else
						 	{
					 
						 		do{
						 			SubUserDetails info=new SubUserDetails();
						 			info.setId(rs.getString(1));
						 			info.setFirstName(rs.getString(2));
						 			
						 			info.setLastName(rs.getString(3));
						 			info.setClinicName(rs.getString(4));
						 			info.setAddress(rs.getString(5));
						 			info.setMobileNumber(rs.getString(6));
						 			info.setRole(rs.getString(7));
						 			info.setEmailId(rs.getString(8));
						 			info.setUserName(rs.getString(9));
						 			info.setIsActive(rs.getString(10));
						 			subuserinfo.add(info);
						 		}while(rs.next());
						 
						 	}
					 	}
					 
					 catch(Exception e)
			            {
			            	traceError=1;
			            	System.out.println(e);
			            }
			            
			            if(traceError == 1)
			            {
			            	try
			            	{
			            		resobj.put("StatusCode", 401);
			            		resobj.put("Message", "Internalerror");
			            		return resobj.toString();
			            	}
			            	
			            	catch(Exception e)
			            	{
			            		System.out.println(e);
			            	}
			            	
			            }
					 
					return gson.toJson(subuserinfo);
				}
			 
			  
			  
			  
			  
			 
			  
		 //---------------------------------------------------------------Activate Sub user---------------------------------------------------------------------
	 
		         public String ActivateSubUser(String userid,String isactive,String username) 
		         {
		        	 JSONObject resobj=new JSONObject();
		        	 
		        	 if(Integer.parseInt(isactive)!=1) 
		        	 {
		        		 System.out.println("in side if");
		        	 String activate="update subscriber_module set isactive=1  where subscriptionid='"+userid+"' ";
		        	 
		        	 String activatelogin="update login_master set user_isActive=0  where  user_name='"+username+"' ";
		        	 try 
		        	 {
		        		 PreparedStatement pst=con.prepareStatement(activate);
		        		 PreparedStatement pst1=con.prepareStatement(activatelogin);
		        		 pst.executeUpdate();
		        		 pst1.executeUpdate();
		        		 resobj.put("Message", "Success");
		        	 }
		        	 catch(Exception e)
		        	 {
		        		 return e.toString();
		        	 }
		        	 }
		        	 else
		        	 {
		        		 try
		        		 {
							resobj.put("Message", "Usre is Active");
		        		 }
		        		 catch (JSONException e)
		        		 {
							e.printStackTrace();
						 } 
		        	 }
		        		
		        	 
		        	 
		        	 return resobj.toString();
		         }


		       //---------------------------------------------------------------Suspend the Active User---------------------------------------------------------------------
		         
		         public String suspendSubUser(String userid,String isactive,String username) 
		         {
		        	 JSONObject resobj=new JSONObject();
		        	 
		        	 if(Integer.parseInt(isactive)!=0) 
		        	 {
		        		 System.out.println("in side if");
		        		 String suspend="update subscriber_module set isactive=0 where subscriptionid='"+userid+"' ";
		        	 
		        		 String suspendlogin="update login_master set user_isActive=1 where  user_name='"+username+"'";
		        		 try 
		        		 {
		        			 PreparedStatement pst=con.prepareStatement(suspend);
		        		 
		        			 PreparedStatement pst1=con.prepareStatement(suspendlogin);
		        			 pst.executeUpdate();
		        			 pst1.executeUpdate();
		        			 resobj.put("Message", "Success");
		        		 }
		        		 catch(Exception e)
		        		 {
		        			 return e.toString();
		        		 }
		        	 }
		        	 else
		        	 {
		        		 try
		        		 {
							resobj.put("Message", "Usre is Suspended");
		        		 }
		        		 catch (JSONException e)
		        		 {
							e.printStackTrace();
						 } 
		        	 }
		        		
		        	 
		        	 
		        	 return resobj.toString();
		         }
		         
		         
		         //---------------------------------------------------------------Activate Admin user---------------------------------------------------------------------
		         
		         public String ActivateAdminUser(String userid,String isactive,String username) 
		         {
		        	 JSONObject resobj=new JSONObject();
		        	 
		        	 if(Integer.parseInt(isactive)!=1) 
		        	 {
		        		 String adminrole="doctor";
		        		 System.out.println("in side if");
		        	 String activate1="update subscriber_module set isactive=1  where subscriptionid='"+userid+"' ";
		        	 
		        	 //String activatelogin1="update login_master set user_isActive=0  where  subscriptionid='"+userid+"' and  user_roll='"+adminrole+"' and user_RegistrationId IS NULL ";
		        	 String activatelogin1="update login_master set user_isActive=0  where  subscriptionid='"+userid+"' ";
		        	 try 
		        	 {
		        		 PreparedStatement pst=con.prepareStatement(activate1);
		        		 PreparedStatement pst1=con.prepareStatement(activatelogin1);
		        		 pst.executeUpdate();
		        		 pst1.executeUpdate();
		        		 resobj.put("Message", "Success");
		        	 }
		        	 catch(Exception e)
		        	 {
		        		 return e.toString();
		        	 }
		        	 }
		        	 else
		        	 {
		        		 try
		        		 {
							resobj.put("Message", "Admin is Active");
		        		 }
		        		 catch (JSONException e)
		        		 {
							e.printStackTrace();
						 } 
		        	 }
		        		
		        	 
		        	 
		        	 return resobj.toString();
		         }


		       //---------------------------------------------------------------Suspend the Active Admin User---------------------------------------------------------------------
		         
		         public String suspendAdminUser(String userid,String isactive,String username) 
		         {
		        	 JSONObject resobj=new JSONObject();
		        	 
		        	 if(Integer.parseInt(isactive)!=0) 
		        	 {
		        		// String adminrole="doctor";
		        		 System.out.println("in side if");
		        		 String suspend1="update subscriber_module set isactive=0 where subscriptionid='"+userid+"' ";
		        	 
		        		 
		        		
		        		 String suspendlogin1="update login_master set user_isActive=1 where  subscriptionid='"+userid+"'";
		        		 String suspendsubuser="update subscriber_module set isActive=0 where parentid='"+userid+"'";
		        		 String suspendsubuser1="update login_master set isActive=1 where parentid='"+userid+"' and user_RegistrationId='"+userid+"'";
		        		 try 
		        		 {
		        			 PreparedStatement pst=con.prepareStatement(suspend1);
		        		 
		        			 PreparedStatement pst1=con.prepareStatement(suspendlogin1);
		        			 
		        			 PreparedStatement pst2=con.prepareStatement(suspendsubuser);
		        			 
		        			 PreparedStatement pst3=con.prepareStatement(suspendsubuser1);
		        			 
		        			 
		        			 pst.executeUpdate();
		        			 pst1.executeUpdate();
		        			 
		        			 pst2.executeUpdate();
		        			 pst3.executeUpdate();
		        			 
		        			 resobj.put("Message", "Success");
		        		 }
		        		 catch(Exception e)
		        		 {
		        			 return e.toString();
		        		 }
		        	 }
		        	 else
		        	 {
		        		 try
		        		 {
							resobj.put("Message", "Admin is Suspended");
		        		 }
		        		 catch (JSONException e)
		        		 {
							e.printStackTrace();
						 } 
		        	 }
		        		
		        	 
		        	 
		        	 return resobj.toString();
		         }

	
		
 }








