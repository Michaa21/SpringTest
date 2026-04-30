package com.example.springtest.api.dto.request;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExternalStudentRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-30T12:37:42.336115200+05:00[Asia/Yekaterinburg]")
public class ExternalStudentRequest {

  private UUID studentId;

  private String name;

  private String email;

  private Integer age;

  public ExternalStudentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExternalStudentRequest(UUID studentId, String name, String email, Integer age) {
    this.studentId = studentId;
    this.name = name;
    this.email = email;
    this.age = age;
  }

  public ExternalStudentRequest studentId(UUID studentId) {
    this.studentId = studentId;
    return this;
  }

  /**
   * Get studentId
   * @return studentId
  */
  @NotNull @Valid 
  @Schema(name = "studentId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("studentId")
  public UUID getStudentId() {
    return studentId;
  }

  public void setStudentId(UUID studentId) {
    this.studentId = studentId;
  }

  public ExternalStudentRequest name(String name) {
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

  public ExternalStudentRequest email(String email) {
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

  public ExternalStudentRequest age(Integer age) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExternalStudentRequest externalStudentRequest = (ExternalStudentRequest) o;
    return Objects.equals(this.studentId, externalStudentRequest.studentId) &&
        Objects.equals(this.name, externalStudentRequest.name) &&
        Objects.equals(this.email, externalStudentRequest.email) &&
        Objects.equals(this.age, externalStudentRequest.age);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, name, email, age);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalStudentRequest {\n");
    sb.append("    studentId: ").append(toIndentedString(studentId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
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

