package com.brightcode.mediadeck.models;

import com.brightcode.mediadeck.data.AppUserRepository;
import com.brightcode.mediadeck.security.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;

public enum Theme {
    DARK("/css/dark.css", "navbar navbar-inverse navbar-fixed-top bar"),
    LIGHT("/css/light.css", "navbar navbar-default navbar-fixed-top bar"),
    SEAFOAM("/css/seafoam.css", "navbar navbar-fixed-top bar"),
    SLATE("/css/slate.css", "navbar navbar-fixed-top bar");

    private final String cssPath;
    private final String navMode;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AppUserRepository appUserRepository;


    Theme(String cssPath, String navMode) {
        this.cssPath = cssPath;
        this.navMode = navMode;
    }

    public String getCssPath() {
        return cssPath;
    }

    public String getNavMode() {
        return navMode;
    }
}
