package com.example.server.manager.web.pulltool;

public class RootContextPath {

    private String contextPath;

    public RootContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getURI(String uri) {
        String prefix;
        if (contextPath != null && contextPath.length() > 0 && !"/".equals(contextPath)) {
            prefix = contextPath;
        } else {
            prefix = "";
        }
        if (uri.startsWith("/")) {
            return prefix + uri;
        } else {
            return prefix + "/" + uri;
        }
    }
}
