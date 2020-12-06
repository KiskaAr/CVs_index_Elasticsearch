package com.cv_index.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Please enter you index name
@Document(indexName = "cv_s")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CV {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String cvId;

	@Field(type = FieldType.Text)
	private String name;

	@Field(type = FieldType.Text)
	private String extension;

	@Field(type = FieldType.Text)
	private String content;

	public CV(String content, String name, String extension) {
		this.content = content;
		this.name = name;
		this.extension = extension;
	}

	@Override
	public String toString() {
		return "CV [cvId=" + cvId + ", name=" + name + ", extension=" + extension + ", content=" + content + "]";
	}

	//Getters and setters : need to get CV attributes for controller methods
	public String getId() {return cvId;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getExtension() {return extension;}
	public void setExtension(String extension) {this.extension = extension;}
	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}

}
