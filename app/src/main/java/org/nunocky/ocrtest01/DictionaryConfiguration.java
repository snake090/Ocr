package org.nunocky.ocrtest01;

/**
 * Created by Owner on 2015/10/29.
 */
public class DictionaryConfiguration {
    private String Url;
    private String Dic1;
    private String Dic2;
    private String Word;
    private String Scope;
    private String Match;
    private String Merge;
    private String Prof;
    private String PageSize;
    private String PageIndex;
    private String Url1;
    private String Item;


    private boolean kind;//true 英語から日本語 false 日本語から英語


    public boolean isKind() {
        return kind;
    }


    public DictionaryConfiguration(boolean kind) {
        Url = "http://public.dejizo.jp/NetDicV09.asmx/SearchDicItemLite?";
        Dic1 = "EJdict";
        Dic2 = "EdictJE";
        Scope = "HEADWORD";
        Match = "EXACT";
        Merge = "AND";
        Prof = "XHTML";
        PageSize = "10";
        PageIndex = "0";
        this.kind = kind;

        Url1 = "http://public.dejizo.jp/NetDicV09.asmx/GetDicItemLite?";

    }

    public String getUrl() {

        if (kind) {
            return Url + "Dic=" + Dic1 + "&" + "Word=" + Word + "&" + "Scope=" + Scope + "&" + "Match=" + Match + "&" + "Merge=" + Merge + "&" + "Prof=" + Prof + "&" + "PageSize=" + PageSize + "&" + "PageIndex=" + PageIndex;
        } else {
            return Url + "Dic=" + Dic2 + "&" + "Word=" + Word + "&" + "Scope=" + Scope + "&" + "Match=" + Match + "&" + "Merge=" + Merge + "&" + "Prof=" + Prof + "&" + "PageSize=" + PageSize + "&" + "PageIndex=" + PageIndex;
        }
    }


    public void setWord(String word) {
        Word = word;
    }

    public String getWord() {

        return Word;
    }

    public String getUrl1() {
        if (kind) {
            return Url1 + "Dic=" + Dic1 + "&" + "Item=" + Item + "&" + "Loc=" + "&" + "Prof=" + Prof;
        } else {
            return Url1 + "Dic=" + Dic2 + "&" + "Item=" + Item + "&" + "Loc=" + "&" + "Prof=" + Prof;
        }
    }

    public void setItem(String item) {
        Item = item;
    }
}
