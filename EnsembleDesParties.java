import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class EnsembleDesParties {

    static {
        // Définir la précision maximale pour les entiers
        System.setProperty("java.math.BigDecimal.defaultScale", "20000");
    }

    public static BigInteger combinaisons(int n, int k) {
        BigInteger resultat = BigInteger.ONE;
        for (int i = 1; i <= k; i++) {
            resultat = resultat.multiply(BigInteger.valueOf(n - i + 1)).divide(BigInteger.valueOf(i));
        }
        return resultat;
    }

    public static BigInteger rangSousEnsemble(int n, List<Integer> sousEnsemble) {
        int tailleSousEnsemble = sousEnsemble.size();
        if (tailleSousEnsemble == 0) {
            return BigInteger.ONE;
        }
        if (!sousEnsemble.stream().allMatch(x -> 1 <= x && x <= n)) {
            return BigInteger.valueOf(-1); // Sous-ensemble non valide
        }

        BigInteger rang = BigInteger.ONE; // Commence à 1
        for (int k = 1; k < tailleSousEnsemble; k++) {
            rang = rang.add(combinaisons(n, k)); // Ajoute les combinaisons de taille inférieure
        }

        Collections.sort(sousEnsemble);
        for (int i = 0; i < tailleSousEnsemble; i++) {
            int x = sousEnsemble.get(i);
            for (int y = 1; y < x; y++) {
                if (!sousEnsemble.subList(0, i).contains(y)) { // Évite de compter deux fois les paires
                    rang = rang.add(combinaisons(n - y, tailleSousEnsemble - i - 1));
                }
            }
        }
        return rang.add(BigInteger.ONE); // Ajouter 1 pour décaler les rangs de 1
    }

    public static List<Integer> sousEnsembleRang(int n, BigInteger rang) {
        if (rang.compareTo(BigInteger.ONE) < 0) {
            return null;
        }
        int tailleSousEnsemble = 0;

        while (rang.compareTo(combinaisons(n, tailleSousEnsemble)) > 0) {
            rang = rang.subtract(combinaisons(n, tailleSousEnsemble));
            tailleSousEnsemble++;
        }

        if (tailleSousEnsemble == 0) {
            return new ArrayList<>();
        }

        List<Integer> sousEnsemble = new ArrayList<>();
        List<Integer> elementsRestants = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            elementsRestants.add(i);
        }

        for (int i = 0; i < tailleSousEnsemble; i++) {
            for (int j = 0; j < elementsRestants.size(); j++) {
                int x = elementsRestants.get(j);
                BigInteger combinaisonsRestantes = combinaisons(n - x, tailleSousEnsemble - i - 1);
                if (rang.compareTo(combinaisonsRestantes) <= 0) {
                    sousEnsemble.add(x);
                    elementsRestants.remove(j);
                    break;
                } else {
                    rang = rang.subtract(combinaisonsRestantes);
                }
            }
        }
        return sousEnsemble;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n\n\t\t+-----------------------------------------------------------+");
            System.out.println("\t\t|BIENVENUE DANS LE PROGRAMME SUR  LES ENSEMBLES DES PARTIES |");
            System.out.println("\t\t+-----------------------------------------------------------+\n");
            System.out.println("\t\t1. Déterminer l'ensemble d'éléments d'un rang");
            System.out.println("\t\t2. Déterminer le rang d'un ensemble d'éléments");
            System.out.println("\t\t3. Quitter\n");

            System.out.print("\t\tchoix: ");
            String choix = scanner.nextLine();

            try {
                if (choix.equals("1")) {
                    System.out.print("Entrez le rang: ");
                    BigInteger rang = scanner.nextBigInteger();
                    if (rang.compareTo(BigInteger.ONE) < 0) {
                        throw new IllegalArgumentException("Le rang doit être un entier positif.");
                    }
                    System.out.println("Ensemble d'éléments correspondant à ce rang: " + sousEnsembleRang(65536, rang));
                } else if (choix.equals("2")) {
                    System.out.print("Entrez les éléments de l'ensemble séparés par des espaces: ");
                    String[] elementsStr = scanner.nextLine().split(" ");
                    List<Integer> ens = new ArrayList<>();
                    for (String elementStr : elementsStr) {
                        int element = Integer.parseInt(elementStr);
                        if (element < 1 || element > 65536) {
                            throw new IllegalArgumentException("Les éléments doivent être compris entre 1 et 65536.");
                        }
                        ens.add(element);
                    }
                    System.out.println("Rang correspondant à l'ensemble : " + rangSousEnsemble(65536, ens));
                } else if (choix.equals("3")) {
                    break;
                } else {
                    System.out.println("Choix invalide. Veuillez saisir 1, 2 ou 3.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
        scanner.close();
    }
}
