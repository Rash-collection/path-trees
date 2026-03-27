/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package elPath;

/**
 * <p>Main class for developing tests, or for user's testings.</p>
 * @author rash4
 */
public class RunTest {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        final var p = new LaPath("fesf/ f/sd f/ds/f /sd sdf/ / d.sex");
        if(p != null) System.out.println(p.toString());
        final LaNode rut = new LaNode("root1", null);
        final LaNode sam = new LaNode("Sam", rut);
        rut.putIf(sam, false);
        System.out.println(rut.absolutePath());
        System.out.println(sam.absolutePath());
    }
}