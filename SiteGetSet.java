package com.sample.jsonparsing;

/**
 * Created by rajasingh on 4/29/2016.
 */
public class SiteGetSet {

    public String siteName;
    public String siteId;

    public SiteGetSet(String siteName, String siteId){
        this.siteName = siteName;
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
