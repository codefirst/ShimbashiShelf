package net.reduls.gomoku.analysis.ipadic;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import net.reduls.gomoku.Tagger;
import net.reduls.gomoku.Morpheme;

/**
 * IPA辞書に基づきテキストを形態素(トークン)単位で分割するトークナイザ
 * 分割された各形態素(トークン)は、その原形にまとめられる
 * ex) '買った' ==分割==> '買っ'+'た' ==原形変換==> '買う'+'た'
 */
public final class IpadicTokenizer extends Tokenizer {
    /** 形態素解析器 */
    private final Tagger tagger;

    /** 現在ポイントしているトークン(形態素) */
    private Iterator<Morpheme> curToken = new ArrayList<Morpheme>().iterator();

    /** これまでに読み込まれた文字数 */
    private int offset = 0;

    /** 行読み込み用 */
    private BufferedReader br;

    private TermAttribute termAtt;
    private OffsetAttribute offsetAtt;
    private TypeAttribute typeAtt;

    /**
     * 形態素解析器および対象テキスト({@link Reader})をもとに、トークナイザを作成する
     *
     * @params tagger 形態素解析器。辞書としてはIPA辞書が指定されている必要がある。
     * @params in トークナイズの対象となるテキストを読み込む{@link Reader}
     */
    public IpadicTokenizer(Tagger tagger, Reader in) {
	super(in);
	this.tagger = tagger;
	br = new BufferedReader(in);

	termAtt = addAttribute(TermAttribute.class);
	offsetAtt = addAttribute(OffsetAttribute.class);
	typeAtt = addAttribute(TypeAttribute.class);
    }

    /**
     * トークンを一つ読み進める。
     *
     * @return 終端トークンに達した場合はfalseを、それ以外はtrueを返す
     * @throws IOException 入力エラーが発生した場合に送出される
     */
    @Override
    public boolean incrementToken() throws IOException {
	clearAttributes();  // NOTE: Tokenizerを継承する場合は、このメソッド呼び出しが必須らしい

	// 形態素(トークン)を一つ分読み込む
	final Morpheme m = readMorpheme();
	if(m==null)
	    return false;  // 終端に達した

	// 位置設定
	offset = m.start+m.surface.length();
	offsetAtt.setOffset(correctOffset(m.start), correctOffset(offset));

	// Morpheme.featureをパースする
	// IPA辞書のフォーマットに依存した処理
	final int p1=m.feature.indexOf(",");
	final int p2=m.feature.indexOf(",",p1+1);
	final int p3=m.feature.indexOf(",",p2+1);
	final int p4=m.feature.indexOf(",",p3+1);
	final int p5=m.feature.indexOf(",",p4+1);
	final int p6=m.feature.indexOf(",",p5+1);
	final int p7=m.feature.indexOf(",",p6+1);

	// 品詞設定
        // 原形以降はもともと存在しないので、そのままセットする
        typeAtt.setType(m.feature);

	// ターム設定
        // 表層形をそのまま使う
        termAtt.setTermBuffer(m.surface);

	return true;
    }

    @Override
    public final void end() {
	final int finalOffset = correctOffset(offset);
	offsetAtt.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void reset() throws IOException {
	super.reset();
	offset = 0;
	curToken = new ArrayList<Morpheme>().iterator();
    }

    @Override
    public void reset(Reader reader) throws IOException {
	super.reset(reader);
	reset();
	br = new BufferedReader(reader);
    }

    private Morpheme readMorpheme() throws IOException {
	if(curToken.hasNext()==false) {
	    final String line = br.readLine();
	    if(line==null)
		return null;

	    curToken = tagger.parse(line).iterator();
	    return readMorpheme();
	}
	return curToken.next();
    }
}