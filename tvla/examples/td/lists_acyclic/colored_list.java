public class Node {
  public Node n;
  public boolean color;

  /** Precondition: the input is an acyclic singly-linked list where the colors alternate.
   * That is, if the color of a node is v then the color of the next node is !v.
   * Postcondition: the output is an acyclic singly-linked list where the colors alternate.
   * Additionally, if the last cell in the input list has the color 'false' then the color
   * of the output list are negated with respect to the input list, and otherwise they
   * are the same as in the input list.
   */
  public static void flip(Node head) {
    Node t = head;
    boolean lastColor = false;
    // Get the color of the last node.
    while (t != null) {
      lastColor = t.color;
      System.out.print(lastColor + " ");
      t = t.n;
    }
    System.out.println();

    if (!lastColor) {
      // Flip the colors.
      t = head;
      while (t != null) {
        t.color = !t.color;
        System.out.print(t.color + " ");
        t = t.n;
      }
    }
  }
}