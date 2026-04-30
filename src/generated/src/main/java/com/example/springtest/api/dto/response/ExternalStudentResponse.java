package com.example.springtest.api.dto.response;

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
 * ExternalStudentResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-30T12:37:42.905044100+05:00[Asia/Yekaterinburg]")
public class ExternalStudentResponse {

  private UUID studentId;

  private String extraInfo;

  public ExternalStudentResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExternalStudentResponse(UUID studentId, String extraInfo) {
    this.studentId = studentId;
    this.extraInfo = extraInfo;
  }

  public ExternalStudentResponse studentId(UUID studentId) {
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

  public ExternalStudentResponse extraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
    return this;
  }

  /**
   * Get extraInfo
   * @return extraInfo
  */
  @NotNull 
  @Schema(name = "extraInfo", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("extraInfo")
  public String getExtraInfo() {
    return extraInfo;
  }

  public void setExtraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExternalStudentResponse externalStudentResponse = (ExternalStudentResponse) o;
    return Objects.equals(this.studentId, externalStudentResponse.studentId) &&
        Objects.equals(this.extraInfo, externalStudentResponse.extraInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, extraInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalStudentResponse {\n");
    sb.append("    studentId: ").append(toIndentedString(studentId)).append("\n");
    sb.append("    extraInfo: ").append(toIndentedString(extraInfo)).append("\n");
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

