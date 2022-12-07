package demo;

import java.util.HashMap;
import java.util.Scanner;

public class hashmap {

    private String hashname;

    public hashmap(String hashname) {
        this.hashname = hashname;
    }

    public String toString() {
        return "hashname : " + hashname;
    }

    public static void main(String args[]) {

        HashMap<String, hashmap> demos = new HashMap<String, hashmap>(10);

        demos.put("Joe", new hashmap("Joe"));
        demos.put("Andy", new hashmap("Andy"));
        demos.put("Grey", new hashmap("Grey"));
        demos.put("Kiki", new hashmap("Kiki"));
        demos.put("Anto", new hashmap("Anto"));

        Scanner keyboard = new Scanner(System.in);
        String name = "";
        do {
            System.out.print("\nEnter a name to look up in the map. ");
            System.out.println("Press enter to quit.");

            name = keyboard.nextLine();
            if (demos.containsKey(name)) {
                hashmap e = demos.get(name);
                System.out.println("Name found: " + e.toString());

            } else if (!name.equals("")) {
                System.out.println("Name not found");
            }
        } while (!name.equals(""));
    }
}
