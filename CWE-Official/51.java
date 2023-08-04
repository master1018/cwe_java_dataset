@Stateless
public class ConverterSessionBean extends Component implements KeyListener, ConverterSessionRemote {
...
private StringBuffer enteredText = new StringBuffer();
private BigDecimal yenRate = new BigDecimal("115.3100");
public ConverterSessionBean() {
super();
...
addKeyListener(this);
}
public BigDecimal dollarToYen(BigDecimal dollars) {
BigDecimal result = dollars.multiply(yenRate);
return result.setScale(2, BigDecimal.ROUND_DOWN);
}
public void keyTyped(KeyEvent event) {
...
}
public void keyPressed(KeyEvent e) {
}
public void keyReleased(KeyEvent e) {
}
public void paint(Graphics g) {...}
...
}
