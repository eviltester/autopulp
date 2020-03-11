package uk.co.compendiumdev.thepulper.v003;

public class PulperDropDownMenuItem {

    private final String menuId;
    private final String menuTitle;
    private final String pageTitle;
    private final String menuPath;

    public PulperDropDownMenuItem(final String menuid, final String menutitle, final String pagetitle) {
        this.menuId = menuid;
        this.menuTitle = menutitle;
        this.pageTitle = pagetitle;
        this.menuPath = menutitle;
    }

    public PulperDropDownMenuItem(final String menuid, final String menutitle, final String pagetitle, final String menupath) {
        this.menuId = menuid;
        this.menuTitle = menutitle;
        this.pageTitle = pagetitle;
        this.menuPath = menupath;
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

    public String menuPath(){
        return menuPath;
    }

    public boolean isSubMenu() {
        return menuPath.contains(" > ");
    }

    public String getParentTitle() {
        final String[] menuItems = menuPath.split(" > ");
        return menuItems[0];
    }
}
