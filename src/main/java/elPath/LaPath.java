/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package elPath;

/**
 * <p>Path class for trees.</p>
 * <p>Similar to the built in {@link java.nio.file.Path}, but not for narrow use.</p>
 * @author rash4
 */
public class LaPath {
    protected final static String SPL = "/";
    private String[] values;
    private Type type;
    public LaPath(String path){
        this.resolve(path);
        if(this.type == null) throw new 
            IllegalArgumentException("String path is not valid.");
    }
    protected final boolean resolve(String path){
        if(path == null || path.isBlank())return false;
        final var tp = validate(path);
        if(tp == null)return false;
        this.type = tp;
        final var vals = path.trim().split(java.util.regex.Pattern.quote(SPL));
        final int leng = vals.length;
        this.values = new String[leng];
        for(int i = 0; i < leng; i++){
            if(i < leng - 1 && vals[i].contains("."))
                throw new IllegalArgumentException(
                        "folder can't have (.) or any special char in its name");
            this.values[i] = vals[i].trim();
        }
        return true;
    }
    protected final static Type validate(String path){
        if(path == null || path.isEmpty()) return null;
        final var hm = path.split(java.util.regex.Pattern.quote(SPL));
        final int lasty = hm.length - 1;
        for(int i = 0; i < lasty; i++){
            if(hm[i].contains("."))return null;
        }
        // this after the loop, because if any of the other objects are not valid
        // it's gonna return NULL aka not valid.
        if(hm[lasty].contains("."))return Type.File;
        else return Type.Folder;
    }
    public String[] values(){
        return this.values;
    }
    @Override public String toString(){
        final var sb = new StringBuilder();
        final int len = this.values.length;
        for(int i = 0; i < len; i++){
            if(i == len - 1){
                if(this.type == Type.File) sb.append(this.values[i])
                        .append("[").append(Type.File.name()).append("]");
                else{
                    sb.append(this.values[i]).append("[").append(this.type.name()).append("]");
                }
                continue;
            }
            sb.append(this.values[i]).append(SPL);
        }
        return sb.toString();
    }
    public enum Type{
        File,
        Folder
        ;
    }
}