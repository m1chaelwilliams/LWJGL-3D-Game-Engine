package graphics;

import org.lwjgl.opengl.GL15;

public class EBO {
    private int ID;
    private int length;

    public EBO(int[] data) {
        ID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        length = data.length;
    }
    public void bind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ID);
    }
    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    public void dispose() {
        GL15.glDeleteBuffers(ID);
    }
    public int getID() {
        return ID;
    }
    public int getLength() {
        return length;
    }
}
