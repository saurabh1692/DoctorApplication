package io.doctorApp.doc;
//package io.doctorApp.doc.BusinessLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController

public class DocInterfaceController {
	
	@Autowired
	BusinessLogic bl=new BusinessLogic();
	
	//1|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
	@RequestMapping(method=RequestMethod.POST,value="/register/{userid}")
	public String createNewUser(@RequestBody UserInformation pr, @PathVariable String userid) {
		
		return bl.createNewUser(pr, userid);
		
	}
	//2|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
	
	@RequestMapping(method=RequestMethod.POST,value="/CreateNewAppointment/{userid}")
	public String CreateNewAppointmnet(@RequestBody String AI ,@PathVariable String userid)
	{
		
				return bl.CreateNewAppointment(AI, userid);
	}
	//3|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
	
	@RequestMapping(method=RequestMethod.GET,value="/GetPatientAppointmentDetails/{userid}")
	public String GetPatientAppointmentDetails(@PathVariable String userid) {
		
		return bl.GetPatientAppointmentDetails(userid);
	} 
	//4|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetAllPatientPaymentDetails/{Date_Detail}/{userid}")
	   public String GetAllPatientPaymentDetails(@PathVariable String Date_Detail, @PathVariable String userid) {
		return bl.GetAllPatientPaymentDetails(Date_Detail, userid);
		
	}

    //|||||5||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetIndividualPatientPaymentDetails/{RegistrationId}/{userid}")
    public String GetIndividualPatientPaymentDetails(@PathVariable String  RegistrationId, @PathVariable String  userid)
    {
		System.out.println("service 5");
    
    	return bl.GetIndividualPatientPaymentDetails(RegistrationId, userid);	
    }
	  
	
	//|||||6|||||||||||||ok|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    
	@RequestMapping(method=RequestMethod.GET,value="/GetExpactedPatientsAppointmentVisitByWeek/{currentDate}/{daysInterval}/{userid}")
	public String GetExpactedPatientsAppointmentVisitByWeek(@PathVariable String currentDate,@PathVariable String daysInterval, @PathVariable String userid)
    {
    	return bl.GetExpactedPatientsAppointmentVisitByWeek(currentDate, daysInterval, userid);
    }
	
	  //||||||7||||||||||||ok|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetExpactedPatientsAppointmentVisitTime/{VisitTime}/{VisitDate}/{userid}")

	public String GetExpactedPatientsAppointmentVisitTime(@PathVariable String VisitTime,@PathVariable String VisitDate, @PathVariable String userid)
    {
    	return bl.GetExpactedPatientsAppointmentVisitTime(VisitTime, VisitDate, userid);
    }
	
    //||||||8|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetRegistrationIdBeforeRegistration")
		public String GetRegistrationIdBeforeRegistration()
    	{
    	return bl.GetRegistrationIdBeforeRegistration();
    	}
		
		//|||||||9||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		
		@RequestMapping(method=RequestMethod.GET,value="/SavePatientInformation/{fixedFields}/{dynamicFields}/{userid}")
	
		public String SavePatientInformation(@PathVariable String fixedFields,@PathVariable String dynamicFields, @PathVariable String userid ) {
	
		return bl.SavePatientInformation(fixedFields, dynamicFields, userid);
		}
	
		//||||10|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		@RequestMapping(method=RequestMethod.GET,value="/SaveGroupPatientInformation/{grouplist}/{patientRegId}/{userid}")
    	public String SaveGroupPatientInformation(@PathVariable String grouplist,@PathVariable String patientRegId, @PathVariable String userid)
		{
    	return bl.SaveGroupPatientInformation(grouplist,patientRegId, userid);
    	}
	
		//|||11||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		@RequestMapping(method=RequestMethod.GET,value="/GetAllPatients/{userid}")
		public String GetAllPatients(@PathVariable String userid)
		{
		return bl.GetAllPatients(userid);
		}
	    //||||||12|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||


		@RequestMapping(method=RequestMethod.GET,value="/GetSelectedPatientRecords/{patientRegId}/{userid}") 
        public String GetSelectedPatientRecords(@PathVariable String patientRegId, @PathVariable String userid)
        {
        	return bl.GetSelectedPatientRecords(patientRegId, userid);
        } 
        //||||13|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		
		@RequestMapping(method=RequestMethod.GET,value="/GetSelectedPatientRecordsList/{userid}") 

        public String GetSelectedPatientRecordsList(@PathVariable String userid) ////string patientRegId
        {
			System.out.println("in service no 13");
        	return bl.GetSelectedPatientRecordsList(userid);
        }
		
		//|||||14||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		@RequestMapping(method=RequestMethod.GET,value="/GetTodayPassedAppointedPatient/{offset}/{limit}/{userid}")

