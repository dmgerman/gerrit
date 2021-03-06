begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|send
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|Account
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
name|exceptions
operator|.
name|EmailException
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|mail
operator|.
name|Address
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Sends an email alerting a user to a new change for them to review. */
end_comment

begin_class
DECL|class|NewChangeSender
specifier|public
specifier|abstract
class|class
name|NewChangeSender
extends|extends
name|ChangeEmail
block|{
DECL|field|reviewers
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|reviewersByEmail
specifier|private
specifier|final
name|Set
argument_list|<
name|Address
argument_list|>
name|reviewersByEmail
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|extraCC
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|extraCC
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|extraCCByEmail
specifier|private
specifier|final
name|Set
argument_list|<
name|Address
argument_list|>
name|extraCCByEmail
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|NewChangeSender (EmailArguments args, ChangeData changeData)
specifier|protected
name|NewChangeSender
parameter_list|(
name|EmailArguments
name|args
parameter_list|,
name|ChangeData
name|changeData
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|,
literal|"newchange"
argument_list|,
name|changeData
argument_list|)
expr_stmt|;
block|}
DECL|method|addReviewers (Collection<Account.Id> cc)
specifier|public
name|void
name|addReviewers
parameter_list|(
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|cc
parameter_list|)
block|{
name|reviewers
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
DECL|method|addReviewersByEmail (Collection<Address> cc)
specifier|public
name|void
name|addReviewersByEmail
parameter_list|(
name|Collection
argument_list|<
name|Address
argument_list|>
name|cc
parameter_list|)
block|{
name|reviewersByEmail
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
DECL|method|addExtraCC (Collection<Account.Id> cc)
specifier|public
name|void
name|addExtraCC
parameter_list|(
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|cc
parameter_list|)
block|{
name|extraCC
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
DECL|method|addExtraCCByEmail (Collection<Address> cc)
specifier|public
name|void
name|addExtraCCByEmail
parameter_list|(
name|Collection
argument_list|<
name|Address
argument_list|>
name|cc
parameter_list|)
block|{
name|extraCCByEmail
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|String
name|threadId
init|=
name|getChangeMessageThreadId
argument_list|()
decl_stmt|;
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
name|threadId
argument_list|)
expr_stmt|;
name|setHeader
argument_list|(
literal|"References"
argument_list|,
name|threadId
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|notify
operator|.
name|handling
argument_list|()
condition|)
block|{
case|case
name|NONE
case|:
case|case
name|OWNER
case|:
break|break;
case|case
name|ALL
case|:
default|default:
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|extraCC
argument_list|)
expr_stmt|;
name|extraCCByEmail
operator|.
name|stream
argument_list|()
operator|.
name|forEach
argument_list|(
name|cc
lambda|->
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|cc
argument_list|)
argument_list|)
expr_stmt|;
comment|// $FALL-THROUGH$
case|case
name|OWNER_REVIEWERS
case|:
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|reviewers
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|addByEmail
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|reviewersByEmail
argument_list|,
literal|true
argument_list|)
expr_stmt|;
break|break;
block|}
name|rcptToAuthors
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|formatChange ()
specifier|protected
name|void
name|formatChange
parameter_list|()
throws|throws
name|EmailException
block|{
name|appendText
argument_list|(
name|textTemplate
argument_list|(
literal|"NewChange"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|useHtml
argument_list|()
condition|)
block|{
name|appendHtml
argument_list|(
name|soyHtmlTemplate
argument_list|(
literal|"NewChangeHtml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getReviewerNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getReviewerNames
parameter_list|()
block|{
if|if
condition|(
name|reviewers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|id
range|:
name|reviewers
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|getNameFor
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|setupSoyContext ()
specifier|protected
name|void
name|setupSoyContext
parameter_list|()
block|{
name|super
operator|.
name|setupSoyContext
argument_list|()
expr_stmt|;
name|soyContext
operator|.
name|put
argument_list|(
literal|"ownerName"
argument_list|,
name|getNameFor
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"reviewerNames"
argument_list|,
name|getReviewerNames
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|supportsHtml ()
specifier|protected
name|boolean
name|supportsHtml
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

