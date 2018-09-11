package com.du.frameapplication.pojo;

public class TabEntry {
    private Integer id;
    private String name;

    public static TabEntry init(int id,String nmae){
        TabEntry tabEntry = new TabEntry();
        tabEntry.setId(id);
        tabEntry.setName(nmae);
        return tabEntry;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