        public String GetTodayPassedAppointedPatient(@PathVariable int offset,@PathVariable int limit, @PathVariable String userid)
        {
        	return bl.GetTodayPassedAppointedPatient(offset, limit, userid);
        }
		//||||15|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
        
		@RequestMapping(method=RequestMethod.GET,value="/GetIndividualPatientInfo/{RegistrationId}/{userid}") 

        public String GetIndividualPatientInfo(@PathVariable String RegistrationId, @PathVariable String userid) ////string patientRegId
        {
        	return bl.GetIndividualPatientInfo(RegistrationId, userid);
        }
        
        
        //||||||16|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		@RequestMapping(method=RequestMethod.GET,value="/gettotalpatient/{userid}") 

		public String GettotalPatientRecord(@PathVariable String userid)
        {
        	return bl.GettotalPatientRecord(userid);
        }
      //|||||||17||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		@RequestMapping(method=RequestMethod.GET,value="/GetMedicineByRegistrationIdAndVisitId/{searchCondition}/{visitid}/{userid}") 

        public String GetMedicineByRegistrationIdAndVisitId(@PathVariable String searchCondition,@PathVariable  String visitid, @PathVariable String userid)
        {
        	return bl.GetMedicineByRegistrationIdAndVisitId(searchCondition, visitid, userid);
        }
        //||||||18|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		@RequestMapping(method=RequestMethod.GET,value="/GetPatientBySearchCondition/{searchCondition}/{userid}") 
		public String GetPatientBySearchCondition(@PathVariable String searchCondition, @PathVariable String userid)
        {
        	return bl.GetPatientBySearchCondition(searchCondition, userid);
        }
        //||||19|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		@RequestMapping(method=RequestMethod.GET,value="/GetSelectedPatientImages/{PatientRegID}/{userid}") 
        public String GetSelectedPatientImages(@PathVariable String PatientRegID, @PathVariable String userid)
        {
        	return bl.GetSelectedPatientImages(PatientRegID, userid);
        }
        //||||20|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		@RequestMapping(method=RequestMethod.GET,value="/GetGroupDetails/{userid}")
        public String GetGroupDetails(@PathVariable String userid)
        {
        	return bl.GetGroupDetails(userid);
        }
        //|||||21||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
        
		@RequestMapping(method=RequestMethod.GET,value="/GetPatientsRecordsByLimit/{offSet}/{limit}/{userid}") 

        public String GetPatientsRecordsByLimit(@PathVariable int offSet,@PathVariable  int limit, @PathVariable String userid)
        {
        	return bl.GetPatientsRecordsByLimit(offSet, limit, userid);
        }
        
		
      //|||||||22||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		@RequestMapping(method=RequestMethod.GET,value="/GetPatientsVisitDetailsByLimit/{offSet}/{limit}/{patientRegId}/{userid}") 

		public String GetPatientsVisitDetailsByLimit(@PathVariable int offSet,@PathVariable  int limit,@PathVariable  String patientRegId, @PathVariable  String userid)
        {
        	return bl.GetPatientsVisitDetailsByLimit(offSet, limit, patientRegId, userid);
        }
        
        
        //|||23||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		@RequestMapping(method=RequestMethod.GET,value="/GetPatientsListBySearchQuery/{searchQueryString}/{dynamicColumns}/{userid}") 
 
        public String GetPatientsListBySearchQuery(@PathVariable String searchQueryString,@PathVariable  String dynamicColumns, String userid)
        {
        	return bl.GetPatientsListBySearchQuery(searchQueryString, dynamicColumns, userid);
        }
        
        
        //|||||24||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		@RequestMapping(method=RequestMethod.GET,value="/GetPatientsListByMedicine/{searchpatientmedicine}/{userid}") 

		public String GetPatientsListBymedicine(@PathVariable String searchpatientmedicine, @PathVariable String userid)
        {
        	return bl.GetPatientsListBymedicine(searchpatientmedicine, userid);
        }
        

        //|||||25||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		@RequestMapping(method=RequestMethod.GET,value="/GetExpactedPatientsVisitByDate/{currentDate}/{daysInterval}/{userid}") 
 
        public String GetExpactedPatientsVisitByDate(@PathVariable String currentDate,@PathVariable  String daysInterval, @PathVariable String userid)
        {
 
			return bl.GetExpactedPatientsVisitByDate(currentDate, daysInterval, userid);
        }
        
        
        //||26--------------------------------------------------------------------------------------------------------------

		@RequestMapping(method=RequestMethod.GET,value="/GetLastWeekMedicineConsumption/{currentDate}/{offSet}/{medininetype}/{userid}") 

