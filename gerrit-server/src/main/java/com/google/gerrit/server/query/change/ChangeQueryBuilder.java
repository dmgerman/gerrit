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
DECL|package|com.google.gerrit.server.query.change
package|package
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
name|common
operator|.
name|data
operator|.
name|ApprovalTypes
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
name|reviewdb
operator|.
name|AccountGroup
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
name|Change
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
name|Project
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
name|RevId
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
name|ReviewDb
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
name|CurrentUser
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
name|IdentifiedUser
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
name|account
operator|.
name|AccountResolver
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
name|account
operator|.
name|GroupCache
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
name|config
operator|.
name|AuthConfig
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
name|config
operator|.
name|WildProjectName
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
name|project
operator|.
name|ChangeControl
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
name|IntPredicate
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
name|Predicate
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
name|QueryBuilder
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
name|QueryParseException
import|;
end_import

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
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
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
name|AbbreviatedObjectId
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
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Parses a query string meant to be applied to change objects.  */
end_comment

begin_class
DECL|class|ChangeQueryBuilder
specifier|public
class|class
name|ChangeQueryBuilder
extends|extends
name|QueryBuilder
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|PAT_LEGACY_ID
specifier|private
specifier|static
specifier|final
name|Pattern
name|PAT_LEGACY_ID
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^[1-9][0-9]*$"
argument_list|)
decl_stmt|;
DECL|field|PAT_CHANGE_ID
specifier|private
specifier|static
specifier|final
name|Pattern
name|PAT_CHANGE_ID
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^[iI][0-9a-f]{4,}.*$"
argument_list|)
decl_stmt|;
DECL|field|DEF_CHANGE
specifier|private
specifier|static
specifier|final
name|Pattern
name|DEF_CHANGE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^([1-9][0-9]*|[iI][0-9a-f]{4,}.*)$"
argument_list|)
decl_stmt|;
DECL|field|PAT_COMMIT
specifier|private
specifier|static
specifier|final
name|Pattern
name|PAT_COMMIT
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^([0-9a-fA-F]{4,"
operator|+
name|RevId
operator|.
name|LEN
operator|+
literal|"})$"
argument_list|)
decl_stmt|;
DECL|field|PAT_EMAIL
specifier|private
specifier|static
specifier|final
name|Pattern
name|PAT_EMAIL
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+"
argument_list|)
decl_stmt|;
DECL|field|PAT_LABEL
specifier|private
specifier|static
specifier|final
name|Pattern
name|PAT_LABEL
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^[a-zA-Z][a-zA-Z0-9]*((=|>=|<=)[+-]?|[+-])\\d+$"
argument_list|)
decl_stmt|;
DECL|field|FIELD_AGE
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_AGE
init|=
literal|"age"
decl_stmt|;
DECL|field|FIELD_BRANCH
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_BRANCH
init|=
literal|"branch"
decl_stmt|;
DECL|field|FIELD_CHANGE
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_CHANGE
init|=
literal|"change"
decl_stmt|;
DECL|field|FIELD_COMMIT
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_COMMIT
init|=
literal|"commit"
decl_stmt|;
DECL|field|FIELD_DRAFTBY
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_DRAFTBY
init|=
literal|"draftby"
decl_stmt|;
DECL|field|FIELD_IS
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_IS
init|=
literal|"is"
decl_stmt|;
DECL|field|FIELD_HAS
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_HAS
init|=
literal|"has"
decl_stmt|;
DECL|field|FIELD_LABEL
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_LABEL
init|=
literal|"label"
decl_stmt|;
DECL|field|FIELD_LIMIT
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_LIMIT
init|=
literal|"limit"
decl_stmt|;
DECL|field|FIELD_OWNER
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_OWNER
init|=
literal|"owner"
decl_stmt|;
DECL|field|FIELD_PROJECT
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_PROJECT
init|=
literal|"project"
decl_stmt|;
DECL|field|FIELD_REF
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_REF
init|=
literal|"ref"
decl_stmt|;
DECL|field|FIELD_REVIEWER
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_REVIEWER
init|=
literal|"reviewer"
decl_stmt|;
DECL|field|FIELD_STARREDBY
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_STARREDBY
init|=
literal|"starredby"
decl_stmt|;
DECL|field|FIELD_STATUS
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_STATUS
init|=
literal|"status"
decl_stmt|;
DECL|field|FIELD_TOPIC
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_TOPIC
init|=
literal|"topic"
decl_stmt|;
DECL|field|FIELD_TR
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_TR
init|=
literal|"tr"
decl_stmt|;
DECL|field|FIELD_VISIBLETO
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_VISIBLETO
init|=
literal|"visibleto"
decl_stmt|;
DECL|field|FIELD_WATCHEDBY
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_WATCHEDBY
init|=
literal|"watchedby"
decl_stmt|;
DECL|field|mydef
specifier|private
specifier|static
specifier|final
name|QueryBuilder
operator|.
name|Definition
argument_list|<
name|ChangeData
argument_list|,
name|ChangeQueryBuilder
argument_list|>
name|mydef
init|=
operator|new
name|QueryBuilder
operator|.
name|Definition
argument_list|<
name|ChangeData
argument_list|,
name|ChangeQueryBuilder
argument_list|>
argument_list|(
name|ChangeQueryBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Arguments
specifier|static
class|class
name|Arguments
block|{
DECL|field|dbProvider
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|rewriter
specifier|final
name|Provider
argument_list|<
name|ChangeQueryRewriter
argument_list|>
name|rewriter
decl_stmt|;
DECL|field|userFactory
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|changeControlFactory
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|changeControlGenericFactory
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlGenericFactory
decl_stmt|;
DECL|field|accountResolver
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
DECL|field|groupCache
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|authConfig
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|approvalTypes
specifier|final
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
DECL|field|wildProjectName
specifier|final
name|Project
operator|.
name|NameKey
name|wildProjectName
decl_stmt|;
annotation|@
name|Inject
DECL|method|Arguments (Provider<ReviewDb> dbProvider, Provider<ChangeQueryRewriter> rewriter, IdentifiedUser.GenericFactory userFactory, ChangeControl.Factory changeControlFactory, ChangeControl.GenericFactory changeControlGenericFactory, AccountResolver accountResolver, GroupCache groupCache, AuthConfig authConfig, ApprovalTypes approvalTypes, @WildProjectName Project.NameKey wildProjectName)
name|Arguments
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|Provider
argument_list|<
name|ChangeQueryRewriter
argument_list|>
name|rewriter
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
parameter_list|,
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlGenericFactory
parameter_list|,
name|AccountResolver
name|accountResolver
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
name|ApprovalTypes
name|approvalTypes
parameter_list|,
annotation|@
name|WildProjectName
name|Project
operator|.
name|NameKey
name|wildProjectName
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|rewriter
operator|=
name|rewriter
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|changeControlGenericFactory
operator|=
name|changeControlGenericFactory
expr_stmt|;
name|this
operator|.
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|approvalTypes
operator|=
name|approvalTypes
expr_stmt|;
name|this
operator|.
name|wildProjectName
operator|=
name|wildProjectName
expr_stmt|;
block|}
block|}
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (CurrentUser user)
name|ChangeQueryBuilder
name|create
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
function_decl|;
block|}
DECL|field|args
specifier|private
specifier|final
name|Arguments
name|args
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|CurrentUser
name|currentUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeQueryBuilder (Arguments args, @Assisted CurrentUser currentUser)
name|ChangeQueryBuilder
parameter_list|(
name|Arguments
name|args
parameter_list|,
annotation|@
name|Assisted
name|CurrentUser
name|currentUser
parameter_list|)
block|{
name|super
argument_list|(
name|mydef
argument_list|)
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
block|}
annotation|@
name|Operator
DECL|method|age (String value)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|age
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|AgePredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|change (String query)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|change
parameter_list|(
name|String
name|query
parameter_list|)
block|{
if|if
condition|(
name|PAT_LEGACY_ID
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
operator|new
name|LegacyChangeIdPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|Change
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|query
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PAT_CHANGE_ID
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
if|if
condition|(
name|query
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'i'
condition|)
block|{
name|query
operator|=
literal|"I"
operator|+
name|query
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ChangeIdPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|query
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
annotation|@
name|Operator
DECL|method|status (String statusName)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|status
parameter_list|(
name|String
name|statusName
parameter_list|)
block|{
if|if
condition|(
literal|"open"
operator|.
name|equals
argument_list|(
name|statusName
argument_list|)
condition|)
block|{
return|return
name|status_open
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
literal|"closed"
operator|.
name|equals
argument_list|(
name|statusName
argument_list|)
condition|)
block|{
return|return
name|ChangeStatusPredicate
operator|.
name|closed
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"reviewed"
operator|.
name|equalsIgnoreCase
argument_list|(
name|statusName
argument_list|)
condition|)
block|{
return|return
operator|new
name|IsReviewedPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ChangeStatusPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|statusName
argument_list|)
return|;
block|}
block|}
DECL|method|status_open ()
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|status_open
parameter_list|()
block|{
return|return
name|ChangeStatusPredicate
operator|.
name|open
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|has (String value)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|has
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
literal|"star"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
operator|new
name|IsStarredByPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|currentUser
argument_list|)
return|;
block|}
if|if
condition|(
literal|"draft"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
if|if
condition|(
name|currentUser
operator|instanceof
name|IdentifiedUser
condition|)
block|{
return|return
operator|new
name|HasDraftByPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
operator|(
operator|(
name|IdentifiedUser
operator|)
name|currentUser
operator|)
operator|.
name|getAccountId
argument_list|()
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
annotation|@
name|Operator
DECL|method|is (String value)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|is
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
literal|"starred"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
operator|new
name|IsStarredByPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|currentUser
argument_list|)
return|;
block|}
if|if
condition|(
literal|"watched"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
operator|new
name|IsWatchedByPredicate
argument_list|(
name|args
argument_list|,
name|currentUser
argument_list|)
return|;
block|}
if|if
condition|(
literal|"visible"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|is_visible
argument_list|()
return|;
block|}
if|if
condition|(
literal|"reviewed"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
operator|new
name|IsReviewedPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|)
return|;
block|}
try|try
block|{
return|return
name|status
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// not status: alias?
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
annotation|@
name|Operator
DECL|method|commit (String id)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|commit
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
operator|new
name|CommitPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|AbbreviatedObjectId
operator|.
name|fromString
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|project (String name)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|project
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ProjectPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|branch (String name)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|branch
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|BranchPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|topic (String name)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|topic
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|TopicPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|ref (String ref)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
operator|new
name|RefPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|ref
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|label (String name)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|label
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|LabelPredicate
argument_list|(
name|args
operator|.
name|changeControlGenericFactory
argument_list|,
name|args
operator|.
name|userFactory
argument_list|,
name|args
operator|.
name|dbProvider
argument_list|,
name|args
operator|.
name|approvalTypes
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|starredby (String who)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|starredby
parameter_list|(
name|String
name|who
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|OrmException
block|{
name|Account
name|account
init|=
name|args
operator|.
name|accountResolver
operator|.
name|find
argument_list|(
name|who
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"User "
operator|+
name|who
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
operator|new
name|IsStarredByPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
comment|//
name|args
operator|.
name|userFactory
operator|.
name|create
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|watchedby (String who)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|watchedby
parameter_list|(
name|String
name|who
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|OrmException
block|{
name|Account
name|account
init|=
name|args
operator|.
name|accountResolver
operator|.
name|find
argument_list|(
name|who
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"User "
operator|+
name|who
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
operator|new
name|IsWatchedByPredicate
argument_list|(
name|args
argument_list|,
name|args
operator|.
name|userFactory
operator|.
name|create
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|draftby (String who)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|draftby
parameter_list|(
name|String
name|who
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|OrmException
block|{
name|Account
name|account
init|=
name|args
operator|.
name|accountResolver
operator|.
name|find
argument_list|(
name|who
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"User "
operator|+
name|who
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
operator|new
name|HasDraftByPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|visibleto (String who)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|visibleto
parameter_list|(
name|String
name|who
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|OrmException
block|{
name|Account
name|account
init|=
name|args
operator|.
name|accountResolver
operator|.
name|find
argument_list|(
name|who
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|!=
literal|null
condition|)
block|{
return|return
name|visibleto
argument_list|(
name|args
operator|.
name|userFactory
operator|.
name|create
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|// If its not an account, maybe its a group?
comment|//
name|AccountGroup
name|g
init|=
name|args
operator|.
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|who
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|!=
literal|null
condition|)
block|{
return|return
name|visibleto
argument_list|(
operator|new
name|SingleGroupUser
argument_list|(
name|args
operator|.
name|authConfig
argument_list|,
name|g
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
name|Collection
argument_list|<
name|AccountGroup
argument_list|>
name|matches
init|=
name|args
operator|.
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|ExternalNameKey
argument_list|(
name|who
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|matches
operator|!=
literal|null
operator|&&
operator|!
name|matches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroup
name|group
range|:
name|matches
control|)
block|{
name|ids
operator|.
name|add
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|visibleto
argument_list|(
operator|new
name|SingleGroupUser
argument_list|(
name|args
operator|.
name|authConfig
argument_list|,
name|ids
argument_list|)
argument_list|)
return|;
block|}
throw|throw
name|error
argument_list|(
literal|"No user or group matches \""
operator|+
name|who
operator|+
literal|"\"."
argument_list|)
throw|;
block|}
DECL|method|visibleto (CurrentUser user)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|visibleto
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
operator|new
name|IsVisibleToPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
comment|//
name|args
operator|.
name|changeControlFactory
argument_list|,
comment|//
name|user
argument_list|)
return|;
block|}
DECL|method|is_visible ()
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|is_visible
parameter_list|()
block|{
return|return
name|visibleto
argument_list|(
name|currentUser
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|owner (String who)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|owner
parameter_list|(
name|String
name|who
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|OrmException
block|{
name|Account
name|account
init|=
name|args
operator|.
name|accountResolver
operator|.
name|find
argument_list|(
name|who
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"User "
operator|+
name|who
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
operator|new
name|OwnerPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|reviewer (String nameOrEmail)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|reviewer
parameter_list|(
name|String
name|nameOrEmail
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|OrmException
block|{
name|Account
name|account
init|=
name|args
operator|.
name|accountResolver
operator|.
name|find
argument_list|(
name|nameOrEmail
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"Reviewer "
operator|+
name|nameOrEmail
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
operator|new
name|ReviewerPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|tr (String trackingId)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|tr
parameter_list|(
name|String
name|trackingId
parameter_list|)
block|{
return|return
operator|new
name|TrackingIdPredicate
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|trackingId
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|bug (String trackingId)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|bug
parameter_list|(
name|String
name|trackingId
parameter_list|)
block|{
return|return
name|tr
argument_list|(
name|trackingId
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|limit (String limit)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|limit
parameter_list|(
name|String
name|limit
parameter_list|)
block|{
return|return
name|limit
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|limit
argument_list|)
argument_list|)
return|;
block|}
DECL|method|limit (int limit)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|limit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
return|return
operator|new
name|IntPredicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|(
name|FIELD_LIMIT
argument_list|,
name|limit
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|object
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Operator
DECL|method|sortkey_after (String sortKey)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|sortkey_after
parameter_list|(
name|String
name|sortKey
parameter_list|)
block|{
return|return
operator|new
name|SortKeyPredicate
operator|.
name|After
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|sortKey
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|sortkey_before (String sortKey)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|sortkey_before
parameter_list|(
name|String
name|sortKey
parameter_list|)
block|{
return|return
operator|new
name|SortKeyPredicate
operator|.
name|Before
argument_list|(
name|args
operator|.
name|dbProvider
argument_list|,
name|sortKey
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|resume_sortkey (String sortKey)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|resume_sortkey
parameter_list|(
name|String
name|sortKey
parameter_list|)
block|{
return|return
name|sortkey_before
argument_list|(
name|sortKey
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|hasLimit (Predicate<ChangeData> p)
specifier|public
name|boolean
name|hasLimit
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
block|{
return|return
name|find
argument_list|(
name|p
argument_list|,
name|IntPredicate
operator|.
name|class
argument_list|,
name|FIELD_LIMIT
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|hasSortKey (Predicate<ChangeData> p)
specifier|public
name|boolean
name|hasSortKey
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
block|{
return|return
name|find
argument_list|(
name|p
argument_list|,
name|SortKeyPredicate
operator|.
name|class
argument_list|,
literal|"sortkey_after"
argument_list|)
operator|!=
literal|null
operator|||
name|find
argument_list|(
name|p
argument_list|,
name|SortKeyPredicate
operator|.
name|class
argument_list|,
literal|"sortkey_before"
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|defaultField (String query)
specifier|protected
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|defaultField
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
name|query
operator|.
name|startsWith
argument_list|(
literal|"refs/"
argument_list|)
condition|)
block|{
return|return
name|ref
argument_list|(
name|query
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|DEF_CHANGE
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|change
argument_list|(
name|query
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PAT_COMMIT
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|commit
argument_list|(
name|query
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PAT_EMAIL
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|Predicate
operator|.
name|or
argument_list|(
name|owner
argument_list|(
name|query
argument_list|)
argument_list|,
name|reviewer
argument_list|(
name|query
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
throw|throw
name|error
argument_list|(
literal|"Cannot lookup user"
argument_list|,
name|err
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|PAT_LABEL
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
name|label
argument_list|(
name|query
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
name|error
argument_list|(
literal|"Unsupported query:"
operator|+
name|query
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

