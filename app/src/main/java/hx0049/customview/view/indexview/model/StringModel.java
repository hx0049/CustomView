package hx0049.customview.view.indexview.model;

import hx0049.customview.view.indexview.utils.PinYinUtil;

/**
 * Created by hx on 2016/12/19.
 */

public class StringModel {
    String content;
    String pingYin;
    char firstLetter;
    boolean isFirstPosition;
    private char[] allLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'
            , 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};


    public StringModel(String content) {
        this.content = content;
        this.pingYin = PinYinUtil.getInstance().getSelling(content);
        this.firstLetter = pingYin.charAt(0);
        boolean isLetter = false;
        for (int i = 0; i < 26; i++) {
            if (allLetter[i] == firstLetter) {
                isLetter = true;
                break;
            }else if(allLetter[i] - 32 == firstLetter){
                this.firstLetter += 32;
                isLetter = true;
            }
        }
        if (!isLetter) {
            firstLetter = '#';
        }
    }

    public char getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(char firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPingYin() {
        return pingYin;
    }

    public void setPingYin(String pingYin) {
        this.pingYin = pingYin;
    }

    public boolean isFirstPosition() {
        return isFirstPosition;
    }

    public void setFirstPosition(boolean firstPosition) {
        isFirstPosition = firstPosition;
    }
}
