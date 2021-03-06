package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.sourcecode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import jp.ac.osaka_u.ist.sdl.scorpio.ScorpioGUI;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.ElementInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodCallInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodInfo;

/**
 * ソースコードを表示するパネル
 * 
 * @author higo
 * 
 */
class SourceCodePanel extends JPanel {

	class HighlightedBackgroundBorder implements Border {

		final MethodInfo method;
		final CodeCloneInfo codeclone;

		public HighlightedBackgroundBorder(final MethodInfo method,
				final CodeCloneInfo codeclone) {
			this.method = method;
			this.codeclone = codeclone;
		}

		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {

			g.setColor(CODECLONE_COLOR);

			final int fileID = this.method.getFileID();
			final FileInfo file = FileController.getInstance(ScorpioGUI.ID)
					.getFile(fileID);

			for (final ElementInfo element : this.codeclone.getElements()) {
				if (element.getMethodID() == this.method.getID()) {
					final int fromLine = element.getFromLine();
					final int toLine = element.getToLine();
					final int startY = fromLine * height / file.getLOC();
					final int lengthY = (toLine - fromLine) * height
							/ file.getLOC() + 1;
					g.drawRect(0, startY, width, lengthY);
				}
			}
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(0, 0, 0, 0);
		}

		public boolean isBorderOpaque() {
			return true;
		}
	}

	SourceCodePanel(final MethodInfo method) {

		this.method = method;

		this.fileNameField = new JTextField();
		this.fileNameField.setEditable(false);

		this.sourceCodeArea = new JTextArea();
		this.sourceCodeArea.setTabSize(1);
		this.sourceCodeArea.setEditable(false);

		this.sourceCodeScrollPane = new JScrollPane(this.sourceCodeArea);
		this.sourceCodeScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.sourceCodeScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.sourceCodeScrollPane.setRowHeaderView(new LineNumberView(
				this.sourceCodeArea));

		this.setLayout(new BorderLayout());
		this.add(this.fileNameField, BorderLayout.NORTH);
		this.add(this.sourceCodeScrollPane, BorderLayout.CENTER);

		final int fileID = this.method.getFileID();
		final FileInfo file = FileController.getInstance(ScorpioGUI.ID)
				.getFile(fileID);
		this.readFile(file);
	}

	/**
	 * ファイルを読み込む
	 * 
	 * @param file
	 *            　読み込むファイル
	 */
	void readFile(final FileInfo file) {

		this.fileNameField.setText(file.getName());

		this.sourceCodeArea.replaceSelection("");

		try {

			final InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file.getName()), "JISAutoDetect");
			final StringBuilder sb = new StringBuilder();
			for (int c = reader.read(); c != -1; c = reader.read()) {
				sb.append((char) c);
			}
			reader.close();
			this.sourceCodeArea.append(sb.toString());

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	/**
	 * コードクローン部分を強調表示する
	 * 
	 * @param codeclone
	 *            　強調表示するコードクローン
	 */
	void addHighlight(final CodeCloneInfo codeclone) {

		// コードクローンを強調表示
		final DefaultHighlightPainter codeclonePainter = new DefaultHighlightPainter(
				CODECLONE_COLOR);
		for (final ElementInfo element : codeclone.getElements()) {
			final int methodID = element.getMethodID();
			if (this.method.getID() == methodID) {

				try {
					final int fromLine = element.getFromLine();
					final int fromColumn = element.getFromColumn();
					final int toLine = element.getToLine();
					final int toColumn = element.getToColumn();

					final int fromOffset = this.sourceCodeArea
							.getLineStartOffset(fromLine - 1)
							+ (fromColumn - 1);
					final int toOffset = this.sourceCodeArea
							.getLineStartOffset(toLine - 1)
							+ (toColumn - 1);

					this.sourceCodeArea.getHighlighter().addHighlight(
							fromOffset, toOffset, codeclonePainter);

				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}

		// メソッド間クローンの基点となっているメソッド呼び出しをハイライト
		final DefaultHighlightPainter methodcallPainter = new DefaultHighlightPainter(
				METHODCALL_COLOR);
		for (final MethodCallInfo methodcall : codeclone.getCalls()) {
			if (methodcall.getCallerID() == this.method.getID()) {
				try {
					final int fromLine = methodcall.getFromLine();
					final int fromColumn = methodcall.getFromColumn();
					final int toLine = methodcall.getToLine();
					final int toColumn = methodcall.getToColumn();

					final int fromOffset = this.sourceCodeArea
							.getLineStartOffset(fromLine - 1)
							+ (fromColumn - 1);
					final int toOffset = this.sourceCodeArea
							.getLineStartOffset(toLine - 1)
							+ (toColumn - 1);

					this.sourceCodeArea.getHighlighter().addHighlight(
							fromOffset, toOffset, methodcallPainter);

				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}

		// スクロールバーをハイライト表示
		this.sourceCodeScrollPane.getVerticalScrollBar().setBorder(
				new HighlightedBackgroundBorder(this.method, codeclone));
	}

	/**
	 * コードクローンを表示する（スクロールする）
	 * 
	 * @param codeclone
	 *            　表示するコードクローン
	 */
	void display(final CodeCloneInfo codeclone) {

		final Document doc = this.sourceCodeArea.getDocument();
		final Element root = doc.getDefaultRootElement();
		ElementInfo element = null;
		for (final ElementInfo e : codeclone.getElements()) {
			final int methodID = e.getMethodID();
			if (this.method.getID() == methodID) {
				element = e;
				break;
			}
		}

		if (null == element) {
			return;
		}

		// 下の modelToViewの返り値をnullにしないために強制的に正の値を設定
		this.sourceCodeArea.setBounds(new Rectangle(10, 10));

		try {
			Element elem = root.getElement(Math.max(1,
					element.getFromLine() - 2));
			if (null != elem) {
				Rectangle rect = this.sourceCodeArea.modelToView(elem
						.getStartOffset());
				Rectangle vr = this.sourceCodeScrollPane.getViewport()
						.getViewRect();
				if ((null != rect) && (null != vr)) {
					rect.setSize(10, vr.height);
					this.sourceCodeArea.scrollRectToVisible(rect);
				}
				this.sourceCodeArea.setCaretPosition(elem.getStartOffset());
			}

			// textArea.requestFocus();
		} catch (BadLocationException e) {
			System.err.println(e.getMessage());
		}
	}

	final MethodInfo method;

	final private JTextField fileNameField;

	final private JTextArea sourceCodeArea;

	final private JScrollPane sourceCodeScrollPane;

	final private static Color CODECLONE_COLOR = new Color(255, 200, 0, 125);

	final private static Color METHODCALL_COLOR = new Color(0, 0, 255, 125);
}
