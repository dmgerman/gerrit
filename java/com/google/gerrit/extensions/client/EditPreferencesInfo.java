begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_comment
comment|/* This class is stored in Git config file. */
end_comment

begin_class
DECL|class|EditPreferencesInfo
specifier|public
class|class
name|EditPreferencesInfo
block|{
DECL|field|tabSize
specifier|public
name|Integer
name|tabSize
decl_stmt|;
DECL|field|lineLength
specifier|public
name|Integer
name|lineLength
decl_stmt|;
DECL|field|indentUnit
specifier|public
name|Integer
name|indentUnit
decl_stmt|;
DECL|field|cursorBlinkRate
specifier|public
name|Integer
name|cursorBlinkRate
decl_stmt|;
DECL|field|hideTopMenu
specifier|public
name|Boolean
name|hideTopMenu
decl_stmt|;
DECL|field|showTabs
specifier|public
name|Boolean
name|showTabs
decl_stmt|;
DECL|field|showWhitespaceErrors
specifier|public
name|Boolean
name|showWhitespaceErrors
decl_stmt|;
DECL|field|syntaxHighlighting
specifier|public
name|Boolean
name|syntaxHighlighting
decl_stmt|;
DECL|field|hideLineNumbers
specifier|public
name|Boolean
name|hideLineNumbers
decl_stmt|;
DECL|field|matchBrackets
specifier|public
name|Boolean
name|matchBrackets
decl_stmt|;
DECL|field|lineWrapping
specifier|public
name|Boolean
name|lineWrapping
decl_stmt|;
DECL|field|indentWithTabs
specifier|public
name|Boolean
name|indentWithTabs
decl_stmt|;
DECL|field|autoCloseBrackets
specifier|public
name|Boolean
name|autoCloseBrackets
decl_stmt|;
DECL|field|showBase
specifier|public
name|Boolean
name|showBase
decl_stmt|;
DECL|field|theme
specifier|public
name|Theme
name|theme
decl_stmt|;
DECL|field|keyMapType
specifier|public
name|KeyMapType
name|keyMapType
decl_stmt|;
DECL|method|defaults ()
specifier|public
specifier|static
name|EditPreferencesInfo
name|defaults
parameter_list|()
block|{
name|EditPreferencesInfo
name|i
init|=
operator|new
name|EditPreferencesInfo
argument_list|()
decl_stmt|;
name|i
operator|.
name|tabSize
operator|=
literal|8
expr_stmt|;
name|i
operator|.
name|lineLength
operator|=
literal|100
expr_stmt|;
name|i
operator|.
name|indentUnit
operator|=
literal|2
expr_stmt|;
name|i
operator|.
name|cursorBlinkRate
operator|=
literal|0
expr_stmt|;
name|i
operator|.
name|hideTopMenu
operator|=
literal|false
expr_stmt|;
name|i
operator|.
name|showTabs
operator|=
literal|true
expr_stmt|;
name|i
operator|.
name|showWhitespaceErrors
operator|=
literal|false
expr_stmt|;
name|i
operator|.
name|syntaxHighlighting
operator|=
literal|true
expr_stmt|;
name|i
operator|.
name|hideLineNumbers
operator|=
literal|false
expr_stmt|;
name|i
operator|.
name|matchBrackets
operator|=
literal|true
expr_stmt|;
name|i
operator|.
name|lineWrapping
operator|=
literal|false
expr_stmt|;
name|i
operator|.
name|indentWithTabs
operator|=
literal|false
expr_stmt|;
name|i
operator|.
name|autoCloseBrackets
operator|=
literal|false
expr_stmt|;
name|i
operator|.
name|showBase
operator|=
literal|false
expr_stmt|;
name|i
operator|.
name|theme
operator|=
name|Theme
operator|.
name|DEFAULT
expr_stmt|;
name|i
operator|.
name|keyMapType
operator|=
name|KeyMapType
operator|.
name|DEFAULT
expr_stmt|;
return|return
name|i
return|;
block|}
block|}
end_class

end_unit

