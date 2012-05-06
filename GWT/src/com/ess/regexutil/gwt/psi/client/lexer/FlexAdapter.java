/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ess.regexutil.gwt.psi.client.lexer;

import java.io.IOException;

/**
 * @author max
 */
public class FlexAdapter extends Lexer {
  private FlexLexer myFlex = null;
  private int myTokenType = 0;
  private String myText;

  private int myEnd;
  private int myState;

  public FlexAdapter(final FlexLexer flex) {
    myFlex = flex;
  }

  public FlexLexer getFlex() {
    return myFlex;
  }

  @Override
  public void start(final String buffer, int startOffset, int endOffset, final int initialState) {
    myText = buffer;
    myEnd = endOffset;
    myFlex.reset(myText, startOffset, endOffset, initialState);    
    myTokenType = 0;
  }

  @Override
  public int getState() {
    if (myTokenType == 0) locateToken();
    return myState;
  }

  @Override
  public int getTokenType() {
    if (myTokenType == 0) locateToken();
    return myTokenType;
  }

  @Override
  public int getTokenStart() {
    if (myTokenType == 0) locateToken();
    return myFlex.getTokenStart();
  }

  @Override
  public int getTokenEnd() {
    if (myTokenType == 0) locateToken();
    return myFlex.getTokenEnd();
  }

  @Override
  public void advance() {
    if (myTokenType == 0) locateToken();
    myTokenType = 0;
  }

  @Override
  public String getBufferSequence() {
    return myText;
  }

  @Override
  public int getBufferEnd() {
    return myEnd;
  }

  private void locateToken() {
    if (myTokenType != 0) return;
    try {
      myState = myFlex.yystate();
      myTokenType = myFlex.advance();
    }
    catch (IOException e) { /*Can't happen*/ }
    catch (Error e) {
      // add lexer class name to the error
      final Error error = new Error(myFlex.getClass().getName() + ": " + e.getMessage());
      error.setStackTrace(e.getStackTrace());
      throw error;
    }
  }
}
