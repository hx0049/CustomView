package hx0049.customview.view.expandableview.model;

import java.util.List;

/**
 * Created by hx on 2016/12/26.
 */

public class ExpandableModel<T> {
    String name;
    List<T> childList;

    public ExpandableModel(String name, List<T> childList) {
        this.name = name;
        this.childList = childList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getChildList() {
        return childList;
    }

    public void setChildList(List<T> childList) {
        this.childList = childList;
    }
}
