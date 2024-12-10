package Server.Utils;

import Server.Exception.NotFoundException;
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
			throw new NotFoundException("Unknown website: " + website);
		}
	}
	private Utils() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated.");
	}
	
	public static String DepressBytes(byte[] bytes) throws IOException {
		if(bytes.length > 2 && bytes[0] == (byte) 0x1f && bytes[1] == (byte) 0x8b) {
			try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
				GZIPInputStream gzip = new GZIPInputStream(bais);
				InputStreamReader isr = new InputStreamReader(gzip);
				BufferedReader br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} else {
			return new String(bytes);
		}
	}
	
	public static JSONObject getJsonObject16(byte[] body_byte) throws IOException {
		String body_str = DepressBytes(body_byte);
		String jsonBody = body_str.replaceAll("mtopjsonp16\\((.*)", "$1");
		jsonBody = jsonBody.substring(0, jsonBody.length() - 1);
		return new JSONObject(jsonBody);
	}
}
