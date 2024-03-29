import javax.swing.JTextField;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPlaceHolderTextField extends PlaceHolderTextField {
    private Shape shape;

    public RoundedPlaceHolderTextField(String placeholder) {
        super(placeholder);
        setOpaque(false); // torna o campo de texto completamente transparente
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); // Arredondamento dos cantos
        }

        g2.setColor(getBackground());
        g2.fill(shape);

        super.paintComponent(g);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Não faça nada para evitar que a borda padrão seja desenhada
    }
}
