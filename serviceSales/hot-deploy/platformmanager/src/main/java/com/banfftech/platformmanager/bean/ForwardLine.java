package main.java.com.banfftech.platformmanager.bean;

import java.util.List;

/**
 * Created by S on 2018/7/9.
 */
public class ForwardLine {


    private String id;
    private String name;
    private List<ForwardLine> children;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<ForwardLine> getChildren() {
        return children;
    }
    public void setChildren(List<ForwardLine> children) {
        this.children = children;
    }
    public ForwardLine(String id, String name, List<ForwardLine> children) {
        super();
        this.id = id;
        this.name = name;
        this.children = children;
    }
    public ForwardLine() {
        super();
        // TODO Auto-generated constructor stub
    }


}
