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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-22T16:30:47.820508400+05:00[Asia/Yekaterinburg]")
public class ExternalStudentRequest {

  private UUID studentId;

  private String name;

  public ExternalStudentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExternalStudentRequest(UUID studentId, String name) {
    this.studentId = studentId;
    this.name = name;
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
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
        Objects.equals(this.name, externalStudentRequest.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalStudentRequest {\n");
    sb.append("    studentId: ").append(toIndentedString(studentId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

