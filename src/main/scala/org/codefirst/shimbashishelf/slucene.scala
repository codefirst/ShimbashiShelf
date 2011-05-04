package org.codefirst.shimbashishelf

import org.apache.lucene.document.Field
import org.apache.lucene.document.Field.Index
import org.apache.lucene.document.Field.Store
import org.apache.lucene.index.Term

object SLucene {
  implicit def tuple2term(x : (String, String)) : Term =
    new Term(x._1, x._2)

  implicit def tuple2field(x : (String, String, Field.Store, Field.Index)) : Field =
    new Field(x._1, x._2, x._3, x._4)
}
