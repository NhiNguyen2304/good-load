package com.rubik.goodloadkafka.dto;

public class Opportunity {
        private String id;
        private String name;
        private String title;
        private String price;
        private String date;

        public Opportunity(String id, String name, String title, String price, String date) {
            this.id = id;
            this.name = name;
            this.title = title;
            this.price = price;
            this.date = date;
        }

        public Opportunity() {
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String toString() {
            return this.name + ", " + this.title + "!";
        }
}
