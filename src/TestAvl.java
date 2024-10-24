import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TestAvl {
    // Test program
    public static void main(String[] args) {
        AvlTree<Integer> t = new AvlTree<>();
        final int SMALL = 40;
        final int NUMS = 1000000;  // must be even
        final int GAP = 37;

        System.out.println("Checking... (no more output means success)");

        // Inserts sequence of numbers "GAP" apart into AVL Tree
        for (int i = GAP; i != 0; i = (i + GAP) % NUMS) {
            t.insert(i);
            // Checks the balance of the tree after insertion
            if (NUMS < SMALL) {
                t.checkBalance();
            }
        }

        // Removes odd numbers
        for (int i = 1; i < NUMS; i += 2) {
            t.remove(i);
            if (NUMS < SMALL) {
                t.checkBalance();
            }
        }

        // Write AVL Tree to "output_testavl.txt"
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output_testavl.txt"))) {
            t.printTree(writer);
            writer.flush();  // Ensure all data is written out
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + e.getMessage());
        }

        // Checks to see that the minimum value is 2 and the maximum value is NUMS - 2
        try {
            if (t.findMin() != 2 || t.findMax() != NUMS - 2) {
                System.out.println("FindMin or FindMax error!");
            }
        } catch (UnderflowException e) {
            System.err.println("Tree is empty, unable to find minimum or maximum.");
        }

        // Checks to see if the AVL Tree contains even numbers (it should)
        for (int i = 2; i < NUMS; i += 2) {
            if (!t.contains(i)) {
                System.out.println("Find error1!");
            }
        }

        // Checks to see if the AVL Tree contains odd numbers (it shouldn't)
        for (int i = 1; i < NUMS; i += 2) {
            if (t.contains(i)) {
                System.out.println("Find error2!");
            }
        }
    }
}
