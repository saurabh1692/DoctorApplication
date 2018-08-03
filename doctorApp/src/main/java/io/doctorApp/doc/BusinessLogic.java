package io.doctorApp.doc;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;



import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;




@RestController
public class BusinessLogic {
	
	
	
	//ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("applicationContext.xml");
	
	

	//final static  Logger logger=Logger.getLogger(BusinessLogic.class);
	final static  org.apache.logging.log4j.Logger logger= LogManager.getLogger(BusinessLogic.class);

	@Autowired
	public DocServices service=new DocServices();
	//1----------------------------------------------------------------------------------------------------------------------------------------
	public String createNewUser(UserInformation pr, String userid) {
		try {
			if(pr!=null) {
		
			
		return service.createNewUser(pr, userid);	
		}
			else {
				logger.info("UserInformation details is not fullfill by the User");
			}
		}
		catch(Exception e)
		{
			 logger.info("In registration service: error: "+e);

		}
		return "In registration service";
	}
	

    //||2|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    public String CreateNewAppointment(String AI, String userid)
    {
    	try {
			if(AI!=null) {
		
			
		return service.CreateNewAppointment(AI, userid);	
		}
			else {
				logger.info("PatientAppoientment details is not fullfill by the User");
			}
		}
		catch(Exception e)
		{
			 logger.info("In registration service: error: "+e);

		}
		return "CreateNewAppointment";
    	//return service.CreateNewAppointment(AI);
    }
	
	
    //|||||3||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    
    
    public String GetPatientAppointmentDetails(String userid)
    {
    	logger.info("PatientAppoientment details is not fullfill by the User");
    	return service.GetPatientAppointmentDetails(userid);
    }
   
    //||||||4|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    
    public String GetAllPatientPaymentDetails(String Date_Detail, String userid)
    {
    	return service.GetAllPatientPaymentDetails(Date_Detail, userid);
    }
    
    

    //|||||5||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    
    
    public String GetIndividualPatientPaymentDetails(String  RegistrationId, String userid)
    {
    
    	return service.GetIndividualPatientPaymentDetails(RegistrationId, userid);	
    }
    
    
    //|||||6||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    public String GetExpactedPatientsAppointmentVisitByWeek(String currentDate, String daysInterval, String userid)
    {
    	return service.GetExpactedPatientsAppointmentVisitByWeek(currentDate, daysInterval, userid);
    }
    
    //||||||7|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetExpactedPatientsAppointmentVisitTime(String VisitTime, String VisitDate, String userid)
    {
            	return service.GetExpactedPatientsAppointmentVisitTime(VisitTime, VisitDate, userid);
    }
            
          //||||||8|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetRegistrationIdBeforeRegistration()
            {
            	return service.GetRegistrationIdBeforeRegistration();
            }
            
          //|||||||9||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            
            public String SavePatientInformation(String fixedFields, String dynamicFields, String userid)
            {
            	return service.SavePatientInformation(fixedFields, dynamicFields, userid);
            }
                 
            
            //||||10|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            
            public String SaveGroupPatientInformation(String grouplist, String patientRegId, String userid)
            {
            	return service.SaveGroupPatientInformation(grouplist, patientRegId, userid);
            }
            
            
          //|||11||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetAllPatients(String userid)
            {
            	return service.GetAllPatients(userid);
            }

            //||||||12|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetSelectedPatientRecords(String patientRegId, String userid)
            {
            	return service.GetSelectedPatientRecords(patientRegId, userid);
            } 
            
            
            //||||13|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetSelectedPatientRecordsList(String userid) ////string patientRegId
            {
            	logger.info("Busines Logic in service 13");
            	return service.GetSelectedPatientRecordsList(userid);
            }
            
            //|||||14||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetTodayPassedAppointedPatient(int offset, int limit, String userid)
            {
            	return service.GetTodayPassedAppointedPatient(offset, limit, userid);
            }
            
            //||||15|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            
           
            public String GetIndividualPatientInfo(String RegistrationId, String userid) ////string patientRegId
            {
            	return service.GetIndividualPatientInfo(RegistrationId, userid);
            }
            
            
            //||||||16|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            
            public String GettotalPatientRecord(String userid)
            {
            	return service.GettotalPatientRecord(userid);
            }
            
          //|||||||17||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            
            
            
            public String GetMedicineByRegistrationIdAndVisitId(String searchCondition, String visitid, String userid)
            {
            	return service.GetMedicineByRegistrationIdAndVisitId(searchCondition, visitid, userid);
            }
            

