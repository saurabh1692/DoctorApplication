package io.doctorApp.doc;

public class ServiceResponce {

    public ServiceResponce()
    {

    }

    public ServiceResponce(String serviceStatus,String serviceMessage,String serviceExceptionDetails,String serviceExceptionCode)
    {
        this.ServiceStatus = serviceStatus;
        this.ServiceMessage = serviceMessage;
        this.ServiceExceptionDetails = serviceExceptionDetails;
        this.ServiceExceptionCode = serviceExceptionCode;
    }
   
    public String ServiceStatus;

    
    public String ServiceMessage;

    
    public String ServiceExceptionDetails;

    
    public String ServiceExceptionCode;
}

 enum ServiceStatusCode
{
   Success(200),
   NotFound(404),
   NullRefrenceException(5000),
   FormateUnSuppoted(5001),
   MySqlException(5002),
   UserNotExist(5003),
   TransactionRollBack(5004);
		   
			  private int numVal;

			  ServiceStatusCode(int numVal) {
			        this.numVal = numVal;
			    }

			    public int getNumVal() {
			        return numVal;
			    }
}
 

