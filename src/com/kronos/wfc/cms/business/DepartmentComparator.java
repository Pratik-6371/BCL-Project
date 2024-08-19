package com.kronos.wfc.cms.business;

import java.util.Comparator;

public class DepartmentComparator implements Comparator<Department>{

	@Override
	public int compare(Department a1, Department a2) {
		// TODO Auto-generated method stub
		return a1.getCode().compareTo(a2.getCode());
				}
	
	

}