            //||||||18|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetPatientBySearchCondition(String searchCondition, String userid)
            {
            	return service.GetPatientBySearchCondition(searchCondition, userid);
            }
            
            
            //||||19|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            
            
            
            public String GetSelectedPatientImages(String Patient, String userid)
            {
            	return service.GetSelectedPatientImages(Patient, userid);
            }
            
            //||||20|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            
            public String GetGroupDetails(String userid)
            {
            	return service.GetGroupDetails(userid);
            }
            //|||||21||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            
            
            public String GetPatientsRecordsByLimit(int offSet, int limit, String userid)
            {
            	return service.GetPatientsRecordsByLimit(offSet, limit, userid);
            }
            
          //|||||||22||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetPatientsVisitDetailsByLimit(int offSet, int limit, String patientRegId, String userid)
            {
            	return service.GetPatientsVisitDetailsByLimit(offSet, limit, patientRegId, userid);
            }
            
            //|||23||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            
            public String GetPatientsListBySearchQuery(String searchQueryString, String dynamicColumns, String userid)
            {
            	return service.GetPatientsListBySearchQuery(searchQueryString, dynamicColumns, userid);
            }
            
            //|||||24||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetPatientsListBymedicine(String searchpatientmedicine, String userid)
            {
            	return service.GetPatientsListBymedicine(searchpatientmedicine, userid);
            }
            //|||||25||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetExpactedPatientsVisitByDate(String currentDate, String daysInterval, String userid)
            {
            	return service.GetExpactedPatientsVisitByDate(currentDate, daysInterval, userid);
            }
            //||26|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetLastWeekMedicinConsumption(String currentDate, int offSet, String medininetype, String userid)
            {
            	return service.GetLastWeekMedicinConsumption(currentDate, offSet, medininetype, userid);
            }
            
            
            //|||27||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetMedicinConsumptionDetails(String currentDate, int offSet, String medininetype, String userid)
            {
            	return service.GetMedicinConsumptionDetails(currentDate, offSet, medininetype, userid);
            }
          //|||||28||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            
            public String GetYearlyMedicinConsumption(String targetYear, String targetMonth,String medicinetype, String userid)
            {
            	return service.GetYearlyMedicinConsumption(targetYear, targetMonth, medicinetype, userid);
            }
            //29,30,31 is not public services
            //||||||||32|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            public String GetPatientsListBasedOnGroupSearchQuery(String searchQueryString, String groupName, int limit, int offset, String userid)
            {
            	return service.GetPatientsListBasedOnGroupSearchQuery(searchQueryString, groupName, limit, offset, userid);
            }
            
            //|||||||||33||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

            
            
            public String GetPatientsListBasedOnGroupSearchQueryGrid(String searchQueryString, String groupName, String userid) throws Exception
            {
            	return service.GetPatientsListBasedOnGroupSearchQueryGrid(searchQueryString, groupName, userid);
            }

	//34----------------------------------------------------------------------------------------------------------------------------------------
	public String UpdatePatientInformation(String patient, String userid) 
	{
		return service.UpdatePatientInformation(patient, userid);
	}
	
	//35----------------------------------------------------------------------------------------------------------------------------------------
	
	public String SavePatientVisitInformation(String patientVisitInformation, String userid)
	 {
		 return service.SavePatientVisitInformation(patientVisitInformation, userid);
	 }
	 
     //||||||||||36|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String OverridePatientVisitInformation(String patientVisitInformation, String userid)
     {
     	return service.OverridePatientVisitInformation(patientVisitInformation, userid);
     }
     
   //|||||||||37|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String DeleteCurrentVisitInfo(String PatientVisitId, String DelVisitDetails, String userid)
     {
    	 return service.DeleteCurrentVisitInfo(PatientVisitId, DelVisitDetails, userid);
     }
     
