package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssortmentResponseModel {
    private Long id;
    private String name;
}