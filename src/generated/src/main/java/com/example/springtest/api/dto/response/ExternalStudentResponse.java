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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-22T14:20:31.870200+05:00[Asia/Yekaterinburg]")
public class ExternalStudentResponse {

  private UUID id;

  private String extraInfo;

  public ExternalStudentResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExternalStudentResponse(UUID id, String extraInfo) {
    this.id = id;
    this.extraInfo = extraInfo;
  }

  public ExternalStudentResponse id(UUID id) {
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
    return Objects.equals(this.id, externalStudentResponse.id) &&
        Objects.equals(this.extraInfo, externalStudentResponse.extraInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, extraInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalStudentResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

