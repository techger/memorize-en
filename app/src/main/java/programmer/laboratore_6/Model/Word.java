package programmer.laboratore_6.Model;

/**
 * Created by Byambaa on 10/26/2015.
 */
public class Word {
     private String word;
    private String description;

    public Word(String _word, String _description) {
        this.word = _word;
        this.description = _description;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
