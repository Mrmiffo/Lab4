package Memory;

public class Slumptest {
  public static void main(String[] arg) {
    Integer[] a = new Integer[1500000];
    for (int i = 0; i < a.length; i++)
      a[i] = new Integer(i+1);
    Verktyg.slumpOrdning(a);
    System.out.println("Nu skall talen 1 till " + a.length + " skrivas ut i slumpm�ssig ordning");
    for (int i = 0; i < a.length; i++)
      System.out.print(a[i] + " ");
    System.out.println();
    System.out.println("Nu skall talen 1 till " + a.length + " skrivas ut i en annan slumpm�ssig ordning");
    Verktyg.slumpOrdning(a);
    for (int i = 0; i < a.length; i++)
      System.out.print(a[i] + " ");
    System.out.println();
  }
}