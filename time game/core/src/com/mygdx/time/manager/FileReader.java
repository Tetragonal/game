package com.mygdx.time.manager;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileReader {
	public static String readFile(String path) throws IOException{
		FileHandle file = Gdx.files.internal(path);
		String text = file.readString();
		return text;
	}
	
	public static void writeFile(String path, String content) throws IOException{
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path), "utf-8"))) {
					writer.write(content);
		}
	}
}
