import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj2 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java Proj2 <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLinesRequested = Integer.parseInt(args[1]);

        System.out.println("Starting Proj2 main method...");
        System.out.printf("Input file: %s, Lines requested: %d%n", inputFileName, numLinesRequested);

        ArrayList<Integer> values = new ArrayList<>();

        // Open the input file
        try (FileInputStream inputFileNameStream = new FileInputStream(inputFileName);
             Scanner inputFileNameScanner = new Scanner(inputFileNameStream);
             BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {

            // Ignore first line (header)
            if (inputFileNameScanner.hasNextLine()) {
                String header = inputFileNameScanner.nextLine();
                System.out.println("Ignoring header line: " + header);
            }

            int count = 0;
            while (inputFileNameScanner.hasNextLine() && count < numLinesRequested) {
                String line = inputFileNameScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] valuesSplit = line.split(";");
                    if (valuesSplit.length > 0) {
                        try {
                            int value = Integer.parseInt(valuesSplit[0].trim()); // Assuming the value to insert is in the first column
                            values.add(value);
                            count++;
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping invalid number: " + valuesSplit[0]);
                        }
                    }
                }
            }

            // Create AVL and BST trees
            AvlTree<Integer> avlTree = new AvlTree<>();
            BinarySearchTree<Integer> bst = new BinarySearchTree<>();

            // Insert and time for AVL Tree
            long startInsertAvl = System.nanoTime();
            for (int value : values) {
                avlTree.insert(value);
            }
            long endInsertAvl = System.nanoTime();
            long avlInsertTime = endInsertAvl - startInsertAvl;

            // Insert and time for BST
            long startInsertBst = System.nanoTime();
            for (int value : values) {
                bst.insert(value);
            }
            long endInsertBst = System.nanoTime();
            long bstInsertTime = endInsertBst - startInsertBst;
            // Search and time for AVL Tree
            long startSearchAvl = System.nanoTime();
            for (int value : values) {
                avlTree.contains(value);
            }
            long endSearchAvl = System.nanoTime();
            long avlSearchTime = endSearchAvl - startSearchAvl;

            // Search and time for BST
            long startSearchBst = System.nanoTime();
            for (int value : values) {
                bst.contains(value);
            }
            long endSearchBst = System.nanoTime();
            long bstSearchTime = endSearchBst - startSearchBst;

            // Write timing results to output file in CSV format
            writer.write(String.format("Number of lines,Insertion time (BST, ns),Insertion time (AVL, ns),Search time (BST, ns),Search time (AVL, ns)%n"));
            writer.write(String.format("%d,%d,%d,%d,%d%n", numLinesRequested, bstInsertTime, avlInsertTime, bstSearchTime, avlSearchTime));

            // Print human-readable timing results to console
            System.out.printf("Number of lines: %d%n", numLinesRequested);
            System.out.printf("BST Insertion time: %d ns%n", bstInsertTime);
            System.out.printf("AVL Insertion time: %d ns%n", avlInsertTime);
            System.out.printf("BST Search time: %d ns%n", bstSearchTime);
            System.out.printf("AVL Search time: %d ns%n", avlSearchTime);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFileName);
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + e.getMessage());
        }
    }

    static class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {
        private static class BinaryNode<AnyType> {
            AnyType element;
            BinaryNode<AnyType> left;
            BinaryNode<AnyType> right;

            BinaryNode(AnyType theElement) {
                this(theElement, null, null);
            }

            BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt) {
                element = theElement;
                left = lt;
                right = rt;
            }
        }

        private BinaryNode<AnyType> root;

        public BinarySearchTree() {
            root = null;
        }

        public void insert(AnyType x) {
            root = insert(x, root);
        }

        private BinaryNode<AnyType> insert(AnyType x, BinaryNode<AnyType> t) {
            if (t == null) {
                return new BinaryNode<>(x, null, null);
            }

            int compareResult = x.compareTo(t.element);
            if (compareResult < 0) {
                t.left = insert(x, t.left);
            } else if (compareResult > 0) {
                t.right = insert(x, t.right);
            }
            // Duplicate; do nothing
            return t;
        }

        public boolean contains(AnyType x) {
            return contains(x, root);
        }

        private boolean contains(AnyType x, BinaryNode<AnyType> t) {
            if (t == null) {
                return false;
            }

            int compareResult = x.compareTo(t.element);

            if (compareResult < 0) {
                return contains(x, t.left);
            } else if (compareResult > 0) {
                return contains(x, t.right);
            } else {
                return true; // Match
            }
        }
    }
}
