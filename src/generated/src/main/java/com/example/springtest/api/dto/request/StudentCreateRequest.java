package com.example.springtest.api.dto.request;

import java.net.URI;
import java.util.Objects;
import com.example.springtest.api.dto.request.LessonCreateRequest;
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
 * StudentCreateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-30T15:10:45.890509600+05:00[Asia/Yekaterinburg]")
public class StudentCreateRequest {

  private String name;

  private String email;

  private Integer age;

  @Valid
  private List<@Valid LessonCreateRequest> lessons = new ArrayList<>();

  public StudentCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StudentCreateRequest(String name, String email, Integer age, List<@Valid LessonCreateRequest> lessons) {
    this.name = name;
    this.email = email;
    this.age = age;
    this.lessons = lessons;
  }

  public StudentCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull @Size(min = 2, max = 50) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StudentCreateRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public StudentCreateRequest age(Integer age) {
    this.age = age;
    return this;
  }

  /**
   * Get age
   * minimum: 10
   * maximum: 99
   * @return age
  */
  @NotNull @Min(10) @Max(99) 
  @Schema(name = "age", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("age")
  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public StudentCreateRequest lessons(List<@Valid LessonCreateRequest> lessons) {
    this.lessons = lessons;
    return this;
  }

  public StudentCreateRequest addLessonsItem(LessonCreateRequest lessonsItem) {
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
  @NotNull @Valid 
  @Schema(name = "lessons", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lessons")
  public List<@Valid LessonCreateRequest> getLessons() {
    return lessons;
  }

  public void setLessons(List<@Valid LessonCreateRequest> lessons) {
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
    StudentCreateRequest studentCreateRequest = (StudentCreateRequest) o;
    return Objects.equals(this.name, studentCreateRequest.name) &&
        Objects.equals(this.email, studentCreateRequest.email) &&
        Objects.equals(this.age, studentCreateRequest.age) &&
        Objects.equals(this.lessons, studentCreateRequest.lessons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, email, age, lessons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StudentCreateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
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