		public String GetLastWeekMedicinConsumption(@PathVariable String currentDate,@PathVariable  int offSet,@PathVariable  String medininetype, @PathVariable  String userid)
        {
        	return bl.GetLastWeekMedicinConsumption(currentDate, offSet, medininetype, userid);
        }
        
        
        //|||27--------------------------------------------------------------------------------------------------------------

        
		@RequestMapping(method=RequestMethod.GET,value="/GetMedicinesConsumptionDetails/{currentDate}/{offSet}/{medininetype}/{userid}") 
		public String GetMedicinesConsumptionDetails(@PathVariable String currentDate,@PathVariable  int offSet,@PathVariable String medininetype, @PathVariable String userid)
        {
        	return bl.GetMedicinConsumptionDetails(currentDate, offSet, medininetype, userid);
        }
      //|||||28--------------------------------------------------------------------------------------------------------------

        
		@RequestMapping(method=RequestMethod.GET,value="/GetYearlyMedicineConsumption/{targetYear}/{targetMonth}/{medicinetype}/{userid}") 

        public String GetYearlyMedicinConsumption(@PathVariable String targetYear,@PathVariable String targetMonth,@PathVariable String medicinetype, @PathVariable String userid)
        {
			System.out.println("at service Interface");
        	return bl.GetYearlyMedicinConsumption(targetYear, targetMonth, medicinetype, userid);
        }
        
        //29,30,31 is not public services

        //||||||||32--------------------------------------------------------------------------------------------------------------

		@RequestMapping(method=RequestMethod.GET,value="/GetPatientsListBasedOnGroupSearchQuery/{searchQueryString}/{groupName}/{limit}/{offset}/{userid}") 

        public String GetPatientsListBasedOnGroupSearchQuery(@PathVariable String searchQueryString,@PathVariable String groupName,@PathVariable int limit,@PathVariable int offset, @PathVariable String userid)
        {
        	return bl.GetPatientsListBasedOnGroupSearchQuery(searchQueryString, groupName, limit, offset, userid);
        }
        
        //|||||||||33--------------------------------------------------------------------------------------------------------------

        
		@RequestMapping(method=RequestMethod.GET,value="/GetPatientsListBasedOnGroupSearchQueryGrid/{searchQueryString}/{groupName}/{userid}") 

        public String GetPatientsListBasedOnGroupSearchQueryGrid(@PathVariable String searchQueryString,@PathVariable String groupName, @PathVariable String userid) throws Exception
        {
        	return bl.GetPatientsListBasedOnGroupSearchQueryGrid(searchQueryString, groupName, userid);
        }

	//34--------------------------------------------------------------------------------------------------------------||

	@RequestMapping(method=RequestMethod.POST,value="/UpdatePatientInformation/{userid}")
	public String UpdatePatientInformation(@RequestBody String patient, @PathVariable String userid) 
	{
		return bl.UpdatePatientInformation(patient, userid);
	}
	
    //|||||||||||35--------------------------------------------------------------------------------------------------------------
    
	@RequestMapping(method=RequestMethod.POST,value="/SavePatientVisitInformation/{userid}", headers = {"content-type=application/json"})
	
	public String SavePatientVisitInformation(@RequestBody String patientVisitInformation, @PathVariable String userid)
	{
		//System.out.println(patientVisitInformation.getPatientRegId()+" at interface");
		return bl.SavePatientVisitInformation(patientVisitInformation, userid);
	}

	//|||||||||||36|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
	@RequestMapping(method=RequestMethod.POST,value="/OverridePatientVisitInformation/{userid}")
	public String OverridePatientVisitInformation(@RequestBody String patientVisitInformation, @PathVariable String userid)
	{
		return bl.OverridePatientVisitInformation(patientVisitInformation, userid);
	}
	
	  //|||||||||37|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
	@RequestMapping(method=RequestMethod.POST,value="/DeleteCurrentVisitInfo/{PatientVisitId}/{DelVisitDetails}/{userid}")
    public String DeleteCurrentVisitInfo(@PathVariable String PatientVisitId,@PathVariable String DelVisitDetails, @PathVariable String userid)
	{
   	 	return bl.DeleteCurrentVisitInfo(PatientVisitId, DelVisitDetails, userid);
    }
    
	
	 //||||||||||||38|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetPatientVisitInformationByRegistrationId/{patientRegId}/{userid}")

    public String GetPatientVisitInformationByRegistrationId(@PathVariable String patientRegId, @PathVariable String userid){
   	return bl.GetPatientVisitInformationByRegistrationId(patientRegId, userid); 
    }
    
	
	 //|||||||||||||39||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/RepeatMedicineByRegistrationId/{patientRegId}/{visitid}/{userid}")
    public String RepeatMedicineByRegistrationId(@PathVariable String patientRegId, @PathVariable String visitid, @PathVariable String userid) {
   	 return bl.RepeatMedicineByRegistrationId(patientRegId, visitid, userid);
    }
	
