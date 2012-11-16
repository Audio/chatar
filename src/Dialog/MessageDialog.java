package Dialog;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MessageDialog {

    public static enum WindowType {MESSAGE, CONFIRM, INPUT}
    public static enum MessageType {WARN, ERROR, QUESTION}


    private static Object dialog(WindowType window, MessageType messageType,
                                 String title, String message, JFrame parent) {
        int swingType;
        switch (messageType) {
            case WARN:
                swingType = JOptionPane.WARNING_MESSAGE; break;
            case QUESTION:
                swingType = JOptionPane.QUESTION_MESSAGE; break;
            default:
                swingType = JOptionPane.ERROR_MESSAGE;
        }

        if ( window.equals(WindowType.MESSAGE) ) {
            JOptionPane.showMessageDialog(parent, message, title, swingType);
        } else if ( window.equals(WindowType.CONFIRM) ) {

            Object[] options = {"Ano", "Ne"};
            int button = JOptionPane.showOptionDialog(parent, message, title,
                        JOptionPane.YES_NO_OPTION, swingType, null, options, options[0]);
            return button == 0;

        } else if ( window.equals(WindowType.INPUT) ) {
            return JOptionPane.showInputDialog(parent, message, title, swingType);
        }

        return null;
    }

    public static void error(String title, String message, JFrame parent) {
        dialog(WindowType.MESSAGE, MessageType.ERROR, title, message, parent);
    }

    public static void warning(String title, String message, JFrame parent) {
        dialog(WindowType.MESSAGE, MessageType.WARN, title, message, parent);
    }

    public static String inputQuestion(String title, String message, JFrame parent) {
        return (String) dialog(WindowType.INPUT, MessageType.QUESTION, title, message, parent);
    }

    public static boolean confirmQuestion(String title, String message, JFrame parent) {
        return (boolean) dialog(WindowType.CONFIRM, MessageType.QUESTION, title, message, parent);
    }

    public static String showSetNicknameDialog() {
        return inputQuestion("Nastavení přezdívky", "Zvolte novou přezdívku", null);
    }

}
