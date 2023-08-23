package com.namsan.learnjsoup1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.namsan.learnjsoup1.dao.DatabaseHelper;
import com.namsan.learnjsoup1.entity.WordExtend;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedWordHandler extends DatabaseHelper {

    public SavedWordHandler(Context context) {
        super(context);
    }

    public boolean create(WordExtend wordExtend){
        List<String> examples = wordExtend.getExamples();

        Gson gson = new Gson();

        String exampleJson = gson.toJson(examples);
        ContentValues values = new ContentValues();
        values.put("url",wordExtend.getUrl());
        values.put("title",wordExtend.getTitle());
        values.put("wordname", wordExtend.getWordName());
        values.put("wordform", wordExtend.getWordForm());
        values.put("definition", wordExtend.getDefinition());
        values.put("examples", exampleJson);


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        boolean isSuccessful = sqLiteDatabase.insert("savedwords", null, values)>0;
        sqLiteDatabase.close();

        return isSuccessful;
    }

    public ArrayList<WordExtend> readSavedWords(String orderBy){
        ArrayList<WordExtend> wordExtendArrayList = new ArrayList<>();
        String query = null;
        switch (orderBy){
            case "new":
                query = "select * from savedwords order by id DESC";
                break;
            case "old":
                query = "select * from savedwords order by id asc";
                break;
            case "az":
                query = "select * from savedwords order by wordname collate nocase asc";
                break;
            case "za":
                query = "select * from savedwords order by wordname collate nocase DESC";
                break;
        }


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                int id= Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String wordName = cursor.getString(cursor.getColumnIndexOrThrow("wordname"));
                String wordForm = cursor.getString(cursor.getColumnIndexOrThrow("wordform"));
                String definition = cursor.getString(cursor.getColumnIndexOrThrow("definition"));
                String examplesJson = cursor.getString(cursor.getColumnIndexOrThrow("examples"));

                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>(){}.getType();

                ArrayList<String> examples = gson.fromJson(examplesJson,type);

                WordExtend word = new WordExtend(title,url,wordName,wordForm,definition,examples);
                word.setId(id);
                wordExtendArrayList.add(word);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return wordExtendArrayList;
    }

    public boolean delete(int id){
        boolean isDeleted;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        isDeleted = sqLiteDatabase.delete("savedwords", "id='"+id+"'", null)>0;
        sqLiteDatabase.close();

        return isDeleted;
    }
}
