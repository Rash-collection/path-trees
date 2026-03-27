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
 * <ol> Node's RULE
 * <li>parent == NULL && file(no.extension) ==> root-folder (tree)</li>
 * <li>parent != NULL && file(no.extension) ==> folder      (branch)</li>
 * <li>parent != NULL && file   .extension  ==> file        (leaf)</li>
 * <li>parent == NULL && file   .extension  ==> file-on-root(leaf)...not allowed</li>
 * </ol>
 * @author rash4
 */
public class AbsNode<T extends AbsNode<T>> {
    public final static ChainRoot ROOT = new ChainRoot();
    private Map<String, T> children = new HashMap<>();
    private String name;
    private T parent;
    private NodeType type;
    private String absolutePath;
    private int level = 0;
    public AbsNode(String name, T parent){
        if(name == null || name.trim().isBlank())
            throw new IllegalArgumentException(
                    "name (NULL, empty or blank) are invalid values.");
        this.name = name.trim(); // safely assigne the name value.
        this.constructNode(parent);
    }
    final void constructNode(T parent){
        // name must be instantiated already -_-
        final boolean file = validate(this.name).isFile();
        if(parent == null){
            if(file)
                throw new UnsupportedOperationException(
                        "This edge case is not allowed (a leaf with (Trees) root nodes.)");
            else{
                this.type = NodeType.Tree;
                ROOT.add(this.name, this.self());
                this.absolutePath = this.name + ":";
            }
        }else parent.putIf(this.self(), false);
    }
    // the main put validator...!!
    protected final void putIf(T child, boolean overwrite){
        if(this.adaptChild(child, overwrite))
            this.children.put(child.name(), child);
    }
    protected final boolean adaptChild(T child, boolean overwrite){
        if(this.name == null || this.name.isBlank() || child == null)return false;
        if(child.name().contains(":"+SPL))throw new IllegalArgumentException();
        if(!overwrite && this.children.containsKey(child.name()))return false;
        child.setStatsBasedOn(this.self());
        return true;
    }
    protected final void resetChildren(){
        if(this.children.isEmpty())return;
        for(var child : this.children.values()){
            child.setStatsBasedOn(this.self());
            child.resetChildren();
        }
    }
    protected final void setStatsBasedOn(T parent){
        if(this.name.contains(":"+SPL))throw new IllegalArgumentException();
        this.parent = parent;
        this.level = parent.plusLevel();
        this.absolutePath = parent.absolutePath() + SPL + this.name;
        if(this.isFile()){ 
            this.type = NodeType.Leaf;
            this.children.clear(); // make sure it's empty for a leaf.
        }else this.resetChildren(); // make sure even children follow-reset.
    }
    protected boolean isFile(){return validate(this.absolutePath).isFile();}
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
    // total destruction, only the parent who called this method remain on it's parent's list.
    // this method should be called upon removing the target from the list...
    // or maybe all is handled recursivly right!?
    public void dispose(){
        if(!this.isEmpty())for(var child : this.children.values()){
            child.dispose();
        }this.children.clear();
        if(ROOT.root.containsKey(this.name)){
            ROOT.root.remove(this.name);
        }else if(this.connectedToParent())
            this.parent = null;
    }
    public String name(){return this.name;}
    protected Map<String,T> children(){return this.children;}
    protected int plusLevel(){return this.level + 1;}
    public T self(){return (T)this;}
    protected final static class ChainRoot<T extends AbsNode<T>>{
        private ChainRoot(){}
        public final Map<String, T> root = new HashMap<>();
        protected ChainRoot add(String name, T value){
            root.putIfAbsent(name, value);
            return this;
        }
        protected ChainRoot remove(String name){
            root.remove(name);
            return this;
        }
        @SuppressWarnings("unused")
        private void clear(){
            root.clear();
        }
    }
    public enum NodeType{
        Tree,
        Branch,
        Leaf,
        ;
        public boolean isTree()  {return this == Tree;}
        public boolean isBranch(){return this == Branch;}
        public boolean isLeaf()  {return this == Leaf;}
    }
}