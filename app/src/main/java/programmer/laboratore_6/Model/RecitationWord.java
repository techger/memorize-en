package programmer.laboratore_6.Model;

/**
 * Created by Byambaa on 11/15/2015.
 */
public class RecitationWord {
    String recitationEnglish;
    String recitationType;
    String recitationMongolia;

    public RecitationWord(String recitationEnglish, String recitationType, String recitationMongolia) {

        this.recitationEnglish = recitationEnglish;
        this.recitationType = recitationType;
        this.recitationMongolia = recitationMongolia;
    }

    public String getRecitationEnglish() {
        return recitationEnglish;
    }

    public void setRecitationEnglish(String recitationEnglish) {
        this.recitationEnglish = recitationEnglish;
    }

    public String getRecitationType() {
        return recitationType;
    }

    public void setRecitationType(String recitationType) {
        this.recitationType = recitationType;
    }

    public String getRecitationMongolia() {
        return recitationMongolia;
    }

    public void setRecitationMongolia(String recitationMongolia) {
        this.recitationMongolia = recitationMongolia;
    }


}
