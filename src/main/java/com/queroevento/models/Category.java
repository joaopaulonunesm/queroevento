package com.queroevento.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Type;

@Entity
public class Category {

	@Id
	@SequenceGenerator(name = "CATEGORYSEQ", sequenceName = "CATEGORY_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CATEGORYSEQ")
	private Long id;

	private String name;

	private String urlName;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String imageUrl;

	private int ammountEvents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getAmmountEvents() {
		return ammountEvents;
	}

	public void setAmmountEvents(int ammountEvents) {
		this.ammountEvents = ammountEvents;
	}

}