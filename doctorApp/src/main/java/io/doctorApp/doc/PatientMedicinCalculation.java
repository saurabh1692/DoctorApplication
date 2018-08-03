package io.doctorApp.doc;
import java.io.*;

import io.doctorApp.doc.DrServiceResponce.MedicinTypeCode;

public class PatientMedicinCalculation 
{
	
	  public String CalculateTotalQuntity(VisitMedicin visitMedicin)
      {
          String result = "";
        // System.out.println("at PatientMedicinCalculation "+visitMedicin.getMedicintype());
//System.out.println(GetMedicinType(visitMedicin.getMedicintype()));
          
          System.out.println("visitMedicin Type: "+GetMedicinType(visitMedicin.getMedicintype()));
          switch (GetMedicinType(visitMedicin.getMedicintype()))
          {
        
              case  100:
                  result = CalculateGlobulesQuntity(visitMedicin);
                  break;
              case 101:
                  result = CalculateSyrupQuntity(visitMedicin);
                  break;
                 
              case 102:
               
            	 // System.out.println("tablets");
                  result = CalculateTabletsQuntity(visitMedicin);
                 
                  break;
              case 103:
                  result = CalculateBioChemicQuntity(visitMedicin);
                  break;
              case 104:
                  result = CalculatePowderQuntity(visitMedicin);
                  break;
              case 105:
                  result = CalculateLiqidQuntity(visitMedicin);
                  break;
              case 106:
                  result = CalculateOintmentQuntity(visitMedicin); 
                  break;
              default:

                  break;
          }
          
          System.out.println("result: "+result);
          return result;

      }
	
	  
	  private String CalculateGlobulesQuntity(VisitMedicin visitMedicie)
      {
          String calculatedDram = "";
          int days = 1;
         // visitMedicie.setMedicinday(String.valueOf(days));
          days=Integer.parseInt(visitMedicie.getMedicinday());
          int dose = ConvertDoseToInt((visitMedicie.getMedicinDos()));
          int quantity = 1;
          quantity=Integer.parseInt(visitMedicie.getMedicinQuntity());
          //visitMedicie.setMedicinday(String.valueOf(quantity));//.setMedicienday();
         
          int totalNombersOfGlobules = days * dose * quantity;
          
          if (totalNombersOfGlobules >= 20 && totalNombersOfGlobules < 40)
              calculatedDram = "1/2 Dram";
         
          else if (totalNombersOfGlobules >= 40 && totalNombersOfGlobules < 78)
              calculatedDram = " 2 half Dram";
       
          else if (totalNombersOfGlobules >= 78 && totalNombersOfGlobules < 115)
              calculatedDram = "1 Dram";
          
          else
          {
              int totalremaningGloubes = totalNombersOfGlobules % 106;
              String totalremaninggloubes = String.valueOf(totalremaningGloubes / 35);
              calculatedDram += String.valueOf(totalNombersOfGlobules / 106) + " full & " + totalremaninggloubes+" half Dram";
          }
          return calculatedDram;
      }
      private String CalculateSyrupQuntity(VisitMedicin visitMedicie)
      {
          String calculatedSyrup = "";
          int days = 1;
          days=Integer.parseInt(visitMedicie.getMedicinday());
          //visitMedicie.Medicinday=String.valueOf(days); 

          int dose = ConvertDoseToInt((visitMedicie.getMedicinDos()));
          int quantity = 1;
          quantity=Integer.parseInt(visitMedicie.getMedicinQuntity());
          //visitMedicie.Medicinday=String.valueOf(quantity);
         
          int totalsyrep = days * dose * quantity;
          if (totalsyrep > 0 && totalsyrep <= 500)
          {
              calculatedSyrup += String.valueOf(totalsyrep / 35)+ " ml";
          }
          else
          {
              calculatedSyrup += String.valueOf(totalsyrep / 22) + " ml";
          }
          System.out.println("quntity calculation at 95  " + calculatedSyrup);
          return calculatedSyrup;
      }
      private String CalculateTabletsQuntity(VisitMedicin visitMedicie)
      {
          String calculatedTablets ="";
          int days = 1;
         
        //  visitMedicie.Medicinday=String.valueOf(days); 
        days=Integer.parseInt( visitMedicie.getMedicinday());
       
        int dose = ConvertDoseToInt((visitMedicie.getMedicinDos()));
       
          int quantity = 1;
          //visitMedicie.Medicinday=String.valueOf(quantity);
          System.out.println("quantity: "+visitMedicie.getMedicinQuntity());
          quantity=Integer.parseInt(visitMedicie.getMedicinQuntity());
         
          int totalTablets = days * dose * quantity;
       
          if (totalTablets > 0)
          {
              calculatedTablets += (totalTablets / 10)+" Dram";
          }
         System.out.println(calculatedTablets);
          return calculatedTablets;
      }
      private String CalculateBioChemicQuntity(VisitMedicin visitMedicie)
      {
          String calculatedBiochemic = "";
          int days = 1;
          days=Integer.parseInt(visitMedicie.getMedicinday());
         // visitMedicie.setMedicinday(String.valueOf(days)); 
         
          int dose = ConvertDoseToInt(visitMedicie.getMedicinDos());
          int quantity = 1;
          quantity=Integer.parseInt(visitMedicie.getMedicinQuntity());
         // visitMedicie.setMedicinday(String.valueOf(quantity));;
       
          int totalBioChemic = days * dose * quantity;
          if (totalBioChemic> 0 )
          {
              calculatedBiochemic += String.valueOf(totalBioChemic / 277) + " Dram";
          }

          return calculatedBiochemic;
      }
      private String CalculateLiqidQuntity(VisitMedicin visitMedicie)
      {
          String calculatedLiqid = "";
          int days = 1;
          days=Integer.parseInt(visitMedicie.getMedicinday());
         // visitMedicie.setMedicinday(String.valueOf(days)); 
          
          int dose = ConvertDoseToInt(visitMedicie.getMedicinDos());
          int quantity = 1;
          quantity=Integer.parseInt(visitMedicie.getMedicinQuntity());
         // visitMedicie.setMedicinday(String.valueOf(quantity));
          
          int totalBioChemic = days * dose * quantity;
          if (totalBioChemic > 0 && totalBioChemic <= 500)
          {
              calculatedLiqid += String.valueOf(totalBioChemic/35) + " ml";
          }
          else
          {
              calculatedLiqid +=String.valueOf (totalBioChemic / 22) + " ml";
          }

          return calculatedLiqid;
      }
      private String CalculatePowderQuntity(VisitMedicin visitMedicie)
      {
          String calculatedPowder = "";
          int days = 1;
          days=Integer.parseInt(visitMedicie.getMedicinday());
          //visitMedicie.setMedicinday(String.valueOf(days)); 
         
          int dose = ConvertDoseToInt((visitMedicie.getMedicinDos()));
          int quantity = 1;
          quantity=Integer.parseInt(visitMedicie.getMedicinQuntity());
          //visitMedicie.setMedicinday(String.valueOf(quantity));
          
          int totalBioChemic = days * dose * quantity;


          return calculatedPowder;
      }
	  
      
      private String CalculateOintmentQuntity(VisitMedicin visitMedicie)
      {
          String calculatedOintment = "";
          int days = 1;
          days=Integer.parseInt(visitMedicie.getMedicinday());
          //visitMedicie.setMedicinday(String.valueOf(days)); 
         
          int dose = ConvertDoseToInt((visitMedicie.getMedicinDos()));
          int quantity = 1;
          quantity=Integer.parseInt(visitMedicie.getMedicinQuntity());
          //visitMedicie.setMedicinday(String.valueOf(quantity));
          
          int totalOintment = days * dose * quantity;
          
          if (totalOintment > 0 && totalOintment <= 500)
          {
        	  calculatedOintment += String.valueOf(totalOintment/35) + " ml";
          }
          else
          {
        	  calculatedOintment +=String.valueOf (totalOintment / 22) + " ml";
          }

          return calculatedOintment;
      }
	  
	  
	  
	  
	int  GetMedicinType(String medicinType)
      {
          int returnResult=0;
          System.out.println("medicinType: "+medicinType);
          switch (medicinType)
          {
              case "tablets":
              case "Tablets":
                  returnResult = MedicinTypeCode.tablets.getNumVal();
                  break;
              case "globules":
              case "Globules":
                  returnResult = MedicinTypeCode.globules.getNumVal();
                  break;
              case "syrup":
              case "Syrup":
                  returnResult = MedicinTypeCode.syrup.getNumVal();
                  break;
              case "biochemic":
              case "Biochemic":  
                  returnResult = MedicinTypeCode.biochemic.getNumVal();
                  break;
              case "powder":
              case "Powder":
                  returnResult = MedicinTypeCode.powder.getNumVal();
                  break;
              case "liquid":
              case  "Liquid":
                  returnResult = MedicinTypeCode.liquid.getNumVal();
                  break;
              case "ointment":
              case "Ointment":
                     returnResult = MedicinTypeCode.ointment.getNumVal();
            	  break;
            	  
          }
          System.out.println("returnResult: "+returnResult);
          return returnResult;
      }
	 
