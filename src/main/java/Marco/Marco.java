package Marco;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Jasper.JasperObject;

public class Marco {

	public static void main(String[] args) {
		System.out.println("Hola Mundo");

		Map pars = new HashMap<String,Object>();
		pars.put("pFechaIni", getSqlDate(getUtilDate("01/01/2018")));
		pars.put("pFechaFin", getSqlDate(getUtilDate("01/12/2021")));
		System.out.println(pars.get("pFechaIni"));
		System.out.println(pars.get("pFechaFin"));
		String file = "C:\\seempenia\\VENTAS\\ventas_backend\\ReportesJasper\\InformePrendasActivasDetalle.jrxml";

		JasperObject j = new JasperObject("jdbc:postgresql://192.168.1.129:5432/ventas", "postgres", "xolotlan", file);
		j.toPdf("reporte.pdf", pars);
		j.toXlsx("reporte.xlsx", pars);
	}

	private static Date getUtilDate(String dateStr) {
		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			System.out.println(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}
	
	private static java.sql.Date getSqlDate (java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

}
