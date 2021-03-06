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

import org.hibernate.annotations.Type;

import com.queroevento.enums.CatalogStatusEvent;
import com.queroevento.enums.StatusEvent;
import com.queroevento.enums.TurbineType;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Date getCreateEventDate() {
		return createEventDate;
	}

	public void setCreateEventDate(Date createEventDate) {
		this.createEventDate = createEventDate;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getCompleteDescription() {
		return completeDescription;
	}

	public void setCompleteDescription(String completeDescription) {
		this.completeDescription = completeDescription;
	}

	public Integer getPeopleEstimate() {
		return peopleEstimate;
	}

	public void setPeopleEstimate(Integer peopleEstimate) {
		this.peopleEstimate = peopleEstimate;
	}

	public Boolean getShowPhoneNumber() {
		return showPhoneNumber;
	}

	public void setShowPhoneNumber(Boolean showPhoneNumber) {
		this.showPhoneNumber = showPhoneNumber;
	}

	public Boolean getShowEmail() {
		return showEmail;
	}

	public void setShowEmail(Boolean showEmail) {
		this.showEmail = showEmail;
	}

	public StatusEvent getStatus() {
		return status;
	}

	public void setStatus(StatusEvent status) {
		this.status = status;
	}

	public CatalogStatusEvent getCatalogStatus() {
		return catalogStatus;
	}

	public void setCatalogStatus(CatalogStatusEvent catalogStatus) {
		this.catalogStatus = catalogStatus;
	}

	public TurbineType getTurbineType() {
		return turbineType;
	}

	public void setTurbineType(TurbineType turbineType) {
		this.turbineType = turbineType;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

}