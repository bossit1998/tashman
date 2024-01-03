package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.tm.tashman.enums.Language;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicModel {
    private Long id;
    private Language language;
    private Integer page;
    private Integer pageSize;
}