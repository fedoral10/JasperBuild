package Marco;

import java.util.Map;

import Jasper.JasperObject;
import Utils.InputParameters;

public class Marco {

	public static void main(String[] args) {

		InputParameters ip = new InputParameters(args);
		try {

			JasperObject jo = new JasperObject(ip.getConnectionString(), ip.getUser(), ip.getPass(), ip.getJRXMLFile());

			switch (ip.getFormat().toLowerCase()) {
				case "view":
					jo.toScreen(ip.getParameters());
					break;
				case "pdf":
					jo.toPdf(ip.getOutputFile(), ip.getParameters());
					break;
				case "xlsx":
					jo.toXlsx(ip.getOutputFile(), ip.getParameters());
					break;
				case "base64":
					jo.toBase64(null, ip.getParameters());
					break;
				default:
					System.out.println("Formatos soportados XLSX, PDF y base64");
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
