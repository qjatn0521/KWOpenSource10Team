package com.example.myapplication.sports.model;
import com.google.gson.annotations.SerializedName;

public class League {
        @SerializedName("name")
        private String name;
        @SerializedName("country")
        private String country;
        @SerializedName("logo")
        private String logo;
        @SerializedName("flag")
        private String flag;

        // Getter and setter methods for each field

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        public String getLogo() {
                return logo;
        }

        public void setLogo(String logo) {
                this.logo = logo;
        }

        public String getFlag() {
                return flag;
        }

        public void setFlag(String flag) {
                this.flag = flag;
        }
}
