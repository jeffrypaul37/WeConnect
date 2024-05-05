package com.webapp.weconnect.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    public Location(){

    }
    public Location(String name){
        this.name = name;
    }
    public Long getId(){ return Id;}

    public void setId(Long Id){
        this.Id = Id;
    }
    public String getName(){ return name; }

    public void setName(String name) {
        this.name = name;
    }

}