	  //|||||||||||||40||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    
	@RequestMapping(method=RequestMethod.GET,value="/GetMedicinList")

	public List<String> GetMedicinList(){
    return bl.GetMedicinList();
    }
	
	
	//|||||||||||||41||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/DeletePatientVisitById/{VisitID}/{userid}")
    public String DeletePatientVisitById(@PathVariable String VisitID, String userid)
    {
   	 return bl.DeletePatientVisitById(VisitID, userid);
    }
	
	 
    //||||||||||||||42|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/DeletePatientInfoById/{PatientRegID}/{userid}")
	public String DeletePatientInfoById(@PathVariable String PatientRegID, @PathVariable String userid)
    {
   	 return bl.DeletePatientInfoById(PatientRegID, userid);
    }
	
	
	//|||||||||||||||43||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    
	@RequestMapping(method=RequestMethod.GET,value="/SuspendPatientInfoById/{PatientRegID}/{userid}")
    public String SuspendPatientInfoById(@PathVariable String PatientRegID, @PathVariable String userid)
    {
   	 return bl.SuspendPatientInfoById(PatientRegID, userid);
    }	
	
	   //||||||||||||||||44|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	     
	@RequestMapping(method=RequestMethod.GET,value="/ActivePatientInfoById/{PatientRegID}/{userid}") 
	     public String ActivePatientInfoById(@PathVariable String PatientRegID, @PathVariable String userid)
	     {
	    	 return bl.ActivePatientInfoById(PatientRegID, userid);
	     }
	
	
	//|||||||||||||||||45||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    
	@RequestMapping(method=RequestMethod.POST,value="/UpdatePatientVisitInformation/{PatientVisitId}/{newVisitInfo}/{userid}") 
    public String UpdatePatientVisitInformation(@PathVariable String PatientVisitId, @PathVariable String newVisitInfo, @PathVariable String userid)
    {
   	 	return bl.UpdatePatientVisitInformation(PatientVisitId, newVisitInfo, userid);
    }
	
	 //||||||||||||||||46|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.POST,value="/AddMedicinesData/{medicinename}") 
    public String AddMedicinesData(@PathVariable String medicinename)
    {
		return bl.AddMedicinesData(medicinename);
    }
	
	
	  //|||||||||||||||47||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    
	@RequestMapping(method=RequestMethod.POST,value="/UpdatePatientEditTotalQuntity/{PatientVisitId}/{newMedicinequntity}/{userid}") 
    public String UpdatePatientEditTotalQuntity(@PathVariable String PatientVisitId,@PathVariable String newMedicinequntity , @PathVariable String userid)
    {
		return bl.UpdatePatientEditTotalQuntity(PatientVisitId, newMedicinequntity, userid);
    }
	
	 //|||||||||||||||||48||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    
		@RequestMapping(method=RequestMethod.POST,value="/UpdatePatientVisitInformationAndAmount/{visit_id}/{totamamount}/{paydAmount}/{ConcessionAmount}/{PaymentMode}/{userid}") 
	    public String UpdatePatientVisitInformationAndAmount(@PathVariable String visit_id,@PathVariable String totamamount,@PathVariable String paydAmount,@PathVariable String ConcessionAmount,@PathVariable String PaymentMode, @PathVariable String userid)
	    {
	   	 	return bl.UpdatePatientVisitInformationAndAmount(visit_id, totamamount, paydAmount, ConcessionAmount,PaymentMode, userid);
	    }
	
	 //||||||||||||||||||49|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetpatientvisitPayment/{PatientVisitId}/{userid}")
    public String GetpatientvisitPayment(@PathVariable String PatientVisitId, @PathVariable String userid)
    {
   	 return bl.GetpatientvisitPayment(PatientVisitId, userid);
    }
//**************************************************************************************
	@RequestMapping(method=RequestMethod.GET,value="/checkMultileVisitdate/{PatientRegID}/{SecondVisitDate}/{userid}")
	 public boolean CheckMultileVisitdate(@PathVariable String PatientRegID,@PathVariable Date SecondVisitDate, @PathVariable String userid) {
		return bl.CheckMultileVisitdate(PatientRegID, SecondVisitDate, userid);
	}
    //||||||||||||||||||||51|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetPatientVisitTotalPages/{PatientRegID}/{userid}")
    public String GetPatientVisitTotalPages(@PathVariable String PatientRegID, String userid)
    {
   	return bl.GetPatientVisitTotalPages(PatientRegID, userid);
    }
    
