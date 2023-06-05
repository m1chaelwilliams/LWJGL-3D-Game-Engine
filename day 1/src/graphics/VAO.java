package graphics;

import org.lwjgl.opengl.GL30;

public class VAO {
    private int ID;

    public VAO() {
        ID = GL30.glGenVertexArrays();
        bind();
    }
    public void bind() {GL30.glBindVertexArray(ID);}
    public void unbind() {GL30.glBindVertexArray(0);}
    public int getID() {return ID;}
    public void dispose() {GL30.glDeleteVertexArrays(ID);}

    public void linkToVAO(int location, int size) {
        GL30.glVertexAttribPointer(location, size, GL30.GL_FLOAT, false, 0, 0);
        GL30.glEnableVertexAttribArray(location);
    }
}
