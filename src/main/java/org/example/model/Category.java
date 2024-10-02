package org.example.model;

import lombok.Data;
import org.example.utils.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
@Data
public class Category {
    private final int id;
    private final String slug;
    private final String name;
}