    //||||||||||||||||||53|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/LoginUser/{username}/{password}")
	public String LoginUser(@PathVariable String username,@PathVariable String password)
    {
   	 	return bl.LoginUser(username, password);
    }
	//||||||||||||||||||53-A|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		@RequestMapping(method=RequestMethod.GET,value="/AdminLoginUser/{username}/{password}")
		public String AdminLoginUser(@PathVariable String username,@PathVariable String password)
	    {
	   	 	return bl.AdminLoginUser(username, password);
	    }
	 //|||||||||||||||||||55||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.POST,value="/ResetUserPassword/{userName}/{userEmailId}/{userid}")
    public String ResetUserPassword(@PathVariable String userName,@PathVariable String userEmailId, @PathVariable String userid)
    {
		 	//System.out.println(userName+"  "+userEmailId);
			return bl.ResetUserPassword(userName, userEmailId, userid);
    }
	
	//||||||||||||||||||||56|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	
		@RequestMapping(method=RequestMethod.GET,value="/ChangeUserPassword/{userName}/{newPassword}")
	    public String ChangeUserPassword(@PathVariable String userName,@PathVariable String newPassword)
	    {
				return bl.ChangeUserPassword(userName, newPassword);
	    }
	
    //|||||||||||||||||||||||58||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/SendEmailAtToDayPatient/{userid}")
	public String SendEmailAtToDayPatient(@PathVariable String userid)
    {
   	 	return bl.SendEmailAtToDayPatient(userid);
    }
	
	//|||||||||||59||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

   
	@RequestMapping(method=RequestMethod.POST,value="/GetSendMail/{Emailid}/{AppoitementTime}/{patientname}/{Appoitementdate}/{userid}")
	public boolean GetSendMail(@PathVariable String Emailid,@PathVariable String AppoitementTime,@PathVariable String patientname,@PathVariable String Appoitementdate , @PathVariable String userid)
    {
   	 return bl.GetSendMail(Emailid, AppoitementTime, patientname, Appoitementdate, userid);
    }
	
    //||||||||||||||60|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetListOfColumns")
    public List<String> GetListOfColumns()
    {
    	return bl.GetListOfColumns();
    }
	 //||63|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.POST,value="/UploadMain/{userid}")

    public String Upload(InputStream Uploading, String userid)
    {
		return bl.Upload(Uploading, userid);
    }
	
    //||64|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@RequestMapping(method=RequestMethod.GET,value="/GetPatientDiagnosesList/{RegId}/{userid}")

	public String GetPatientDiagnosesList(@PathVariable String RegId, @PathVariable String userid)
    {
		
   	 return bl.GetPatientDiagnosesList(RegId, userid);
   	 
    }
	
	//64.1**********************************************************************************

	@RequestMapping(method=RequestMethod.POST,value="/InsertPatientRecords1/{userid}")
	
	 public  String InsertPatientRecords(@RequestBody PatientInformation patientInfo, @PathVariable String userid)
	 {
	
		return bl.InsertPatientRecords(patientInfo, userid);
	 }
	
	//**********************************************************************************
	@RequestMapping(method=RequestMethod.GET,value="/GetAllColumnsData")
	public ColumnData GetAllColumnsData()
	{
		return bl.GetAllColumnsData();
	}
	//---------------------------------------------Upload Image and file services--------------------------------------
	
	//****U2***************************************************************************************************
	@RequestMapping(method=RequestMethod.GET,value="/GetpaitentimageById/{patientRegId}/{userid}")
	 public  String GetpaitentimageById(@PathVariable String patientRegId, @PathVariable String userid) {
		return bl.GetpaitentimageById(patientRegId, userid);
	}
	//*****U1***************************************************************************************************
	@RequestMapping(method=RequestMethod.POST,value="/InsertPatientRecords/{userid}")
	
    public String insertPatientRecords1(@RequestBody PatientInformation patientInfo, @PathVariable String userid)
	{
		
		return bl.insertPatientRecords1(patientInfo, userid);
	}
	//*********U3********************************************************************************************
	@RequestMapping(method=RequestMethod.POST,value="/Upload/{fileName}/{RegId}/{userid}")
	public String Uploading(@RequestParam("file") MultipartFile Uploading,@PathVariable String RegId,@PathVariable String fileName, @PathVariable String userid)
	{
				
				return bl.upload(Uploading,fileName,RegId, userid);
	}

	//***********U4******************************************************************************************
	
	@RequestMapping(method=RequestMethod.GET,value="/Download/{Id}/{fileName}/{userid}")
	
	public ResponseEntity<byte[]>  Download(@PathVariable String Id,@PathVariable String fileName, @PathVariable String userid) throws IOException
    {
		 return bl.Download(Id, fileName, userid);
    }
	