     //||||||||||||38|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String GetPatientVisitInformationByRegistrationId(String patientRegId, String userid){
    	return service.GetPatientVisitInformationByRegistrationId(patientRegId, userid); 
     }
     //|||||||||||||39||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String RepeatMedicineByRegistrationId(String patientRegId, String visitid, String userid) {
    	 return service.RepeatMedicineByRegistrationId(patientRegId, visitid, userid);
     }
     
     //|||||||||||||40||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

     public List<String> GetMedicinList(){
    	 
     return service.GetMedicinList();
     
     }
     //|||||||||||||41||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String DeletePatientVisitById(String VisitID, String userid)
     {
    	return service.DeletePatientVisitById(VisitID, userid); 
     }
     
     //||||||||||||||42|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String DeletePatientInfoById(String PatientRegID, String userid)
     {
    	return service.DeletePatientInfoById(PatientRegID, userid) ;
     }
     
     //|||||||||||||||43||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String SuspendPatientInfoById(String PatientRegID, String userid)
     {
    	 return service.SuspendPatientInfoById(PatientRegID, userid);
     }
     
   //||||||||||||||||44|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String ActivePatientInfoById(String PatientRegID, String userid)
     {
    	 return service.ActivePatientInfoById(PatientRegID, userid);
     }
     
     
     //|||||||||||||||||45||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String UpdatePatientVisitInformation(String PatientVisitId, String newVisitInfo, String userid)
     {
    	 return service.UpdatePatientVisitInformation(PatientVisitId, newVisitInfo, userid);
     }
     
     //||||||||||||||||46|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String AddMedicinesData(String medicinename)
     {
    	 return service.AddMedicinesData(medicinename);
     }
     //|||||||||||||||47||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String UpdatePatientEditTotalQuntity(String PatientVisitId, String newMedicinequntity, String userid)
     {
    	 return service.UpdatePatientEditTotalQuntity(PatientVisitId, newMedicinequntity, userid);
     }
     //|||||||||||||||||48||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     
     public String UpdatePatientVisitInformationAndAmount(String visitid, String totalamount, String paydAmount, String ConcessionAmount,String PaymentMode, String userid)
     {
    	 return service.UpdatePatientVisitInformationAndAmount(visitid, totalamount, paydAmount, ConcessionAmount,PaymentMode, userid);
     }
     
     //||||||||||||||||||49|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String GetpatientvisitPayment(String patientRegID, String userid)
     {
    	 return service.GetpatientvisitPayment(patientRegID, userid);
     }
     
     //||||||||||||||||||||51|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String GetPatientVisitTotalPages(String PatientRegID, String userid)
     {
    	return service.GetPatientVisitTotalPages(PatientRegID, userid);
     }
     
     //||||||||||||||||||53|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String LoginUser(String userName, String userPassword)
     {
    	 return service.LoginUser(userName, userPassword);
     }
     
   //||||||||||||||||||53-A|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String AdminLoginUser(String userName, String userPassword)
     {
    	 return service.AdminLoginUser(userName, userPassword);
     }
     //|||||||||||||||||||55||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     public String ResetUserPassword(String userName, String userEmailId, String userid)
     {
    	 return service.ResetUserPassword(userName, userEmailId, userid);
    	 
     }
     //||||||||||||||||||||56|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

     public String ChangeUserPassword(String userName,String newPassword)
     {
    	 return service.ChangeUserPassword(userName, newPassword);
     }
     //|||||||||||||||||||||||58||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

     public String SendEmailAtToDayPatient(String userid)
     {
    	 return service.SendEmailAtToDayPatient(userid);
     }
     
   //|||||||||||59||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

     public boolean GetSendMail(String Emailid, String AppoitementTime, String patientname, String Appoitementdate, String userid)
     {
    	 return service.GetSendMail(Emailid, AppoitementTime, patientname, Appoitementdate, userid);
     }
     
     
     //||||||||||||||60|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

 
     public List<String> GetListOfColumns()
     {
     	return service.GetListOfColumns();
     }

     //||63|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

     
     public String Upload(InputStream Uploading, String userid)
     {
    	 return service.Upload(Uploading, userid);
     }
     
     //||64|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

     public String GetPatientDiagnosesList(String RegId, String userid)
     {
    	 return service.GetPatientDiagnosesList(RegId, userid);
     }
     
     //||65|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
	/*****************************************************************/
	ProcessMySqlSelectCommandResult processmysql=new ProcessMySqlSelectCommandResult();
		
     public String InsertPatientRecords(PatientInformation patientInfo, String userid)
     
     {
		return processmysql.InsertPatientRecords(patientInfo, userid);
	 }
     
     //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
     
     public ColumnData GetAllColumnsData()
     {
 		return service.GetAllColumnsData();
 	 }
     //*****************************************************************************************
     public boolean CheckMultileVisitdate( String PatientRegID, Date SecondVisitDate, String userid)
     {
    	 return service.CheckMultileVisitdate(PatientRegID, SecondVisitDate, userid);
     }
     //||Uploading and Downloading  file service |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
     WCFUploader upload=new WCFUploader();
     
     //WCFUploader upload= (WCFUploader) context.getBean("myPath");
     
   //******U1***********************************************************************************************
     public String insertPatientRecords1(PatientInformation patientInfo, String userid)
     {
    	 System.out.println("U1");
		 return upload.insertPatientRecords(patientInfo, userid);
	 }
	
     //*******U2**********************************************************************************************
	public String GetpaitentimageById(String patientRegId, String userid)
	{
		return upload.GetpaitentimageById(patientRegId, userid);
	}
	
	//*********U3********************************************************************************************
	  public String upload(MultipartFile Uploading, String fileName,String RegId, String userid)
	  {
		 //FileInputStream Uploading=null;
		  //return upload.upload(Uploading,RegId,fileName);
		  logger.info("In Upload Service U3 MultipartFile: "+Uploading+" filename: "+fileName+" RegId: "+RegId);
		  return upload.upload(Uploading,fileName,RegId, userid);
	   }
	
	//***********U4******************************************************************************************
	 public ResponseEntity<byte[]>  Download(String Id, String fileName, String userid) throws FileNotFoundException
     {
		 return upload.Download(Id, fileName, userid);
     }

	//*************U5****************************************************************************************
	
	  public String UploadImage(MultipartFile Uploading,String fileName,String RegId, String userid)
	  {
		  return upload.UploadImage(Uploading,fileName,RegId, userid);
	  }
	
	  //***************U6**************************************************************************************
    public String UpdateImage(MultipartFile Uploading, String fileName, String RegId, String userid)
    {
    	 return upload.UpdateImage(Uploading, fileName, RegId, userid);
    }
    
	//*****************U7************************************************************************************
	
	public String UploadVisitImage(MultipartFile Uploading, String fileName, String RegId, String userid)
    {
		return upload.UploadVisitImage(Uploading, fileName, RegId, userid);
    }
	
	//*******************U8**********************************************************************************
	
	public String UploadGroupImage(MultipartFile Uploading,String fileName, String Name,String Descripton, String userid) 
	{
   	 	return upload.UploadGroupImage(Uploading,fileName, Name, Descripton, userid);
    }
	
	//*******************U9**********************************************************************************
	 public String  Delete(String Id, String fileName,String delvalue, String userid) throws FileNotFoundException, SQLException
     {
		 return upload.Delete(Id, fileName,delvalue, userid);
     }
	
