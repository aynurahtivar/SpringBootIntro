package com.tpe.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonProperty("bookName")   //JSON formatinda name fieldini bu key ile goster
    private String name;

    @JsonIgnore // book objemi JSON a maplerken student i alma
    @ManyToOne//1s---> Many Book
    private Student student; //one
}
