import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceHolderTextField extends JTextField {
    private String placeholder;
    private boolean focused;

    public PlaceHolderTextField(String placeholder) {
        this.placeholder = placeholder;
        this.focused = false;
        setForeground(Color.GRAY);
        setText(placeholder);

        // Adicionando FocusListener
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!focused) {
                    setText("");
                    setForeground(Color.BLACK);
                    focused = true;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY);
                    focused = false;
                }
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                    focused = true;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY);
                    focused = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // not needed
            }
        });
    }
}
