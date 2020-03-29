//Author name: Thomas Greaney
//Author student ID number: 19258909
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class WordSearchPuzzleDriver {
    public static void main(String args[]) {
        String[] words0 = {"rowing","pasta","ketchup","tom","greaney","black","white","wordsearch","code"};
        List<String> words1 = Arrays.asList(words0);
        WordSearchPuzzle instance1 = new WordSearchPuzzle(words1);
        WordSearchPuzzle instance2 = new WordSearchPuzzle("BasicEnglish.txt", 30, 3, 10);
        WordSearchPuzzle instance3 = new WordSearchPuzzle("BNCwords.txt", 100, 0, 100);
        WordSearchPuzzle instance4 = new WordSearchPuzzle("Non-existent-file", 2, 1 ,6);//filename is invalid
        WordSearchPuzzle instance5 = new WordSearchPuzzle("BNCwords.txt", 1, 10, 10);
        WordSearchPuzzle instance6 = new WordSearchPuzzle("BasicEnglish.txt", 0, 3, 7);//wordCount is 0
                
        String[] words2 = {"We","never","knew","what","friends","we","had","until","we","came","to","Leningrad"};
        words1 = Arrays.asList(words2);
        WordSearchPuzzle instance7 = new WordSearchPuzzle(words1);

        System.out.println("\nInstance1:");
        instance1.showWordSearchPuzzle(false);
        System.out.println("\nInstance2:");
        instance2.showWordSearchPuzzle(true);
        System.out.println("\nInstance3:");
        instance3.showWordSearchPuzzle(true);
        System.out.println("\nInstance4:");
        instance5.showWordSearchPuzzle(false);
        System.out.println("\nInstance5:");
        instance7.showWordSearchPuzzle(false);

        System.out.println("\ninstance2 using getPuzzleAsString():\n");
        System.out.println(instance2.getPuzzleAsString());

        //Use the getWordSearchList method to create another wordsearch with the same words
        System.out.println("\nInstance1 is referenced to a new WordSearchPuzzle which is generated with the words\n from instance2 using getWordSearchList():\n");
        instance1 = new WordSearchPuzzle(instance2.getWordSearchList());
        instance1.showWordSearchPuzzle(true);

        System.out.println("\nPrint the new version of instance1 manually by using getPuzzleAsGrid() with for loops:\n");
        char[][] instance1Grid = instance1.getPuzzleAsGrid();
        for(int i = 0; i < instance1Grid.length; i++){
            for(int j = 0; j<instance1Grid[0].length; j++){
                System.out.print(instance1Grid[i][j] + " ");
            }
            System.out.println();
        }
        
        //The next two instructions should show no puzzle and not crash, as the "user" inputted invalid info when initialising these instances.
        System.out.println("\nThe next two instances were intitiated incorrectly by the user so they are empty:\n");
        instance6.showWordSearchPuzzle(false);
        instance4.showWordSearchPuzzle(true);
    }
}