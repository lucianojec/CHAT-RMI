/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uniasselvi.util;

/**
 *
 * @author Genilto Vanzin
 */
import edu.uniasselvi.bean.Message;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

/**
 * @author Genilto Vanzin
 *
 */
public class Solicita {

    // Solicita dados da Pessoa ao Usuário
    public static String getString(String msg, boolean obrigatorio, String valorDefaut)
            throws Exception {

        String info = JOptionPane.showInputDialog(msg, valorDefaut);

        while ((!(info instanceof String) || info.equals("")) && obrigatorio) {

            // Se clicou em cancelar
            if (!(info instanceof String)) {
                if (JOptionPane.showConfirmDialog(null,
                        "Cancelar entrada?", "Atenção",
                        JOptionPane.YES_NO_OPTION) == 0) {
                    throw new Exception("CANCEL");
                }
            }

            info = JOptionPane.showInputDialog(msg + "\n* Obrigatório.");

        }
        return info;
    }

    // Formata a Data para Saída
    public static String formataData(Date data) {
        if (data == null) {
            return null;
        }
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formato.format(data);
    }

    public static boolean getBoolean(String msg) {
        int opcao = JOptionPane.showConfirmDialog(null, msg, "Atenção",
                JOptionPane.YES_NO_OPTION);
        return (opcao == 0);
    }

    // Formata uma mensagem para saída
    public static String formataMensagem(Message ms){
        return "";
    }
}