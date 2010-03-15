/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.searchmaster.model;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.TextExtractor;

/**
 *
 * @author madrax
 */
public class AltTextExtractor extends TextExtractor {
  
	public AltTextExtractor(final Segment segment) {
		super(segment);
	}

  @Override
  public boolean excludeElement(StartTag startTag) {
   return startTag.getName().equals(HTMLElementName.P) || "control".equalsIgnoreCase(startTag.getAttributeValue("class"));
  }
}
