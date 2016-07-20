begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|reviewdb
operator|.
name|server
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
name|ReviewersUtil
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
name|AccountVisibility
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
name|GerritServerConfig
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|SuggestReviewers
specifier|public
class|class
name|SuggestReviewers
block|{
DECL|field|DEFAULT_MAX_SUGGESTED
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_MAX_SUGGESTED
init|=
literal|10
decl_stmt|;
DECL|field|dbProvider
specifier|protected
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|protected
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|reviewersUtil
specifier|protected
specifier|final
name|ReviewersUtil
name|reviewersUtil
decl_stmt|;
DECL|field|suggestAccounts
specifier|private
specifier|final
name|boolean
name|suggestAccounts
decl_stmt|;
DECL|field|suggestFrom
specifier|private
specifier|final
name|int
name|suggestFrom
decl_stmt|;
DECL|field|maxAllowed
specifier|private
specifier|final
name|int
name|maxAllowed
decl_stmt|;
DECL|field|maxAllowedWithoutConfirmation
specifier|private
specifier|final
name|int
name|maxAllowedWithoutConfirmation
decl_stmt|;
DECL|field|limit
specifier|protected
name|int
name|limit
decl_stmt|;
DECL|field|query
specifier|protected
name|String
name|query
decl_stmt|;
DECL|field|maxSuggestedReviewers
specifier|protected
specifier|final
name|int
name|maxSuggestedReviewers
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--limit"
argument_list|,
name|aliases
operator|=
block|{
literal|"-n"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"maximum number of reviewers to list"
argument_list|)
DECL|method|setLimit (int l)
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|l
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|l
operator|<=
literal|0
condition|?
name|maxSuggestedReviewers
else|:
name|Math
operator|.
name|min
argument_list|(
name|l
argument_list|,
name|maxSuggestedReviewers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--query"
argument_list|,
name|aliases
operator|=
block|{
literal|"-q"
block|}
argument_list|,
name|metaVar
operator|=
literal|"QUERY"
argument_list|,
name|usage
operator|=
literal|"match reviewers query"
argument_list|)
DECL|method|setQuery (String q)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|q
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|q
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|getSuggestAccounts ()
specifier|public
name|boolean
name|getSuggestAccounts
parameter_list|()
block|{
return|return
name|suggestAccounts
return|;
block|}
DECL|method|getSuggestFrom ()
specifier|public
name|int
name|getSuggestFrom
parameter_list|()
block|{
return|return
name|suggestFrom
return|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
DECL|method|getMaxAllowed ()
specifier|public
name|int
name|getMaxAllowed
parameter_list|()
block|{
return|return
name|maxAllowed
return|;
block|}
DECL|method|getMaxAllowedWithoutConfirmation ()
specifier|public
name|int
name|getMaxAllowedWithoutConfirmation
parameter_list|()
block|{
return|return
name|maxAllowedWithoutConfirmation
return|;
block|}
annotation|@
name|Inject
DECL|method|SuggestReviewers (AccountVisibility av, IdentifiedUser.GenericFactory identifiedUserFactory, Provider<ReviewDb> dbProvider, @GerritServerConfig Config cfg, ReviewersUtil reviewersUtil)
specifier|public
name|SuggestReviewers
parameter_list|(
name|AccountVisibility
name|av
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|ReviewersUtil
name|reviewersUtil
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
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|this
operator|.
name|reviewersUtil
operator|=
name|reviewersUtil
expr_stmt|;
name|this
operator|.
name|maxSuggestedReviewers
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"suggest"
argument_list|,
literal|"maxSuggestedReviewers"
argument_list|,
name|DEFAULT_MAX_SUGGESTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|this
operator|.
name|maxSuggestedReviewers
expr_stmt|;
name|String
name|suggest
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"suggest"
argument_list|,
literal|null
argument_list|,
literal|"accounts"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"OFF"
operator|.
name|equalsIgnoreCase
argument_list|(
name|suggest
argument_list|)
operator|||
literal|"false"
operator|.
name|equalsIgnoreCase
argument_list|(
name|suggest
argument_list|)
condition|)
block|{
name|this
operator|.
name|suggestAccounts
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|suggestAccounts
operator|=
operator|(
name|av
operator|!=
name|AccountVisibility
operator|.
name|NONE
operator|)
expr_stmt|;
block|}
name|this
operator|.
name|suggestFrom
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"suggest"
argument_list|,
literal|null
argument_list|,
literal|"from"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxAllowed
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"addreviewer"
argument_list|,
literal|"maxAllowed"
argument_list|,
name|PostReviewers
operator|.
name|DEFAULT_MAX_REVIEWERS
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxAllowedWithoutConfirmation
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"addreviewer"
argument_list|,
literal|"maxWithoutConfirmation"
argument_list|,
name|PostReviewers
operator|.
name|DEFAULT_MAX_REVIEWERS_WITHOUT_CHECK
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

