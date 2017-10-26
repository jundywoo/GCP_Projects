import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimePoc {

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
		System.out.println(sdf.format(new Date()));
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(sdf.format(new Date()));
	}

}
