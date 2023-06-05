package graphics;

import org.lwjgl.opengl.GL15;

public class VBO {

    private int ID;

    public VBO(float[] data) {
        ID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
    }
    public void bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
    }
    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    public void dispose() {
        GL15.glDeleteBuffers(ID);
    }
    public int getID() {return ID;}
}
