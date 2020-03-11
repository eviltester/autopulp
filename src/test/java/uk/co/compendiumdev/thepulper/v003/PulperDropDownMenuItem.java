package uk.co.compendiumdev.thepulper.v003;

import org.openqa.selenium.By;

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

    public boolean hasId() {
        return menuId!=null && menuId.length()>0;
    }
}