	/* public  enum MedicinTypeCode
	  {
	     
		 
	          globules(100),
	          syrup(101),
	          tablets(102),
	          biochemic(103) ,
	          powder (104),
	          liquid (105);
		  
		  private int numVal;

		  MedicinTypeCode(int numVal) {
		        this.numVal = numVal;
		    }

		    public int getNumVal() {
		        return numVal;
		    }
	  }*/
	  
	   int ConvertDoseToInt(String dose)
       {
		   System.out.println("dose: "+dose);
           int result = 1;
//           switch (dose)
//           {
//               case "BD":
//                   result = 2;
//                   break;
//               case "OD":
//                   result = 1;
//                   break;
//               case "TDS":
//                   result = 3;
//                   break;
//               case "HS":
//                   result = 1;
//                   break;
//               case "SOS":
//                   result = 4;
//                   break;
//               case "LA":
//                   result = 1;
//                   break;
//           }
           
           
           switch (dose)
         {
             case "BD - Twice a day":
                 result = 2;
                 break;
             case "OD - Morning":
                 result = 1;
                 break;
             case "OD - Evening":
                 result = 1;
                 break;
             case "TDS - Thrice a day":
                 result = 3;
                 break;
             case "HS - Night":
                 result = 1;
                 break;
             case "SOS - As needed":
                 result = 4;
                 break;
             case "LA - Local app":
                 result = 1;
                 break;
             case "Every Hour":
                 result = 12;
                 break;
             case "Every 2 hour":
                 result = 6;
                 break;
             case "Every 2nd day":
                 result = 3;
                 break;
             case "Every 3rd day":
                 result = 2;
                 break;
             case "Every 4th day":
                 result = 1;
                 break;
             case "Weekly":
                 result = 1;
                 break;
             case "Every 2nd week":
                 result = 2;
                 break;
         }
           System.out.println("result: "+result);
           return result;
       }

}

