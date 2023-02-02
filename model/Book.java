package net.thumbtack.school.library.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Book {
    private String name;
    private List<String> authorList = new ArrayList<>();
    private List<String> branchList = new ArrayList<>();
    // хватит и int. 0- не сохранен в БД, > 0 - сохранен
    // так в реальных БД
    private int id;
    private Employee reader;
    private Employee owner;
    private int reservedForThisDays;
}
