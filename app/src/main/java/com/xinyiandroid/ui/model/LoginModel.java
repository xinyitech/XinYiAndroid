package com.xinyiandroid.ui.model;

import java.io.Serializable;

/**
 * Created by wxy on 2018/2/26.
 */

public class LoginModel implements Serializable{

	/**
	 * access_token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYWUdfQUxCQyIsImV4cCI6MTUyMTYwMjIwOX0.HfHrJbE-6PXOB4VmCbjiTuEd1MxlAmqjeb1VJEuO2Mz493L33aUQE4F1bysgli1CxDcwzeu5LV8MHg-QakKzhA
	 * token_type : Bearer
	 * authUser : {"userId":50475,"userName":"XYG_ALBC","emailAddress":"erp02@xinyiglass.com","employeeId":37316,"passwordDate":"2018-03-09 09:07:54","startDate":"2018-03-09 00:00:00","endDate":null,"employeeNumber":"216407","fullName":"彭美,","ipAddress":"183.62.140.12","description":null,"loginId":94435683,"language":"ZHS","authorities":null,"enabled":true,"username":"XYG_ALBC"}
	 * expires_in : 86400
	 */

	private String access_token;
	private String token_type;
	private AuthUserBean authUser;
	private int expires_in;

	public String getAccess_token() { return access_token;}

	public void setAccess_token(String access_token) { this.access_token = access_token;}

	public String getToken_type() { return token_type;}

	public void setToken_type(String token_type) { this.token_type = token_type;}

	public AuthUserBean getAuthUser() { return authUser;}

	public void setAuthUser(AuthUserBean authUser) { this.authUser = authUser;}

	public int getExpires_in() { return expires_in;}

	public void setExpires_in(int expires_in) { this.expires_in = expires_in;}

	public static class AuthUserBean {
		/**
		 * userId : 50475
		 * userName : XYG_ALBC
		 * emailAddress : erp02@xinyiglass.com
		 * employeeId : 37316
		 * passwordDate : 2018-03-09 09:07:54
		 * startDate : 2018-03-09 00:00:00
		 * endDate : null
		 * employeeNumber : 216407
		 * fullName : 彭美,
		 * ipAddress : 183.62.140.12
		 * description : null
		 * loginId : 94435683
		 * language : ZHS
		 * authorities : null
		 * enabled : true
		 * username : XYG_ALBC
		 */

		private int userId;
		private String userName;
		private String emailAddress;
		private int employeeId;
		private String passwordDate;
		private String startDate;
		private Object endDate;
		private String employeeNumber;
		private String fullName;
		private String ipAddress;
		private Object description;
		private int loginId;
		private String language;
		private Object authorities;
		private boolean enabled;
		private String username;

		public int getUserId() { return userId;}

		public void setUserId(int userId) { this.userId = userId;}

		public String getUserName() { return userName;}

		public void setUserName(String userName) { this.userName = userName;}

		public String getEmailAddress() { return emailAddress;}

		public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress;}

		public int getEmployeeId() { return employeeId;}

		public void setEmployeeId(int employeeId) { this.employeeId = employeeId;}

		public String getPasswordDate() { return passwordDate;}

		public void setPasswordDate(String passwordDate) { this.passwordDate = passwordDate;}

		public String getStartDate() { return startDate;}

		public void setStartDate(String startDate) { this.startDate = startDate;}

		public Object getEndDate() { return endDate;}

		public void setEndDate(Object endDate) { this.endDate = endDate;}

		public String getEmployeeNumber() { return employeeNumber;}

		public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber;}

		public String getFullName() { return fullName;}

		public void setFullName(String fullName) { this.fullName = fullName;}

		public String getIpAddress() { return ipAddress;}

		public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress;}

		public Object getDescription() { return description;}

		public void setDescription(Object description) { this.description = description;}

		public int getLoginId() { return loginId;}

		public void setLoginId(int loginId) { this.loginId = loginId;}

		public String getLanguage() { return language;}

		public void setLanguage(String language) { this.language = language;}

		public Object getAuthorities() { return authorities;}

		public void setAuthorities(Object authorities) { this.authorities = authorities;}

		public boolean isEnabled() { return enabled;}

		public void setEnabled(boolean enabled) { this.enabled = enabled;}

		public String getUsername() { return username;}

		public void setUsername(String username) { this.username = username;}
	}
}
