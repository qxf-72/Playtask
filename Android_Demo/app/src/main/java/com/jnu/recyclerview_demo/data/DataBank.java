package com.jnu.recyclerview_demo.data;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBank {
    final String DATA_FILENAME = "book_items.data";

    // 从文件中读取数据
    public ArrayList<BookItem> LoadBookItem(Context context) {
        ArrayList<BookItem> bookItems = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(DATA_FILENAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            bookItems = (ArrayList<BookItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            Log.d("Serializable", "LoadBookItem: " + bookItems.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return bookItems;
    }

    // 将数据保存到文件中
    public void SaveBookItem(Context context, ArrayList<BookItem> bookItems) {
        try {
            FileOutputStream fileOut = context.openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new java.io.ObjectOutputStream(fileOut);
            objectOut.writeObject(bookItems);
            objectOut.close();
            fileOut.close();
            Log.d("Serializable", "Data is serialized and saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