//=======================================================Subscriber Module by Ashish========================================================================================//
	  
	  //65-----------------------------------------Add Subscriber Details------------------------------------------------------------------//
	
	public String AddSubscriber(String subscriberdetails) 
	{
		
		return service.AddSubscriber(subscriberdetails);
	}
     
	
   //66---------------------------------------Update Subscriber Details--------------------------------------------------// 	
	
	public String UpdateSubscriber(String subscriberdetails) 
	{
		
		return service.UpdateSubscriber(subscriberdetails);
	}


 //67--------------------------------------Delete Subscriber Detail--------------------------------------------------------------//
	
	public String DeleteSubscriber(String userid)
	{
		
		return service.DeleteSubscriber(userid);
	}


	
//68-----------------------------------------Add Medicine Group Details----------------------------------------------------------//
	
	public String AddMedicineGroup(String medicinedetails, String userid)
	{
		return service.AddMedicineGroup(medicinedetails, userid);
	}


//69-----------------------------------------Get Medicine Group Details----------------------------------------------------------//	
	

	public String GetMedicineGroup(String medicinedetails, String userid)
	{
		return service.GetMedicineGroup(medicinedetails, userid);
	}


	
	
//70-----------------------------------------Update Medicine Group Details----------------------------------------------------------//
	
	public String UpdateMedicineGroup(String medicinedetails, String userid) 
	{
		return service.UpdateMedicineGroup(medicinedetails, userid);
	}

//71---------------------------------------Set Header Value For Header Space------------------------------------------------------//	
	
	public String SetHeaderSize(String value, String userid) 
	{
		return service.SetHeaderSize(value, userid);
	}

//72---------------------------------------Get Header Value For Header Space------------------------------------------------------//
	
	
	public String GetHeaderSize(String userid) 
	{
		return service.GetHeaderSize(userid);
	}
	
	
//73-------------------------------------------Sub user Details--------------------------------------------------------------------//
			
	public String SubUserDetails(String userdetails,String userid)
	{
				
		return service.SubUserDetails(userdetails,userid);
			
	}

	
//74.----------------------------Check user is main user or sub user-------------------------------------------------------------//

	public String checkUser(String userid)
	{
		return service.checkUser(userid);
	}


	