	//*************U5****************************************************************************************
	 @RequestMapping(method=RequestMethod.POST,value="/UploadImage/{fileName}/{RegId}/{userid}")
	 
	 public String UploadImage(@RequestParam("file") MultipartFile Uploading,@PathVariable String RegId,@PathVariable String fileName, @PathVariable String userid)
	 {
		 	//System.out.println("U5 sevice");
		 	return bl.UploadImage(Uploading,fileName,RegId, userid);
	 }
	//***************U6**************************************************************************************
	  @RequestMapping(method=RequestMethod.POST,value="/UpdateImage/{fileName}/{RegId}/{userid}")  
	  public String UpdateImage(@RequestParam("file") MultipartFile Uploading,@PathVariable String fileName,@PathVariable String RegId, @PathVariable String userid)
	  {
		  return bl.UpdateImage(Uploading, fileName, RegId, userid);
	  }
	
	//*****************U7************************************************************************************
  @RequestMapping(method=RequestMethod.POST,value="/UploadVisitImage/{fileName}/{RegId}/{userid}") 
  public String UploadVisitImage(@RequestParam("file") MultipartFile Uploading,@PathVariable  String fileName,@PathVariable String RegId, @PathVariable String userid)
  {
		return bl.UploadVisitImage(Uploading, fileName, RegId, userid);
  }
	
	//*****U8***************************************************************************************************
  @RequestMapping(method=RequestMethod.POST,value="/UploadGroupImage/{fileName}/{Name}/{Description}/{userid}") 
	
  public String UploadGroupImage( @RequestParam("file") MultipartFile Uploading,@PathVariable String fileName,@PathVariable String Name,@PathVariable String Description, @PathVariable String userid)
  //public String UploadGroupImage(@RequestParam("file") MultipartFile fileName,@PathVariable String Name,@PathVariable String Description)

  {
		
	  return bl.UploadGroupImage( Uploading,fileName, Name, Description, userid);
	
  }
  
  //**U9*****************************************************************************************************
  @RequestMapping(method=RequestMethod.GET,value="/Delete/{Id}/{fileName}/{delvalue}/{userid}")
  public String Delete(@PathVariable String Id,@PathVariable String fileName,@PathVariable String delvalue, @PathVariable String userid) throws FileNotFoundException, SQLException {
	  return bl.Delete(Id, fileName,delvalue, userid);
  }
  
  //=======================================================Subscriber Module by Ashish========================================================================================//
  
  //65-----------------------------------------Add Subscriber Details------------------------------------------------------------------//
  
  @RequestMapping(method=RequestMethod.POST,value="/addsubscriber")
  
  public String AddSubscriber(@RequestBody String subscriberdetails)
  {
	  return bl.AddSubscriber(subscriberdetails);
  }
  
  //66-------------------------------------Update Subscriber Details---------------------------------------------------------------//
  
 @RequestMapping(method=RequestMethod.POST,value="/updatesubscriber")
  
  public String UpdateSubscriber(@RequestBody String subscriberdetails)
  {
	  return bl.UpdateSubscriber(subscriberdetails);
  }
 
 //67--------------------------------------Delete Subscriber Detail--------------------------------------------------------------//
 
 @RequestMapping(method=RequestMethod.GET,value="/deletesubscriber/{userid}")
 
 	public String DeleteSubscriber(@PathVariable String userid)
 	{
	  return bl.DeleteSubscriber(userid);
 	}
  
 
 //68-----------------------------------------Add Medicine Group Details----------------------------------------------------------//
 
 @RequestMapping(method=RequestMethod.POST,value="/addmedicinegroup/{userid}")
 
 public String AddMedicineGroup(@RequestBody String medicinedetails, @PathVariable String userid)
 {
	  return bl.AddMedicineGroup(medicinedetails, userid);
 }
 

 //69-----------------------------------------Get Medicine Group Details----------------------------------------------------------//
 
 @RequestMapping(method=RequestMethod.GET,value="/getmedicinegroup/{groupname}/{userid}")
 
 public String GETMedicineGroup(@PathVariable String groupname, @PathVariable String userid)
 {
	  return bl.GetMedicineGroup(groupname, userid);
 }
 
//70-----------------------------------------Update Medicine Group Details----------------------------------------------------------//
 
@RequestMapping(method=RequestMethod.POST,value="/updatemedicinegroup/{userid}")
 
