package com.example.stellarinvestment.model.project;

import com.example.stellarinvestment.amazon.Constants;
import com.example.stellarinvestment.model.IdBasedEntity;

import javax.persistence.*;

@Entity
@Table(name = "project_images")
public class ProjectImage extends IdBasedEntity {
	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	public ProjectImage() {
	}

	public ProjectImage(String name, Project project) {
		this.name = name;
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Transient
	public String getImagePath() {
		return Constants.S3_BASE_URI + "/project-images/" + project.getId() + "/extras/" + this.name;
	}
}
