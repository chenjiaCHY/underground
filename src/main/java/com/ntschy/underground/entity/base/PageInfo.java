package com.ntschy.underground.entity.base;

import com.ntschy.underground.entity.Action;
import lombok.Data;

import java.util.List;

@Data
public class PageInfo {

    private Integer pageID;

    private String pageName;

    private List<Action> actionList;
}
