package Jasper;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperCompileManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JasperObject {
	static Connection conn = null;
	JasperReport report;

	public JasperObject(String connString, String user, String pass, String jrxmlFile) {
		try {
			conn = DriverManager.getConnection(connString, user, pass); // ("jdbc:mysql://localhost/tecnireg_osc1",
																		// "root", "");
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("Error de conexión: " + e.getMessage());
			System.exit(4);
		}
		try {
			/*
			 * Map parameters = new HashMap(); parameters.put("TITULO", "PAISES");
			 * parameters.put("FECHA", new java.util.Date());
			 */
			this.report = JasperCompileManager.compileReport(jrxmlFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toPdf(String output, Map parametros) {
		try {
			JasperPrint print = JasperFillManager.fillReport(this.report, parametros, conn);

			// Exporta el informe a PDF
			//JasperExportManager.exportReportToPdfFile(print, output);
			
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
			exporter.exportReport();
			
			//SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
			
			
			// Para visualizar el pdf directamente desde java
			JasperViewer.viewReport(print, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*
			 * Cleanup antes de salir
			 */
			try {
				if (conn != null) {
					conn.rollback();
					System.out.println("ROLLBACK EJECUTADO");
//					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void toXlsx(String output, Map parametros) {
		try {
			JasperPrint print = JasperFillManager.fillReport(report, parametros, conn);
			
			// Exporta el informe a Xlsx
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(print));
			
			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setIgnoreGraphics(false);
			
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
			
			// JasperExportManager.exportReportToXlsx(print, output);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			// Para visualizar el pdf directamente desde java
			JasperViewer.viewReport(print, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*
			 * Cleanup antes de salir
			 */
			try {
				if (conn != null) {
					conn.rollback();
					System.out.println("ROLLBACK EJECUTADO");
//					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
