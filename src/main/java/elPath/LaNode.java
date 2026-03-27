/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package elPath;

import static elPath.LaPath.SPL;
import static elPath.LaPath.validate;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Simple nodes for general trees purposes.</p>
 * @author rash4
 */
public class LaNode<E, T extends LaNode<E, T>> {
    public final static ChainRoot root = new ChainRoot();
    private Map<String, T> children = new HashMap<>();
    private String name;
    private T parent;
    private NodeType type;
    private String absolutePath;
    private int level = 0;
    public LaNode(String name){
        this.name = name;
        // make things automatic..
    }
    // the main put validator...!!
    protected void putIf(T child, boolean overwrite){
        if(this.adaptChild(child.name(), child, overwrite))
            this.children.put(child.name(), child);
    }
    protected final boolean adaptChild(String name, T child, boolean overwrite){
        if(name == null || name.isBlank() || child == null)return false;
        if(child.name().contains(":"+SPL))throw new IllegalArgumentException();
        if(!overwrite && this.children.containsKey(name))return false;
        child.setStatsBasedOn(this.self());
        return true;
    }
    protected final void resetChildren(){
        for(var child : this.children.values()){
            child.setStatsBasedOn(this.self());
            child.resetChildren();
        }
    }
    protected final void setStatsBasedOn(T parent){
        if(this.name.contains(":"+SPL))throw new IllegalArgumentException();
        this.parent = parent;
        this.level = parent.plusLevel();
        this.absolutePath = this.cachAbsPath();
        if(this.isFile()){ 
            this.type = NodeType.Leaf;
            this.children.clear(); // make sure it's empty for a leaf.
        }
    }
    protected boolean isFile(){return validate(this.absolutePath) == LaPath.Type.File;}
    public boolean isTree(){return this.type == NodeType.Tree;}
    public boolean isBranch(){return this.type == NodeType.Branch;}
    public boolean isLeaf(){return this.type == NodeType.Leaf;}
    public boolean isEmpty(){return this.children.isEmpty();}
    public boolean isRoot(){return this.level == 0;}
    public boolean isOrphaned(){return !this.connectedToParent();}
    /**This makes sure that the parent is it's parent XD*/
    public final boolean connectedToParent(){
        return this.parent != null && this.parent.children().containsValue(this.self());
    }
    public String absolutePath(){
        return this.absolutePath;
    }
    protected String cachAbsPath(){
        if(this.connectedToParent()){
            return this.parent.absolutePath() + SPL + this.name;
        }return this.name;
    }
    // total destruction, only the parent who called this method remain on it's parent's list.
    // this method should be called upon removing the target from the list...
    public void dispose(){
        if(!this.isEmpty())for(var child : this.children.values()){
            child.dispose();
        }this.children.clear();
        if(root.ROOT.containsKey(this.name)){
            root.ROOT.remove(this.name);
        }else if(this.connectedToParent())
            this.parent = null;
    }
    public String name(){return this.name;}
    protected Map<String,T> children(){return this.children;}
    protected int plusLevel(){return this.level + 1;}
    public T self(){return (T)this;}
    protected final static class ChainRoot{
        private ChainRoot(){}
        public final Map<String, LaNode<String,?>> ROOT = new HashMap<>();
        protected ChainRoot add(String name, LaNode<String,?> value){
            ROOT.putIfAbsent(name, value);
            return this;
        }
        protected ChainRoot remove(String name){
            ROOT.remove(name);
            return this;
        }
        private void clear(){
            ROOT.clear();
        }
    }
    public enum NodeType{
        Tree,
        Branch,
        Leaf,
        ;
    }
}