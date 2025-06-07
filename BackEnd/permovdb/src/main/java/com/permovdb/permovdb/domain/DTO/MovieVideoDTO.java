package com.permovdb.permovdb.domain.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.permovdb.permovdb.domain.POJO.VideoMetadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieVideoDTO {
    private int id;
    private List<VideoMetadata> results;
}
