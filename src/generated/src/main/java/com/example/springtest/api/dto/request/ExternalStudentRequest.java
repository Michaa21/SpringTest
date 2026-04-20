package com.example.springtest.api.dto.request;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-20T14:11:11.265379+05:00[Asia/Yekaterinburg]")
public class ExternalStudentRequest {

  private String extraInfo;

  public ExternalStudentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExternalStudentRequest(String extraInfo) {
    this.extraInfo = extraInfo;
  }

  public ExternalStudentRequest extraInfo(String extraInfo) {
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
    ExternalStudentRequest externalStudentRequest = (ExternalStudentRequest) o;
    return Objects.equals(this.extraInfo, externalStudentRequest.extraInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extraInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalStudentRequest {\n");
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

