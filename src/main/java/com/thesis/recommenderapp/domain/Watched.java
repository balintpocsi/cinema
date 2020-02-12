package com.thesis.recommenderapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Watched {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Item item;
    private Integer rating;

}
