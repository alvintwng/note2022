/**
 * To demonstrate the using of JUnit, in Maven
 *
 * Right-click on 'TestJUnitMaven', and select Tools > Create Tests
 */
package com.z.testjunitmaven;

public class TestJUnitMaven {

    public int add(int... number) {
        int total = 0;
        for (int i : number) {
            total += i;
        }
        return total;
    }

    public int multiply(int... number) {
        int product = 1;
        for (int i : number) {
            product *= i;
        }
        return product;
    }
}
/* TestJUnitMavenTest.java
package com.z.testjunitmaven;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestJUnitMavenTest {

    @org.junit.jupiter.api.Test
    public void testAdd() {
        TestJUnitMaven cal = new TestJUnitMaven();
        assertEquals(60, cal.add(10, 20, 30));
    }

    @Test
    public void testMultiply() {
        TestJUnitMaven cal = new TestJUnitMaven();
        assertEquals(6000, cal.multiply(10, 20, 30));
    }
}
*/
