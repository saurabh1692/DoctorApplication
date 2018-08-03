package io.doctorApp.doc;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateSearchOprations
{
    //-----------------------------------------------------------------------------------------------------------
	static HashMap<String, String> dicColumnsMapping;
    public static String GenerateSearchQuery(SearchQuery objSearchQuery, String subscription)
    {
        String resultMySqlCommand = "";
        if (objSearchQuery != null)
        {
          String searchCondition = ReplaceColumnsInSearchQuery(objSearchQuery);
          if (objSearchQuery.getSelectedDynamicColumnsList() != null && objSearchQuery.getSelectedDynamicColumnsList().size() <= 0)
           {
        	  resultMySqlCommand =  "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name," +
                      "patient_information_master.patient_last_name,patient_information_master.patient_gender,patient_information_master.patient_email_id,patient_information_master.patient_address,patient_information_master.patient_phone_number" +
                      " from patient_information_master  inner join patient_image_table " +
                      "  on patient_information_master.patient_reg_id = patient_image_table.patient_reg_id where "+ searchCondition +" and patient_information_master.subscriptionid='"+subscription+"'";
           }
           else
           {
              resultMySqlCommand = "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name,"
              		+ "patient_information_master.patient_last_name, patient_information_master.patient_gender,patient_information_master.patient_email_id,"
              		+ "patient_information_master.patient_address,patient_information_master.patient_phone_number,patient_image_table.image_path,patient_image_table.patient_reg_id,"
              		+ "patient_image_table.image_id"
              		+ " from patient_information_master "
              		+ " left join patient_dynamic_table on patient_information_master.patient_reg_id = patient_dynamic_table.patient_reg_id "
              		+ " left join patient_image_table on patient_information_master.patient_reg_id = patient_image_table.patient_reg_id"
              		+ " where " + searchCondition +" and  patient_information_master.subscriptionid='"+subscription+"'";
           }
        }
        return resultMySqlCommand;
    }
    //-----------------------------------------------------------------------------------------------------------
    static String ReplaceColumnsInSearchQuery(SearchQuery objSearchQuery)
    {
    	String strQuery = "";
    	
        String StringQuery = objSearchQuery.getSearchQueryString();
        String[] words = StringQuery.split(",");                    
        int count = 0;
        String NewStr ="";
        String oprationalString ="";
        for(int i=0;i<words.length;i++)
        {
        	String word=words[i];
            for (int index = 0; index < 1; index++)
            {

                if (count % 2 != 0)
                {
                    NewStr = NewStr + "'%" + word + "%'";
                 }
                else
                {
                        NewStr =NewStr + word;
                }
            }
            count++;
        }
        oprationalString = NewStr;
        
        
        dicColumnsMapping = new HashMap<String, String>();
        
        dicColumnsMapping.put("First Name", "patient_information_master.patient_first_name");
        dicColumnsMapping.put("Middle Name", "patient_information_master.patient_middle_name");
        dicColumnsMapping.put("Last Name", "patient_information_master.patient_last_name");
        dicColumnsMapping.put("Gender","patient_information_master.patient_gender");
        dicColumnsMapping.put("Marital Status","patient_information_master.patient_marital_status");
        dicColumnsMapping.put("Date of Birth","patient_information_master.patient_date_of_birth");
        dicColumnsMapping.put("Country","patient_information_master.patient_nationality");
        dicColumnsMapping.put("State", "patient_information_master.patient_state");
        dicColumnsMapping.put("City", "patient_information_master.patient_city");
        dicColumnsMapping.put("EmailId", "patient_information_master.patient_email_id");
        dicColumnsMapping.put("Phone Number","patient_information_master.patient_phone_number");
        dicColumnsMapping.put("Profession","patient_information_master.patient_profession");
        dicColumnsMapping.put("Religion","patient_information_master.patient_religion");
        dicColumnsMapping.put("Address","patient_information_master.patient_address");
        dicColumnsMapping.put("Registration Id","patient_information_master.patient_reg_id");
        dicColumnsMapping.put("Disease","patient_information_master.patient_Symptoms");
       
      
        for (String key : dicColumnsMapping.keySet())
        {
            String str = oprationalString.replaceAll(key, dicColumnsMapping.get(key));
            strQuery=str;
            oprationalString = str;
        }
    
        System.out.println(objSearchQuery.getSearchQueryString());
        System.out.println(oprationalString);
        boolean isFixexColumnUsed = oprationalString == objSearchQuery.getSearchQueryString() == true ? false : true;
        if (objSearchQuery.getSelectedDynamicColumnsList() != null && objSearchQuery.getSelectedDynamicColumnsList().size() > 0)
        {
 System.out.println("if condition");
            int indexOfFirstItem = Math.abs(oprationalString.indexOf(objSearchQuery.getSelectedDynamicColumnsList().get(0)));
           
            String subStringDynamicQuery = oprationalString.substring(indexOfFirstItem-1);
            
            String subStringFixedQuery = oprationalString.substring(0, indexOfFirstItem-1);
           
            String strOprationalString = subStringDynamicQuery;
            
            strOprationalString = strOprationalString.replace("AND", "");
            
            strOprationalString = strOprationalString.replace("OR", "");
           
            String inQuery = isFixexColumnUsed == true ? "(patient_dynamic_table.column_name, patient_dynamic_table.column_value) IN   (" : " (patient_dynamic_table.column_name , patient_dynamic_table.column_value)";
            
            for(int i=0;i<objSearchQuery.getSelectedDynamicColumnsList().size();i++)
            {
        	   String columnName=objSearchQuery.getSelectedDynamicColumnsList().get(i);
        	   String strSubString = strOprationalString.substring(0);
        	   int indexFirstSingleQuotes =Math.abs(strSubString.indexOf('\''));
        	   String strColumnValue = strSubString.substring(0, indexFirstSingleQuotes);
        	   inQuery += "('" + columnName + "','" + strColumnValue + "')";
            }
          //  inQuery = inQuery.replace(",","");
          System.out.println(inQuery+" at line 120");
            inQuery += ")";
           
            subStringFixedQuery += inQuery;
           
            strQuery=subStringFixedQuery;
            
            return subStringFixedQuery;
        }
        return strQuery;
    } 
                            
//*************************************************************************************************************************
static String GenerateSearchQueryForGroupSearch(String query, String groupQuery, int limit, int offset, String subscription)
         {
        String resultQuery1 = "";
        
        if (query.isEmpty()== false)
        
        {
           SearchQuery search=new SearchQuery();
           search.setSearchQueryString(query);
        
           search.setSelectedDynamicColumnsList(new ArrayList<String>());
           String tmpQry = ReplaceColumnsInSearchQuery(search);

           if (tmpQry.isEmpty() == false)
           {
               resultQuery1 = "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name,"
                		             + "patient_information_master.patient_last_name,patient_image_table.patient_reg_id,patient_image_table.image_path,"
                		             + "patient_image_table.image_id,patient_information_master.patient_address,"
                		             + "patient_information_master.patient_phone_number,patient_information_master.patient_email_id,"
                		             + "patient_information_master.patient_gender from patient_information_master left join patient_group_table"
                		             + " on patient_information_master.patient_reg_id = patient_group_table.patient_reg_id  left join patient_image_table "
                		             + "on patient_information_master.patient_reg_id = patient_image_table.patient_reg_id where "
                		             + "patient_information_master.patient_isDelete = '0' AND patient_information_master.subscriptionid='"+subscription+"' and patient_group_table.subscriptionid='"+subscription+"' and patient_image_table.subscriptionid='"+subscription+"'";//+tmpQry+"='"+groupQuery+"' ";
               
                int indexOfGrupNameColumn = tmpQry.indexOf("GroupName");
                System.out.println("indexOfGrupNameColumn: "+indexOfGrupNameColumn);
                if (indexOfGrupNameColumn > 1)
                {
                	System.out.println("in if");
                    //String StrSubStringGroupQuery = tmpQry.substring(indexOfGrupNameColumn);
                  
                	String StrSubStringMainQuery = tmpQry.substring(0, indexOfGrupNameColumn);
                   
                	 int index=groupQuery.indexOf("(");
                	 
                	// StrSubStringGroupQuery = tmpQry.substring(tmpQry.indexOf("(")).replaceAll("(","").replaceAll(")","");
                	 
                	 String tmpStrQuery=groupQuery.substring(index).replaceAll("\\)","").replaceAll("\\(", "");
                	
                    String[] groupArray = tmpStrQuery.split(",");
                    String grouplist ="";
                    
                    for(int i=0;i<groupArray.length;i++)
                    {
                	   String str=groupArray[i];
                        grouplist += "'" + str + "',";
                    }
                   System.out.println("grouplist: "+grouplist);
                    
                    grouplist = grouplist.replaceAll(",$","");
                    resultQuery1 += StrSubStringMainQuery + " patient_group_table.patient_group_name IN (" + grouplist + ")  LIMIT  " + limit + " OFFSET " + offset + "";
                    return resultQuery1;
                }
                else if (indexOfGrupNameColumn == 0 || indexOfGrupNameColumn == 1)
                {
                   int index = groupQuery.indexOf("(");
                   System.out.println("index: "+index);
                   String tmpStrQuery = groupQuery.substring(index).replaceAll("\\)","").replaceAll("\\(","");
                   
                   String[] groupArray = tmpStrQuery.split(",");
                   
                   String grouplist = "";
                   
                   for(int i=0;i<groupArray.length;i++)
                    {
                	   	String str=groupArray[i];
                        grouplist += "'" + str + "',";
                    }
                   
                   grouplist = grouplist.replaceAll(",$","");
                   
                   resultQuery1 += "patient_group_table.patient_group_name IN (" + grouplist + ")  LIMIT  " + limit  + " OFFSET " + offset + "";
                   return resultQuery1;
                }
                else
                {
                   resultQuery1 = "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name,"
                		   			+ "patient_information_master.patient_last_name,patient_image_table.patient_reg_id,"
                    				+ "patient_image_table.image_path,patient_image_table.image_id,patient_information_master.patient_address,"
                    				+ "patient_information_master.patient_phone_number,patient_information_master.patient_email_id,"
                    				+ "patient_information_master.patient_gender from patient_information_master "
                    				+ " inner join patient_image_table on patient_information_master.patient_reg_id =" 
                    				+ "patient_image_table.patient_reg_id where patient_information_master.patient_isDelete = '0' "
                    				+" AND "+tmpQry+"="+groupQuery+" and patient_information_master.subscriptionid='"+subscription+"' and patient_image_table.subscriptionid='"+subscription+"' LIMIT  " + limit + " OFFSET " + offset + "";
                   return resultQuery1;
                }
            }
          
        }
        return resultQuery1;
    }
   //***********************************************************************************************************************
static String GenerateSearchQueryForGroupSearchGrid(String query, String groupQuery, String subscription)
    {
    	 String resultQuery = "";
       
    
    	 
    	 System.out.println("query: "+query);
    	 System.out.println("groupQuery: "+groupQuery);
        if (query.isEmpty() == false)
        {
        	SearchQuery search=new SearchQuery();
        	search.setSearchQueryString(query);
        	
          search.setSelectedDynamicColumnsList(new ArrayList<String>());
          
          String tmpQry= "";
          
          if(query.contains("="))
          {
        	  int mediindex = query.indexOf("=");
              
              String meditype = query.substring(0, mediindex);
              String mediname = query.substring(mediindex+1);
              
              System.out.println("meditype: "+meditype +" mediname: "+mediname);
             
              if(meditype.equals("Medicine"))
              {
            	  tmpQry = "patient_information_master.patient_reg_id in(select  distinct patient_reg_id from patient_medicien_details where medicien_name like "+mediname+" and subscriptionid='"+subscription+"');";
              }
          }
        
          else
          {
        	  tmpQry = ReplaceColumnsInSearchQuery(search);
          }
        	
           
          
           System.out.println("tmpQury: "+tmpQry);
            if (tmpQry.isEmpty() == false)
            {
                String resultQuery1 = "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name,patient_information_master.patient_last_name,patient_image_table.patient_reg_id,patient_image_table.image_path,patient_image_table.image_id,patient_information_master.patient_address,patient_information_master.patient_phone_number,patient_information_master.patient_email_id,patient_information_master.patient_gender from patient_information_master  left join patient_group_table on patient_information_master.patient_reg_id = patient_group_table.patient_reg_id  left join patient_image_table on" +
                  " patient_information_master.patient_reg_id = patient_image_table.patient_reg_id where patient_information_master.patient_isDelete = '0' and patient_information_master.subscriptionid='"+subscription+"' AND " ;//+ tmpQry +"="+groupQuery+"";
                
                int indexOfGrupNameColumn = tmpQry.indexOf("GroupName");
                System.out.println("indexOfGrupNameColumn: "+indexOfGrupNameColumn);
                if (indexOfGrupNameColumn > 1)
                {
                    String strSubStringGroupQuery = tmpQry.substring(indexOfGrupNameColumn);
                    String StrSubStringMainQuery = tmpQry.substring(0, indexOfGrupNameColumn);
                    strSubStringGroupQuery = ((tmpQry.substring(tmpQry.indexOf('(')).replaceAll("(",""))).replaceAll(")","");
                    String[] groupArray = strSubStringGroupQuery.split(",");
                    String grouplist = "";
                    
                   for(int i=0;i<groupArray.length;i++)
                    {
                	   
                	   String str=groupArray[i];
                        grouplist += "'" + str + "',";
                        
                    }
                    grouplist = grouplist.replaceAll(",","");
                    resultQuery1 += StrSubStringMainQuery + " patient_group_table.patient_group_name IN (" + grouplist + ")";
                    return resultQuery1;
                }
                
                else if (indexOfGrupNameColumn == 0 || indexOfGrupNameColumn == 1)
                {
                	 int index = tmpQry.indexOf('(');
                	 System.out.println("index: "+index);
                	
                	 String grouplist = "";
                	 
                	 if(index >= 0)
                	 {
                	
                		 String tmpStrQuery = ((tmpQry.substring(index)).replace(")","")).replaceFirst("(","");
                		 String[] groupArray = tmpStrQuery.split(",");
                		 
                		 for(int i=0;i<groupArray.length;i++)
                  
                		 {
                			 String str=groupArray[i];
                			 grouplist += "'" + str + "',";
                		 }
                		 grouplist = grouplist.replace(",","");
                	 }
                	 else
                	 {
                		 grouplist= "'"+groupQuery+"'";
                	 }
                    
                	 resultQuery1 += "patient_group_table.patient_group_name IN (" + grouplist + ")";
                	 System.out.println();
                    return resultQuery1;
                }
                else
                {
                	
                		String resultQuery2 = "Select distinct patient_information_master.patient_reg_id,patient_information_master.patient_first_name,patient_information_master.patient_last_name,patient_image_table.patient_reg_id,patient_image_table.image_path,patient_image_table.image_id," +
                                "patient_information_master.patient_address,patient_information_master.patient_phone_number,patient_information_master.patient_email_id,patient_information_master.patient_gender" +
                                " from patient_information_master  inner join patient_image_table on patient_information_master.patient_reg_id =patient_image_table.patient_reg_id where patient_information_master.patient_isDelete = '0' AND " + tmpQry +"and patient_information_master.subscriptionid='"+subscription+"'";
                    
                 	  
                	  System.out.println("resultQuery2: "+ resultQuery2);
                    return resultQuery2;
                }
            }
         
        }
        System.out.println(resultQuery+" resultQuery");
        return resultQuery;
    }
}
