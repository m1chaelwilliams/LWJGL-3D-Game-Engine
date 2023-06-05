package graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import gameObjects.Light;

public class ShaderProgram {
    Map <String, Integer> uniforms = new HashMap<>();

    private int ID;
    private int vertID;
    private int fragID;

    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertID = loadShaderSource(vertexFile, GL20.GL_VERTEX_SHADER);
        fragID = loadShaderSource(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        ID = GL20.glCreateProgram();
        GL20.glAttachShader(ID, vertID);
        GL20.glAttachShader(ID, fragID);
        GL20.glLinkProgram(ID);
        GL20.glValidateProgram(ID);

        String[] uniforms = {
            "model", "view", "proj"
        };
        genUniforms(uniforms);
    }

    public void bind() {
        GL20.glUseProgram(ID);
    }

    private int loadShaderSource(String shaderFile, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/shaderfiles/" + shaderFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error loading shader: " + shaderFile);
            e.printStackTrace();
            delete();
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}

        return shaderID;
    }
    public int getID() {
        return ID;
    }

    public void delete() {
        GL20.glDeleteProgram(ID);
        GL20.glDetachShader(ID, vertID);
        GL20.glDetachShader(ID, fragID);
        GL20.glDeleteShader(vertID);
        GL20.glDeleteShader(fragID);
    }

    // utils
    public void putDataIntoUniformMat4(Matrix4f matrix, String uniform) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
        matrix.get(buffer);
        GL20.glUniformMatrix4fv(getUniform(uniform), false, buffer);
    }
    public void putDataIntoUniformVec3(Vector3f vec, String uniform) {
        GL20.glUniform3f(getUniform(uniform), vec.x, vec.y, vec.z);
    }
    public void createUniform(String name) {
        int uniformLocation = GL30.glGetUniformLocation(ID, name);
        uniforms.put(name, uniformLocation);
    }
    public int getUniform(String name) {
        try {
            return uniforms.get(name);
        } 
        catch (Exception e) // if uniform doesn't exist, create one
        {
            System.out.println("Could not find uniform variable");
            createUniform(name);
            return uniforms.get(name);
        }
    }
    public void genUniforms(String[] names) {
        for(String name : names) {
            createUniform(name);
            uniforms.put(name, getUniform(name));
        }
    }

    public void configureLighting(Light light, Vector3f cameraPosition) {
        putDataIntoUniformVec3(cameraPosition, "camPos");
        putDataIntoUniformVec3(light.getPosition(), "light.position");
        putDataIntoUniformVec3(light.getAmbience(), "light.Ia");
        putDataIntoUniformVec3(light.getDiffuse(), "light.Id");
        putDataIntoUniformVec3(light.getSpec(), "light.Is");
    }
}
