//Author name: Thomas Greaney
//Author student ID number: 19258909
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

public class WordSearchPuzzle {

    private char[][] puzzle;
    private List<String> puzzleWords;

    public WordSearchPuzzle(List<String> userSpecifiedWords){
        puzzleWords = userSpecifiedWords;
        puzzleWords.replaceAll(String::toUpperCase);
        initiatePuzzle();
    }

    public WordSearchPuzzle(String wordFile, int wordCount, int shortest, int longest){
        puzzleWords = new ArrayList<String>();
        List<String> words = new ArrayList<>();
        try {
            FileReader aFileReader = new FileReader(wordFile);
            BufferedReader aBufferReader = new BufferedReader(aFileReader);
            String aWord = aBufferReader.readLine();
            while (aWord != null) {
                if(aWord.length() <= longest && aWord.length() >= shortest) {
                    words.add(aWord.toUpperCase());
                }
                aWord = aBufferReader.readLine() ;
            }
            aBufferReader.close();
            aFileReader.close();
        }
        catch(IOException x) {
            puzzle = new char[0][0];
            return;
        }
        wordCount = Math.min(wordCount, words.size());//the .txt file mightn't meet the users requirements.
        for(int i = 0; i<wordCount; i++){
            int randomIndex = (int)(Math.random()*words.size());
            puzzleWords.add(words.get(randomIndex));
            words.remove(randomIndex);
        }
        initiatePuzzle();
    }

    private void initiatePuzzle(){
        if(puzzleWords.size() == 0){
            puzzle = new char[0][0];
            return;
        }
        puzzleWords.sort(Comparator.comparing(String::length));
        int size = (int)(Math.ceil(Math.sqrt(sumLengthPuzzleWords()*1.5))), maxWordLength = puzzleWords.get(puzzleWords.size()-1).length();
        size = Math.max(size, maxWordLength);
        puzzle = new char[size][size];
        generateWordSearchPuzzle();
    }

    public List<String> getWordSearchList(){
        List<String> wordSearchList = new ArrayList<>();
        //have to remove the words location.
        for (String currentWord : puzzleWords) {
            wordSearchList.add(currentWord.substring(0, currentWord.indexOf(',')));
        }
        return wordSearchList;
    }

    public char[][] getPuzzleAsGrid(){
        return puzzle;
    }

    public String getPuzzleAsString(){
        int puzzleLength = puzzle.length, next = 0;
        char[] oneDWordSearch = new char[(puzzleLength*puzzleLength*2)+puzzleLength];
        for(int i = 0; i < puzzleLength; i++){
            for(int j = 0; j<puzzleLength; j++){
                oneDWordSearch[next] = puzzle[i][j];
                next++;
                oneDWordSearch[next] = ' ';
                next++;
            }
            oneDWordSearch[next] = '\n';
            next++;
        }
        return String.copyValueOf(oneDWordSearch);
    }

    public void showWordSearchPuzzle(boolean hide){
        System.out.println("\nWordsearch Puzzle\n\n" + getPuzzleAsString());
        if(hide){
            List<String> wordSearchList = getWordSearchList();
            for (String s : wordSearchList) {
                System.out.println(s);
            }
        }else{
            for (String s : puzzleWords){
                System.out.println(s);
            }
        }
    }

