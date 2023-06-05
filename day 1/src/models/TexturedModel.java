package models;

import graphics.*;

public class TexturedModel extends BaseModel {
    private Texture texture;
    public TexturedModel(VAO vao, EBO ebo, Texture texture) {
        super(vao, ebo);
        this.texture = texture;
    }
    public Texture getTexture() {return texture;}
}
