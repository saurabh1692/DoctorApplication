package io.doctorApp.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Caching
{
   
	public interface ISomeBehaviour {
		   boolean AsyncCall_Delegate(List<DynamicColumns> listDynamicColumns) ;
		}
	
	// public final Delegator DO_SHOW = new Delegator(OUTPUT_ARGS,Void.TYPE);
	//public  boolean AsyncCall_Delegate(List<DynamicColumns> listDynamicColumns) ;
		//return false;
	
	
	//public  Dictionary<String, String> dicColumnsCacheObject = new Dictionary<String, String>();
    public  HashMap<String, String> dicColumnsCacheObject = new HashMap<String, String>();
    public  List<String> lstColumnsCache = new ArrayList<String>();
    String Registration_Id;
    String FirstName;
    String SurName;
    String MobileNo;
    String EmailId;
    String Religion;
    String Date_Of_Birth;
    String Marital_Status;
   
    {
    
    lstColumnsCache.add(Registration_Id);
    lstColumnsCache.add(FirstName);
    lstColumnsCache.add(SurName);
    lstColumnsCache.add(MobileNo);
    lstColumnsCache.add(EmailId);
    lstColumnsCache.add(Religion);
    lstColumnsCache.add(Date_Of_Birth);
    lstColumnsCache.add(Marital_Status);
    }
    		
   // {
    //	"Registration_Id","FirstName","SurName","MobileNo","EmailId","Religion","Date_Of_Birth","Marital_Status"
    	//};
    
    public  boolean AddColumnsInCacheObject(String key, String value)
    {
        if(key.isEmpty() == false && (value).isEmpty() == false && dicColumnsCacheObject.containsKey(key) == false)
        {
            dicColumnsCacheObject.put(key, value);
            return true;
        }
        else
        {
            return false;
        }
    }

    public  boolean AddDynamicColumnsClassObjectInCache(List<DynamicColumns> listDynamicColumns)
    {
        if (listDynamicColumns != null && listDynamicColumns.size()>0)
        {
            //foreach(DynamicColumns dynColumn : listDynamicColumns)
            for(int i=0;i<listDynamicColumns.size();i++)
        	{
            	DynamicColumns dynColumn=listDynamicColumns.get(i);
                if(dicColumnsCacheObject.containsKey(dynColumn.getColumnName())== false)
                {
                    dicColumnsCacheObject.put(dynColumn.getColumnName(), dynColumn.getColumnName());
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public  boolean AddFixedColumnsInCacheList(String columnName)
    {
        if (columnName.isEmpty() == false && lstColumnsCache.contains(columnName) == false)
        {
            lstColumnsCache.add(columnName);
            return true;
        }
        else
        {
            return false;
        }
    }

    public  boolean AddDynamicColumnsInCacheList(List<DynamicColumns> listDynamicColumns)
    {
        if (listDynamicColumns != null && listDynamicColumns.size() > 0)
        {
           // foreach (DynamicColumns dynColumn in listDynamicColumns)
        	  for(int i=0;i<listDynamicColumns.size();i++)
          	{
              	DynamicColumns dynColumn=listDynamicColumns.get(i);
                if (lstColumnsCache.contains(dynColumn.getColumnName()) == false)
                {
                    lstColumnsCache.add(dynColumn.getColumnName());
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }
}
