package io.doctorApp.doc;

import java.util.HashMap;

public class CacheData
    {
	
	public HashMap<String, String> getHashMap(){
	
	
        HashMap<String, String> dicColumnsMapping = new HashMap<String, String>();
      
            try
            {
            	          
            dicColumnsMapping.put("First Name","patient_first_name");
            dicColumnsMapping.put("Middle Name","patient_middle_name");
            dicColumnsMapping.put("Last Name", "patient_last_name");
            dicColumnsMapping.put("Gender", "patient_gender");
            dicColumnsMapping.put("Marital Status", "patient_marital_status");
            dicColumnsMapping.put("Date of Birth","patient_date_of_birth");
            dicColumnsMapping.put("Country", "patient_nationality");
            dicColumnsMapping.put("State","patient_state");
            dicColumnsMapping.put("City", "patient_city");
            dicColumnsMapping.put("EmailId","patient_email_id");
            dicColumnsMapping.put("Phone Number", "patient_phone_number");
            dicColumnsMapping.put("Profession", "patient_profession");
            dicColumnsMapping.put("Religion", "patient_religion");
            dicColumnsMapping.put("Address", "patient_address");
            dicColumnsMapping.put("Registration Id","patient_reg_id"); 
            }
        
            catch(Exception e) {
            	System.out.println(e);
            }
           	System.out.println("casheData"+dicColumnsMapping);
            return dicColumnsMapping;

	}
}