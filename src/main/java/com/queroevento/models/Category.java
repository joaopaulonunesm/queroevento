package com.queroevento.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}