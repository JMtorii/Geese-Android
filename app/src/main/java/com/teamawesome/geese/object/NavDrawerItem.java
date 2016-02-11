package com.teamawesome.geese.object;

import com.teamawesome.geese.R;

/**
 * Created by lcolam on 2/10/16.
 */
public class NavDrawerItem {
    public int icon;
    public String name;

    public NavDrawerItem(Builder builder) {
        this.icon = builder.icon;
        this.name = builder.name;
    }

    public int getIcon() { return icon; }
    public String getName() { return name; }

    public static class Builder {
        private int icon;
        private String name;

        public Builder() {
            icon = R.drawable.ic_action_kebab;
            name = "Navigation Drawer Item";
        }

        public Builder icon(int i) { icon = i; return this; }
        public Builder name(String n) { name = n; return this; }
        public NavDrawerItem build() {
            return new NavDrawerItem(this);
        }
    }
}
