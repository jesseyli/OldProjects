package enigma;

import java.util.*;

public class test {

	public static void main(String[] args) {
	    int[] A = {0, 1, 2};
	    System.out.println(nextPerm(A));
	    for (int i: A) {
	        System.out.println(i);
	    }
	    /** Machine m = new Machine();
	    Rotor rotor5 = new Rotor("I", m, Rotor.toIndex("A".charAt(0)));
	    Rotor rotor4 = new Rotor("IV", m, Rotor.toIndex("A".charAt(0)));
	    Rotor rotor3 = new Rotor("III", m, Rotor.toIndex("A".charAt(0)));
	    FixedRotor rotor2 = new FixedRotor("BETA", m, Rotor.toIndex("A".charAt(0)));
	    Reflector rotor1 = new Reflector("B", m);
	    Rotor[] rotors = {rotor1, rotor2, rotor3, rotor4, rotor5};
	    m.replaceRotors(rotors);
	    m.setRotors("AXLE");
	    String str = "* C GAMMA II VIII III ABBB "; */
	    
	    //System.out.println(m.convert("HYIHLBKOMLIUYDCMPPSFSZWSQCNJEX"));
		/** System.out.println(rotor5.convertForward(5));
	    System.out.println(rotor4.convertForward(8));
	    System.out.println(rotor3.convertForward(21));
	    System.out.println(rotor2.convertForward(9));
        System.out.println(rotor1.convertForward(22));
        System.out.println(rotor2.convertBackward(7));
        System.out.println(rotor3.convertBackward(23));
        System.out.println(rotor4.convertBackward(25));
        System.out.println(rotor5.convertBackward(9)); */
	}
	static boolean nextPerm(int[] A) {
        int N = A.length;
        int k = N - 1;
        List<Integer> S = new ArrayList<Integer>();
        List<Integer> larger = new ArrayList<Integer>();
        while (k >= 0) {
            for (int value: S) {
                if (value > A[k]) {
                    larger.add(value);
                }
                if (larger.size() > 0) {
                    int v = Collections.min(larger);
                    S.remove(S.indexOf(v));
                    S.add(A[k]);
                    A[k] = v;
                    Collections.sort(S);
                    for (int i = k + 1; i < N - 1; i += 1) {
                        A[i] = S.get(i - k + 1);            
                    }
                    return true;
                } else {
                    S.add(A[k]);
                    k -= 1;
                }
            }
        }
        return false;
    }
}

