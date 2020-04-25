package uk.co.compendiumdev.thepulper.abstractions.navigation;

import org.openqa.selenium.By;

public class PulperDropDownMenuItem {

    private final String menuId;
    private final String menuTitle;
    private final String pageTitle;
    private final String menuPath;
    private String href;

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

    public static PulperDropDownMenuItem withoutId(final String menuTitle, final String pageTitle) {
        return new PulperDropDownMenuItem("", menuTitle, pageTitle);
    }

    public static PulperDropDownMenuItem subMenuWithoutId(final String pageTitle, final String menuPath) {
        String[] path = menuPath.split(" > ");
        return new PulperDropDownMenuItem("", path[1], pageTitle, menuPath);
    }

    public static PulperDropDownMenuItem subMenuItemWithoutId(final String menuPath, final String pageTitle) {
        return subMenuWithoutId(pageTitle, menuPath);
    }

    public static PulperDropDownMenuItem withId(final String menuTitle, final String pageTitle, final String menuId) {
        return new PulperDropDownMenuItem(menuId, menuTitle, pageTitle);
    }

    public static PulperDropDownMenuItem subMenuItemWithId(final String menuPath, final String pageTitle, final String menuId) {
        String[] path = menuPath.split(" > ");
        return new PulperDropDownMenuItem(menuId, path[1], pageTitle, menuPath);
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

    public By getLocator() {
        if(hasId()){
            return By.id(menuId);
        }else{
            return By.linkText(menuTitle);
        }
    }

    public String getLocatorCss() {
        if(hasId()){
            return "#" + menuId;
        }else{
            return menuTitle;
        }
    }

    public boolean hasId() {
        return menuId!=null && menuId.length()>0;
    }

    public void hasUrl(final String href) {
        this.href = href;
    }

    public String getUrl() {
        return href;
    }
}
