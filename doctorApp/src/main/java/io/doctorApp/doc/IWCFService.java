package io.doctorApp.doc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface IWCFService {
	
	@RequestMapping(method=RequestMethod.POST)
	 Employee GetEmployee(int employeeId);
}


class Employee 
{
   
    public String Name ;
    
    public String Gender ;
    
    public String Salary ;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getSalary() {
		return Salary;
	}

	public void setSalary(String salary) {
		Salary = salary;
	}

	
    
}