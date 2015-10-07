import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class Snipper {
	
	boolean doEverything(String inputName) {
		String outputName = removeExtension(inputName);
		if(outputName.compareTo(inputName) != 0)
			return doEverything(inputName, outputName);
		else
			return doEverything(inputName, outputName + "_out");
	}
	
	boolean doEverything(String inputName, String outputName) {
	    File file = new File(inputName);
	    byte[] bytes = read(file);
	    byte[] mdatBytes = findMdat(bytes, (int)file.length());
	    if(mdatBytes != null) {
	    	writeFile(mdatBytes, outputName);
	    	return true;
	    }
	    else 
	    	return false;
	}
	
	String removeExtension(String input) {
		int idx = input.lastIndexOf(".");
		return input.substring(0, idx);
	}
	
	int getIntFrom4Bytes(byte byte1, byte byte2, byte byte3, byte byte4) {
		int ret = (Byte.toUnsignedInt(byte1) << 24) + (Byte.toUnsignedInt(byte2) << 16) 
				+ (Byte.toUnsignedInt(byte3) << 8) + Byte.toUnsignedInt(byte4);
		return ret;
	}
	
	boolean isMdat(byte byte1, byte byte2, byte byte3, byte byte4) {
		return byte1 == 'm' && byte2 == 'd' && byte3 == 'a' && byte4 == 't';
	}
	
	byte[] findMdat(byte[] file, int length) {
		int offset = 0;
		while(length > 8 && offset + 8 <= length) {
			int size = getIntFrom4Bytes(file[0 + offset], file[1 + offset], 
					file[2 + offset], file[3 + offset]);
			if (size < 0)
				return null;
			if(isMdat(file[4 + offset], file[5 + offset], file[6 + offset], file[7 + offset])) {
				byte[] ret = new byte[size - 8];
				for(int i = 0; i < size - 8; ++ i)
					ret[i] = file[offset + 8 + i];
				return ret;
			} else 
				offset += size;
		}
		return null;
	}
	
	byte[] read(File file){
	    byte[] result = new byte[(int)file.length()];
	    try {
	      InputStream input = null;
	      try {
	        int totalBytesRead = 0;
	        input = new BufferedInputStream(new FileInputStream(file));
	        while(totalBytesRead < result.length){
	          int bytesRemaining = result.length - totalBytesRead;
	          //input.read() returns -1, 0, or more :
	          int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
	          if (bytesRead > 0)
	            totalBytesRead = totalBytesRead + bytesRead;
	        }
	        /*
	         the above style is a bit tricky: it places bytes into the 'result' array; 
	         'result' is an output parameter;
	         the while loop usually has a single iteration only.
	        */
	        //log("Num bytes read: " + totalBytesRead);
	      }
	      finally {
	        //log("Closing input stream.");
	        input.close();
	      }
	    }
	    catch (FileNotFoundException ex) {
	      //log("File not found.");
	    }
	    catch (IOException ex) {
	      //log(ex);
	    }
	    return result;
	  }
	
	void writeFile(byte[] bytes, String fileName) {
		try {
			FileOutputStream stream = new FileOutputStream(fileName);
			try {
				stream.write(bytes);
			} finally {
				stream.close();
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + fileName);
		} catch (IOException e) {
			System.err.println("IOException: " + fileName);
		}
	}
	
	void writeBytesToFileStream(FileOutputStream str, byte[] bytes, int off, int len) throws IOException {
		str.write(bytes, off, len);
	}

}
