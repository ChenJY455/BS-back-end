package Server.Utils;

import lombok.Getter;
import org.json.JSONObject;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class Utils {
	@Getter
	public enum WebsiteType {
		TB("TB"),
		JD("JD");
		
		private final String website;
		WebsiteType(String website) {
			this.website = website;
		}
		
		public static WebsiteType fromString(String website) {
			for (WebsiteType type : WebsiteType.values()) {
				if (type.getWebsite().equalsIgnoreCase(website)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Unknown website: " + website);
		}
	}
	private Utils() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated.");
	}
	
	public static String decompressGzip(byte[] compressedData) throws IOException {
		try (InputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
		     GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
		     BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream))) {
			
			StringBuilder decompressedData = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				decompressedData.append(line);
			}
			return decompressedData.toString();
		}
	}
	
	public static JSONObject getJsonObject16(byte[] body_byte) throws IOException {
		String body_str;
		if(body_byte.length > 2 && body_byte[0] == (byte) 0x1f && body_byte[1] == (byte) 0x8b) {
			try(ByteArrayInputStream bais = new ByteArrayInputStream(body_byte)) {
				GZIPInputStream gzip = new GZIPInputStream(bais);
				InputStreamReader isr = new InputStreamReader(gzip);
				BufferedReader br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				body_str = sb.toString();
			}
		} else {
			body_str = new String(body_byte);
		}
		String jsonBody = body_str.replaceAll("mtopjsonp16\\((.*)", "$1");
		jsonBody = jsonBody.substring(0, jsonBody.length() - 1);
		return new JSONObject(jsonBody);
	}
}
