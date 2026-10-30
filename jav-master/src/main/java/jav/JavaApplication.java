package jav;

import jav.multithread.ThreadOne;

public class JavaApplication {
	public static void main(String []args) {
		ThreadOne t = new ThreadOne();
		t.deadlock();
	}
}