//75.=======================================Get Logo for Doctor =================================================================//	
	
	public String getLogoName(String userid) 
	{
		return service.getLogoName(userid);
	}


//76.---------------------------------Sent Mail To Patient---------------------------------------------------------------------//	
	
	public String sendMail(String regid, String email, String userid)
	{
		
		return service.sendMail(regid, email, userid);
	}

	
	
//77.-----------------------------------Get Patient Email Id---------------------------------------------------------------//
	

	public String getMailId(String regid, String userid)
	{
		return service.getMailId(regid, userid);
	}


//78.-----------------------------Save Notes In File----------------------------------------------------------------------//	
	
	public String saveNotesInFile(String notesdetail, String regid, String userid) 
	{
		
		return service.saveNotesInFile(notesdetail, regid, userid);
	}

//79.----------------------------Get Notes From File--------------------------------------------------------------//
	
	public String getNotes(String regid, String userid) 
	{
		return service.getNotes(regid, userid);
	}	
	
	//80--------------------------------------------------------------Edit Due Amount -------------------------------------------------
	
	
	
	public String  EditPayment(String visitid,int SubmitDueAmount,String paymentRemark) {
		return service.EditPayment(visitid, SubmitDueAmount, paymentRemark);
	}
	
	
	//81-----------------------------------------------------Change Payment mode-----------------------------------// 
	
	public String ChangePaymentMode(String visitid,String paymentmode,String visitDate) {
		return service.ChangePaymentMode(visitid, paymentmode,visitDate);
	}
	
	 //82-----------------------------------------------------Permission To sub user As Manage User-----------------------------------// 
	
	public String Permission(String userid) {
		return service.Permission(userid);
	}
	
	//83-----------------------------------------------------Get sub user Details--------------------------------------------------//
	
	public String getsubuserinformation( String userid) {
		
		return service.getSubUserDetails(userid);
	}


//84.Ashish------------------------------------Set Email and Password of Doctor--------------------------------------------------------//
	
	public String saveEmailAndPassword(String emaildetail, String userid) 
	{
		
		return service.saveEmailAndPassword(emaildetail,userid);
	}

//85.-----------------------------------------Upload Logo -------------------------------------------------------------------//
	public String uploadLogo(MultipartFile uploading, String userid)
	{
		
		return upload.uploadLogo(uploading, userid);
	} 
	
	
	//80--------------------------------------------------------------Forgot password send OTP------------------------------------------
	
	public String SendOTP(String userid)
	  {
		return service.SendOTP(userid);
	  }
	//81-------------------------------------------------------------------------Check-OTP----------------------------------------------------------
	 public String checkotp(String givenotp,String fileName)
	  {
		 return service.checkotp(givenotp,fileName);
	  }
	 
	 
	 //-------------------------------------------------------------------Managing the Admin Portal--------------------------------------------------------//
	  
	  
	  //82---------------------------------------------------------------Admin  Login ---------------------------------------------------------------------
	  
	 public String AdminLogin(String username,String password)
  		{
		  return service.AdminLogin(username,password);
	 
  		}
	 
	 
	//---------------------------------------------------------------GEt All Admin Docotr ---------------------------------------------------------------------
	  
	  public String GetAllAdminDoctor()
	  {
		  return service.GetAllAdminDoctor();
	  }
	  
	  
	  
	//---------------------------------------------------------------GEt All Admin Docotr_sub UserDetails ---------------------------------------------------------------------
	  
	  public String GetAllAdminDoctor_subuserdetails(String parentid) {
		 return service.GetAllAdminDoctor_subuserdetails(parentid);
	  }

	  //---------------------------------------------------------------Activate Sub user---------------------------------------------------------------------
	  
         public String ActivateSubUser(String userid,String isactive,String username) 
         {
        	 return service.ActivateSubUser(userid, isactive, username);
         }
         
         
         //---------------------------------------------------------------Suspend the Active User---------------------------------------------------------------------
         
         public String suspendSubUser(String userid,String isactive,String username) 
         {
        	 return service.suspendSubUser(userid, isactive, username);
         }
         
         
         
         //---------------------------------------------------------------Activate Admin user---------------------------------------------------------------------
		  
         public String ActivateAdminUser(String userid,String isactive,String username) 
         {
        	 return service.ActivateAdminUser(userid, isactive, username);
         }
         
         
         //---------------------------------------------------------------Suspend the Active Admin User---------------------------------------------------------------------
         
         public String suspendAdminUser(String userid,String isactive,String username) 
         {
        	 return service.suspendAdminUser(userid, isactive, username);
         }
	
}
