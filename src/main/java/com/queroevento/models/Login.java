package com.queroevento.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Login {

	@Id
	@SequenceGenerator(name = "LOGINSEQ", sequenceName = "LOGIN_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "LOGINSEQ")
	private Long id;
	private String email;
	private String password;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_company")
	private Company company;
	private Date createDate;
	private String token;
	private Date expirationTokenDate;
	private Boolean active;
}