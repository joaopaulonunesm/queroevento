package com.queroevento.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {

	@Id
	@SequenceGenerator(name = "COMPANYSEQ", sequenceName = "COMPANY_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "COMPANYSEQ")
	private Long id;
	@Column(nullable = false)
	private String name;
	private String urlName;
	private String urlImage;
	private String state;
	private String city;
	private String phoneNumber;
	private String cellPhoneNumber;
	private String contactEmail;
	private String representativeName;
	private Boolean moderator = false;
}