package net.reduls.gomoku.analysis.ipadic;

import java.io.Reader;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import net.reduls.gomoku.Tagger;

/**
 * {@link IpadicTokenizer}を用いてテキストをトークンに分割する{@link Analyzer}クラス
 */
public class IpadicAnalyzer extends Analyzer {
    /** 形態素解析器 */
    private final Tagger tagger;
    /**
     * {@link Tagger}インスタンスを受け取りアナライザを作成する。
     *
     * @params tagger 形態素解析クラス。辞書にはIPA辞書が指定されている必要がある
     */
    public IpadicAnalyzer(Tagger tagger) {
	this.tagger = tagger;
    }

    /**
     * {@link TokenStream}を作成する。
     * この{@link TokenStream}インスタンスは、引数で渡された{@link Reader}から
     * 読み込まれるテキストをトークンに分割する。
     *
     * @params fieldName トークナイズの対象となるluceneのフィールド名
     * @params reader トークナイズの対象テキストを読み込む`{@link Reader}インスタンス
     * @return {@link TokenStream}インスタンス
     */
    @Override
    public final TokenStream tokenStream(String fieldName, Reader reader) {
	return new IpadicTokenizer(tagger, reader);
    }

    /**
     * {@link TokenStream}インスタンスを返す。
     * このインスタンスは、可能な限り再利用される。
     * @params fieldName トークナイズの対象となるluceneのフィールド名
     * @params reader トークナイズの対象テキストを読み込む`{@link Reader}インスタンス
     * @return {@link TokenStream}インスタンス
     */
    @Override
    public final TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
	IpadicTokenizer prev = (IpadicTokenizer)getPreviousTokenStream();
	if(prev == null) {
	    prev = (IpadicTokenizer)tokenStream(fieldName, reader);
	    setPreviousTokenStream(prev);
	} else {
	    prev.reset(reader);
	}
	return prev;
    }
}