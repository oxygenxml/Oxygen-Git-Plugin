package com.oxygenxml.sdksamples.workspace.git.jaxb.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userCredentials")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserCredentialsList {

	@XmlElement(name = "credential")
	private List<UserCredentials> credentials = new ArrayList<UserCredentials>();

	public List<UserCredentials> getCredentials() {
		return credentials;
	}

	public void setCredentials(List<UserCredentials> credentials) {
		this.credentials = credentials;
	}

}
