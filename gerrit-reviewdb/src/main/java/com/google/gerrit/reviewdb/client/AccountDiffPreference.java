begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_comment
comment|/** Diff formatting preferences of an account */
end_comment

begin_class
DECL|class|AccountDiffPreference
specifier|public
class|class
name|AccountDiffPreference
block|{
comment|/** Default number of lines of context. */
DECL|field|DEFAULT_CONTEXT
specifier|public
specifier|static
specifier|final
name|short
name|DEFAULT_CONTEXT
init|=
literal|10
decl_stmt|;
comment|/** Context setting to display the entire file. */
DECL|field|WHOLE_FILE_CONTEXT
specifier|public
specifier|static
specifier|final
name|short
name|WHOLE_FILE_CONTEXT
init|=
operator|-
literal|1
decl_stmt|;
comment|/** Typical valid choices for the default context setting. */
DECL|field|CONTEXT_CHOICES
specifier|public
specifier|static
specifier|final
name|short
index|[]
name|CONTEXT_CHOICES
init|=
block|{
literal|3
block|,
literal|10
block|,
literal|25
block|,
literal|50
block|,
literal|75
block|,
literal|100
block|,
name|WHOLE_FILE_CONTEXT
block|}
decl_stmt|;
DECL|enum|Whitespace
specifier|public
specifier|static
enum|enum
name|Whitespace
implements|implements
name|CodedEnum
block|{
DECL|enumConstant|IGNORE_NONE
name|IGNORE_NONE
argument_list|(
literal|'N'
argument_list|)
block|,
comment|//
DECL|enumConstant|IGNORE_SPACE_AT_EOL
name|IGNORE_SPACE_AT_EOL
argument_list|(
literal|'E'
argument_list|)
block|,
comment|//
DECL|enumConstant|IGNORE_SPACE_CHANGE
name|IGNORE_SPACE_CHANGE
argument_list|(
literal|'S'
argument_list|)
block|,
comment|//
DECL|enumConstant|IGNORE_ALL_SPACE
name|IGNORE_ALL_SPACE
argument_list|(
literal|'A'
argument_list|)
block|;
DECL|field|code
specifier|private
specifier|final
name|char
name|code
decl_stmt|;
DECL|method|Whitespace (final char c)
specifier|private
name|Whitespace
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
name|code
operator|=
name|c
expr_stmt|;
block|}
DECL|method|getCode ()
specifier|public
name|char
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
DECL|method|forCode (final char c)
specifier|public
specifier|static
name|Whitespace
name|forCode
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Whitespace
name|s
range|:
name|Whitespace
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|code
operator|==
name|c
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
DECL|method|createDefault (Account.Id accountId)
specifier|public
specifier|static
name|AccountDiffPreference
name|createDefault
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|AccountDiffPreference
name|p
init|=
operator|new
name|AccountDiffPreference
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|p
operator|.
name|setIgnoreWhitespace
argument_list|(
name|Whitespace
operator|.
name|IGNORE_NONE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setTabSize
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|p
operator|.
name|setLineLength
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|p
operator|.
name|setSyntaxHighlighting
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|p
operator|.
name|setShowWhitespaceErrors
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|p
operator|.
name|setShowLineEndings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|p
operator|.
name|setIntralineDifference
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|p
operator|.
name|setShowTabs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|p
operator|.
name|setContext
argument_list|(
name|DEFAULT_CONTEXT
argument_list|)
expr_stmt|;
name|p
operator|.
name|setManualReview
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|ignoreWhitespace
specifier|protected
name|char
name|ignoreWhitespace
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|)
DECL|field|tabSize
specifier|protected
name|int
name|tabSize
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|)
DECL|field|lineLength
specifier|protected
name|int
name|lineLength
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|5
argument_list|)
DECL|field|syntaxHighlighting
specifier|protected
name|boolean
name|syntaxHighlighting
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|6
argument_list|)
DECL|field|showWhitespaceErrors
specifier|protected
name|boolean
name|showWhitespaceErrors
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|7
argument_list|)
DECL|field|intralineDifference
specifier|protected
name|boolean
name|intralineDifference
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|8
argument_list|)
DECL|field|showTabs
specifier|protected
name|boolean
name|showTabs
decl_stmt|;
comment|/** Number of lines of context when viewing a patch. */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|9
argument_list|)
DECL|field|context
specifier|protected
name|short
name|context
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|10
argument_list|)
DECL|field|skipDeleted
specifier|protected
name|boolean
name|skipDeleted
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|11
argument_list|)
DECL|field|skipUncommented
specifier|protected
name|boolean
name|skipUncommented
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|12
argument_list|)
DECL|field|expandAllComments
specifier|protected
name|boolean
name|expandAllComments
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|13
argument_list|)
DECL|field|retainHeader
specifier|protected
name|boolean
name|retainHeader
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|14
argument_list|)
DECL|field|manualReview
specifier|protected
name|boolean
name|manualReview
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|15
argument_list|)
DECL|field|showLineEndings
specifier|protected
name|boolean
name|showLineEndings
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|16
argument_list|)
DECL|field|hideTopMenu
specifier|protected
name|boolean
name|hideTopMenu
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|17
argument_list|)
DECL|field|hideLineNumbers
specifier|protected
name|boolean
name|hideLineNumbers
decl_stmt|;
DECL|method|AccountDiffPreference ()
specifier|protected
name|AccountDiffPreference
parameter_list|()
block|{   }
DECL|method|AccountDiffPreference (Account.Id accountId)
specifier|public
name|AccountDiffPreference
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
block|}
DECL|method|AccountDiffPreference (AccountDiffPreference p)
specifier|public
name|AccountDiffPreference
parameter_list|(
name|AccountDiffPreference
name|p
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|p
operator|.
name|accountId
expr_stmt|;
name|this
operator|.
name|ignoreWhitespace
operator|=
name|p
operator|.
name|ignoreWhitespace
expr_stmt|;
name|this
operator|.
name|tabSize
operator|=
name|p
operator|.
name|tabSize
expr_stmt|;
name|this
operator|.
name|lineLength
operator|=
name|p
operator|.
name|lineLength
expr_stmt|;
name|this
operator|.
name|syntaxHighlighting
operator|=
name|p
operator|.
name|syntaxHighlighting
expr_stmt|;
name|this
operator|.
name|showWhitespaceErrors
operator|=
name|p
operator|.
name|showWhitespaceErrors
expr_stmt|;
name|this
operator|.
name|showLineEndings
operator|=
name|p
operator|.
name|showLineEndings
expr_stmt|;
name|this
operator|.
name|intralineDifference
operator|=
name|p
operator|.
name|intralineDifference
expr_stmt|;
name|this
operator|.
name|showTabs
operator|=
name|p
operator|.
name|showTabs
expr_stmt|;
name|this
operator|.
name|skipDeleted
operator|=
name|p
operator|.
name|skipDeleted
expr_stmt|;
name|this
operator|.
name|skipUncommented
operator|=
name|p
operator|.
name|skipUncommented
expr_stmt|;
name|this
operator|.
name|expandAllComments
operator|=
name|p
operator|.
name|expandAllComments
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|p
operator|.
name|context
expr_stmt|;
name|this
operator|.
name|retainHeader
operator|=
name|p
operator|.
name|retainHeader
expr_stmt|;
name|this
operator|.
name|manualReview
operator|=
name|p
operator|.
name|manualReview
expr_stmt|;
name|this
operator|.
name|hideTopMenu
operator|=
name|p
operator|.
name|hideTopMenu
expr_stmt|;
name|this
operator|.
name|hideLineNumbers
operator|=
name|p
operator|.
name|hideLineNumbers
expr_stmt|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
DECL|method|getIgnoreWhitespace ()
specifier|public
name|Whitespace
name|getIgnoreWhitespace
parameter_list|()
block|{
return|return
name|Whitespace
operator|.
name|forCode
argument_list|(
name|ignoreWhitespace
argument_list|)
return|;
block|}
DECL|method|setIgnoreWhitespace (Whitespace ignoreWhitespace)
specifier|public
name|void
name|setIgnoreWhitespace
parameter_list|(
name|Whitespace
name|ignoreWhitespace
parameter_list|)
block|{
name|this
operator|.
name|ignoreWhitespace
operator|=
name|ignoreWhitespace
operator|.
name|getCode
argument_list|()
expr_stmt|;
block|}
DECL|method|getTabSize ()
specifier|public
name|int
name|getTabSize
parameter_list|()
block|{
return|return
name|tabSize
return|;
block|}
DECL|method|setTabSize (int tabSize)
specifier|public
name|void
name|setTabSize
parameter_list|(
name|int
name|tabSize
parameter_list|)
block|{
name|this
operator|.
name|tabSize
operator|=
name|tabSize
expr_stmt|;
block|}
DECL|method|getLineLength ()
specifier|public
name|int
name|getLineLength
parameter_list|()
block|{
return|return
name|lineLength
return|;
block|}
DECL|method|setLineLength (int lineLength)
specifier|public
name|void
name|setLineLength
parameter_list|(
name|int
name|lineLength
parameter_list|)
block|{
name|this
operator|.
name|lineLength
operator|=
name|lineLength
expr_stmt|;
block|}
DECL|method|isSyntaxHighlighting ()
specifier|public
name|boolean
name|isSyntaxHighlighting
parameter_list|()
block|{
return|return
name|syntaxHighlighting
return|;
block|}
DECL|method|setSyntaxHighlighting (boolean syntaxHighlighting)
specifier|public
name|void
name|setSyntaxHighlighting
parameter_list|(
name|boolean
name|syntaxHighlighting
parameter_list|)
block|{
name|this
operator|.
name|syntaxHighlighting
operator|=
name|syntaxHighlighting
expr_stmt|;
block|}
DECL|method|isShowWhitespaceErrors ()
specifier|public
name|boolean
name|isShowWhitespaceErrors
parameter_list|()
block|{
return|return
name|showWhitespaceErrors
return|;
block|}
DECL|method|setShowWhitespaceErrors (boolean showWhitespaceErrors)
specifier|public
name|void
name|setShowWhitespaceErrors
parameter_list|(
name|boolean
name|showWhitespaceErrors
parameter_list|)
block|{
name|this
operator|.
name|showWhitespaceErrors
operator|=
name|showWhitespaceErrors
expr_stmt|;
block|}
DECL|method|isShowLineEndings ()
specifier|public
name|boolean
name|isShowLineEndings
parameter_list|()
block|{
return|return
name|showLineEndings
return|;
block|}
DECL|method|setShowLineEndings (boolean showLineEndings)
specifier|public
name|void
name|setShowLineEndings
parameter_list|(
name|boolean
name|showLineEndings
parameter_list|)
block|{
name|this
operator|.
name|showLineEndings
operator|=
name|showLineEndings
expr_stmt|;
block|}
DECL|method|isIntralineDifference ()
specifier|public
name|boolean
name|isIntralineDifference
parameter_list|()
block|{
return|return
name|intralineDifference
return|;
block|}
DECL|method|setIntralineDifference (boolean intralineDifference)
specifier|public
name|void
name|setIntralineDifference
parameter_list|(
name|boolean
name|intralineDifference
parameter_list|)
block|{
name|this
operator|.
name|intralineDifference
operator|=
name|intralineDifference
expr_stmt|;
block|}
DECL|method|isShowTabs ()
specifier|public
name|boolean
name|isShowTabs
parameter_list|()
block|{
return|return
name|showTabs
return|;
block|}
DECL|method|setShowTabs (boolean showTabs)
specifier|public
name|void
name|setShowTabs
parameter_list|(
name|boolean
name|showTabs
parameter_list|)
block|{
name|this
operator|.
name|showTabs
operator|=
name|showTabs
expr_stmt|;
block|}
comment|/** Get the number of lines of context when viewing a patch. */
DECL|method|getContext ()
specifier|public
name|short
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/** Set the number of lines of context when viewing a patch. */
DECL|method|setContext (final short context)
specifier|public
name|void
name|setContext
parameter_list|(
specifier|final
name|short
name|context
parameter_list|)
block|{
assert|assert
literal|0
operator|<=
name|context
operator|||
name|context
operator|==
name|WHOLE_FILE_CONTEXT
assert|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|isSkipDeleted ()
specifier|public
name|boolean
name|isSkipDeleted
parameter_list|()
block|{
return|return
name|skipDeleted
return|;
block|}
DECL|method|setSkipDeleted (boolean skip)
specifier|public
name|void
name|setSkipDeleted
parameter_list|(
name|boolean
name|skip
parameter_list|)
block|{
name|skipDeleted
operator|=
name|skip
expr_stmt|;
block|}
DECL|method|isSkipUncommented ()
specifier|public
name|boolean
name|isSkipUncommented
parameter_list|()
block|{
return|return
name|skipUncommented
return|;
block|}
DECL|method|setSkipUncommented (boolean skip)
specifier|public
name|void
name|setSkipUncommented
parameter_list|(
name|boolean
name|skip
parameter_list|)
block|{
name|skipUncommented
operator|=
name|skip
expr_stmt|;
block|}
DECL|method|isExpandAllComments ()
specifier|public
name|boolean
name|isExpandAllComments
parameter_list|()
block|{
return|return
name|expandAllComments
return|;
block|}
DECL|method|setExpandAllComments (boolean expand)
specifier|public
name|void
name|setExpandAllComments
parameter_list|(
name|boolean
name|expand
parameter_list|)
block|{
name|expandAllComments
operator|=
name|expand
expr_stmt|;
block|}
DECL|method|isRetainHeader ()
specifier|public
name|boolean
name|isRetainHeader
parameter_list|()
block|{
return|return
name|retainHeader
return|;
block|}
DECL|method|setRetainHeader (boolean retain)
specifier|public
name|void
name|setRetainHeader
parameter_list|(
name|boolean
name|retain
parameter_list|)
block|{
name|retainHeader
operator|=
name|retain
expr_stmt|;
block|}
DECL|method|isManualReview ()
specifier|public
name|boolean
name|isManualReview
parameter_list|()
block|{
return|return
name|manualReview
return|;
block|}
DECL|method|setManualReview (boolean manual)
specifier|public
name|void
name|setManualReview
parameter_list|(
name|boolean
name|manual
parameter_list|)
block|{
name|manualReview
operator|=
name|manual
expr_stmt|;
block|}
DECL|method|isHideTopMenu ()
specifier|public
name|boolean
name|isHideTopMenu
parameter_list|()
block|{
return|return
name|hideTopMenu
return|;
block|}
DECL|method|setHideTopMenu (boolean hide)
specifier|public
name|void
name|setHideTopMenu
parameter_list|(
name|boolean
name|hide
parameter_list|)
block|{
name|hideTopMenu
operator|=
name|hide
expr_stmt|;
block|}
DECL|method|isHideLineNumbers ()
specifier|public
name|boolean
name|isHideLineNumbers
parameter_list|()
block|{
return|return
name|hideLineNumbers
return|;
block|}
DECL|method|setHideLineNumbers (boolean hide)
specifier|public
name|void
name|setHideLineNumbers
parameter_list|(
name|boolean
name|hide
parameter_list|)
block|{
name|hideLineNumbers
operator|=
name|hide
expr_stmt|;
block|}
block|}
end_class

end_unit