 public String UpdateMedicineGroup(@RequestBody String medicinedetails, @PathVariable String userid)
 {
	  return bl.UpdateMedicineGroup(medicinedetails, userid);
 }
 
//71---------------------------------------Set Header Value For Header Space------------------------------------------------------//

@RequestMapping(method=RequestMethod.GET,value="/SetHeaderSize/{value}/{userid}")

public String SetHeaderSize(@PathVariable String value, @PathVariable String userid)
{
	  return bl.SetHeaderSize(value, userid);
}

//72---------------------------------------Get Header Value For Header Space------------------------------------------------------//

@RequestMapping(method=RequestMethod.GET,value="/GetHeaderSize/{userid}")

public String GetHeaderSize(@PathVariable String userid)
{
	//System.out.println("header size");
	  return bl.GetHeaderSize(userid);
}

//73--------------------------------------------Sub User Details------------------------------------------------------------------//

	@RequestMapping(method=RequestMethod.POST,value="/subuserDetails/{userid}")
	public String SubUserDetails(@RequestBody String userdetails,@PathVariable String userid )
	{
		return bl.SubUserDetails(userdetails,userid);
	}

	
//74.----------------------------Check user is main user or sub user-------------------------------------------------------------//
	
	@RequestMapping(method=RequestMethod.GET,value="/checkUserType/{userid}")
 
	public String checkUser(@PathVariable String userid)
	{
		//System.out.println("header size");
		  return bl.checkUser(userid);
	}
	
//75.=======================================Get Logo for Doctor =================================================================//
	
	@RequestMapping(method=RequestMethod.GET,value="/getLogoName/{userid}")
	 
	public String getLogoName(@PathVariable String userid)
	{
		  return bl.getLogoName(userid);
	}

	
//76.---------------------------------Sent Mail To Patient---------------------------------------------------------------------//
	
	@RequestMapping(method=RequestMethod.GET,value="/sendMail/{regid}/{email}/{userid}")
	 
	public String sendMail(@PathVariable String regid,  @PathVariable String email, @PathVariable String userid)
	{
		//DemoPdf pdf = new DemoPdf();
		 return bl.sendMail(regid, email, userid);
	}

	
//77.-----------------------------------Get Patient Email Id---------------------------------------------------------------//
	
	
	@RequestMapping(method=RequestMethod.GET,value="/getMailId/{regid}/{userid}")
	 
	public String getMailId(@PathVariable String regid, @PathVariable String userid)
	{
		
		 return bl.getMailId(regid, userid);
	}
	

	//78.-----------------------------Save Notes In File----------------------------------------------------------------//
	
	@RequestMapping(method=RequestMethod.POST,value="/saveNotesInFile/{regid}/{userid}")
	public String saveNotesInFile(@RequestBody String notesdetail,@PathVariable String regid, @PathVariable String userid )
	{
		return bl.saveNotesInFile(notesdetail, regid, userid);
	}
	
	
	//79.----------------------------Get Notes From File--------------------------------------------------------------//
	
	@RequestMapping(method=RequestMethod.GET,value="/getNotes/{regid}/{userid}")
	 
	public String getNotes(@PathVariable String regid, @PathVariable String userid)
	{
		
		 return bl.getNotes(regid, userid);
	}
	
	
	//80--------------------------------------------------------------Edit Due Amount -------------------------------------------------
	
			@RequestMapping(method=RequestMethod.GET,value="/EditPatientAmount/{visitid}/{SubmitDueAmount}/{paymentRemark}")
			
			public String  EditPayment(@PathVariable String visitid,@PathVariable int SubmitDueAmount,@PathVariable String paymentRemark) 
			{
				return bl.EditPayment(visitid, SubmitDueAmount, paymentRemark);
			}
   //81-----------------------------------------------------Change Payment mode-----------------------------------// 
			@RequestMapping(method=RequestMethod.POST,value="/ChangePaymentMode/{visitid}/{mode}/{visitDate}")
			
			public String ChangePaymentMode(@PathVariable String visitid,@PathVariable String mode,@PathVariable String visitDate) {
				return bl.ChangePaymentMode(visitid, mode,visitDate);
			}
			
			//82-----------------------------------------------------Permission To sub user As Manage User-----------------------------------// 
			
			@RequestMapping(method=RequestMethod.GET,value="/permissionToSubUserasManageUser/{userid}")
			public String Permission(@PathVariable String userid) {
				return bl.Permission(userid);
			} 
			
			//83-----------------------------------------------------Get sub user Details--------------------------------------------------//
			
			@RequestMapping(method=RequestMethod.GET,value="/GetsubuserInfo/{userid}")
			public String getsubuserinformation(@PathVariable String userid) {
			
			return bl.getsubuserinformation(userid);
		} 
			
	
	//84.Ashish------------------------------------Set Email and Password of Doctor--------------------------------------------------------//
			
