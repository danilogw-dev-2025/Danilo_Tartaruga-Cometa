package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUtils {

    /**
     * Converte a string de data de YYYY-MM-DD (padrão do BD) para DD/MM/YYYY.
     * Deve ser chamada na camada de apresentação.
     */
    public static String formatarDataBrasileira(String dataString) {
        if (dataString == null || dataString.isEmpty()) {
            return "";
        }
        try {
            LocalDate data = LocalDate.parse(dataString, DateTimeFormatter.ISO_LOCAL_DATE);
            DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return data.format(formatoSaida);
        } catch (Exception e) {
            return dataString;
        }
    }
}