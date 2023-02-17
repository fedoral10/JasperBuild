package Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParameters {
    private List<String> arguments;

    public InputParameters(String[] args) {
        this.arguments = Arrays.asList(args);
    }

    private String getValue(String prefix, String ExceptionText) throws Exception {
        int index = this.arguments.indexOf(prefix);
        if (index == -1) {
            throw new Exception(ExceptionText);
        }
        if (arguments.get(index + 1).startsWith("-")) {
            throw new Exception("Valor de " + prefix + " no valido");
        }
        String value = arguments.get(index + 1);

        return value;
    }
    
    private String getOptionalValue(String prefix) throws Exception {
        int index = this.arguments.indexOf(prefix);
        if(index == -1)
        	return null;
        if (arguments.get(index + 1).startsWith("-")) {
            throw new Exception("Valor de " + prefix + " no valido");
        }
        String value = arguments.get(index + 1);

        return value;
    }

    private String getValue(String prefix) throws Exception {
        int index = this.arguments.indexOf(prefix);
        if (arguments.get(index + 1).startsWith("-")) {
            throw new Exception("Valor de " + prefix + " no valido");
        }
        return arguments.get(index + 1);
    }

    public String getConnectionString() throws Exception {
        return getValue("-c", "Debe definir la cadena de conexion");
    }

    public String getFormat() {
        String format = null;
        try {
            format = getValue("-f", "*** Valor de \"-f\" PDF|XLSX  no proporcionado, la salida se realizara a pantalla ***");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if (format == null) {
            format = "view";
        }
        return format;
    }

    public String getUser() throws Exception {
        return getOptionalValue("-u");
    }

    public String getPass() throws Exception {
        return getOptionalValue("-p");
    }

    public String getJRXMLFile() throws Exception {
        return getValue("-jrxml", "Archivo .jrxml no definido");
    }

    public String getOutputFile() throws Exception {
        return getValue("-o", "No ha definido archivo de salida");
    }

    public Map getParameters() throws Exception {
        int index = this.arguments.indexOf("-params");

        if (index == -1)
            return null;

        String patternStr = "\\[[A-z|\\.|\\s|0-9*|/|,|=|]+\\]";

        Pattern pattern = Pattern.compile(patternStr);
        int firstBrachetIndex = index + 1;

        int lastBrackedIndex = lastBrackedIndex(firstBrachetIndex);
        if (lastBrackedIndex < 0) {
            throw new Exception("No ha cerrado llave de parametros");
        }

        String parameters = fuseParameters(firstBrachetIndex, lastBrackedIndex);
        // System.out.println("parametros: " + parameters);
        Matcher matcher = pattern.matcher(parameters);

        if (!matcher.find()) {
            throw new Exception("Error en sintaxis de parametros");
        }

        return buildMapParameters(matcher.group());
    }

    private Map<String, Object> buildMapParameters(String parameters) throws Exception {
        Map<String, Object> map = new HashMap<>();
        parameters = parameters.replace("[", "");
        parameters = parameters.replace("]", "");
        List<String> listParameters = Arrays.asList(parameters.split(","));
        Iterator<String> i = listParameters.iterator();
        try {
            while (i.hasNext()) {
                String parametro = i.next().trim();
                getEntriesFromParemeters(parametro, map);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return map;
    }

    private void getEntriesFromParemeters(String parameter, Map<String, Object> map) throws Exception {

        if(!parameter.contains("=")){
            throw new Exception("Debe establecer el valor de utilizando '=' \"" + parameter + "\"");
        }
        String[] parameterArr = parameter.split(" ");

        if (parameterArr.length <= 1) {
            throw new Exception("Sintaxis erronea en el parametro \"" + parameter + "\"");
        }
        
        String[] parameterValue=parameterArr[1].split("=");
        // System.out.println(param[0]+ "|"+param[1]+ "|"+param[2]);
        try {
            switch (parameterArr[0].toLowerCase()) {
                case "java.lang.boolean":
                    map.put(parameterValue[0], Boolean.parseBoolean(parameterValue[1].trim()));
                    break;
                case "java.lang.double":
                    map.put(parameterValue[0], Double.parseDouble(parameterValue[1].trim()));
                    break;
                case "java.lang.float":
                    map.put(parameterValue[0], Float.parseFloat(parameterValue[1].trim()));
                    break;
                case "java.lang.integer":
                    map.put(parameterValue[0], Integer.parseInt(parameterValue[1].trim()));
                    break;
                case "java.lang.long":
                    map.put(parameterValue[0], Long.parseLong(parameterValue[1].trim()));
                    break;
                case "java.lang.short":
                    map.put(parameterValue[0], Short.parseShort(parameterValue[1].trim()));
                    break;
                case "java.lang.string":
                    map.put(parameterValue[0], parameterValue[1].trim());
                    break;
                case "java.math.bigdecimal":
                    map.put(parameterValue[0], BigDecimal.valueOf(Double.parseDouble(parameterValue[1].trim())));
                    break;
                case "java.sql.date":
                    map.put(parameterValue[0], getSqlDate(getUtilDate(parameterValue[1].trim())));
                    break;
                case "java.sql.time":
                    map.put(parameterValue[0], getSqlTime(getUtilDate(parameterValue[1].trim())));
                    break;
                case "java.sql.timestamp":
                    map.put(parameterValue[0], getSqlTimestamp(getUtilDate(parameterValue[1].trim())));
                    break;
                case "java.util.date":
                    map.put(parameterValue[0], getUtilDate(parameterValue[1].trim()));
                    break;
                default:
                    throw new Exception("Tipo de dato no soportado");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private Date getUtilDate(String dateStr) throws Exception {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
        } catch (Exception ex) {
            throw ex;
        }
        return date;
    }

    private java.sql.Date getSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    private java.sql.Time getSqlTime(java.util.Date date) {
        return new java.sql.Time(date.getTime());
    }

    private java.sql.Timestamp getSqlTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    private String fuseParameters(int firstBracketIndex, int lastBracketIndex) {
        StringBuilder builder = new StringBuilder();
        for (int x = firstBracketIndex; x <= lastBracketIndex; x++) {
            builder.append(this.arguments.get(x) + " ");
        }
        return builder.toString();
    }

    private int lastBrackedIndex(int firstBrachetIndex) {
        int index = -1;
        for (int x = firstBrachetIndex; x < this.arguments.size(); x++) {
            if (this.arguments.get(x).contains("]"))
                ;
            index = x;
        }
        return index;
    }
}