			@RequestMapping(method=RequestMethod.POST,value="/saveEmailAndPassword/{userid}")
			public String saveEmailAndPassword(@RequestBody String emaildetail, @PathVariable String userid )
			{
				return bl.saveEmailAndPassword(emaildetail, userid);
			}	
	
			
//85.-----------------------------------------Upload Logo -------------------------------------------------------------------//
			
			@RequestMapping(method=RequestMethod.POST,value="/uploadLogo/{userid}") 
			
			  public String uploadLogo( @RequestParam("file") MultipartFile uploading, @PathVariable String userid)
			  //public String UploadGroupImage(@RequestParam("file") MultipartFile fileName,@PathVariable String Name,@PathVariable String Description)

			  {
					
				  return bl.uploadLogo( uploading, userid);
				
			  }
			
		
//==================================Test Excel File=====================================================================//
			
			@RequestMapping(method=RequestMethod.POST,value="/excel") 
			
			  public String excel( @RequestParam("file") MultipartFile uploading)
			  {
					File file = new File("D:\\"+uploading.getOriginalFilename());
					try {
						uploading.transferTo(file);
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  System.out.println(uploading.getOriginalFilename());
				  return "progress";
				
			  }
			
			
			
			//80--------------------------------------------------------------Forgot password send OTP------------------------------------------
			@RequestMapping(method=RequestMethod.GET,value="/sendOTP/{userid}")
			public String SendOTP(@PathVariable String userid)
			  {
				return bl.SendOTP(userid);
			  }
			
			
			
			//81-------------------------------------------------------------------------Check-OTP----------------------------------------------------------
			@RequestMapping(method=RequestMethod.GET,value="/CheckOTP/{givenotp}/{fileName}") 
			public String checkotp(@PathVariable String givenotp,@PathVariable String fileName)
			  {
				 return bl.checkotp(givenotp,fileName);
			  }
			
			
			
			 //|||||||||||||||||||||||||||||||||||||||||||||||||||||||||Managing the Admin Portal|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||//
			  
			  
			  //82---------------------------------------------------------------Admin  Login ---------------------------------------------------------------------
			@RequestMapping(method=RequestMethod.GET,value="/AdminLogin/{username}/{password}") 
			  public String AdminLogin(@PathVariable String username,@PathVariable String password)
		  		{
				  return bl.AdminLogin(username,password);
			 
		  		}
			
			//83---------------------------------------------------------------GEt All Admin Doctor ---------------------------------------------------------------------
			@RequestMapping(method=RequestMethod.GET,value="/GetAllAdminDoctor") 
			  public String GetAllAdminDoctor()
			  {
				  return bl.GetAllAdminDoctor();
			  }
			
			
			//84---------------------------------------------------------------GEt All Admin Docotr_sub UserDetails ---------------------------------------------------------------------
			@RequestMapping(method=RequestMethod.GET,value="/GetAllAdminDoctor_subuserdetails/{parentid}") 
			  public String GetAllAdminDoctor_subuserdetails(@PathVariable String parentid) {
				 return bl.GetAllAdminDoctor_subuserdetails(parentid);
			  }
			
			
			
			//85---------------------------------------------------------------Activate Sub user---------------------------------------------------------------------
			@RequestMapping(method=RequestMethod.GET,value="/Activesubuser/{userid}/{isactive}/{username}") 
	        public String ActivateSubUser(@PathVariable String userid,@PathVariable String isactive,@PathVariable String username) 
	        {
	       	 return bl.ActivateSubUser(userid, isactive, username);
	        }
			 
			
			
			 //86---------------------------------------------------------------Suspend the Active User---------------------------------------------------------------------
			@RequestMapping(method=RequestMethod.GET,value="/Suspendsubuser/{userid}/{isactive}/{username}") 
	        public String suspendSubUser(@PathVariable String userid,@PathVariable String isactive,@PathVariable String username) 
	        {
	       	 return bl.suspendSubUser(userid, isactive, username);
	        }
			
			
			
			//87---------------------------------------------------------------Activate Admin user---------------------------------------------------------------------
					@RequestMapping(method=RequestMethod.GET,value="/ActiveAdminUser/{userid}/{isactive}/{username}") 
			        public String ActivateAdminUser(@PathVariable String userid,@PathVariable String isactive,@PathVariable String username) 
			        {
			       	 return bl.ActivateAdminUser(userid, isactive, username);
			        }
					 
					
					
					 //88---------------------------------------------------------------Suspend the Active Admin User---------------------------------------------------------------------
					@RequestMapping(method=RequestMethod.GET,value="/SuspendAdminUser/{userid}/{isactive}/{username}") 
			        public String suspendAdminUser(@PathVariable String userid,@PathVariable String isactive,@PathVariable String username) 
			        {
			       	 return bl.suspendAdminUser(userid, isactive, username);
			        }
}







