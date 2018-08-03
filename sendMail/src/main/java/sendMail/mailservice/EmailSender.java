package sendMail.mailservice;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;





@RestController
public class EmailSender {
	@RequestMapping(method=RequestMethod.POST,value="/sendmail/{emailId}/{data1}/{username}")
	public String createNewUser(@PathVariable final String  emailId, @PathVariable final String data1,@PathVariable final String username) {
		JSONObject resobj=new JSONObject();
		 
		 new Thread(new Runnable() {
			
             public void run() {
            	 System.out.println(emailId);
            	
            	
                 try {
                	 String context="";
            		 float value=Float.parseFloat(data1);
            		 if(value < 18.5) {
                		 context="You are Under weight"; 
                	 }
                	 else if(value>18.5 && value<24.9){
                		  context="Congratulations Your Weight is Normal."; 
                	 } else if(value>25 && value<29.9){
                		  context="You are Over Weight"; 
                	 }else if(value>30) {
                		  context="You are Weight is Obese";
                	 }
                     String body = " Dear " + username + ",\n Your BODY MASS INDEX(BMI)  is on Behalf of Your Data filling by you .\n\n " + context + " \n Your BMI is : " + data1 + "\n \n Instructions:\n\n Under Weight  : BMI is less than 18.5 "
                     		+ "\n Normal Weight  : BMI is 18.5 to 24.9\n Over Weight  : BMI is 25 to 29.9\n Obese  : BMI is 30 More. \n\n\n Thanks\n\n Life Style Health Fondation";
                     String Subject = "Body Mass Index(BMI).";
                     GMailsender sender = new GMailsender(
                    
                             "saurabh@iratechnologies.com",

                             "9956118545");



                     sender.sendMail(Subject,body,

                             "saurabh@iratechnologies.com", emailId);
                     resobj.put("Message", "Success");
                 }
                 catch (Exception e)
                 {
                	 System.out.println(e);
                 }

             }

         }).start();
     
    
   
		//return bl.createNewUser(pr, userid);
		return resobj.toString();
	}
	
	
}
