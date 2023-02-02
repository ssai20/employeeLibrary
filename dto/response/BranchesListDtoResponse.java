package net.thumbtack.school.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor

public class BranchesListDtoResponse {
    private List<String> branchesListDtoResponse = new ArrayList<>();
}
