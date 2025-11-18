//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package br.com.fiap.utilidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validacoes {
    public static boolean validarCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    public static boolean validarCrm(String crm) {
        return crm != null && crm.matches("[a-zA-Z0-9]{3,}");
    }

    public static boolean validarData(String data) {
        if (data == null) {
            return false;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate.parse(data, formatter);
                return true;
            } catch (DateTimeParseException var2) {
                return false;
            }
        }
    }


}