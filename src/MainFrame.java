import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private User user;
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    public MainFrame(User user) {
        this.user = user;

        setTitle("Mini Shopee - Halo, " + user.getFullName());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        add(new JScrollPane(panel));

        List<Product> products = productDAO.getAllProducts();

        for (Product p : products) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

            ImageIcon icon = loadImage(p.getProductId());
            JLabel imgLabel = new JLabel(icon);
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel name = new JLabel(p.getName());
            name.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel price = new JLabel("Rp " + p.getPrice());
            price.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton beli = new JButton("Beli");
            beli.setAlignmentX(Component.CENTER_ALIGNMENT);
            beli.addActionListener(e -> checkout(p));

            card.add(imgLabel);
            card.add(name);
            card.add(price);
            card.add(beli);

            panel.add(card);
        }
    }

    private ImageIcon loadImage(int id) {
        String file = switch (id) {
            case 1 -> "kaos.png";
            case 2 -> "sneaker.png";
            case 3 -> "headset.png";
            default -> "noimg.png";
        };

        ImageIcon icon = new ImageIcon("images/" + file);
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void checkout(Product p) {
        String qtyStr = JOptionPane.showInputDialog("Jumlah yang dibeli:");
        if (qtyStr == null) return;

        int qty = Integer.parseInt(qtyStr);
        boolean ok = orderDAO.createOrder(user.getUserId(), p, qty);

        if (ok) JOptionPane.showMessageDialog(this, "Checkout berhasil!");
        else JOptionPane.showMessageDialog(this, "Checkout gagal!");
    }
}

