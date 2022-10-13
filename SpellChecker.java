import java.util.*;
import java.io.*;
public class SpellChecker implements SpellCheckerInterface
{

    Set<String> fileWords = new HashSet<>();

    //constructor, creates a dictionary based off of the given file
    public SpellChecker( String fileName )
    {
        fileWords = createDictionary( fileName ); 
    }


    private Set<String> createDictionary( String file )
    {

        File fileName = new File( file );

        try 
        {
        
        Scanner scan = new Scanner( fileName );
        
        while( scan.hasNext() ){
            String word = scan.next();
            word = fixed( word );
            fileWords.add( word );
        }

        }catch( FileNotFoundException e ){
            System.out.println( "file does not exist." );
        }
        
        return fileWords;

    }

    //creates an array list for mispelled words, scans the inputted file
    //and adds words not contained in the dictionary to the list
    public List<String> getIncorrectWords( String fileName )
    {

        
            List<String> mispelled = new ArrayList<>();

        try
        {

            File file = new File( fileName );
            Scanner input = new Scanner( file );


            while( input.hasNextLine() )
            {

                String line = input.nextLine();
                String[] wordList = line.split( " " );

                for ( int i = 0; i< wordList.length; i ++ )
                {

                    String word =  fixed( wordList[ i ] );
                    
                    if( !fileWords.contains( word ) && !mispelled.contains( word ) && word.length() > 0 )
                        mispelled.add( word ); 
                    
                }

            }
        }catch( FileNotFoundException e){

            System.out.println( "file does not exist." );

        }

        return mispelled;
    }

    //creates a set of strings that are possible corrections to the 
    //words in the mispelled list
    public Set<String> getSuggestions( String word )
    {   
        Set<String> suggestions = new HashSet<>();
        corrections( word, suggestions );
        return suggestions;

    }

    private void corrections( String word, Set<String> suggestions )
    {   
        
        StringBuilder incorrectWord = new StringBuilder( fixed( word ) );

        //add a letter at every instance of the string
        for ( int i = 0; i <= incorrectWord.length(); i ++ )
        {
            for ( char letters = 'a'; letters <= 'z'; letters++ )
            {       
                StringBuilder correctedWord = new StringBuilder( incorrectWord.toString() );
                    
                correctedWord.insert( i , letters );
                String correctedWordString = correctedWord.toString();
                if ( fileWords.contains(  correctedWordString  )  )
                    suggestions.add( correctedWordString );        
            }

        }

        //remove a letter at every instance of the string
        for( int i = 0; i <   incorrectWord.length(); i ++ )
        {
            String removed = word.substring( 0, i ) + word.substring( i + 1 );
            removed =  fixed( removed );

            if ( fileWords.contains( removed ) )
                suggestions.add( removed );
                
        }

        //swap adjacent charecters within string
        for ( int i = 0; i<= incorrectWord.length()-2; i ++ )
        {
            char swappedArray[] = incorrectWord.toString().toCharArray();
            char temp = swappedArray[ i ]; 

            swappedArray[i] = swappedArray[ i+1 ];
            swappedArray[i+1] = temp;

            String swapped = String.valueOf( swappedArray );

            if ( fileWords.contains( swapped ) )
                suggestions.add( swapped  );
        }

    }


    //removes all puncuation and digits and converts to to lower case
    private String fixed( String unfixed )
    {
        unfixed = unfixed.toLowerCase();
        unfixed = unfixed.replaceAll( "\\p{Punct}" , "" );
        unfixed = unfixed.replaceAll( "[^A-Za-z0-9]" , "" );
        return unfixed;
    }




}
