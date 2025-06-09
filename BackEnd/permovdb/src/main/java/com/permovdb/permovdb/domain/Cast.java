package com.permovdb.permovdb.domain;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// @JsonIgnoreProperties(ignoreUnknown = true)
public class Cast {

    private Integer id;

    private ArrayList<CastMember> cast;

    private ArrayList<CastMember> crew;

}