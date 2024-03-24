package com.projects.socialapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "role" })
public enum Role {
    USER,ADMIN
}
