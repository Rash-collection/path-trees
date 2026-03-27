/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package elPath;

/**
 * <p>Concrete class, because the dynamic superclass (in a sense) is treated as abstract-class.</p>
 * @author rash4
 */
public class LaNode extends AbsNode<LaNode>{
    public LaNode(String name, LaNode parent) {
        super(name, parent);
    }
}