    private void generateWordSearchPuzzle(){
        int dimension = puzzle.length, row, column, wordLength, wordDirection;
        //row and column hold where we will start to place the word.
        int[] maxXLength = new int[dimension], maxYLength = new int[dimension], temp;
        for(int i = 0; i < dimension; i++){
            maxXLength[i] = dimension;
            maxYLength[i] = dimension;
        }
        /*
        These arrays store the maximum word length a given row or column in
        the puzzle can have placed in it. For example, if row 0 only has room for
        a four letter word then maxXLength[0] would hold the value 4.

        If you place the largest words first you are less likely to end up in a situation where
        it is impossible to place a word of certain length into the puzzle. This saves computation.
        puzzleWords is already sorted by length.
         */
        for(int i = puzzleWords.size()-1; i >= 0; i--){
            //place each word.
            String wordToPlace = puzzleWords.get(i);
            wordLength = wordToPlace.length();
            wordDirection = (int)(Math.random()*4);
            //0 means place the word left to right, 1:right to left, 2:up to down, 3:down to up

            //NOTES: See is there a way to half the length of this if statement!
            //this if statement checks if the word can be placed in the wordsearch in its current orientation, and if it cant, it changes the orientation.
            //if it still cant be placed, it restarts the whole process.
            if(wordDirection < 2){
                temp = getValidRowOrColumn(wordLength, maxXLength);
                if(temp.length == 0){
                    wordDirection = wordDirection+2;
                    temp = getValidRowOrColumn(wordLength, maxYLength);
                    if(temp.length == 0){
                        retry();
                        return;
                    }
                }
            }else{
                temp = getValidRowOrColumn(wordLength, maxYLength);
                if(temp.length == 0){
                    wordDirection = wordDirection-2;
                    temp = getValidRowOrColumn(wordLength, maxXLength);
                    if(temp.length == 0){
                        retry();
                        return;
                    }
                }
            }
            //It may seem like the same comparison is executed twice here, but the previous if statement
            //could change the value of wordDirection.
            if(wordDirection < 2){
                //temp holds the rows which we can place the word in.
                row = temp[(int)(Math.random()*temp.length)];
                temp = getRowOrColumnPlaces(row, wordLength, dimension, true);
                //temp holds the possible places in the row where we can start to place the word.
                column = temp[(int)(Math.random()*temp.length)];
            }else{
                //temp holds the columns which we can place the word in.
                column = temp[(int)(Math.random()*temp.length)];
                temp = getRowOrColumnPlaces(column, wordLength, dimension, false);
                //temp holds the possible places in the column where we can start to place the word.
                row = temp[(int)(Math.random()*temp.length)];
            }
            for(int j = 0; j < wordLength; j++){
                switch(wordDirection){
                    case(0):
                        puzzle[row][column+j] = wordToPlace.charAt(j);
                        maxYLength[column+j] = calculateSpaceAvailable(false, column+j);
                        break;
                    case(1):
                        puzzle[row][column+j] = wordToPlace.charAt(wordLength-(j+1));
                        maxYLength[column+j] = calculateSpaceAvailable(false, column+j);
                        break;
                    case(2):
                        puzzle[row+j][column] = wordToPlace.charAt(j);
                        maxXLength[row+j] = calculateSpaceAvailable(true, row+j);
                        break;
                    case(3):
                        puzzle[row+j][column] = wordToPlace.charAt(wordLength-(j+1));
                        maxXLength[row+j] = calculateSpaceAvailable(true, row+j);
                        break;
                }
            }
            if(wordDirection < 2){
                maxXLength[row] = calculateSpaceAvailable(true, row);
                if(wordDirection == 0){
                    puzzleWords.set(i, wordToPlace + ",[" + row + "]" + "[" + column + "]" + "R");
                }else{
                    puzzleWords.set(i, wordToPlace + ",[" + row + "]" + "[" + (column+wordLength-1) + "]" + "L");
                }
            }else{
                maxYLength[column] = calculateSpaceAvailable(false, column);
                if(wordDirection == 2){
                    puzzleWords.set(i, wordToPlace + ",[" + row + "]" + "[" + column + "]" + "D");
                }else{
                    puzzleWords.set(i, wordToPlace + ",[" + (row+wordLength-1) + "]" + "[" + column + "]" + "U");
                }
            }
        }
        //Put list of words into alphabetical order for later
        puzzleWords.sort(Comparator.comparing( String::toString ));
        //Fill in blanks with random characters.
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(puzzle[i][j] == 0){
                    puzzle[i][j] = (char)('A' + (int)(Math.random() * 26));
                }
            }
        }
    }

    private int[] getRowOrColumnPlaces(int index, int wordLength, int rowOrColumnLength, boolean rowOrColumn){
        //method returns places a word can be placed in a row or column.
        int[] validPlaces = new int[rowOrColumnLength];
        int countLetters = 0, countPlace = 0;
        for(int i = 0; i < rowOrColumnLength; i++){
            if((rowOrColumn && puzzle[index][i] == 0) || (!rowOrColumn && puzzle[i][index] == 0)){
                if(countLetters < wordLength) {
                    countLetters++;
                }
                if(countLetters == wordLength){
                    validPlaces[countPlace] = (i-wordLength)+1;
                    countPlace++;
                }
            }else{
                countLetters = 0;
            }
        }
        return Arrays.copyOf(validPlaces, countPlace);
    }

    private int[] getValidRowOrColumn(int wordLength, int[] eachRowOrColumnCapacity){
        //This method returns which columns or rows can hold a given word.
        int size = eachRowOrColumnCapacity.length, counter = 0;
        int[] validRowsOrColumns = new int[size];

        for(int i = 0; i<size; i++){
            if(eachRowOrColumnCapacity[i] >= wordLength){
                validRowsOrColumns[counter] = i;
                counter++;
            }
        }
        return Arrays.copyOf(validRowsOrColumns, counter);
    }

    private int calculateSpaceAvailable(boolean row, int position) {
        /*
        This method will return the maximum word length a given row or column can hold.
        The parameter row specifies if we want to check a row or a column.
        The parameter position specifies which column or row we want to check.
         */
        int available = 0, possibleAvailable = 0;
        for (int i = 0; i < puzzle.length; i++) {
            if ((puzzle[position][i] == 0 && row) || (puzzle[i][position] == 0 && !row)) {
                possibleAvailable++;
            } else {
                while (i < puzzle.length && ((row && puzzle[position][i] != 0) || (!row && puzzle[i][position] != 0))) {
                    i++;
                }
                i--;
                available = Math.max(possibleAvailable, available);
                possibleAvailable = 0;
            }
        }
        //check because the last space could have been free.
        return Math.max(possibleAvailable, available);
    }

    private int sumLengthPuzzleWords(){
        int sum = 0;
        for (String puzzleWord : puzzleWords) {
            sum += puzzleWord.length();
        }
        return sum;
    }

    private void retry(){
        /*
        There is a very very small chance that generateWordSearchPuzzle will place words in the puzzle in a way
        which makes it impossible to place the next word. This method resets the puzzle and tries again.
         */
        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle.length; j++){
                puzzle[i][j] = 0;
            }
        }
        generateWordSearchPuzzle();
    }
}