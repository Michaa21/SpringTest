package com.example.springtest.api.dto.request;

import java.net.URI;
import java.util.Objects;
import com.example.springtest.api.dto.request.LessonCreateRequest;
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
 * StudentCreateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-10T15:03:25.072660600+05:00[Asia/Yekaterinburg]")
public class StudentCreateRequest {

  private String name;

  private UUID externalId;

  @Valid
  private List<@Valid LessonCreateRequest> lessons = new ArrayList<>();

  public StudentCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StudentCreateRequest(String name, UUID externalId, List<@Valid LessonCreateRequest> lessons) {
    this.name = name;
    this.externalId = externalId;
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
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StudentCreateRequest externalId(UUID externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * Get externalId
   * @return externalId
  */
  @NotNull @Valid 
  @Schema(name = "externalId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("externalId")
  public UUID getExternalId() {
    return externalId;
  }

  public void setExternalId(UUID externalId) {
    this.externalId = externalId;
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
        Objects.equals(this.externalId, studentCreateRequest.externalId) &&
        Objects.equals(this.lessons, studentCreateRequest.lessons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, externalId, lessons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StudentCreateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
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

