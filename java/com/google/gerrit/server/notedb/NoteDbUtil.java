begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CharMatcher
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|GitDateFormatter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|GitDateFormatter
operator|.
name|Format
import|;
end_import

begin_class
DECL|class|NoteDbUtil
specifier|public
class|class
name|NoteDbUtil
block|{
comment|/**    * Returns an AccountId for the given email address. Returns empty if the address isn't on this    * server.    */
DECL|method|parseIdent (PersonIdent ident, String serverId)
specifier|public
specifier|static
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|parseIdent
parameter_list|(
name|PersonIdent
name|ident
parameter_list|,
name|String
name|serverId
parameter_list|)
block|{
name|String
name|email
init|=
name|ident
operator|.
name|getEmailAddress
argument_list|()
decl_stmt|;
name|int
name|at
init|=
name|email
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
if|if
condition|(
name|at
operator|>=
literal|0
condition|)
block|{
name|String
name|host
init|=
name|email
operator|.
name|substring
argument_list|(
name|at
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|.
name|equals
argument_list|(
name|serverId
argument_list|)
condition|)
block|{
name|Integer
name|id
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|email
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|at
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|Account
operator|.
name|id
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|NoteDbUtil ()
specifier|private
name|NoteDbUtil
parameter_list|()
block|{}
DECL|method|formatTime (PersonIdent ident, Timestamp t)
specifier|public
specifier|static
name|String
name|formatTime
parameter_list|(
name|PersonIdent
name|ident
parameter_list|,
name|Timestamp
name|t
parameter_list|)
block|{
name|GitDateFormatter
name|dateFormatter
init|=
operator|new
name|GitDateFormatter
argument_list|(
name|Format
operator|.
name|DEFAULT
argument_list|)
decl_stmt|;
comment|// TODO(dborowitz): Use a ThreadLocal or use Joda.
name|PersonIdent
name|newIdent
init|=
operator|new
name|PersonIdent
argument_list|(
name|ident
argument_list|,
name|t
argument_list|)
decl_stmt|;
return|return
name|dateFormatter
operator|.
name|formatDate
argument_list|(
name|newIdent
argument_list|)
return|;
block|}
DECL|field|INVALID_FOOTER_CHARS
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|INVALID_FOOTER_CHARS
init|=
name|CharMatcher
operator|.
name|anyOf
argument_list|(
literal|"\r\n\0"
argument_list|)
decl_stmt|;
DECL|method|sanitizeFooter (String value)
specifier|static
name|String
name|sanitizeFooter
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// Remove characters that would confuse JGit's footer parser if they were
comment|// included in footer values, for example by splitting the footer block into
comment|// multiple paragraphs.
comment|//
comment|// One painful example: RevCommit#getShorMessage() might return a message
comment|// containing "\r\r", which RevCommit#getFooterLines() will treat as an
comment|// empty paragraph for the purposes of footer parsing.
return|return
name|INVALID_FOOTER_CHARS
operator|.
name|trimAndCollapseFrom
argument_list|(
name|value
argument_list|,
literal|' '
argument_list|)
return|;
block|}
block|}
end_class

end_unit

