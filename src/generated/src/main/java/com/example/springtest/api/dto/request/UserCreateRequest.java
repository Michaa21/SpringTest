package com.example.springtest.api.dto.request;

import java.net.URI;
import java.util.Objects;
import com.example.springtest.api.dto.request.ProfileRequest;
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
 * UserCreateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-20T14:11:11.265379+05:00[Asia/Yekaterinburg]")
public class UserCreateRequest {

  private String username;

  private ProfileRequest profile;

  public UserCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserCreateRequest(String username, ProfileRequest profile) {
    this.username = username;
    this.profile = profile;
  }

  public UserCreateRequest username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
  */
  @NotNull @Size(min = 1, max = 100) 
  @Schema(name = "username", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserCreateRequest profile(ProfileRequest profile) {
    this.profile = profile;
    return this;
  }

  /**
   * Get profile
   * @return profile
  */
  @NotNull @Valid 
  @Schema(name = "profile", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("profile")
  public ProfileRequest getProfile() {
    return profile;
  }

  public void setProfile(ProfileRequest profile) {
    this.profile = profile;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserCreateRequest userCreateRequest = (UserCreateRequest) o;
    return Objects.equals(this.username, userCreateRequest.username) &&
        Objects.equals(this.profile, userCreateRequest.profile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, profile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserCreateRequest {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
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

