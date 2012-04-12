package edu.duq.mehok;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

	public static String getFileContents(InputStream is) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int i;
		try {
			i = is.read();
			while (i != -1)
			{
				byteArrayOutputStream.write(i);
				i = is.read();
			}
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return byteArrayOutputStream.toString();
	}

}
