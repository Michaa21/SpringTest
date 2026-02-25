package com.example.springtest.api.model;

import java.net.URI;
import java.util.Objects;
import com.example.springtest.api.model.LessonResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StudentResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class StudentResponse {

  private Long id;

  private String name;

  @Valid
  private List<@Valid LessonResponse> lessons;

  public StudentResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public StudentResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StudentResponse lessons(List<@Valid LessonResponse> lessons) {
    this.lessons = lessons;
    return this;
  }

  public StudentResponse addLessonsItem(LessonResponse lessonsItem) {
    if (this.lessons == null) {
      this.lessons = new ArrayList<>();
    }
    this.lessons.add(lessonsItem);
    return this;
  }

  /**
   * Get lessons
   * @return lessons
  */
  @Valid 
  @Schema(name = "lessons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lessons")
  public List<@Valid LessonResponse> getLessons() {
    return lessons;
  }

  public void setLessons(List<@Valid LessonResponse> lessons) {
    this.lessons = lessons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudentResponse studentResponse = (StudentResponse) o;
    return Objects.equals(this.id, studentResponse.id) &&
        Objects.equals(this.name, studentResponse.name) &&
        Objects.equals(this.lessons, studentResponse.lessons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, lessons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StudentResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lessons: ").append(toIndentedString(lessons)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

