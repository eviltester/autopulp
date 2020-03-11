package uk.co.compendiumdev.thepulper.v003;

public class PulperDropDownMenuItem {

    private final String menuId;
    private final String menuTitle;
    private final String pageTitle;

    public PulperDropDownMenuItem(final String menuid, final String menutitle, final String pagetitle) {
        this.menuId = menuid;
        this.menuTitle = menutitle;
        this.pageTitle = pagetitle;
    }

    public String menuId(){
        return menuId;
    }

    public String menuTitle(){
        return menuTitle;
    }

    public String pageTitle(){
        return pageTitle;
    }

}
