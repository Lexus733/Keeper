package com.example.admin.keeper;


public class Text  extends ListItems{

    private String mText;

    public Text(int id, String name, String text) {
        super(id, name, ListItems.TYPE_ITEM_TEXT);
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
