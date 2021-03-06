package com.microsoft.partnercatalyst.fortis.spark.transforms.topic

import org.scalatest.FlatSpec

class KeywordExtractorSpec extends FlatSpec {
  "The keyword extractor" should "extract overlapping keywords" in {
    val keywords = List(
      "The quick",
      "quick brown",
      "quick brown fox"
    )

    val extractor = new KeywordExtractor("en", keywords)

    val matches = extractor.extractKeywords("The quick brown fox.").map(_.name)
    assert(keywords.forall(keyword => matches.contains(keyword)))
  }

  it should "respect word boundaries" in {
    val keywords = List(
      "brown fox"
    )

    val extractor = new KeywordExtractor("en", keywords)

    // "brown fox" should not be found since "brown" is not prefixed by a word boundary
    var matches = extractor.extractKeywords("The quickbrown fox.")
    assert(matches.isEmpty)

    matches = extractor.extractKeywords("The 123brown fox.")
    assert(matches.isEmpty)

    // "brown fox" should not be found since fox is not postfixed by a word boundary
    matches = extractor.extractKeywords("The quick brown foxjumped")
    assert(matches.isEmpty)

    matches = extractor.extractKeywords("The quick brown fox123")
    assert(matches.isEmpty)

    // "brown fox" *should* be found since a symbol like '#' creates a word boundary
    matches = extractor.extractKeywords("The quick#brown fox#jumped")
    assert(matches.nonEmpty)
  }

  it should "be case-insensitive but preserve keyword case" in {
    val keywords = List(
      "BROwN FoX"
    )

    val extractor = new KeywordExtractor("en", keywords)

    val matches = extractor.extractKeywords("The quick browN fOx").map(_.name)
    assert(matches.head == keywords.head)
  }

  it should "find keywords starting and ending with symbols anywhere" in {
    val keywords = List(
      "{testing}"
    )

    val extractor = new KeywordExtractor("en", keywords)

    val matches = extractor.extractKeywords("Testing{testing}123").map(_.name)
    assert(matches.head == keywords.head)
  }

  /* Note: disabled since only top N words are returned. TODO: add relevant tests for new behavior.
  it should "find keywords many times" in {
    val keywords = List(
      "{testing}"
    )

    val extractor = new KeywordExtractor(keywords)

    val matches = extractor.extractKeywords("Testing{testing}12{testing}3").map(_.name)
    assert(matches.length == 2)
    assert(matches.forall(term => term == "{testing}"))
  }
  */
}