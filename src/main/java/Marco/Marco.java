package Marco;


import java.util.Map;

import Jasper.JasperObject;
import Utils.InputParameters;

public class Marco {

	public static void main(String[] args) {
		
		InputParameters ip = new InputParameters(args);
		try{
			Map x = ip.getParameters();
			System.out.println(x);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}

		/*Map pars = new HashMap<String,Object>();
		pars.put("pFechaIni", getSqlDate(getUtilDate("01/01/2018")));
		pars.put("pFechaFin", getSqlDate(getUtilDate("01/12/2021")));
		System.out.println(pars.get("pFechaIni"));
		System.out.println(pars.get("pFechaFin"));
		String file = "C:\\seempenia\\VENTAS\\ventas_backend\\ReportesJasper\\InformePrendasActivasDetalle.jrxml";

		JasperObject j = new JasperObject("jdbc:postgresql://localhost:5432/ventas", "postgres", "123", file);
		j.toPdf("reporte.pdf", pars);
		j.toXlsx("reporte.xlsx", pars);*/
	}

	

}
