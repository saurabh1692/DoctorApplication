package io.doctorApp.doc;

import java.util.List;

public class SearchQuery
	    {

	private String SearchQueryString;

   
    private  List<String> SelectedDynamicColumnsList;
	       
			public String getSearchQueryString() {
				return SearchQueryString;
			}


			public void setSearchQueryString(String searchQueryString) {
				SearchQueryString = searchQueryString;
			}


			public List<String> getSelectedDynamicColumnsList() {
				return SelectedDynamicColumnsList;
			}


			public void setSelectedDynamicColumnsList(List<String> selectedDynamicColumnsList) {
				SelectedDynamicColumnsList = selectedDynamicColumnsList;
			}
	        
	    }

	  