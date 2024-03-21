package com.projects.socialapp.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String jwt;
    private String msg;
    public AuthResponse(String jwt, String msg)
    {
        super();
        this.jwt = jwt;
        this.msg = msg;
    }
}
