package models;

import graphics.*;

// model that only contains vertices and indices
public class BaseModel {
    VAO vao;
    EBO ebo;
    public BaseModel(VAO vao, EBO ebo) {
        this.vao = vao;
        this.ebo = ebo;
    }
    public VAO getVAO() {return vao;}
    public EBO getEBO() {return ebo;}


}
