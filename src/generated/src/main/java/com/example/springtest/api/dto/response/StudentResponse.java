package com.example.springtest.api.dto.response;

import java.net.URI;
import java.util.Objects;
import com.example.springtest.api.dto.response.LessonResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-30T15:10:47.155578300+05:00[Asia/Yekaterinburg]")
public class StudentResponse {

  private UUID id;

  private String name;

  private String email;

  private Integer age;

  private String extra;

  @Valid
  private List<@Valid LessonResponse> lessons = new ArrayList<>();

  public StudentResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StudentResponse(UUID id, String name, String email, Integer age, String extra, List<@Valid LessonResponse> lessons) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.age = age;
    this.extra = extra;
    this.lessons = lessons;
  }

  public StudentResponse id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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
  @NotNull 
  @Schema(name = "name", example = "Bob", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StudentResponse email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "bob@mail.com", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public StudentResponse age(Integer age) {
    this.age = age;
    return this;
  }

  /**
   * Get age
   * @return age
  */
  @NotNull 
  @Schema(name = "age", example = "18", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("age")
  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public StudentResponse extra(String extra) {
    this.extra = extra;
    return this;
  }

  /**
   * Get extra
   * @return extra
  */
  @NotNull 
  @Schema(name = "extra", example = "extra-info-for-Bob", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("extra")
  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
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
  @NotNull @Valid 
  @Schema(name = "lessons", requiredMode = Schema.RequiredMode.REQUIRED)
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
        Objects.equals(this.email, studentResponse.email) &&
        Objects.equals(this.age, studentResponse.age) &&
        Objects.equals(this.extra, studentResponse.extra) &&
        Objects.equals(this.lessons, studentResponse.lessons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, email, age, extra, lessons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StudentResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    extra: ").append(toIndentedString(extra)).append("\n");
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

