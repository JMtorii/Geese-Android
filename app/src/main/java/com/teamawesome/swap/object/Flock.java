package com.teamawesome.swap.object;

/**
 * Created by MichaelQ on 2015-07-18.
 */
public class Flock {
    public final String name;
    public final String description;
    public final double latitude;
    public final double longitude;
    public final String createdDate;
    public final String privacy;
    public final int members;

    public Flock(FlockBuilder fb) {
        name = fb.name;
        description = fb.description;
        latitude = fb.latitude;
        longitude = fb.longitude;
        createdDate = fb.createdDate;
        privacy = fb.privacy;
        members = fb.members;
    }
    public static class FlockBuilder {
        private String name;
        private String description;
        private double latitude;
        private double longitude;
        private String createdDate;
        private String privacy;
        private int members;

        public FlockBuilder() {
            //defaults
            //TODO: change whenver we get actual data, for now just use UW location
            latitude = 43.4707224;
            longitude = -80.5429343;
            createdDate = "June 1st 2015";
            privacy = "Invite Only";

        }

        public FlockBuilder name(String n) { name = n; return this; }
        public FlockBuilder description(String d) { description = d; return this; }
        public FlockBuilder latitude(double l) { latitude = l; return this; }
        public FlockBuilder longitude(double l) { longitude = l; return this; }
        public FlockBuilder createdDate(String s) { createdDate = s; return this; }
        public FlockBuilder privacy(String s) { privacy = s; return this; }
        public FlockBuilder members(int m) { members = m; return this; }
        public Flock build() {
            return new Flock(this);
        }
    }
}
