package ru.noname.mysnake.api.models;

import java.util.LinkedList;
import java.util.List;

public class SectionsStorage {
    private List<Section> sections = new LinkedList<>();

    public void addSection(Section name) {
        sections.add(name);
    }

    public List<String> getSections() {
        List<String> sectionsStr = new LinkedList<>();
        for(Section s: sections){
            sectionsStr.add(s.getName());
        }
        return sectionsStr;
    }

    public Section getSection(Integer index) {
        return sections.get(index);
    }

}
