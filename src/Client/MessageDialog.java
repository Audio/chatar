package Client;

import javax.swing.JOptionPane;


public class MessageDialog {

    public static enum WindowType {MESSAGE, CONFIRM, INPUT}
    public static enum MessageType {WARN, ERROR, QUESTION}


    private static Object dialog(WindowType window, MessageType messageType,
                                                String title, String message) {
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
            JOptionPane.showMessageDialog(null, message, title, swingType);
        } else if ( window.equals(WindowType.CONFIRM) ) {

            Object[] options = {"Ano", "Ne"};
            int button = JOptionPane.showOptionDialog(null, message, title,
                        JOptionPane.YES_NO_OPTION, swingType, null, options, options[0]);
            return button == 0;

        } else if ( window.equals(WindowType.INPUT) ) {
            return JOptionPane.showInputDialog(null, message, title, swingType);
        }

        return null;
    }

    public static void error(String title, String message) {
        dialog(WindowType.MESSAGE, MessageType.ERROR, title, message);
    }

    public static void warning(String title, String message) {
        dialog(WindowType.MESSAGE, MessageType.WARN, title, message);
    }

    public static String inputQuestion(String title, String message) {
        return (String) dialog(WindowType.INPUT, MessageType.QUESTION, title, message);
    }

    public static boolean confirmQuestion(String title, String message) {
        return (boolean) dialog(WindowType.CONFIRM, MessageType.QUESTION, title, message);
    }

    public static String showSetNicknameDialog() {
        return inputQuestion("Nastavení přezdívky", "Zvolte novou přezdívku");
    }

}
