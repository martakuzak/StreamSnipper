import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class Snipper {
	
	void doEverything(String inputName) {
		String outputName = removeExtension(inputName);
		if(outputName.compareTo(inputName) == 0)
			doEverything(inputName, outputName);
		else
			doEverything(inputName, outputName + "_out");
	}
	
	void doEverything(String inputName, String outputName) {
		System.out.println("doEverything start");
	    File file = new File(inputName);
	    byte[] bytes = read(file);
	    byte[] mdatBytes = findMdat(bytes, (int)file.length());
	    if(mdatBytes != null)
	    	writeFile(mdatBytes, outputName);
	    else 
	    	System.out.println("doEverything null");
	}
	
	String removeExtension(String input) {
		int idx = input.lastIndexOf(".");
		return input.substring(0, idx);
	}
	
	int getIntFrom4Bytes(byte byte1, byte byte2, byte byte3, byte byte4) {
		//System.out.println("getIntFrom4Bytes start");
		//System.out.println("getIntFrom4Bytes " + (int)byte1 +" " +(int)byte2+" "+(int)byte3+""+(int)byte4);
		int ret = (Byte.toUnsignedInt(byte1) << 24) + (Byte.toUnsignedInt(byte2) << 16) 
				+ (Byte.toUnsignedInt(byte3) << 8) + Byte.toUnsignedInt(byte4);
		return ret;
	}
	
	boolean isMdat(byte byte1, byte byte2, byte byte3, byte byte4) {
		System.out.println("isMdat start");
		return byte1 == 'm' && byte2 == 'd' && byte3 == 'a' && byte4 == 't';
	}
	
	byte[] findMdat(byte[] file, int length) {
		System.out.println("isMdat start");
		int offset = 0;
		while(offset + 8 <= length) {
			int size = getIntFrom4Bytes(file[0 + offset], file[1 + offset], 
					file[2 + offset], file[3 + offset]);
			System.out.println("isMdat, offset : " + offset + " , size : " + size + " " + 
					(char)file[4 + offset] + (char)file[5 + offset] + 
					(char)file[6 + offset] + (char) file[7 + offset]);
			if(isMdat(file[4 + offset], file[5 + offset], file[6 + offset], file[7 + offset])) {
				System.out.println("MDAT: " + offset);
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
		System.out.println("read start");
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
	          if (bytesRead > 0){
	            totalBytesRead = totalBytesRead + bytesRead;
	          }
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
		System.out.println("writeFile start");
		System.out.println(bytes[0] + "" + bytes[1]);
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
		System.out.println("writeBytesToFileStream start");
		str.write(bytes, off, len);
	}

	public static void main(String[] args) {
		String fileName = new String("F:\\Szkola\\MGR\\Próbki\\Bug.mp4");
		Snipper sn = new Snipper();
		sn.doEverything(fileName);
	}
}
