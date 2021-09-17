package com.queroevento.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import com.queroevento.enums.CatalogStatusEvent;
import com.queroevento.enums.StatusEvent;
import com.queroevento.enums.TurbineType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {

	@Id
	@SequenceGenerator(name = "EVENTSEQ", sequenceName = "EVENT_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "EVENTSEQ")
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_company", nullable = false)
	private Company company;
	private String title;
	private String urlTitle;
	@ManyToOne
	@JoinColumn(name = "id_category", nullable = false)
	private Category category;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String imageUrl;
	private String state;
	private String city;
	private String local;
	private Double price;
	private String keyword;
	private Long views;
	@JoinColumn(name = "event_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventDate;
	@JoinColumn(name = "create_event_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createEventDate;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String shortDescription;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String completeDescription;
	@JoinColumn(name = "people_estimate")
	private Integer peopleEstimate;
	@JoinColumn(name = "show_phone_numer")
	private Boolean showPhoneNumber;
	@JoinColumn(name = "show_email")
	private Boolean showEmail;
	@Type(type = "org.hibernate.type.EnumType")
	private StatusEvent status;
	@Type(type = "org.hibernate.type.EnumType")
	private CatalogStatusEvent catalogStatus;
	@Type(type = "org.hibernate.type.EnumType")
	private TurbineType turbineType;
}