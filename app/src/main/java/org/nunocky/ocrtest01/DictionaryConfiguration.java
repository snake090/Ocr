package org.nunocky.ocrtest01;

/**
 * Created by Owner on 2015/10/29.
 */
public class DictionaryConfiguration {
    private String Url;
    private String Dic;
    private String Word;
    private String Scope;
    private String Match;
    private String Merge;
    private String PageSize;
    private String PageIndex;

    private boolean kind;//true 英語から日本語 false 日本語から英語


    public boolean isKind() {
        return kind;
    }

    public DictionaryConfiguration(boolean kind){
        Url="http://public.dejizo.jp/NetDicV09.asmx/SearchDicItemLite?";
        Dic="EdictJE";
        Scope="HEADWORD";
        Match="FORAWARD";
        Merge="AND";
        PageSize="10";
        PageIndex="0";
        this.kind=kind;
    }

    public String getUrl(){

        return Url+"Dic="+Dic+"&"+"Word="+Word+"&"+"Scope="+Scope+"&"+"Match="+Match+"&"+"Merge="+Merge+"&"+"PageSize="+PageSize+"&"+"PageIndex="+PageIndex;
    }

    public void setWord(String word) {
        Word = word;
    }
    public String getWord() {

        return  Word;
    }


}
