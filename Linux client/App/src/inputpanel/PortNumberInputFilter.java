package inputpanel;

import java.awt.Toolkit;

import javax.swing.text.*;

class PortNumberInputFilter extends DocumentFilter {
	private int limit;
	PortNumberInputFilter(int length){
    	this.limit = length;
    }
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
    	
        int finalLength = fb.getDocument().getLength() + text.length();
        if(finalLength > limit) {
            fb.replace(offset, length, text.replaceAll("[^0-9]", "").substring(0, limit-fb.getDocument().getLength()), attrs);
            Toolkit.getDefaultToolkit().beep();
        } else
            fb.replace(offset, length, text.replaceAll("[^0-9]", ""), attrs);
    }
}
